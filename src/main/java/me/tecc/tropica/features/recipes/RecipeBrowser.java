package me.tecc.tropica.features.recipes;

import org.bukkit.inventory.Recipe;

import java.util.List;

public class RecipeBrowser {
    private List<Recipe> recipesFor;

    public RecipeBrowser(List<Recipe> recipesFor) {
        this.recipesFor = recipesFor;
    }

    public Recipe get() {
        return recipesFor.get(0);
    }
}
