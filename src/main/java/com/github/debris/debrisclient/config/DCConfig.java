package com.github.debris.debrisclient.config;

import com.github.debris.debrisclient.DebrisClient;
import com.github.debris.debrisclient.config.options.ConfigEnum;
import com.github.debris.debrisclient.feat.QualityColor;
import com.github.debris.debrisclient.inventory.sort.SortCategory;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.debris.debrisclient.config.ConfigFactory.*;
import static com.github.debris.debrisclient.feat.IMBlocker.BUILT_IN_SCREENS;

public class DCConfig implements IConfigHandler {
    private static final DCConfig Instance = new DCConfig();

    private DCConfig() {
    }

    public static DCConfig getInstance() {
        return Instance;
    }

    @Override
    public String getModName() {
        return DebrisClient.MOD_NAME;
    }

    @Override
    public String getConfigFileName() {
        return DebrisClient.MOD_ID + ".json";
    }

    @Override
    public Map<String, List<? extends IConfigBase>> getConfigsPerCategories() {
        LinkedHashMap<String, List<? extends IConfigBase>> map = new LinkedHashMap<>();

        map.put("值", VALUE);
        map.put("兼容", COMPAT);
        map.put("列表", LIST);
        map.put("热键", HOTKEY);
        map.put("禁用", YEETS);

        return map;
    }


    public static final List<IConfigBase> VALUE;

    public static final ConfigBoolean SortingContainersLast = ofBoolean("整理时容器置于末端", true, "潜影盒, 板条箱");
    public static final ConfigBoolean CachedSorting = ofBoolean("整理时使用缓存算法", true, "相比直接操作, 可减少发包");
    public static final ConfigEnum<SortCategory> ItemSortingOrder = ofEnum("物品整理顺序", SortCategory.CREATIVE_INVENTORY, "1.翻译键顺序\n2.按创造模式物品栏顺序\n3.按翻译后名称顺序\n4.按拼音顺序(需要Rei)");
    public static final ConfigBoolean HoldInventoryMoving = ofBoolean("连续物品移动", true, "允许在按下Shift和左键时不断移动物品");
    public static final ConfigBoolean BetterQuickMoving = ofBoolean("更好的物品移动", true, "允许将物品送上工作台");
    public static final ConfigBoolean WheelMoving = ofBoolean("滚轮移动", false);
    public static final ConfigBoolean WheelMovingInvert = ofBoolean("滚轮移动反转", false);
    public static final ConfigBoolean BetterSwapHandsKey = ofBoolean("更好的副手键", true, "允许从容器切换, 但在退出GUI之前无法预览效果");
    public static final ConfigBoolean AnvilLevelView = ofBoolean("铁砧等级显示", true, "生存不可见40级以上");
    public static final ConfigBoolean LibrarianGlowing = ofBoolean("图书管理员发光", false);
    public static final ConfigStringList IMBlockerWhiteList = ofStringList("输入法修复白名单", BUILT_IN_SCREENS, "在这些GUI中不会禁用输入法\n建议用按键添加而不是手动编辑");
    public static final ConfigBoolean EnchantPreview = ofBoolean("附魔预览", false, "暂时失效");
    public static final ConfigBoolean ExtraTooltip = ofBoolean("额外物品提示", true, "需按潜行查看,有以下功能\n附魔书成本,铁砧惩罚,附魔冲突");


    public static final List<IConfigBase> COMPAT;

    public static final ConfigBoolean IMBlocker = ofBoolean("输入法修复", true, "根据GUI的输入需求, 启用或禁用输入法");
    public static final ConfigBoolean ForceASCII = ofBoolean("强制ASCII字体", true, "修改后需重载语言文件");
    public static final ConfigBoolean ProgressResuming = ofBoolean("进度保存", true, "masa菜单");
    public static final ConfigBoolean PinYinSearch = ofBoolean("拼音搜索", true, "需要jech, 支持由MaLiLib驱动的模组");
    public static final ConfigBoolean CommentSearch = ofBoolean("注释搜索", true, "对MaLiLib驱动的模组有效");
    public static final ConfigBoolean TranslationKeySearch = ofBoolean("翻译键搜索", false, "对Jei有效(未实现)");

