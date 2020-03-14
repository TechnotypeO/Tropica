package me.tecc.tropica.features.recipes;

import me.tecc.tropica.Tropica;
import me.tecc.tropica.features.items.TropicaItems;
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
    private final RecipeMenu recipeMenu;

    public RecipeHandler() {
        recipeHandler = this;

        this.recipes = new ArrayList<>();
        this.recipeManager = new RecipeManager();
        this.recipeCreator = new RecipeCreator();

        registerRecipes();

        this.recipeMenu = new RecipeMenu();
        Bukkit.getPluginManager().registerEvents(recipeMenu, Tropica.getTropica());
    }

    private void registerRecipes() {

        //bamboo
        Item bambooBackpack = TropicaItems.TROPICAL_BACKPACK.item();

        String[] bambooBackpackRows = new String[] {
                "BBB",
                "BCB",
                "BBB"
        };

        ItemStack chest_item = NBTEditor.createGameItem(Material.CHEST, 1);
        ItemStack leather_item = NBTEditor.createGameItem(Material.LEATHER, 1);

        Map<Character, ItemStack> bambooBackpackMap = new HashMap<>();
        bambooBackpackMap.put('B', leather_item);
        bambooBackpackMap.put('C', chest_item);


        register("tropical_backpack", bambooBackpack.getItemStack(),
                bambooBackpackRows, bambooBackpackMap, "backpack");

        // jump pad registration
        Item jumpPad = TropicaItems.JUMP_PAD.item();

        String[] jumpPadRows = new String[]{
                "III",
                "ISI",
                "III"
        };

        ItemStack iron_block = NBTEditor.createGameItem(Material.IRON_BLOCK, 1);
        ItemStack slime_ball = NBTEditor.createGameItem(Material.SLIME_BALL, 1);

        Map<Character, ItemStack> jumpPadMap = new HashMap<>();
        jumpPadMap.put('I', iron_block);
        jumpPadMap.put('S', slime_ball);

        register("jumppad", jumpPad.getItemStack(), jumpPadRows, jumpPadMap, "fun");
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
