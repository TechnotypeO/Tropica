package me.tecc.tropica.management;

import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import me.tecc.tropica.TUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RecipeManager implements IManager<Recipe> {
    /**
     * All the recipes that has been registered.
     */
    private Map<String, Recipe> recipes;

    /**
     * Creates a new RecipeManager.
     */
    public RecipeManager() {
        // set default instance values
        this.recipes = new HashMap<>();
    }

    @Override
    public void register(Recipe recipe) {
        this.recipes.put(getRecipeKey(recipe), recipe);
    }

    @Override
    public void unregister(Recipe recipe) {
        this.recipes.remove(getRecipeKey(recipe));
    }

    @Override
    public List<Recipe> getRegistrants() {
        return (List<Recipe>) recipes.values();
    }

    private String getRecipeKey(Recipe recipe) {
        ItemStack result = recipe.getResult();
        if (result.hasItemMeta() && Objects.requireNonNull(result.getItemMeta()).hasLocalizedName())
            return result.getItemMeta().getLocalizedName();
        else return "minecraft:" + result.getType().toString().toLowerCase();
    }
}
