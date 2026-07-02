package com.yousef.harvesthorn.datagen;

import com.yousef.harvesthorn.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public class HarvestHornDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.createPack().addProvider(HarvestHornRecipeProvider::new);
    }

    public static class HarvestHornRecipeProvider extends FabricRecipeProvider {
        public HarvestHornRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        public String getName() {
            return "Harvest Horn recipes";
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput exporter) {
            return new RecipeProvider(registries, exporter) {
                @Override
                public void buildRecipes() {
                    createShapedRecipe(ModItems.IRON_HORN, "iron_horn", Items.IRON_INGOT);
                    createShapedRecipe(ModItems.GOLDEN_HORN, "golden_horn", Items.GOLD_INGOT);
                    createShapedRecipe(ModItems.DIAMOND_HORN, "diamond_horn", Items.DIAMOND);
                    createShapedRecipe(ModItems.NETHERITE_HORN, "netherite_horn", Items.NETHERITE_INGOT);
                    createTheHarverRecipe();
                }

                private void createShapedRecipe(Item result, String recipeName, Item material) {
                    ShapedRecipeBuilder shapedRecipeBuilder = this.shaped(RecipeCategory.TOOLS, result);
                    shapedRecipeBuilder.pattern("MMM")
                            .pattern("MGM")
                            .pattern("MMM")
                            .define('M', material)
                            .define('G', Items.GOAT_HORN);

                    shapedRecipeBuilder.unlockedBy("has_goat_horn", this.has(Items.GOAT_HORN));
                    shapedRecipeBuilder.save(this.output, recipeKey(recipeName));
                }

                private void createTheHarverRecipe() {
                    ShapedRecipeBuilder shapedRecipeBuilder = this.shaped(RecipeCategory.TOOLS, ModItems.THE_HARVER);
                    shapedRecipeBuilder.pattern("NNN")
                            .pattern("NGN")
                            .pattern("NNN")
                            .define('N', Items.NETHERITE_INGOT)
                            .define('G', Items.GOAT_HORN);

                    shapedRecipeBuilder.unlockedBy("has_goat_horn", this.has(Items.GOAT_HORN));
                    shapedRecipeBuilder.save(this.output, recipeKey("the_harver"));
                }

                private ResourceKey<Recipe<?>> recipeKey(String recipeName) {
                    return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath("harvesthorn", recipeName));
                }
            };
        }
    }
}
