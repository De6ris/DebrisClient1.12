package com.github.debris.debrisclient.unsafe.baubles;

import com.github.debris.debrisclient.mixins.client.gui.IGuiScreenMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public abstract class AbstractReforgingTask implements Runnable {
    protected static final Logger LOGGER = LogManager.getLogger(AbstractReforgingTask.class);

    protected final Minecraft client;
    /**
     * For sending reforge packet.
     */
    protected final GuiButton dummyButton;

    public AbstractReforgingTask(Minecraft client) {
        this.client = client;
        this.dummyButton = new GuiButton(0, 0, 0, "");
    }

    @Override
    public void run() {
        try {
            runInternal();
        } catch (RuntimeException e) {
            LOGGER.warn("exception while running reforging task", e);
        }
    }

    private void runInternal() {
        NBTTagCompound baubleTag = this.getBaubleTag();
        this.sendReforgePacket();
        try {
            Thread.sleep(10L);
        } catch (InterruptedException e) {
            return;
        }
        while (canReforge()) {
            NBTTagCompound newTag = this.getBaubleTag();
            if (hasUpdated(baubleTag, newTag)) {
                if (isGoodTag(newTag)) break;
                baubleTag = newTag;
                this.sendReforgePacket();
            }
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    @Nonnull
    protected abstract NBTTagCompound getBaubleTag();

    private boolean hasUpdated(NBTTagCompound tag1, NBTTagCompound tag2) {
        return tag1 != tag2;
    }

    protected abstract boolean canReforge();

    @SuppressWarnings("ConstantConditions")
    protected void sendReforgePacket() {
        GuiScreen screen = this.client.currentScreen;
        if (screen != null) {
            ((IGuiScreenMixin) screen).invokeActionPerformed(this.dummyButton);
        }
    }// assuming the screens will always send packet

    protected abstract boolean isGoodTag(NBTTagCompound tag);
}
