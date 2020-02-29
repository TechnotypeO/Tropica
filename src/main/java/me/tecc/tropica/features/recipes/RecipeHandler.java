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

        //bamboo
        Item bambooBackpack = new Item(HeadManager.createSkull(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6L" +
                        "y90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E2YWQ4YWQ5MTNkZWYxM2JkNT" +
                        "c0MTY1NWU3N2QxMzRlYjFiNTdmMDI5NzBkYWE2YjMzMDgyNzU0ZDFhZmZjNCJ9fX0="
        ));
        bambooBackpack.setName("&aTropical Backpack &7(Right Click)");
        bambooBackpack.setLore(new String[]{
                "&9Information!",
                "&7This tropical backpack can",
                "&7hold up to some items.",
                "",
                "&eRight click to open!"
        });

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
        Item jumpPad = new Item(Material.SLIME_BLOCK, 1,
                "&aJump Pad &7(Place Down)",
                "&9Information!",
                "&7Place this down and jump on it",
                "&7to be launched into the air.",
                "",
                "&r &r &r &r &b◆&3&l COOL TIP &b◆",
                "&7Look at a certain direction while",
                "&7jumping to specify the destination.",
                "",
                "&ePlace on the ground!"
        );

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
