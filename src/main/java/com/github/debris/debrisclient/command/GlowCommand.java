package com.github.debris.debrisclient.command;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.localization.ChatHudText;
import com.github.debris.debrisclient.util.CollectionUtil;
import com.github.debris.debrisclient.util.EntityType;
import com.github.debris.debrisclient.util.RayTraceUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

public class GlowCommand extends AbstractCommand {
    public GlowCommand() {
        super("dcglow");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        Entity entity = RayTraceUtil.getEntity(Minecraft.getMinecraft());
        if (entity == null) {
            sender.sendMessage(ChatHudText.NO_TARGET_ENTITY.translate());
            return;
        }
        ResourceLocation type = EntityType.getType(entity);
        if (type == null) {
            sender.sendMessage(ChatHudText.ENTITY_TYPE_NULL.translate());
            return;
        }
        CollectionUtil.maybeAddToList(DCConfig.GlowEntityList.getStrings(), type.toString());
        sender.sendMessage(ChatHudText.ADDED_TO_LIST.translate(type.toString()));
    }
}
