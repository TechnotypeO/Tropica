package me.tecc.tropica.features.recipes;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.items.Item;
import me.tecc.tropica.menus.Menu;
import me.tecc.tropica.menus.TropicaMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeMenu implements Listener {
    private static RecipeMenu recipeMenu;
    private final List<RecipeFeature> collection;
    private short[] slots = new short[]{(short) 10, (short) 11, (short) 12, (short) 19, (short) 20, (short) 21, (short) 28, (short) 29, (short) 30};

    public RecipeMenu() {
        recipeMenu = this;

        this.collection = new ArrayList<>(RecipeHandler.getInstance().getRecipes());
    }

    public void openMenu(Player player, boolean b) {
        Menu menu = new Menu(6*9, "Recipes");

        for (int i = 0; i < collection.size(); i++) {
            RecipeFeature recipeFeature = collection.get(i);
            menu.setSlot(i, new Item(recipeFeature.getShapedRecipe().getResult().clone()));
        }

        menu.setSlot(49, new Item(Material.RED_STAINED_GLASS_PANE, 1, "&cGo Back"));
        menu.open(player, b);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equalsIgnoreCase("Recipes")) {
            e.setCancelled(true);

            Item item = new Item(e.getCurrentItem());
            if (item.getItemStack() != null && item.getType() != Material.AIR) {
                int slot = e.getRawSlot();

                if (slot >= 10 && slot <= 10 + collection.size()) {
                    String name = ChatColor.stripColor(item.getName());
                    RecipeFeature recipe = null;

                    for (RecipeFeature recipeFeature : collection) {
                        Item result = new Item(recipeFeature.getShapedRecipe().getResult().clone());

                        if (result.getName().equalsIgnoreCase(item.getName())) {
                            recipe = recipeFeature;
                            break;
                        }
                    }

                    if (recipe == null) {
                        return;
                    }

                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.5f);

                    Menu menu = new Menu(6*9, "Recipes > "+name);
                    menu.setSlot(24, new Item(recipe.getShapedRecipe().getResult()));

                    final String[] lines = recipe.getShapedRecipe().getShape();
                    final Map<Integer, ItemStack> map = new HashMap<>();

                    int num = 0;
                    for (String line : lines) {
                        for (char c : line.toCharArray()) {
                            map.put(num, recipe.getShapedRecipe().getIngredientMap().get(c));
                            num++;
                        }
                    }
                    map.forEach((s, i) -> menu.setSlot(slots[s], new Item(i.clone())));

                    menu.setSlot(49, new Item(Material.RED_STAINED_GLASS_PANE, 1, "&cGo Back"));
                    menu.open(player, false);
                    return;
                }

                if (item.getName().equalsIgnoreCase(TUtil.toColor("&cGo Back"))) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.5f);
                    TropicaMenu.getInstance().openTropicaMenu(player, false);
                }
            }

            return;
        }

        if (e.getView().getTitle().contains("Recipes >")) {
            e.setCancelled(true);

            Item item = new Item(e.getCurrentItem());
            if (item.getItemStack() != null && item.getType() != Material.AIR) {
                if (item.getName().equalsIgnoreCase(TUtil.toColor("&cGo Back"))) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.5f);
                    openMenu(player, false);
                }
            }
        }
    }

    public static RecipeMenu getInstance() {
        return recipeMenu;
    }
}
