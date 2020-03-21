package me.tecc.tropica.features.bazaar;

import me.tecc.tropica.features.collection.CollectionManager;
import me.tecc.tropica.items.NBTEditor;
import me.tecc.tropica.items.sCoder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class BazaarItem {
    private final static LinkedList<BazaarItem> l = new LinkedList<>();
    private final static LinkedHashMap<BazaarCategory, LinkedList<BazaarItem>> cachedGroups = new LinkedHashMap<>();
    private static BazaarCategory c = null;

    private final Material mat;
    private final BazaarCategory bazaarCategory;
    private final double cost;
    private final String nbt;

    public BazaarItem(Material mat, double cost) {
        this.mat = mat;
        this.bazaarCategory = c;
        this.cost = cost;
        this.nbt = sCoder.itemStackArrayToBase64(new ItemStack[]{NBTEditor.createGameItem(mat, 1)});

        if (mat != Material.AIR) {
            LinkedList<BazaarItem> list = cachedGroups.getOrDefault(c, new LinkedList<>());
            list.add(this);
            cachedGroups.put(bazaarCategory, list);
        }

        l.add(this);
    }

    public String getNbt() {
        return nbt;
    }

    public static LinkedList<BazaarItem> getGroup(BazaarCategory bazaarCategory) {
        return cachedGroups.get(bazaarCategory);
    }

    public Material getMat() {
        return mat;
    }

    public BazaarCategory getBazaarCategory() {
        return bazaarCategory;
    }

    public double getBuy() {
        return cost;
    }

    public double getSell() {
        return Math.ceil(cost / 4);
    }

    public static void register() {
        l.clear();

        c = BazaarCategory.PLANTS;

        new BazaarItem(Material.WHEAT, 5);
        new BazaarItem(Material.HAY_BLOCK, 45);
        new BazaarItem(Material.WHEAT_SEEDS, 1);
        new BazaarItem(Material.EGG, 50);
        new BazaarItem(Material.CARROT, 10);
        new BazaarItem(Material.BEETROOT, 20);
        new BazaarItem(Material.BEETROOT_SEEDS, 4);
        new BazaarItem(Material.BEETROOT_SOUP, 100);
        new BazaarItem(Material.POTATO, 30);
        new BazaarItem(Material.POISONOUS_POTATO, 5);
        new BazaarItem(Material.PUMPKIN, 40);
        new BazaarItem(Material.PUMPKIN_SEEDS, 10);
        new BazaarItem(Material.PUMPKIN_PIE, 100);
        new BazaarItem(Material.MELON_SLICE, 20);
        new BazaarItem(Material.MELON_SEEDS, 2);
        new BazaarItem(Material.BROWN_MUSHROOM, 5);
        new BazaarItem(Material.BROWN_MUSHROOM_BLOCK, 45);
        new BazaarItem(Material.RED_MUSHROOM, 5);
        new BazaarItem(Material.RED_MUSHROOM_BLOCK, 45);
        new BazaarItem(Material.SUGAR_CANE, 20);
        new BazaarItem(Material.SUGAR, 20);
        new BazaarItem(Material.APPLE, 25);
        new BazaarItem(Material.CACTUS, 50);
        new BazaarItem(Material.SWEET_BERRIES, 50);
        new BazaarItem(Material.HONEY_BOTTLE, 60);
        new BazaarItem(Material.HONEY_BLOCK, 240);
        new BazaarItem(Material.COCOA_BEANS, 10);
        new BazaarItem(Material.BAMBOO, 1);
        new BazaarItem(Material.SEA_PICKLE, 100);
        new BazaarItem(Material.SEAGRASS, 10);
        new BazaarItem(Material.KELP, 1);
        new BazaarItem(Material.CHICKEN, 10);
        new BazaarItem(Material.BEEF, 10);
        new BazaarItem(Material.COD, 20);
        new BazaarItem(Material.MUTTON, 10);
        new BazaarItem(Material.PORKCHOP, 10);
        new BazaarItem(Material.RABBIT, 10);
        new BazaarItem(Material.DANDELION, 5);
        new BazaarItem(Material.POPPY, 5);
        new BazaarItem(Material.BLUE_ORCHID, 5);
        new BazaarItem(Material.ALLIUM, 5);
        new BazaarItem(Material.AZURE_BLUET, 5);
        new BazaarItem(Material.RED_TULIP, 5);
        new BazaarItem(Material.ORANGE_TULIP, 5);
        new BazaarItem(Material.WHITE_TULIP, 5);
        new BazaarItem(Material.PINK_TULIP, 5);
        new BazaarItem(Material.OXEYE_DAISY, 5);
        new BazaarItem(Material.CORNFLOWER, 5);
        new BazaarItem(Material.LILY_OF_THE_VALLEY, 5);
        new BazaarItem(Material.WITHER_ROSE, 5000);
        new BazaarItem(Material.SUNFLOWER, 10);
        new BazaarItem(Material.LILAC, 10);
        new BazaarItem(Material.ROSE_BUSH, 10);
        new BazaarItem(Material.PEONY, 10);
        new BazaarItem(Material.LILY_PAD, 10);
        new BazaarItem(Material.VINE, 10);
        new BazaarItem(Material.OAK_SAPLING, 5);
        new BazaarItem(Material.BIRCH_SAPLING, 5);
        new BazaarItem(Material.SPRUCE_SAPLING, 5);
        new BazaarItem(Material.JUNGLE_SAPLING, 5);
        new BazaarItem(Material.ACACIA_SAPLING, 5);
        new BazaarItem(Material.DARK_OAK_SAPLING,5);
        new BazaarItem(Material.OAK_LEAVES, 10);
        new BazaarItem(Material.BIRCH_LEAVES, 10);
        new BazaarItem(Material.SPRUCE_LEAVES, 10);
        new BazaarItem(Material.JUNGLE_LEAVES, 10);
        new BazaarItem(Material.ACACIA_LEAVES, 10);
        new BazaarItem(Material.DARK_OAK_LEAVES, 10);

        c = BazaarCategory.BLOCKS;

        new BazaarItem(Material.GRASS_BLOCK, 50);
        new BazaarItem(Material.GRASS, 5);
        new BazaarItem(Material.SAND, 10);
        new BazaarItem(Material.SANDSTONE, 40);
        new BazaarItem(Material.RED_SAND, 10);
        new BazaarItem(Material.RED_SANDSTONE, 40);
        new BazaarItem(Material.GRAVEL, 10);
        new BazaarItem(Material.STONE, 10);
        new BazaarItem(Material.ANDESITE, 10);
        new BazaarItem(Material.DIORITE, 10);
        new BazaarItem(Material.GRANITE, 10);
        new BazaarItem(Material.COBBLESTONE, 5);
        new BazaarItem(Material.OAK_LOG, 25);
        new BazaarItem(Material.BIRCH_LOG, 25);
        new BazaarItem(Material.SPRUCE_LOG, 25);
        new BazaarItem(Material.JUNGLE_LOG, 25);
        new BazaarItem(Material.ACACIA_LOG, 25);
        new BazaarItem(Material.DARK_OAK_LOG, 25);

        for (Material m : CollectionManager.getInstance().getCollectionCommand().getListOfMaterials()) {
            if (m.isItem() && m.toString().contains("_WOOL")) {
                new BazaarItem(m, 10);
            }
        }

        new BazaarItem(Material.CLAY, 50);
        new BazaarItem(Material.TERRACOTTA, 100);

        for (Material m : CollectionManager.getInstance().getCollectionCommand().getListOfMaterials()) {
            if (m.isItem() && m.toString().contains("_TERRACOTTA") && !m.toString().contains("_GLAZED")) {
                new BazaarItem(m, 100);
            }
        }

        for (Material m : CollectionManager.getInstance().getCollectionCommand().getListOfMaterials()) {
            if (m.isItem() && m.toString().contains("_GLAZED_TERRACOTTA")) {
                new BazaarItem(m, 200);
            }
        }

        new BazaarItem(Material.GLASS, 100);

        for (Material m : CollectionManager.getInstance().getCollectionCommand().getListOfMaterials()) {
            if (m.isItem() && m.toString().contains("_STAINED_GLASS") && !m.toString().contains("_PANE")) {
                new BazaarItem(m, 100);
            }
        }

        new BazaarItem(Material.SPONGE, 500);
        new BazaarItem(Material.OBSIDIAN, 500);

        new BazaarItem(Material.PRISMARINE, 250);
        new BazaarItem(Material.DARK_PRISMARINE, 250);

        new BazaarItem(Material.SOUL_SAND, 50);
        new BazaarItem(Material.NETHER_BRICKS, 250);
        new BazaarItem(Material.RED_NETHER_BRICKS, 250);

        new BazaarItem(Material.GLOWSTONE, 100);

        c = BazaarCategory.MINERALS;

        new BazaarItem(Material.COAL, 50);
        new BazaarItem(Material.COAL_BLOCK, 50*9);

        new BazaarItem(Material.IRON_INGOT, 150);
        new BazaarItem(Material.IRON_BLOCK, 150*9);

        new BazaarItem(Material.GOLD_INGOT, 250);
        new BazaarItem(Material.GOLD_BLOCK, 250*9);

        new BazaarItem(Material.LAPIS_LAZULI, 300);
        new BazaarItem(Material.LAPIS_BLOCK, 300*9);

        new BazaarItem(Material.REDSTONE, 250);
        new BazaarItem(Material.REDSTONE_BLOCK, 250*9);

        new BazaarItem(Material.QUARTZ, 250);
        new BazaarItem(Material.QUARTZ_BLOCK, 250*9);

        new BazaarItem(Material.DIAMOND, 750);
        new BazaarItem(Material.DIAMOND_BLOCK, 750*9);

        c = BazaarCategory.DROPS;

        new BazaarItem(Material.ROTTEN_FLESH, 25);
        new BazaarItem(Material.BONE, 25);
        new BazaarItem(Material.ARROW, 10);
        new BazaarItem(Material.GUNPOWDER, 50);
        new BazaarItem(Material.SPIDER_EYE, 30);
        new BazaarItem(Material.STRING, 10);
        new BazaarItem(Material.ENDER_PEARL, 75);
        new BazaarItem(Material.SLIME_BALL, 150);
        new BazaarItem(Material.LEATHER, 30);
        new BazaarItem(Material.FEATHER, 30);
        new BazaarItem(Material.RABBIT_HIDE, 50);
        new BazaarItem(Material.RABBIT_FOOT, 200);
        new BazaarItem(Material.BLAZE_ROD, 100);
        new BazaarItem(Material.MAGMA_CREAM, 200);
        new BazaarItem(Material.GHAST_TEAR, 500);

        c = BazaarCategory.VALUABLES;
        new BazaarItem(Material.TURTLE_EGG, 1000);
        new BazaarItem(Material.NAME_TAG, 1600);
        new BazaarItem(Material.SADDLE, 2000);
        new BazaarItem(Material.EXPERIENCE_BOTTLE, 500);
        new BazaarItem(Material.CONDUIT, 5000);
        new BazaarItem(Material.TOTEM_OF_UNDYING, 10000);
    }

    public static LinkedList<BazaarItem> getList() {
        return l;
    }
}
