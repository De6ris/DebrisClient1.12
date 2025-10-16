package com.github.debris.debrisclient.event.forge;

import com.github.debris.debrisclient.util.CullingUtil;
import net.minecraft.client.audio.ISound;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlaySoundListener {
    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent event) {
        ISound sound = event.getResultSound();
        if (sound == null) return;
        if (CullingUtil.shouldMuteSound(sound)) event.setResultSound(null);
    }
}
