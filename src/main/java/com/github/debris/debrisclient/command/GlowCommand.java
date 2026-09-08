package com.github.debris.debrisclient.command;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.localization.ChatHudText;
import com.github.debris.debrisclient.util.CollectionUtil;
import com.github.debris.debrisclient.util.EntityType;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;

public class GlowCommand extends AbstractCommand {
    public GlowCommand() {
        super("dcglow");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != RayTraceResult.Type.ENTITY) {
            sender.sendMessage(ChatHudText.NO_TARGET_ENTITY.translate());
            return;
        }

        Entity entity = mc.objectMouseOver.entityHit;
        ResourceLocation type = EntityType.getType(entity);
        if (type == null) {
            sender.sendMessage(ChatHudText.ENTITY_TYPE_NULL.translate());
            return;
        }
        CollectionUtil.maybeAddToList(DCConfig.GlowEntityList.getStrings(), type.toString());
        sender.sendMessage(ChatHudText.ADDED_TO_LIST.translate(type.toString()));
    }
}
