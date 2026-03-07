## DebrisClient1.12

---

由 Cleanroom Mod Dev Template 生成.

需要 MaLiLib 作为前置.

为简化RLCraft游玩设计.

---

本地构建时, 若遇到`runClient`失败, 需将`gradle.properties`中的`coremod_plugin_class_name`字段的值,
从`com.github.debris.debrisclient.mixin.FermiumMixinInit`改为`com.github.debris.debrisclient.mixin.MixinInit`,
因为我需要在开发环境使用`mixinbooter`, 在模组发布时使用`fermiumbooter`.
