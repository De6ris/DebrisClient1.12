package com.github.debris.debrisclient.mixins.client.inventory;

import com.github.debris.debrisclient.feat.enchant.preview.EnchantPreview;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerEnchantment.class)
public class ContainerEnchantmentMixin {
    @Shadow
    @Final
    private World world;

    @Inject(method = "updateProgressBar", at = @At("RETURN"))
    private void updateSeed(int id, int data, CallbackInfo ci) {
        if (id == 9) {
            EnchantPreview.onSeedUpdate((ContainerEnchantment) (Object) this, this.world);
        }
    }// 9 means all the messages are done
}
