package me.tecc.tropica.items;

import me.tecc.tropica.management.IManager;
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

public class RecipeCreator {
    /**
     * All the recipes ever created.
     * <br>
     * <b>Note</b>; creating recipes is not registering them.
     */
    private Map<String, Recipe> recipes;

    /**
     * Creates a new RecipeManager.
     */
    public RecipeCreator() {
        // set default instance values
        this.recipes = new HashMap<>();
    }


    /*-------------------- CREATION OF RECIPES --------------------*/

    /**
     * Create a shaped recipe quickly.
     * It will still be modifiable after getting the result from this function.
     *
     * @param name The name of the recipe.
     *             This is a crucial part, as the recipe needs a namespaced key
     *             to be able to be created.
     * @param result The result of the recipe.
     *               This is a required parameter, as the recipe needs a result
     *               to be able to be created.
     * @param rows The shape rows. Must have the length of 3.
     *             This is a required parameter, as the recipe needs a shape
     *             (since it is a shaped recipe, for shapeless recipes see
     *             {@link #createShapelessRecipe(String, ItemStack, String,
     *             ItemStack...)})
     *             An explanation of shape rows is this: <br>
     *             The rows are ordered as 123-456-789, which of each row must
     *             be 3 characters long.
     *             Each character must be an alphanumerical character.
     *             Spaces are also allowed, and denote that the space is empty.
     *             Characters that are used in rows (except for spaces, see
     *             previous statement) must be bound to an item
     *             (see {@code items}).
     *             For example, a valid {@code rows} parameter would be
     *             {@code {" I ", "IRI", " I "}}.
     * @param items A map of all bound characters and items used in the shape
     *              rows (see {@code rows}).
     *              This is a required parameter as
     * @param group The item group - if any exists.
     *              This is not a required parameter - only enter it if the
     *              recipe is part of a collection of recipes.
     * @return A recipe by the parameters entered.
     */

    public static ShapedRecipe createShapedRecipe(@NotNull String name, @NotNull ItemStack result, @NotNull String[] rows, Map<Character, ItemStack> items, String group) {
        // create shaped recipe using name for the namespaced key and result param as the recipe result
        ShapedRecipe recipe = new ShapedRecipe(TUtil.getNamespacedKey(name), result);
        // shape rows using rows parameter
        recipe.shape(rows);

        // set all characters to their respective items
        // using items.forEach
        items.forEach((c, item) -> {
            // set ingredient
            // prioritize using items material data

            // check if item material data is not null
            if (item.getData() != null)
                // if item material data is not null,
                // set ingredient using item material data
                recipe.setIngredient(c, item.getData());
            else
                // if item material data is null,
                // set ingredient using item material type
                recipe.setIngredient(c, item.getType());
        });

        // check if group is not null
        if (group != null)
            // if group is not null set group
            recipe.setGroup(group);

        // return final recipe
        return recipe;
    }

    /**
     * Creates a shapeless recipe quickly.
     * It will still be modifiable after getting the result from this function.
     *
     * @param name The name of the recipe.
     *             This is a crucial part, as the recipe needs a namespaced key
     *             to be able to be created.
     * @param result The result of the recipe.
     *               This is a required parameter, as the recipe needs a result
     *               to be able to be created.
     * @param group The item group - if any exists.
     *              This is not a required parameter - only enter it if the
     *              recipe is part of a collection of recipes. <br>
     *              From the Spigot documentation:
     *                  "<i>Recipes with the same group may be grouped
     *                  together when displayed in the client.</i>"
     * @param ingredients All items used in the recipe.
     *                    This is a required parameter, as the recipe needs at
     *                    least one ingredient to function as a shapeless
     *                    recipe.
     * @return A recipe by the parameters entered.
     */
    public static ShapelessRecipe createShapelessRecipe(@NotNull String name, @NotNull ItemStack result, @Nullable String group, @NotNull ItemStack... ingredients) {
        // create shapeless recipe
        ShapelessRecipe recipe = new ShapelessRecipe(TUtil.getNamespacedKey(name), result);

        // check if group is not null
        if (group != null)
            // if group is not null
            recipe.setGroup(group);

        // add all ingredients
        for (ItemStack item : ingredients) {
            // add ingredient
            // prioritize using items material data

            // check if item material data is not null
            if (item.getData() != null)
                // if item material data is not null,
                // add ingredient using item material data
                recipe.addIngredient(item.getAmount(), item.getData());
            else
                // if item material data is null,
                // add ingredient using item material type
                recipe.addIngredient(item.getAmount(), item.getType());
        }

        // return final recipe
        return recipe;
    }
}