    public static final ConfigBoolean RuneTweak = ofBoolean("符文功能", false, "forgotten items: 生存模式查看符文产出");
    public static final ConfigBoolean XRayAutoColorSelection = ofBoolean("XRay自动取色", true, "");
    public static final ConfigBoolean WayStoneTweak = ofBoolean("指路石功能", false, "waystones: 在GUI中将有一个创建xaero路径点的按钮");
    public static final ConfigBoolean AutoReforging = ofBoolean("自动重铸功能", false, "baubles&quality tools: 在GUI中添加按钮");
    public static final ConfigEnum<QualityColor> ReforgingLevel = ofEnum("自动重铸等级:工具品质", QualityColor.BLUE, "其中金色与淡紫色同级");
    public static final ConfigStringList ReforgingWhiteList = ofStringList("自动重铸白名单:丰富的饰品", ImmutableList.of("hearty", "menacing", "violent"), "可查阅语言文件");
    public static final ConfigBoolean DisableSortingOutOfGUI = ofBoolean("禁止在GUI之外整理", false, "InvTweaks");
    public static final ConfigBoolean AutoFish = ofBoolean("自动钓鱼", false, "fishing made better\n含自动续杆\n停止只需切换空手");

    public static final ConfigBoolean StrictMode = ofBoolean("严格模式");
    public static final ConfigBoolean Debug = ofBoolean("调试");
    public static final ConfigInteger DebugX = ofInteger("调试X", 0, -200, 200);
    public static final ConfigInteger DebugY = ofInteger("调试X", 0, -150, 150);


    public static final List<IConfigBase> LIST;
    public static final ConfigStringList CullEntityList = ofStringList("剔除实体渲染列表", ImmutableList.of(), "见EntityTypes");
    public static final ConfigStringList MuteSoundList = ofStringList("静音音效列表", ImmutableList.of(), "见SoundEvents");
    public static final ConfigStringList CullParticleList = ofStringList("剔除粒子列表", ImmutableList.of(), "见ParticleTypes");


    public static final List<ConfigHotkey> HOTKEY;

    private static final KeybindSettings GUI_RELAXED = KeybindSettings.create(KeybindSettings.Context.GUI, KeyAction.PRESS, true, false, false, false);

    public static final ConfigHotkey OpenWindow = ofHotkey("打开配置", "D,C");
    public static final ConfigHotkey ToggleGameMode = ofHotkey("切换游戏模式", "F3,F4", "仅生存创造切换;旁观可用F3+N");
    public static final ConfigHotkey CopyTPCommand = ofHotkey("复制TP指令", "F3,C");
    public static final ConfigHotkey SortInventory = ofHotkey("整理物品栏", "", KeybindSettings.GUI, "比InvTweaks好用(我认为)");
    public static final ConfigHotkey AutoContainerOperation = ofHotkey("自动容器操作", "SPACE", KeybindSettings.GUI, "Rustic:酿造桶\nDisenchanter:袪魔台");
    public static final ConfigHotkey ThrowSimilar = ofHotkey("丢出类似", "LSHIFT,Q", KeybindSettings.GUI, "会丢出当前区域类似物品");
    public static final ConfigHotkey ThrowSection = ofHotkey("清空区域", "SPACE,Q", KeybindSettings.GUI, "全部丢出");
    public static final ConfigHotkey AddToIMBlockerWhiteList = ofHotkey("添加GUI至输入法修复白名单", "", KeybindSettings.GUI, "在GUI中按下");
    public static final ConfigHotkey AlignWithEnderEye = ofHotkey("对齐末影之眼", "");
    public static final ConfigHotkey CopyMeasureData = ofHotkey("复制测量数据", "");
    public static final ConfigHotkey AutoPickUp = ofHotkey("自动拾取", "", KeybindSettings.PRESS_ALLOWEXTRA, "ItemPhysic");
    public static final ConfigHotkey FreeCam = ofHotkey("灵魂出窍", "", "比tweakeroo好在\n玩家不会浮空\n渲染云不会闪烁\n兼容xaero地图");
    public static final ConfigHotkey HoldAttack = ofHotkey("连续左键", "", "比tweakeroo好在关了会停");
    public static final ConfigHotkey HoldUse = ofHotkey("连续右键", "", "比tweakeroo好在关了会停");
    public static final ConfigHotkey AnvilEnchantPlan = ofHotkey("铁砧附魔规划", "", "手持需附魔物品,将附魔书置于背包\n仅供参考, 不一定最优");

