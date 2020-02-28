package me.tecc.tropica.management;

import org.bukkit.inventory.Recipe;

import java.util.List;

public interface IHasDefaultRegistrants {

    void registerDefaults(List<Recipe> recipeList);
    boolean hasRegisteredDefaults();
}
