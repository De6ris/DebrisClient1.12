package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.unsafe.mod.TweakerooAccess;
import com.github.debris.debrisclient.util.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.OptionalDouble;

public class FreeCam {
    private static boolean STATE = false;
    private static CameraEntity freeCamera = null;

    private static Entity cameraCache = null;

    private static float forwardRamped;
    private static float strafeRamped;
    private static float verticalRamped;

    public static void clear(Minecraft client) {
        if (STATE) {
            disable(client);
            STATE = !STATE;
        }
    }

    public static boolean toggle(Minecraft client) {
        if (!Predicates.inGameNoGui(client)) return false;
        if (STATE) {
            disable(client);
        } else {
            enable(client);
        }
        STATE = !STATE;
        return true;
    }

    public static boolean isActive() {
        return STATE;
    }

    public static EntityPlayerSP getFreeCamera() {
        if (!STATE) throw new IllegalStateException("getting free camera at wrong state");
        return freeCamera;
    }

    private static void disable(Minecraft client) {
        freeCamera = null;
    }

    private static void enable(Minecraft client) {
        freeCamera = createCamera(client);
    }

    private static CameraEntity createCamera(Minecraft client) {
        CameraEntity camera = new CameraEntity(client, client.world, client.player.connection, client.player.getStatFileWriter(), client.player.getRecipeBook());
        camera.noClip = true;

        EntityPlayerSP player = client.player;

        camera.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        camera.prevRotationYaw = camera.rotationYaw;
        camera.prevRotationPitch = camera.rotationPitch;
        camera.setRotationYawHead(camera.rotationYaw);
        camera.setRenderYawOffset(camera.rotationYaw);

        return camera;
    }

    public static void preRenderWorld(Minecraft client) {
        if (STATE) {
            cameraCache = client.getRenderViewEntity();
            client.setRenderViewEntity(freeCamera);
        }
    }

    public static void postRenderWorld(Minecraft client) {
        if (STATE) {
            client.setRenderViewEntity(cameraCache);
        }
    }

    public static void onTick(Minecraft client) {
        if (!Predicates.inGameNoGui(client)) return;
        if (!STATE) return;
        CameraEntity camera = freeCamera;

        camera.updateLastTickPosition();

        float forward = 0;
        float vertical = 0;
        float strafe = 0;

        GameSettings options = client.gameSettings;
        if (options.keyBindForward.isKeyDown()) {
            forward++;
        }
        if (options.keyBindBack.isKeyDown()) {
            forward--;
        }
        if (options.keyBindLeft.isKeyDown()) {
            strafe++;
        }
        if (options.keyBindRight.isKeyDown()) {
            strafe--;
        }
        if (options.keyBindJump.isKeyDown()) {
            vertical++;
        }
        if (options.keyBindSneak.isKeyDown()) {
            vertical--;
        }

        if (options.keyBindSprint.isKeyDown()) {
            if (forward != 0) camera.setSprinting(true);
        }

        if (forward == 0) camera.setSprinting(false);

        float rampAmount = 0.15f;
        float speed = strafe * strafe + forward * forward;

        if (forward != 0 && strafe != 0) {
            speed = (float) Math.sqrt(speed * 0.6);
        } else {
            speed = 1;
        }

        forwardRamped = getRampedMotion(forwardRamped, forward, rampAmount) / speed;
        verticalRamped = getRampedMotion(verticalRamped, vertical, rampAmount);
        strafeRamped = getRampedMotion(strafeRamped, strafe, rampAmount) / speed;

        forward = camera.isSprinting() ? forwardRamped * 2 : forwardRamped;

        camera.handleMotion(forward, verticalRamped, strafeRamped);
    }

    private static float getRampedMotion(float current, float input, float rampAmount) {
        if (input != 0) {
            if (input < 0) {
                rampAmount *= -1f;
            }

            // Immediately kill the motion when changing direction to the opposite
            if ((input < 0) != (current < 0)) {
                current = 0;
            }

            current = MathHelper.clamp(current + rampAmount, -1f, 1f);
        } else {
            current *= 0.5f;
        }

        return current;
    }

    private static class CameraEntity extends EntityPlayerSP {
        private CameraEntity(Minecraft client, World world, NetHandlerPlayClient netHandler,
                             StatisticsManager stats, RecipeBook recipeBook) {
            super(client, world, netHandler, stats, recipeBook);
        }

        private void updateLastTickPosition() {
            this.lastTickPosX = this.posX;
            this.lastTickPosY = this.posY;
            this.lastTickPosZ = this.posZ;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
        }

        private void handleMotion(float forward, float up, float strafe) {
            double xFactor = Math.sin(this.rotationYaw * Math.PI / 180D);
            double zFactor = Math.cos(this.rotationYaw * Math.PI / 180D);
            double scale = getMoveSpeed();

            this.motionX = (double) (strafe * zFactor - forward * xFactor) * scale;
            this.motionY = (double) up * scale;
            this.motionZ = (double) (forward * zFactor + strafe * xFactor) * scale;

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

            this.chunkCoordX = (int) Math.floor(this.posX) >> 4;
            this.chunkCoordY = (int) Math.floor(this.posY) >> 4;
            this.chunkCoordZ = (int) Math.floor(this.posZ) >> 4;
        }

        private static double getMoveSpeed() {
            double base = 0.1D;

            if (ModReference.hasMod(ModReference.TWEAKEROO)) {
                OptionalDouble flySpeed = TweakerooAccess.getFlySpeed();
                if (flySpeed.isPresent()) base = flySpeed.getAsDouble();
            }

            return base * 6;
        }
    }
}