    public static final ConfigHotkey ModifierMoveAll = ofHotkey("移动全部:修饰键", "SPACE", GUI_RELAXED, "按住时左键会移动当前区域全部");
    public static final ConfigHotkey ModifierSpreadItem = ofHotkey("分散物品:修饰键", "LMENU", GUI_RELAXED, "按住时点击会尝试将手中物品均分到点击区域全部槽位");
    public static final ConfigHotkey ModifierMoveSimilar = ofHotkey("移动类似:修饰键", "LCONTROL", GUI_RELAXED, "按住时左键会移动当前区域类似物品");

    public static final ConfigHotkey DebugKey = ofHotkey("调试键", "");


    public static final List<IConfigBase> YEETS;

    public static final ConfigBoolean NoReducedDebugInfo = ofBoolean("禁止简化调试信息", false);
    public static final ConfigBoolean DisableNausea = ofBoolean("禁用反胃", false);
    public static final ConfigBoolean DisableBlindness = ofBoolean("禁用失明", false);// TODO why still white fog
    public static final ConfigBoolean DisableEnhancedVisuals = ofBoolean("禁用增强视觉效果", false);
    public static final ConfigBoolean DisablePotionCore = ofBoolean("禁用药水核心客户端效果", false);
    public static final ConfigBoolean DisableSiren = ofBoolean("禁用塞壬效果", false);
    public static final ConfigBoolean CullRidingEntity = ofBoolean("剔除坐骑渲染", false);
    public static final ConfigBoolean DisableSignatureWarning = ofBoolean("禁用签名警告", false, "masa系");
    public static final ConfigBoolean MuteAnvil = ofBoolean("铁砧静音", false);
    public static final ConfigBoolean MuteAegis = ofBoolean("宙斯盾静音", false);

    static {
        VALUE = ImmutableList.of(
                SortingContainersLast,
                CachedSorting,
                ItemSortingOrder,
                HoldInventoryMoving,
                BetterQuickMoving,
                WheelMoving,
                WheelMovingInvert,
                BetterSwapHandsKey,
                AnvilLevelView,
                LibrarianGlowing,
                IMBlockerWhiteList,
                EnchantPreview,
                ExtraTooltip
        );
        COMPAT = ImmutableList.of(
                IMBlocker,
                ForceASCII,
                ProgressResuming,
                PinYinSearch,
                CommentSearch,
                TranslationKeySearch,
                RuneTweak,
                XRayAutoColorSelection,
                WayStoneTweak,
                AutoReforging,
                ReforgingLevel,
                ReforgingWhiteList,
                DisableSortingOutOfGUI,
                AutoFish,
                StrictMode,
                Debug,
                DebugX,
                DebugY
        );
        LIST = ImmutableList.of(
                CullEntityList,
                MuteSoundList,
                CullParticleList
        );
        HOTKEY = ImmutableList.of(
                OpenWindow,
                ToggleGameMode,
                CopyTPCommand,
                SortInventory,
                AutoContainerOperation,
                ThrowSimilar,
                ThrowSection,
                AddToIMBlockerWhiteList,
                AlignWithEnderEye,
                CopyMeasureData,
                AutoPickUp,
                FreeCam,
                HoldAttack,
                HoldUse,
                AnvilEnchantPlan,
                DebugKey,
                ModifierMoveAll,
                ModifierSpreadItem,
                ModifierMoveSimilar
        );
        YEETS = ImmutableList.of(
                NoReducedDebugInfo,
                DisableNausea,
                DisableBlindness,
                DisableEnhancedVisuals,
                DisablePotionCore,
                DisableSiren,
                CullRidingEntity,
                DisableSignatureWarning,
                MuteAnvil,
                MuteAegis
        );
        Instance.load();
    }
}
