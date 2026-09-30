package com.github.debris.debrisclient.config;

import com.github.debris.debrisclient.DebrisClient;
import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.config.api.MatchType;
import com.github.debris.debrisclient.config.api.RequiresMod;
import com.github.debris.debrisclient.config.options.ConfigEnum;
import com.github.debris.debrisclient.feat.BreakingCooldownMode;
import com.github.debris.debrisclient.feat.QualityLevel;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.debris.debrisclient.config.ConfigFactory.*;
import static com.github.debris.debrisclient.feat.IMBlocker.BUILT_IN_SCREENS;

public class DCConfig extends ConfigHandlerImpl {
    public static final String ID = DebrisClient.MOD_ID;
    private static final DCConfig Instance = new DCConfig();

    private DCConfig() {
        super(ID, DebrisClient.MOD_NAME);
    }

    public static DCConfig getInstance() {
        return Instance;
    }

    @Override
    public Map<String, List<? extends IConfigBase>> getConfigsPerCategories() {
        LinkedHashMap<String, List<? extends IConfigBase>> map = new LinkedHashMap<>();

        map.put("值", VALUE);
        map.put("联动", INTEGRATION);
        map.put("列表", LIST);
        map.put("热键", HOTKEY);
        map.put("禁用", YEETS);
        map.put("高亮", GLOWS);

        return map;
    }


    public static final List<IConfigBase> VALUE;

    public static final ConfigBoolean AnvilLevelView = ofBoolean("铁砧等级显示", true, "生存可见40级以上");
    public static final ConfigBoolean EnchantPreview = ofBoolean("附魔预览", false);
    public static final ConfigBoolean EnchantPreviewParallel = ofBoolean("附魔预览并行加速", true, "多线程执行循环");
    public static final ConfigBoolean ExtraTooltip = ofBoolean("额外物品提示", true, "需按Shift查看,有以下功能\n附魔书成本,铁砧惩罚,附魔冲突,经验书折合");
    public static final ConfigEnum<BreakingCooldownMode> BreakingCooldown = ofEnum("挖掘冷却模式", BreakingCooldownMode.NORMAL);


    public static final List<IConfigBase> INTEGRATION;

    public static final ConfigBoolean IMBlocker = ofBoolean("输入法修复", false, "根据GUI的输入需求, 启用或禁用输入法");
    public static final ConfigBoolean ForceASCII = ofBoolean("强制ASCII字体", true, "修改后需重载语言文件");
    public static final ConfigBoolean ProgressResuming = ofBoolean("进度保存", true, "masa菜单");
    public static final ConfigBoolean PinYinSearch = ofBoolean("拼音搜索", true, "需要jech, 支持由MaLiLib驱动的模组");
    public static final ConfigBoolean CommentSearch = ofBoolean("注释搜索", true, "对MaLiLib驱动的模组有效");
    public static final ConfigBoolean TranslationKeySearch = ofBoolean("翻译键搜索", false, "对Jei有效(未实现)");
    @RequiresMod(ModReference.FORGOTTEN_ITEMS)
    public static final ConfigBoolean RuneTweak = ofBoolean("符文功能", false, "forgotten items: 生存模式查看符文产出");
    @RequiresMod(ModReference.XRAY)
    public static final ConfigBoolean XRayAutoColorSelection = ofBoolean("XRay自动取色", true, "");
    @RequiresMod(ModReference.XAERO_MINI_MAP)
    public static final ConfigBoolean FastWaypoint = ofBoolean("快速路径点", false, "xaero路径点: 在指路石和村民GUI添加按钮以快速创建");
    @RequiresMod(value = {ModReference.BOUNTIFUL_BAUBLES, ModReference.QUALITY_TOOLS}, matchType = MatchType.ANY)
    public static final ConfigBoolean AutoReforging = ofBoolean("自动重铸功能", false, "bountiful baubles & quality tools\n在GUI中添加按钮");
    @RequiresMod(ModReference.QUALITY_TOOLS)
    public static final ConfigEnum<QualityLevel> ReforgingLevel = ofEnum("自动重铸等级:工具品质", QualityLevel.BLUE, "其中金色与淡紫色同级");
    @RequiresMod(ModReference.QUALITY_TOOLS)
    public static final ConfigStringList ReforgingWhiteListQT = ofStringList("自动重铸白名单:工具品质", ImmutableList.of("healthy", "quality.lucky.name"), "可查阅语言文件");
    @RequiresMod(ModReference.BOUNTIFUL_BAUBLES)
    public static final ConfigStringList ReforgingWhiteListBB = ofStringList("自动重铸白名单:丰富的饰品", ImmutableList.of("hearty", "menacing", "violent"), "可查阅语言文件");
    @RequiresMod(ModReference.INV_TWEAKS)
    public static final ConfigBoolean DisableSortingOutOfGUI = ofBoolean("禁止在GUI之外整理", false, "InvTweaks");
    @RequiresMod(ModReference.FISHING_MADE_BETTER)
    public static final ConfigBoolean AutoFish = ofBoolean("自动钓鱼", false, "fishing made better\n含自动续杆\n停止只需切换空手");
    @RequiresMod(ModReference.LOCKS)
    public static final ConfigBoolean LocksTweak = ofBoolean("开锁功能", false);
    @RequiresMod(ModReference.XAERO_WORLD_MAP)
    public static final ConfigBoolean XaeroTranslateBiome = ofBoolean("xaero翻译群系", true);

