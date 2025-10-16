package com.github.debris.debrisclient.inventory.section;

import net.minecraft.inventory.Slot;

import java.util.EnumSet;

public enum EnumSection {
    Armor,
    InventoryStorage,
    InventoryHotBar,

    InventoryWhole {
        @Override
        public ContainerSection get() {
            return InventoryStorage.get().mergeWith(InventoryHotBar.get());
        }
    },

    OffHand,

    CreativeTab,

    CraftMatrix,// this is widely used, crafter(grandpa cow), crafting, player crafting
    CraftResult,// this is widely used, cartography, crafter(grandpa cow), crafting, forging, grindstone, player crafting, stone cutter

    FurnaceIn,
    FurnaceOut,
    FurnaceFuel,

    MerchantIn,
    MerchantOut,

    BrewingBottles,
    BrewingIngredient,
    BrewingFuel,

    StoneCutterIn,

    CartographyIn,
    CartographyIn2,

    AnvilIn1,
    AnvilIn2,

    SmithIn1,
    SmithIn2,
    SmithIn3,

    GrindstoneIn,

    FakePlayerActions,
    FakePlayerArmor,
    FakePlayerInventoryStorage,
    FakePlayerInventoryHotBar,
    FakePlayerOffHand,

    FakePlayerEnderChestActions,
    FakePlayerEnderChestInventory,

    BrewingBarrelAuxiliaryUp,
    BrewingBarrelAuxiliaryDown,
    BrewingBarrelInputUp,
    BrewingBarrelInputDown,
    BrewingBarrelOutputUp,
    BrewingBarrelOutputDown,

    DisenchanterFirst,
    DisenchanterSecond,
    DisenchanterThird,

    Baubles,

    QualityToolsReforgingTool,
    QualityToolsReforgingMaterial,

    BountifulBaublesReforging,

    RetroSophisticatedBackpacksLocked,

    Unidentified,// Generally, it is a simple section like chest or shulker box, or absent in special containers that already identified in SectionIdentifier.

    Container {// Those slots in current container that are not for player inventory.

        @Override
        public ContainerSection get() {
            return SectionHandler.streamAllSections()
                    .filter(section -> {
                        if (section.isOf(EnumSection.InventoryHotBar)) return false;
                        if (section.isOf(EnumSection.InventoryStorage)) return false;
                        for (EnumSection action : ACTIONS) {
                            if (section.isOf(action)) return false;
                        }
                        return true;
                    })
                    .reduce(ContainerSection::mergeWith).orElse(ContainerSection.EMPTY);
        }
    },
    ;

    /**
     * Generally absent and returns {@link ContainerSection#EMPTY}. Only call this when your container corresponds.
     * <br>
     * Those overrides this method do not exist in the {@link SectionHandler#sectionMap}, this is very important when you stream all the container sections in
     * {@link SectionHandler#streamAllSections()} or {@link SectionHandler#getSection(Slot)} and so on.
     */
    public ContainerSection get() {
        return SectionHandler.getSection(this);
    }

    public static final EnumSet<EnumSection> ACTIONS = EnumSet.of(
            EnumSection.FakePlayerActions,
            EnumSection.FakePlayerEnderChestActions
    );

}
