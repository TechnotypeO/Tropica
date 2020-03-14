package me.tecc.tropica.features.recipes;

import me.tecc.tropica.items.Item;
import me.tecc.tropica.menus.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class RecipeMenu implements Listener {
    private static RecipeMenu recipeMenu;
    private final List<RecipeFeature> collection;

    public RecipeMenu() {
        recipeMenu = this;

        this.collection = new ArrayList<>(RecipeHandler.getInstance().getRecipes());
    }

    public void openMenu(Player player, boolean b) {
        Menu menu = new Menu(6*9, "Recipes");


        menu.setSlot(49, new Item(Material.RED_STAINED_GLASS_PANE, 1, "&cGo Back"));
        menu.open(player, b);
    }

    public static RecipeMenu getInstance() {
        return recipeMenu;
    }
}