    public static final ConfigBoolean StrictMode = ofBoolean("严格模式");
    public static final ConfigBoolean Debug = ofBoolean("调试");
    public static final ConfigInteger DebugX = ofInteger("调试X", 0, -200, 200);
    public static final ConfigInteger DebugY = ofInteger("调试Y", 0, -150, 150);


    public static final List<IConfigBase> LIST;

    public static final ConfigStringList IMBlockerWhiteList = ofStringList("输入法修复白名单", BUILT_IN_SCREENS, "在这些GUI中不会禁用输入法\n建议用按键添加而不是手动编辑");
    public static final ConfigStringList CullEntityList = ofStringList("剔除实体渲染列表", ImmutableList.of(), "见EntityTypes\n可通过waila查看");
    public static final ConfigStringList MuteSoundList = ofStringList("静音音效列表", ImmutableList.of(), "见SoundEvents");
    public static final ConfigStringList CullParticleList = ofStringList("剔除粒子列表", ImmutableList.of(), "见ParticleTypes");
    public static final ConfigStringList GlowEntityList = ofStringList("发光实体列表", ImmutableList.of(), "见EntityTypes\n可通过waila查看");


    public static final List<ConfigHotkey> HOTKEY;

    public static final ConfigHotkey OpenConfigScreen = ofHotkey("打开配置屏幕", "D,C");
    public static final ConfigHotkey OpenInventoryConfigScreen = ofHotkey("打开物品栏配置屏幕", "D,I");
    public static final ConfigHotkey ToggleGameMode = ofHotkey("切换游戏模式", "F3,F4", "仅生存创造切换;旁观可用F3+N");
    public static final ConfigHotkey CopyTPCommand = ofHotkey("复制TP指令", "F3,C");
    public static final ConfigHotkey AddToIMBlockerWhiteList = ofHotkey("添加GUI至输入法修复白名单", "F5", KeybindSettings.GUI, "在GUI中按下");
    public static final ConfigHotkey AlignWithEnderEye = ofHotkey("对齐末影之眼", "");
    public static final ConfigHotkey CopyMeasureData = ofHotkey("复制测量数据", "");
    @RequiresMod(ModReference.ITEM_PHYSIC)
    public static final ConfigHotkey AutoPickUp = ofHotkey("自动拾取", "", KeybindSettings.PRESS_ALLOWEXTRA, "ItemPhysic");
    public static final ConfigHotkey FreeCam = ofHotkey("灵魂出窍", "", "比tweakeroo好在\n玩家不会浮空\n渲染云不会闪烁\n兼容更多模组");
    public static final ConfigHotkey HoldAttack = ofHotkey("长按左键", "", "比tweakeroo好在关了会停");
    public static final ConfigHotkey HoldUse = ofHotkey("长按右键", "", "比tweakeroo好在关了会停");
    public static final ConfigHotkey AnvilEnchantPlan = ofHotkey("附魔规划", "", "手持需附魔物品,将附魔书置于物品栏\n仅供参考, 不一定最优");
    @RequiresMod({ModReference.REFINED_STORAGE, ModReference.JEI})
    public static final ConfigHotkey FastSearch = ofHotkey("快速搜索", "F", KeybindSettings.GUI, "在RS终端中搜索Jei原料");
    @RequiresMod({ModReference.REFINED_STORAGE, ModReference.JEI})
    public static final ConfigHotkey ClearSearch = ofHotkey("清空搜索", "C", KeybindSettings.GUI, "清空RS终端的搜索栏");

