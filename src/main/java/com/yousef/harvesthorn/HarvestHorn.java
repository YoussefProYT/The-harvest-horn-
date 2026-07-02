package com.yousef.harvesthorn;

import com.yousef.harvesthorn.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HarvestHorn implements ModInitializer {

    public static final String MOD_ID = "harvesthorn";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Harvest Horn mod is loading...");

        ModItems.register();

        // Register the items into the Tools creative tab.
        // Item registration is handled by the registry; no extra creative tab hook is needed for this build.

        LOGGER.info("Harvest Horn mod loaded successfully!");
    }
}
