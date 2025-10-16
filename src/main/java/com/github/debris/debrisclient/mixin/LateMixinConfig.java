package com.github.debris.debrisclient.mixin;

import com.github.debris.debrisclient.ModReference;
import net.minecraftforge.fml.common.Loader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class LateMixinConfig implements IMixinConfigPlugin {
    @Override
    public void onLoad(String s) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!modLoadedForMixin(mixinClassName)) return false;
        if (mixinClassName.contains("CullTaskMixin")) return Loader.isModLoaded(ModReference.TWEAKEROO);// early here
        return true;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    private boolean modLoadedForMixin(String mixinClassName) {
        String packagePath = mixinClassName.substring(0, mixinClassName.lastIndexOf("."));
        String packageName = packagePath.substring(packagePath.lastIndexOf(".") + 1);
//        DebrisClientMod.LOGGER.info("checking mixin {} with package name {}", mixinClassName, packageName);
        return Loader.isModLoaded(packageName);
    }
}
