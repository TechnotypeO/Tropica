package me.tecc.tropica.events;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.features.collection.CollectionManager;
import me.tecc.tropica.features.playerData.PlayerFeature;
import me.tecc.tropica.features.playerData.PlayerWrapper;
import me.tecc.tropica.items.Item;
import me.tecc.tropica.items.NBTEditor;
import me.tecc.tropica.menus.Menu;
import me.tecc.tropica.sidebar.DynamicScoreboard;
import me.tecc.tropica.sidebar.Sidebar;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class BasicEventHandler implements Listener {
    public BasicEventHandler() {
        Bukkit.getPluginManager().registerEvents(this, Tropica.getTropica());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(""); // making join message invisible
        Player player = event.getPlayer();

        Item menu = new Item(Material.NETHER_STAR, 1, "&aTropica Menu &7(Right Click)",
                "&9Information!",
                "&7This is your personal Tropica",
                "&7menu. You can use it any time",
                "&7you want to.",
                "",
                "&eRight click to open!"
        );
        menu.setItemStack(NBTEditor.addInteger(menu.getItemStack(), "nonce", 1));
        player.getInventory().setItem(8, menu.getItemStack());

        new PlayerFeature(player);

        new BukkitRunnable() {

            @Override
            public void run() {
                Sidebar.sidebar(player);
                DynamicScoreboard.updateTeamScoreboard();
            }
        }.runTaskLater(Tropica.getTropica(), 20*2L);

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(""); // making quit message invisible
        PlayerFeature.quit(event.getPlayer());
    }

    @EventHandler
    public void onItemPickUp(EntityPickupItemEvent event) {
        ItemStack itemStack = event.getItem().getItemStack();
        if (NBTEditor.hasInteger(itemStack, "nonce")) {
            return;
        }
        if (event.getEntityType() == EntityType.PLAYER) CollectionManager.getInstance().handleEvent((Player) event.getEntity(), itemStack);

        event.getItem().setItemStack(NBTEditor.addInteger(itemStack, "nonce", 1));
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType().isCreatable()) {

            if (Menu.names.contains(event.getView().getTitle())) {
                return;
            }

            ItemStack[] contents = event.getInventory().getContents();
            boolean modified = false;

            for (int i = 0, contentsLength = contents.length; i < contentsLength; i++) {
                ItemStack content = contents[i];
                if (content != null && content.getType() != Material.AIR) {
                    if (!NBTEditor.hasInteger(content, "nonce")) {
                        content = NBTEditor.addInteger(content, "nonce", 1);
                        contents[i] = content;

                        CollectionManager.getInstance().handleEvent((Player) event.getPlayer(), contents[i]);
                        modified = true;
                    }
                }
            }

            if (modified) {
                event.getInventory().setContents(contents);
            }
        }
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent event) {
        ItemStack itemStack = event.getItem();
        if (NBTEditor.hasInteger(itemStack, "nonce")) {
            return;
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                ItemStack[] contents = event.getSource().getContents();

                for (int i = 0, contentsLength = contents.length; i < contentsLength; i++) {
                    ItemStack content = contents[i];
                    if (content != null && content.getType() != Material.AIR) {
                        if (!NBTEditor.hasInteger(content, "nonce")) {
                            content = NBTEditor.addInteger(content, "nonce", 1);
                            contents[i] = content;
                        }
                    }
                }

                event.getSource().setContents(contents);
            }
        }.runTaskLater(Tropica.getTropica(), 1L);


        event.setCancelled(true);
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        try {
            ItemStack itemStack = event.getInventory().getResult();
            itemStack = NBTEditor.addInteger(event.getInventory().getResult(), "nonce", 1);
            if (itemStack.getType() == Material.PLAYER_HEAD) {
                itemStack = NBTEditor.addString(event.getInventory().getResult(), "uuid", UUID.randomUUID().toString());
            }

            event.getInventory().setResult(itemStack);
        } catch (Exception e) {

        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack result = event.getRecipe().getResult();
        CollectionManager.getInstance().handleEvent(player, result);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            Item clicked = new Item(e.getCurrentItem());
            if (clicked.getName().equals(TUtil.toColor("&aTropica Menu &7(Right Click)"))) {
                e.setCancelled(true);
                return;
            }

            if (e.getHotbarButton() == 8) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemDrpo(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.NETHER_STAR) {
            Item item = new Item(event.getItemDrop().getItemStack());
            if (item.getName().equals(TUtil.toColor("&aTropica Menu &7(Right Click)"))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onAchievement(PlayerAdvancementDoneEvent e) {
        Player player = e.getPlayer();
        String name = e.getAdvancement().getKey().getKey().split("/")[1].replaceAll("_","");
        name = WordUtils.capitalizeFully(name);

        // fancy message and sounds
        TextComponent textComponent = new TextComponent(TUtil.toColor(
                "\n&r &r &r &r &r &a&l◆ &2&lADVANCEMENT DONE &a&l◆\n"+
                        "&r &r &r&7You've completed &f&n"+name+"\n"+
                        "&r &r &r&7Your reward: &b+10 Knowledge Point"+
                        "\n"
        ));

        player.spigot().sendMessage(textComponent);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.5f);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.25f);

        PlayerWrapper playerWrapper = new PlayerWrapper(player);
        int knowledge = playerWrapper.getInt("knowledge") + 10;
        playerWrapper.setInt("knowledge", knowledge);

        Sidebar.updateKnowledge(playerWrapper, 10);
    }
}