    public static final ConfigHotkey DebugKey = ofHotkey("调试键", "");


    public static final List<IConfigBase> YEETS;

    public static final ConfigBoolean NoReducedDebugInfo = ofBoolean("禁止简化调试信息", false);
    public static final ConfigBoolean DisableNausea = ofBoolean("禁用反胃", false);
    public static final ConfigBoolean DisableBlindness = ofBoolean("禁用失明", false);// TODO why still white fog
    public static final ConfigBoolean DisableInvisibility = ofBoolean("禁用隐形", false, "不含玩家");
    @RequiresMod(ModReference.ENHANCED_VISUALS)
    public static final ConfigBoolean DisableEnhancedVisuals = ofBoolean("禁用增强视觉效果", false);
    @RequiresMod(ModReference.POTION_CORE)
    public static final ConfigBoolean DisablePotionCore = ofBoolean("禁用药水核心客户端效果", false);
    @RequiresMod(ModReference.ICE_AND_FIRE)
    public static final ConfigBoolean DisableSiren = ofBoolean("禁用塞壬效果", false);
    public static final ConfigBoolean CullRidingEntity = ofBoolean("剔除坐骑渲染", false);
    public static final ConfigBoolean DisableSignatureWarning = ofBoolean("禁用签名警告", true, "masa系");
    @RequiresMod(value = {ModReference.MO_BENDS, ModReference.TRINKETS_AND_BAUBLES}, matchType = MatchType.ANY)
    public static final ConfigBoolean DisableInternetConnection = ofBoolean("禁用联网", true, "mobends, trinkets and baubles");
    public static final ConfigBoolean MuteAnvil = ofBoolean("铁砧静音", false);
    @RequiresMod(ModReference.LYCANITES_MOBS)
    public static final ConfigBoolean MuteAegis = ofBoolean("宙斯盾静音", false);


    public static final List<IConfigBase> GLOWS;

