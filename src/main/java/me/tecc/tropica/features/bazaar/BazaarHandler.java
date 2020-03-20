package me.tecc.tropica.features.bazaar;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.features.playerData.PlayerWrapper;
import me.tecc.tropica.items.Item;
import me.tecc.tropica.items.Items;
import me.tecc.tropica.items.NBTEditor;
import me.tecc.tropica.items.sCoder;
import me.tecc.tropica.menus.Menu;
import me.tecc.tropica.menus.TropicaMenu;
import me.tecc.tropica.sidebar.Rank;
import me.tecc.tropica.sidebar.Sidebar;
import me.tecc.tropica.storage.BazaarContainer;
import me.tecc.tropica.storage.PublicContainer;
import me.tecc.tropica.texts.Text;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class BazaarHandler implements Listener, CommandExecutor {
    private static BazaarHandler bazaarHandler;
    private final BazaarRunnable bazaarRunnable;
    private LinkedHashMap<UUID, Menu> menuMap = new LinkedHashMap<>();
    private LinkedList<JsonObject> auctions = new LinkedList<>();

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final double MAX_PRICE = 1000000000000.0D;

    private Map<UUID, Integer> pages = new HashMap<>();
    private Map<UUID, BazaarFilter> filter = new HashMap<>();

    private final int[] staticSlots = new int[] {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
    };
    private final int staticSize = 21;


    private final Item staticBlack = new Item(Material.BLACK_STAINED_GLASS_PANE, 1, "&r");
    private final Item staticNext = new Item(Material.BLUE_STAINED_GLASS_PANE, 1,
            "&bNext Page &8➔ &eClick");
    private final Item staticPrevious = new Item(Material.BLUE_STAINED_GLASS_PANE, 1,
            "&bPrevious Page &8➔ &eClick");

    private boolean isLoaded = false;

    public BazaarHandler() {
        bazaarHandler = this;
        new BazaarCache();

        this.bazaarRunnable = new BazaarRunnable(this);
        Bukkit.getPluginManager().registerEvents(this, Tropica.getTropica());
        Tropica.getTropica().getCommand("bazaar").setExecutor(this);
        Tropica.getTropica().getCommand("search").setExecutor(this);
        Tropica.getTropica().getCommand("sell").setExecutor(this);

        this.initAuctions();
    }

    private void initAuctions() {
        PublicContainer.getInstance()
                .getAsync("bazaar", "", new Consumer<Object>() {
            @Override
            public void accept(Object s) {

                final String string = (String) s;
                JsonArray jsonArray;

                if (!string.equals("")) {
                    JsonParser jsonParser = new JsonParser();
                    jsonArray = jsonParser.parse(string).getAsJsonArray();
                } else {
                    jsonArray = new JsonArray();
                }

                for (JsonElement jsonElement : jsonArray) {
                    auctions.add(jsonElement.getAsJsonObject());
                }

                isLoaded = true;

            }
        });
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("Bazaar the Tropical Market")) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                Item item = new Item(e.getCurrentItem());

                if (item.getName().equalsIgnoreCase(TUtil.toColor("&bNext Page &8➔ &eClick"))) {
                    int currentPage = pages.getOrDefault(player.getUniqueId(), 1);
                    pages.put(player.getUniqueId(), currentPage + 1);

                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.5f);
                    openMenu(player, false);
                    return;
                }

                if (item.getName().equalsIgnoreCase(TUtil.toColor("&bPrevious Page &8➔ &eClick"))) {
                    int currentPage = pages.getOrDefault(player.getUniqueId(), 1);
                    pages.put(player.getUniqueId(), currentPage - 1);

                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.5f);
                    openMenu(player, false);
                    return;
                }

                if (item.getName().equalsIgnoreCase(TUtil.toColor("&bSorting Filter"))) {
                    if (e.getClick().isLeftClick()) {
                        BazaarFilter bazaarFilter = filter.getOrDefault(player.getUniqueId(),
                                new BazaarFilter("", 0));
                        int value = bazaarFilter.getType() + 1;
                        if (value > 3) {
                            value = 0;
                        }

                        bazaarFilter.setType(value);
                        filter.put(player.getUniqueId(), bazaarFilter);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 2.0f);
                        updateAuctions(menuMap.get(player.getUniqueId()), player);

                    } else if (e.getClick().isRightClick()) {
                        BazaarFilter bazaarFilter = filter.getOrDefault(player.getUniqueId(),
                                new BazaarFilter("", 0));
                        int value = bazaarFilter.getType() - 1;
                        if (value < 0) {
                            value = 3;
                        }

                        bazaarFilter.setType(value);
                        filter.put(player.getUniqueId(), bazaarFilter);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 2.0f);
                        updateAuctions(menuMap.get(player.getUniqueId()), player);
                    }
                    return;
                }

                if (item.getName().equalsIgnoreCase(TUtil.toColor("&6Search"))) {
                    BazaarFilter bazaarFilter = filter.getOrDefault(player.getUniqueId(),
                            new BazaarFilter("", 0));

                    if (!bazaarFilter.getSearch().equals("")) {
                        if (e.getClick().isRightClick()) {
                            bazaarFilter.setSearch("");
                            filter.put(player.getUniqueId(), bazaarFilter);

                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.5f);
                            updateAuctions(menuMap.get(player.getUniqueId()), player);
                            return;
                        }
                    }

                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.5f);

                    TextComponent textComponent = new TextComponent(
                            TUtil.toColor("&e&lBAZAAR! &7Use &f/search (text) &7to search the bazaar!\n&bClick here to write the command faster!"));
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/search "));
                    textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(TUtil.toColor("&c&lUSAGE: &7/search (text)")).create()));

                    player.spigot().sendMessage(textComponent);

                    return;
                }

                if (item.getName().equalsIgnoreCase(TUtil.toColor("&cGo Back"))) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.5f);
                    TropicaMenu.getInstance().openTropicaMenu(player, false);
                    return;
                }

                if (isAuctionSlot(e.getRawSlot())) {
                    boolean canBuy = NBTEditor.hasString(item.getItemStack(), "true");

                    if (canBuy) {
                        double price = TUtil.fromFancyDouble(NBTEditor.getString(item.getItemStack(), "cost"));
                        int index = Integer.parseInt(NBTEditor.getString(item.getItemStack(), "index"));

                        JsonObject jsonObject = auctions.get(index);
                        String nbt = jsonObject.get("item").getAsString();
                        UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());

                        try {
                            Item boughtItem = BazaarCache.cache.get(nbt).clone();
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.25f);

                            PlayerWrapper wrapper = new PlayerWrapper(player);
                            wrapper.setDouble("cash", wrapper.getDouble("cash") - price);
                            Sidebar.updateCash(wrapper, -price);

                            StringBuilder showItem = new StringBuilder(item.getName());
                            if (boughtItem.hasLore()) {
                                for (String line : boughtItem.getLore()) {
                                    showItem.append("\n").append(line);
                                }
                            }
                            showItem.append("\n");
                            showItem.append("\n&8Amount: x").append(boughtItem.getAmount());

                            auctions.remove(index);

                            executorService.execute(() -> {
                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                                if (offlinePlayer.isOnline()) {
                                    Player p = offlinePlayer.getPlayer();

                                    String rank = Rank.getRank(player);
                                    String prefix = Rank.getPrefix(rank);

                                    String whoBought = prefix + player.getName();

                                    TextComponent t = new TextComponent(TUtil.toColor(
                                            "&e&lBAZAAR! &f"+whoBought + "&7 bought "));

                                    TextComponent i = new TextComponent(TUtil.toColor(item.getName()));
                                    i.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                            new ComponentBuilder(TUtil.toColor(showItem.toString())).create()));
                                    t.addExtra(i);
                                    t.addExtra(TUtil.toColor("&7! &6+$"+TUtil.toFancyDouble(price)));

                                    p.spigot().sendMessage(t);

                                    new BukkitRunnable() {

                                        @Override
                                        public void run() {
                                            PlayerWrapper playerWrapper = new PlayerWrapper(p);
                                            playerWrapper.setDouble("cash", playerWrapper.getDouble("cash") + price);
                                            Sidebar.updateCash(playerWrapper, price);

                                            if (p.getOpenInventory() != null) {
                                                if (p.getOpenInventory().getTitle().equalsIgnoreCase("Items Management")) {
                                                    openCurrentItems(p, false);
                                                }
                                            }
                                        }
                                    }.runTask(Tropica.getTropica());

                                } else {
                                    BazaarContainer.getInstance().getAsync(uuid.toString(),
                                            0.0D, new Consumer<Object>() {
                                        @Override
                                        public void accept(Object o) {
                                            double cash = (double) o;
                                            cash += price;

                                            BazaarContainer.getInstance().setAsync(uuid.toString(), cash, null);
                                        }
                                    });
                                }

                            });

                            Items.giveItem(player, boughtItem.getItemStack());

                            TextComponent t = new TextComponent(TUtil.toColor(
                                    "&e&lBAZAAR! &7You bought "));

                            TextComponent i = new TextComponent(TUtil.toColor(item.getName()));
                            i.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new ComponentBuilder(TUtil.toColor(showItem.toString())).create()));
                            t.addExtra(i);
                            t.addExtra(TUtil.toColor("&7! &c-$"+TUtil.toFancyDouble(price)));

                            player.spigot().sendMessage(t);

                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (menuMap.containsKey(p.getUniqueId())) {
                                    updateAuctions(menuMap.get(p.getUniqueId()), p);
                                }
                            }
                        } catch (ExecutionException ex) {
                            ex.printStackTrace();

                            new Text("&c&lERROR! &7Something went wrong in the bazaar! " +
                                    "Contact an administrator about this!").send(player);
                            player.closeInventory();
                        }
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.25f);
                    }
                    return;
                }

                if (e.getRawSlot() == 26) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.5f);
                    openCurrentItems(player, false);
                }
            }
            return;
        }
        if (e.getView().getTitle().equalsIgnoreCase("Items Management")) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                Item item = new Item(e.getCurrentItem());
                if (item.getName().equalsIgnoreCase(TUtil.toColor("&cGo Back"))) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.5f);
                    openMenu(player, false);
                    return;
                }

                if (e.getRawSlot() >= 0 && e.getRawSlot() <= 5*9) {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1.0f, 2.0f);

                    double price = TUtil.fromFancyDouble(NBTEditor.getString(item.getItemStack(), "cost"));
                    int index = Integer.parseInt(NBTEditor.getString(item.getItemStack(), "index"));

                    JsonObject jsonObject = auctions.get(index);
                    String nbt = jsonObject.get("item").getAsString();
                    UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());

                    Item boughtItem = null;
                    try {
                        boughtItem = BazaarCache.cache.get(nbt).clone();

                        StringBuilder showItem = new StringBuilder(item.getName());
                        if (boughtItem.hasLore()) {
                            for (String line : boughtItem.getLore()) {
                                showItem.append("\n").append(line);
                            }
                        }
                        showItem.append("\n");
                        showItem.append("\n&8Amount: x").append(boughtItem.getAmount());

                        auctions.remove(index);

                        Items.giveItem(player, boughtItem.getItemStack());

                        TextComponent t = new TextComponent(TUtil.toColor(
                                "&e&lBAZAAR! &7You took down "));

                        TextComponent i = new TextComponent(TUtil.toColor(item.getName()));
                        i.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder(TUtil.toColor(showItem.toString())).create()));
                        t.addExtra(i);
                        t.addExtra(TUtil.toColor("&7!"));

                        player.spigot().sendMessage(t);

                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (menuMap.containsKey(p.getUniqueId())) {
                                updateAuctions(menuMap.get(p.getUniqueId()), p);
                            }
                        }
                        openCurrentItems(player, false);

                    } catch (ExecutionException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean isAuctionSlot(int rawSlot) {
        for (int slot : staticSlots) {
            if (slot == rawSlot) {
                return true;
            }
        }

        return false;
    }

    private void openCurrentItems(Player player, boolean b) {
        Menu menu = new Menu(6*9, "Items Management");

        int slot = -1;
        int i = 0;
        for (JsonObject jsonObject : auctions) {
            slot += 1;

            final String uuid = jsonObject.get("uuid").getAsString();
            if (!player.getUniqueId().toString().equals(uuid)) {
                continue;
            }

            final double price = jsonObject.get("price").getAsDouble();

            Item item;
            try {
                item = BazaarCache.cache.get(jsonObject.get("item").getAsString()).clone();
            } catch (Exception e) {
                item = new Item(Material.BARRIER, 1, "&cError!");
            }

            item.setName(jsonObject.get("name").getAsString());

            List<String> lore = item.getLore();
            lore.add("");
            lore.add("&e&m                     ");
            lore.add("&7Cost: &6"+ TUtil.toFancyDouble(price)+" Cash");
            lore.add("");
            lore.add("&7You &b&lSELLING&7 this item &3:)");
            lore.add("&eClick to cancel the auction!");
            item.setLore(lore);

            item.setItemStack(NBTEditor.addString(item.getItemStack(), "cost", String.valueOf(price)));
            item.setItemStack(NBTEditor.addString(item.getItemStack(), "index", String.valueOf(slot)));

            menu.setSlot(i, item);
            i++;
        }

        menu.setSlot(49, new Item(Material.RED_STAINED_GLASS_PANE, 1, "&cGo Back"));

        menu.setSlot(51, new Item(Material.PAPER, 1, "&eHow to sell item(s)?",
                "&8Tutorial",
                "",
                "&3&lSELLING ITEMS!",
                "&e> &bUse &f/sell (price) &bin the chat,",
                "&bwhile holding &aitem &byou want to sell!",
                "",
                "&3&lCANCEL PART!",
                "&e> &bIf you wish to &ctake &bone of your",
                "&bauctions &cdown&b, you must use this menu.",
                "&bClick at the item you want to cancel!",
                "",
                "&7Auctions can get you a lot of &6Cash&7!")
                .addEnchantment(Enchantment.DURABILITY, 1).setHideflags(true));

        menu.open(player, b);
    }

    public void openMenu(Player player, boolean b) {
        if (!isLoaded) {
            new Text("&c&lOOPS! &7Wait a moment," +
                    " the &eBazaar &7is being set up!").send(player);
            return;
        }

        Menu menu;
        if (!menuMap.containsKey(player.getUniqueId())) {
            menu = new Menu(6*9, "Bazaar the Tropical Market");

            menu.setSlot(3, new Item(Material.GOLD_INGOT, 1,
                    "&aBuy",
                    "&7In this place you can buy",
                    "&7mostly everything you need.",
                    "",
                    "&eClick to browse!"));

            menu.setSlot(5, new Item(Material.HOPPER_MINECART, 1,
                    "&aSell",
                    "&7In this place you can sell",
                    "&7your items to earn &6Cash&7.",
                    "",
                    "&eClick to browse!"));
            menu.setSlot(49, new Item(Material.RED_STAINED_GLASS_PANE, 1, "&cGo Back"));

            menu.setSlot(26, new Item(Material.CHEST, 1, "&aItems Management",
                    "&7Manage the items you're selling.",
                    "&aPut up &7new auctions or &ccancel&7 old ones.",
                    "",
                    "&eClick to browse!"));

            menuMap.put(player.getUniqueId(), menu);

        } else {
            menu = menuMap.get(player.getUniqueId());
        }

        for (int i = 0; i < 7; i++) {
            menu.setSlot(37 + i, staticBlack);
        }

        //pages start
        int currentPage = pages.getOrDefault(player.getUniqueId(), 1);
        int auctionsSize = auctions.size();

        if ((double)auctionsSize / 21 > (double)currentPage) {
            menu.setSlot(43, staticNext);
        }

        if (currentPage > 1) {
            menu.setSlot(37, staticPrevious);
        }
        //pages end

        this.updateAuctions(menu, player);


        menu.open(player, b);
    }

    private void updateAuctions(Menu menu, Player player) {
        final UUID uniqueId = player.getUniqueId();

        int currentPage = pages.getOrDefault(uniqueId, 1);
        BazaarFilter bazaarFilter = filter.getOrDefault(uniqueId, new BazaarFilter("", 0));
        LinkedList<JsonObject> jsonObjects = this.getAuctionsByFilter(bazaarFilter.getType());

        for (int i = 0; i < 21; i++) {
            menu.setSlot(staticSlots[i], new Item(Material.AIR));
        }

        for (int i = 0; i < staticSize; i++) {
            final int index = i + (currentPage - 1) * staticSize;
            if (auctions.size()-1 < index) {
                break;
            }

            final int slot = staticSlots[i];
            final JsonObject jsonObject = jsonObjects.get(index);
            final double price = jsonObject.get("price").getAsDouble();
            final String seller = jsonObject.get("seller").getAsString();

            Item item;

            try {
                item = BazaarCache.cache.get(jsonObject.get("item").getAsString()).clone();
            } catch (Exception e) {
                item = new Item(Material.BARRIER, 1, "&cError!");
            }

            PlayerWrapper playerWrapper = new PlayerWrapper(player);
            double currentCash = playerWrapper.getDouble("cash");

            item.setName(jsonObject.get("name").getAsString());

            List<String> lore = item.getLore();
            lore.add("");
            lore.add("&e&m                     ");
            lore.add("&7Seller: "+seller);
            lore.add("&7Cost: &6"+ TUtil.toFancyDouble(price)+" Cash");
            lore.add("");

            if (!player.getUniqueId().toString().equalsIgnoreCase(jsonObject.get("uuid").getAsString())) {

                if (currentCash >= price) {
                    lore.add("&7You &a&lCAN&7 afford this &a:)");
                    lore.add("&a✔ &eClick to buy!");
                } else {
                    double neededCash = price - currentCash;

                    lore.add("&7You &c&lCAN'T&7 afford this &c:(");
                    lore.add("&4✘ &cYou're missing &6$" + TUtil.toFancyDouble(neededCash) + "&c!");
                }
            } else {
                lore.add("&7You &c&lCAN'T&7 buy this &c:(");
                lore.add("&4✘ &cThis is the item you're selling!");
            }
            item.setLore(lore);
            item.setItemStack(NBTEditor.addString(item.getItemStack(), "cost", String.valueOf(price)));
            item.setItemStack(NBTEditor.addString(item.getItemStack(), "index", String.valueOf(index)));
            if (!player.getUniqueId().toString().equalsIgnoreCase(jsonObject.get("uuid").getAsString())) {
                item.setItemStack(NBTEditor.addString(item.getItemStack(), "true", String.valueOf(true)));
            }

            menu.setSlot(slot, item);
        }

        final Item search = new Item(Material.OAK_SIGN, 1, "&6Search",
                "&7Input a text to search",
                "&7and find the matching items.",
                ""
        );
        List<String> searchLore = search.getLore();

        if (bazaarFilter.getSearch().equals("")) {
            searchLore.add("&eClick to search!");
        } else {
            searchLore.add("&7Searching for: &f"+bazaarFilter.getSearch());
            searchLore.add("");
            searchLore.add("&eLeft-click to search!");
            searchLore.add("&6Right-click to reset!");
        }
        search.setLore(searchLore);

        final Item sort = new Item(Material.COMPASS, 1, "&bSorting Filter",
                "&7Change the filter to your",
                "&7own preference.",
                "",
                "&7Currently sorting by:"
        );
        List<String> lore = sort.getLore();
        if (bazaarFilter.getType() == 0) {
            lore.add("&b➤ &a&lOldest");
            lore.add("&8Newest");
            lore.add("&8Highest");
            lore.add("&8Lowest");
        } else if (bazaarFilter.getType() == 1) {
            lore.add("&8Oldest");
            lore.add("&b➤ &a&lNewest");
            lore.add("&8Highest");
            lore.add("&8Lowest");
        } else if (bazaarFilter.getType() == 2) {
            lore.add("&8Oldest");
            lore.add("&8Newest");
            lore.add("&b➤ &a&lHighest");
            lore.add("&8Lowest");
        } else {
            lore.add("&8Oldest");
            lore.add("&8Newest");
            lore.add("&8Highest");
            lore.add("&b➤ &a&lLowest");
        }
        lore.add("");
        lore.add("&eLeft-click to go down!");
        lore.add("&6Right-click to go up!");
        sort.setLore(lore);

        menu.setSlot(41, sort);
        menu.setSlot(39, search);
    }

    private LinkedList<JsonObject> getAuctionsByFilter(int type) {
        LinkedList<JsonObject> jsonObjects = new LinkedList<>(getAuctions());
        if (type == 1) {
            jsonObjects = new LinkedList<>(Lists.reverse(jsonObjects));
        }
        return jsonObjects;
    }

    public LinkedList<JsonObject> getAuctions() {
        return auctions;
    }

    public static BazaarHandler getInstance() {
        return bazaarHandler;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (command.getName().equalsIgnoreCase("bazaar")) {
                openMenu(player, true);
                return true;
            }
            if (command.getName().equalsIgnoreCase("search")) {
                if (args.length >= 1) {
                    BazaarFilter bazaarFilter = filter.getOrDefault(player.getUniqueId(),
                            new BazaarFilter("", 0));
                    bazaarFilter.setSearch(args[0]);
                    filter.put(player.getUniqueId(), bazaarFilter);

                    openMenu(player, true);
                } else {
                    new Text("&c&lOOPS! &7Missing arguments: &f/search (text)&7!").send(player);
                }
                return true;
            }
            if (command.getName().equalsIgnoreCase("sell")) {
                if (args.length >= 1) {
                    try {
                        double price = TUtil.fromFancyDouble(args[0]);

                        if (price < 1) {
                            throw new Exception();
                        }

                        if (price > MAX_PRICE) {
                            throw new Exception();
                        }

                        if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() != Material.AIR) {

                            if (player.getInventory().getHeldItemSlot() == 8) {
                                new Text("&c&lOOPS! &7This item can't be sold!").send(player);
                                return true;
                            }

                            Item item = new Item(player.getInventory().getItemInMainHand().clone());
                            player.getInventory().getItemInMainHand().setAmount(0);
                            player.getInventory().setItemInMainHand(null);

                            String rank = Rank.getRank(player);
                            String prefix = Rank.getPrefix(rank);

                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("price", price);
                            jsonObject.addProperty("item", sCoder.itemStackArrayToBase64(new ItemStack[]{item.getItemStack()}));
                            jsonObject.addProperty("seller", prefix+player.getName());
                            jsonObject.addProperty("uuid", player.getUniqueId().toString());

                            if (item.getMeta().hasDisplayName()) {
                                jsonObject.addProperty("name", item.getName());
                            } else {
                                net.minecraft.server.v1_15_R1.ItemStack itemStack1 = CraftItemStack.asNMSCopy(new ItemStack(item.getType()).clone());
                                String originalName = itemStack1.getItem().g(itemStack1).getLegacyString();
                                jsonObject.addProperty("name", "&a"+originalName);
                            }

                            auctions.add(jsonObject);
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (menuMap.containsKey(p.getUniqueId())) {
                                    updateAuctions(menuMap.get(p.getUniqueId()), p);
                                }
                            }


                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);

                        } else {
                            new Text("&c&lOOPS! &7You aren't holding anything in your hand!").send(player);
                        }

                    } catch (Exception e) {
                        new Text("&c&lOOPS! &7This number is invalid, please try again!").send(player);
                    }
                }
            }
        }
        return true;
    }
}
