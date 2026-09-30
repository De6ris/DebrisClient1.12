package com.github.debris.debrisclient.config;

import com.github.debris.debrisclient.DebrisClient;
import com.github.debris.debrisclient.config.options.ConfigEnum;
import com.github.debris.debrisclient.inventory.feat.WheelMovingMode;
import com.github.debris.debrisclient.inventory.sort.SortCategory;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.config.options.IConfigBase;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.debris.debrisclient.config.ConfigFactory.*;

public class InventoryConfig extends ConfigHandlerImpl {
    public static final String ID = DebrisClient.MOD_ID + "_inventory";


    // value
    public static final ConfigBoolean SwitchPreset = ofBoolean("切换预设", false, "切换该选项的值即可启用或禁用全部物品栏功能");
    public static final ConfigBoolean SortingContainersLast = ofBoolean("整理时容器置于末端", true, "潜影盒, 板条箱");
    public static final ConfigBoolean CachedSorting = ofBoolean("整理时使用缓存算法", true, "相比直接操作, 可减少发包");
    public static final ConfigEnum<SortCategory> ItemSortingOrder = ofEnum("物品整理顺序", SortCategory.CREATIVE_INVENTORY, "1.创造模式物品栏顺序\n2.翻译键顺序\n3.翻译文本顺序\n4.拼音顺序(需要Jech)");
    public static final ConfigBoolean HoldInventoryMoving = ofBoolean("连续物品移动", false, "允许在按下Shift和左键时不断移动物品");
    public static final ConfigBoolean BetterQuickMoving = ofBoolean("更好的物品移动", false, "允许将物品送上工作台");
    public static final ConfigEnum<WheelMovingMode> WheelMoving = ofEnum("滚轮移动", WheelMovingMode.NONE);
    public static final ConfigBoolean BetterSwapHandsKey = ofBoolean("更好的副手键", false, "允许在容器中切换");
    public static final ConfigBoolean BetterHoldingItem = ofBoolean("更好的物品拿取", false, "自动在关闭容器时将拿取的物品放回物品栏");


    // hotkey
    private static final KeybindSettings GUI_RELAXED = KeybindSettings.create(KeybindSettings.Context.GUI, KeyAction.PRESS, true, false, false, false);

    public static final ConfigHotkey SortInventory = ofHotkey("整理物品栏", "", KeybindSettings.GUI, "比InvTweaks好用(我认为)");
    public static final ConfigHotkey AutoContainerOperation = ofHotkey("自动容器操作", "", KeybindSettings.GUI, "Rustic:酿造桶\nDisenchanter:袪魔台");
    public static final ConfigHotkey ThrowSame = ofHotkey("丢出类似", "", KeybindSettings.GUI, "会丢出当前区域类似物品");
    public static final ConfigHotkey ThrowSection = ofHotkey("清空区域", "", KeybindSettings.GUI, "全部丢出");
    public static final ConfigHotkey ModifierMoveStack = ofHotkey("移动一组:修饰键", "", GUI_RELAXED, "按住时左键会移动当前物品");
    public static final ConfigHotkey ModifierMoveAll = ofHotkey("移动全部:修饰键", "", GUI_RELAXED, "按住时左键会移动当前区域全部");
    public static final ConfigHotkey ModifierSpreadItem = ofHotkey("分散物品:修饰键", "", GUI_RELAXED, "按住时点击会尝试将手中物品均分到点击区域全部槽位");
    public static final ConfigHotkey ModifierMoveSame = ofHotkey("移动类似:修饰键", "", GUI_RELAXED, "按住时左键会移动当前区域类似物品");

    private static final InventoryConfig Instance = new InventoryConfig();

    private InventoryConfig() {
        super(ID, DebrisClient.MOD_NAME + " Inventory");
    }

    public static InventoryConfig getInstance() {
        return Instance;
    }

    @Override
    public Map<String, List<? extends IConfigBase>> getConfigsPerCategories() {
        LinkedHashMap<String, List<? extends IConfigBase>> map = new LinkedHashMap<>();

        map.put("值", VALUE);
        map.put("列表", LIST);
        map.put("热键", HOTKEY);

        return map;
    }

    public static final List<IConfigBase> VALUE;
    public static final List<IConfigBase> LIST;
    public static final List<IConfigBase> HOTKEY;

    static {
        VALUE = ImmutableList.of(
                SwitchPreset,
                SortingContainersLast,
                CachedSorting,
                ItemSortingOrder,
                HoldInventoryMoving,
                BetterQuickMoving,
                WheelMoving,
                BetterSwapHandsKey,
                BetterHoldingItem
        );
        LIST = ImmutableList.of(
        );
        HOTKEY = ImmutableList.of(
                SortInventory,
                AutoContainerOperation,
                ThrowSame,
                ThrowSection,
                ModifierMoveStack,
                ModifierMoveAll,
                ModifierSpreadItem,
                ModifierMoveSame
        );
        Instance.load();
    }
}