    public static final ConfigBoolean SkipCullingGlowingEntity = ofBoolean("跳过剔除发光实体", true, "EntityCulling");
    public static final ConfigBoolean AllEntitiesGlowing = ofBoolean("全部实体发光", false, "小心帧率");
    public static final ConfigBoolean GlowingEntitySpawnNotify = ofBoolean("发光实体生成提示", false);
    public static final ConfigBoolean BountifulGlowingColor = ofBoolean("丰富的发光颜色", false);
    public static final ConfigBoolean LibrarianGlowing = ofBoolean("图书管理员发光", false);
    public static final ConfigBoolean ElderGuardianGlowing = ofBoolean("远古守卫者发光", false);
    @RequiresMod(ModReference.LYCANITES_MOBS)
    public static final ConfigBoolean BossGlowing = ofBoolean("Boss发光", false, "Lycanites' mobs");
    @RequiresMod(ModReference.ICE_AND_FIRE)
    public static final ConfigBoolean DragonGlowing = ofBoolean("龙发光(IAF)", false, "Ice and fire");
    @RequiresMod(ModReference.ICE_AND_FIRE)
    public static final ConfigBoolean SeaSerpentGlowing = ofBoolean("海蟒发光", false);
    @RequiresMod(ModReference.DEFILED_LANDS)
    public static final ConfigBoolean GoldenWyrmGlowing = ofBoolean("金色书卷龙发光", false);
    @RequiresMod(ModReference.FAMILIAR_FAUNA)
    public static final ConfigBoolean PixieGlowingFF = ofBoolean("精灵发光(FF)", false, "Familiar Fauna");
    @RequiresMod(ModReference.ICE_AND_FIRE)
    public static final ConfigBoolean PixieGlowingIAF = ofBoolean("精灵发光(IAF)", false, "Ice and fire");
    @RequiresMod(ModReference.QUARK)
    public static final ConfigBoolean StoneLingGlowing = ofBoolean("石精灵发光", false);
    @RequiresMod(ModReference.CHARM)
    public static final ConfigBoolean SpectreGlowing = ofBoolean("幽灵发光", false, "Charm");
    @RequiresMod(ModReference.SCALING_HEALTH)
    public static final ConfigBoolean BlightMobGlowing = ofBoolean("瘟疫生物发光", false, "Scaling Health");
    @RequiresMod(ModReference.ARTIFACTS)
    public static final ConfigBoolean MimicGlowing = ofBoolean("宝箱怪发光", false, "Artifacts");

    static {
        VALUE = ImmutableList.of(
                AnvilLevelView,
                EnchantPreview,
                EnchantPreviewParallel,
                ExtraTooltip,
                BreakingCooldown
        );
        INTEGRATION = ImmutableList.of(
                IMBlocker,
                ForceASCII,
                ProgressResuming,
                PinYinSearch,
                CommentSearch,
                TranslationKeySearch,
                RuneTweak,
                XRayAutoColorSelection,
                FastWaypoint,
                AutoReforging,
                ReforgingLevel,
                ReforgingWhiteListQT,
                ReforgingWhiteListBB,
                DisableSortingOutOfGUI,
                AutoFish,
                LocksTweak,
                XaeroTranslateBiome,
                StrictMode,
                Debug,
                DebugX,
                DebugY
        );
        LIST = ImmutableList.of(
                IMBlockerWhiteList,
                CullEntityList,
                MuteSoundList,
                CullParticleList,
                GlowEntityList
        );
        HOTKEY = ImmutableList.of(
                OpenConfigScreen,
                OpenInventoryConfigScreen,
                ToggleGameMode,
                CopyTPCommand,
                AddToIMBlockerWhiteList,
                AlignWithEnderEye,
                CopyMeasureData,
                AutoPickUp,
                FreeCam,
                HoldAttack,
                HoldUse,
                AnvilEnchantPlan,
                FastSearch,
                ClearSearch,
                DebugKey
        );
        YEETS = ImmutableList.of(
                NoReducedDebugInfo,
                DisableNausea,
                DisableBlindness,
                DisableInvisibility,
                DisableEnhancedVisuals,
                DisablePotionCore,
                DisableSiren,
                CullRidingEntity,
                DisableSignatureWarning,
                DisableInternetConnection,
                MuteAnvil,
                MuteAegis
        );
        GLOWS = ImmutableList.of(
                SkipCullingGlowingEntity,
                AllEntitiesGlowing,
                GlowingEntitySpawnNotify,
                BountifulGlowingColor,
                LibrarianGlowing,
                ElderGuardianGlowing,
                BossGlowing,
                DragonGlowing,
                SeaSerpentGlowing,
                GoldenWyrmGlowing,
                PixieGlowingFF,
                PixieGlowingIAF,
                StoneLingGlowing,
                SpectreGlowing,
                BlightMobGlowing,
                MimicGlowing
        );
        Instance.load();
    }
}
