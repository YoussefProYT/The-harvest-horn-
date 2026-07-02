package com.yousef.harvesthorn.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    public static final Item IRON_HORN = createItem("iron_horn", 100, 5, false, false);
    public static final Item GOLDEN_HORN = createItem("golden_horn", 200, 10, false, false);
    public static final Item DIAMOND_HORN = createItem("diamond_horn", 350, 15, false, false);
    public static final Item NETHERITE_HORN = createItem("netherite_horn", 500, 20, true, false);
    public static final Item THE_HARVER = createItem("the_harver", 2048, 20, true, true);

    private static Item createItem(String name, int maxDamage, int size, boolean fireproof, boolean isHarver) {
        Identifier id = Identifier.fromNamespaceAndPath("harvesthorn", name);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);

        Item.Properties properties = new Item.Properties()
                .setId(key)
                .durability(maxDamage);

        if (fireproof) {
            properties.fireResistant();
        }

        return register(id, new HarvestHornItem(properties, size, isHarver));
    }

    public static void register() {
        ResourceKey<CreativeModeTab> toolsTabKey = ResourceKey.create(
                Registries.CREATIVE_MODE_TAB,
                Identifier.fromNamespaceAndPath("minecraft", "tools_and_utilities")
        );

        ItemGroupEvents.modifyEntriesEvent(toolsTabKey).register(entries -> {
            entries.addAfter(Items.COMPASS, IRON_HORN);
            entries.addAfter(IRON_HORN, GOLDEN_HORN);
            entries.addAfter(GOLDEN_HORN, DIAMOND_HORN);
            entries.addAfter(DIAMOND_HORN, NETHERITE_HORN);
            entries.addAfter(NETHERITE_HORN, THE_HARVER);
        });
    }

    private static Item register(Identifier id, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }

    public static List<Item> getItemsForCreativeTab() {
        List<Item> items = new ArrayList<>();
        items.add(IRON_HORN);
        items.add(GOLDEN_HORN);
        items.add(DIAMOND_HORN);
        items.add(NETHERITE_HORN);
        items.add(THE_HARVER);
        return items;
    }
}
