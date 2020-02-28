package me.tecc.tropica.features.recipes;

import me.tecc.tropica.items.HeadManager;
import me.tecc.tropica.items.Item;
import me.tecc.tropica.items.NBTEditor;
import me.tecc.tropica.items.RecipeCreator;
import me.tecc.tropica.management.RecipeManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RecipeHandler {
    private final RecipeManager recipeManager;
    private final Collection<RecipeFeature> recipes;
    private final RecipeCreator recipeCreator;
    private static RecipeHandler recipeHandler;

    public RecipeHandler() {
        recipeHandler = this;

        this.recipes = new ArrayList<>();
        this.recipeManager = new RecipeManager();
        this.recipeCreator = new RecipeCreator();

        registerRecipes();
    }

    private void registerRecipes() {

        Item bambooBackpack = new Item(HeadManager.createSkull(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6L" +
                        "y90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E2YWQ4YWQ5MTNkZWYxM2JkNT" +
                        "c0MTY1NWU3N2QxMzRlYjFiNTdmMDI5NzBkYWE2YjMzMDgyNzU0ZDFhZmZjNCJ9fX0="
        ));
        bambooBackpack.setName("&aBamboo Backpack &7(Right Click)");
        bambooBackpack.setLore(new String[]{
                "&9Information!",
                "&7This bamboo backpack can",
                "&7hold up to 27 items.",
                "",
                "&eRight click to open!"
        });

        String[] bambooBackpackRows = new String[] {
                "BBB",
                "BCB",
                "BBB"
        };

        ItemStack bamboo_item = NBTEditor.createGameItem(Material.BAMBOO, 64);
        ItemStack chest_item = NBTEditor.createGameItem(Material.CHEST, 1);
        ItemStack leather_item = NBTEditor.createGameItem(Material.LEATHER, 64);

        Map<Character, ItemStack> bambooBackpackMap = new HashMap<>();
        bambooBackpackMap.put('B', leather_item);
        bambooBackpackMap.put('C', chest_item);


        register("bamboo_backpack", bambooBackpack.getItemStack(),
                bambooBackpackRows, bambooBackpackMap, null);
    }

    private void register(@NotNull String name, @NotNull ItemStack result, @NotNull String[] rows,
                          Map<Character, ItemStack> items, String group) {
        ShapedRecipe shapedRecipe = RecipeCreator.createShapedRecipe(name, result, rows, items, group);

        RecipeFeature recipeFeature = new RecipeFeature(shapedRecipe);
        this.recipes.add(recipeFeature);

        Bukkit.addRecipe(shapedRecipe);
    }

    public static RecipeHandler getInstance() {
        return recipeHandler;
    }

    public Collection<RecipeFeature> getRecipes() {
        return recipes;
    }

    public RecipeCreator getRecipeCreator() {
        return recipeCreator;
    }

    public RecipeManager getRecipeManager() {
        return recipeManager;
    }
}
