package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.config.Callbacks;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.localization.ChatHudText;
import com.github.debris.debrisclient.mixins.client.IClientMixin;
import com.github.debris.debrisclient.util.ChatUtil;
import com.github.debris.debrisclient.util.CollectionUtil;
import com.github.debris.debrisclient.util.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.Locale;

public class MiscFeat {
    public static boolean addToIMBlockerWhiteList(Minecraft client) {
        GuiScreen screen = client.currentScreen;
        if (screen == null) {
            Callbacks.LOGGER.warn("adding null screen to im blocker white list?");
            return false;
        }
        String name = screen.getClass().getName();
        List<String> strings = DCConfig.IMBlockerWhiteList.getStrings();

        return CollectionUtil.maybeAddToList(strings, name);
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean alignWithEnderEye(Minecraft client) {
        if (Predicates.notInGame(client)) return false;

        EntityPlayerSP player = client.player;

        AxisAlignedBB box = player.getEntityBoundingBox().grow(32.0D);

        List<EntityEnderEye> eyes = client.world.getEntitiesWithinAABB(EntityEnderEye.class, box);

        if (eyes.isEmpty()) return false;

        PlayerRotation.lookAtEntity(player, eyes.get(0));
        return true;
    }

    public static boolean toggleGameMode(Minecraft client) {
        EntityPlayerSP player = client.player;
        if (player.canUseCommand(2, "")) {
            if (player.isCreative()) {
                player.sendChatMessage("/gamemode 0");
            } else {
                player.sendChatMessage("/gamemode 1");
            }
        } else {
            ((IClientMixin) client).invokeDebugFeedbackTranslated(ChatHudText.GAME_MODE_NOT_PERMITTED.getKey());
        }
        return true;
    }


    public static boolean copyTPCommand(Minecraft client) {
        ChatUtil.addLocalChat(client, ChatHudText.TP_COMMAND_COPIED.translate());
        EntityPlayerSP player = client.player;
        setClipboard(
                String.format(
                        Locale.ROOT,
                        "/tp @s %.2f %.2f %.2f %.2f %.2f",
                        player.posX,
                        player.posY,
                        player.posZ,
                        player.rotationYaw,
                        player.rotationPitch
                )
        );
        return true;
    }

    public static void setClipboard(String content) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(content);
        clipboard.setContents(selection, null);
    }

    public static boolean copyMeasureData(Minecraft client) {
        EntityPlayerSP player = client.player;
        setClipboard(
                String.format(
                        Locale.ROOT,
                        "%.2f %.2f %.2f",
                        player.posX,
                        player.posZ,
                        player.rotationYaw
                )
        );
        return true;
    }

    public static void notifyEntitySpawn(Minecraft mc, Entity entity) {
        if (DCConfig.GlowingEntitySpawnNotify.getBooleanValue() && EntityGlowing.shouldGlow(entity)) {
            ITextComponent name = entity.getDisplayName();
            name.getStyle().setColor(EntityColor.getColor(entity));

            TextComponentString pos = new TextComponentString(String.format("[%.0f,%.0f,%.0f]", entity.posX, entity.posY, entity.posZ));
            pos.getStyle()
                    .setHoverEvent(
                            new HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    ChatHudText.COORDINATES_TOOLTIP.translate()
                            )
                    ).setClickEvent(
                            new ClickEvent(
                                    ClickEvent.Action.RUN_COMMAND,
                                    String.format("/dclook %.0f %.0f %.0f", entity.posX, entity.posY, entity.posZ)
                            )
                    ).setColor(TextFormatting.AQUA);

            ChatUtil.addLocalChat(mc, ChatHudText.ENTITY_SPAWN.translate(name, pos));
        }
    }

    public static boolean debug(Minecraft client) {
        return false;
    }
}
