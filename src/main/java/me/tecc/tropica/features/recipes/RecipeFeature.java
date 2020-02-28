package me.tecc.tropica.features.recipes;

import org.bukkit.inventory.ShapedRecipe;

public class RecipeFeature {
    private ShapedRecipe shapedRecipe;

    public RecipeFeature(ShapedRecipe shapedRecipe) {

        this.shapedRecipe = shapedRecipe;
    }

    public ShapedRecipe getShapedRecipe() {
        return shapedRecipe;
    }
}
