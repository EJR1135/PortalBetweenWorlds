package io.github.EJR1135.portalbetweenworlds.recipe;

import io.github.EJR1135.portalbetweenworlds.block.otherWorldBlocks;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;

import java.util.Objects;

public class otherWorldRecipes {
    @EventListener
    public void registerRecipes(RecipeRegisterEvent event) {
        RecipeRegisterEvent.Vanilla type = RecipeRegisterEvent.Vanilla.fromType(event.recipeId);
        switch (Objects.requireNonNull(type)) {
            case CRAFTING_SHAPELESS -> {
                CraftingRegistry.addShapelessRecipe(
                    new ItemStack(Block.GLOWSTONE, 4),
                    new ItemStack(otherWorldBlocks.portalFrame)
                );
            }
            case CRAFTING_SHAPED -> {
                    CraftingRegistry.addShapedRecipe(
                        new ItemStack(otherWorldBlocks.portalFrame, 1),
                        "GG",
                        "GG",
                        'G', Block.GLOWSTONE
                    );
                }
            }
        }
    }

