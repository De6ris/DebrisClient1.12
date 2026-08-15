## DebrisClient1.12

---

由模板 CleanroomMC/ForgeDevEnv 生成.

需要 MaLiLib 作为前置.

为简化RLCraft游玩设计.

---

本地`runClient`注意事项:

* 需将 MaLiLib 置于`run/mods`中, 若直接`implementation`将与`mixinbooter11`冲突
* 需将`gradle.properties`中的`coremod_plugin_class_name`字段的值,
  从`com.github.debris.debrisclient.mixin.FermiumMixinInit`改为`com.github.debris.debrisclient.mixin.MixinInit`,
  因为我需要在开发环境使用`mixinbooter`, 在模组发布时使用`fermiumbooter`
