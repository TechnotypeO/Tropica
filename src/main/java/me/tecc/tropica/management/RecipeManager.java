package me.tecc.tropica.management;

import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import me.tecc.tropica.TUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RecipeManager implements IManager<Recipe>, IHasDefaultRegistrants {
    /**
     * All the recipes that has been registered.
     */
    private Map<String, Recipe> recipes;

    /**
     * Whether or not default registrants have been registered.
     */
    private boolean hasRegisteredDefaults;

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

    @Override
    public void registerDefaults() {
        @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
        List<Recipe> recipes = Arrays.asList(
                // put recipes here
        );

        for (Recipe recipe : recipes)
            this.register(recipe);

        // keep this line the last
        this.hasRegisteredDefaults = true;
    }

    @Override
    public boolean hasRegisteredDefaults() {
        return this.hasRegisteredDefaults;
    }
}
