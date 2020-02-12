package me.tecc.tropica.items;

import me.tecc.tropica.texts.Text;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class Item implements Cloneable {
    private ItemStack itemStack;

    public Item(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    public Item(Material material, int amount, String name) {
        this.itemStack = new Item(material, amount).setName(name).getItemStack();
    }

    public Item(Material material, int amount, String name, String... strings) {
        this.itemStack = new Item(material, amount).setName(name).setLore(strings).getItemStack();
    }

    public Item(Material material, int amount, String name, List<String> strings) {
        this.itemStack = new Item(material, amount).setName(name).setLore(strings).getItemStack();
    }

    public Item(Material material) {
        this(material, 1);
    }

    public Item(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Item setMaterial(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public Material getMaterial() {
        return this.itemStack.getType();
    }

    public Item setType(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public Material getType() {
        return this.itemStack.getType();
    }

    public Item setName(String string) {
        ItemMeta itemMeta = this.getMeta();
        itemMeta.setDisplayName(new Text(string).toString());
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public String getName() {
        if (this.getItemStack().hasItemMeta()) {
            ItemMeta itemMeta = this.getMeta();
            if (!itemMeta.hasDisplayName()) {
                return "";
            }
            return itemMeta.getDisplayName();
        }
        return "";
    }

    public boolean hasLore() {
        if (this.getItemStack().hasItemMeta()) {
            ItemMeta itemMeta = this.getMeta();
            return itemMeta.hasLore();
        }
        return false;
    }

    public Item setLore(String[] strings) {
        ItemMeta itemMeta = this.getMeta();
        itemMeta.setLore(new Text(strings).toList());
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public Item setLore(List<String> stringList) {
        ItemMeta itemMeta = this.getMeta();
        itemMeta.setLore(new Text(stringList).toList());
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public List<String> getLore() {
        if (this.getItemStack().hasItemMeta()) {
            ItemMeta itemMeta = this.getMeta();
            List<String> strings = new ArrayList<>();
            if (itemMeta.hasLore()) {
                strings = itemMeta.getLore();
            }
            return strings;
        }
        return new ArrayList<>();
    }

    public Item setUnbreakable(boolean unbreakable) {
        ItemMeta itemMeta = this.getMeta();
        itemMeta.setUnbreakable(unbreakable);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public Item setHideflags(boolean hideflags) {
        ItemMeta itemMeta = this.getMeta();
        if (hideflags) {
            itemMeta.addItemFlags(ItemFlag.values());
        } else {
            itemMeta.removeItemFlags(ItemFlag.values());
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public Item setHiddenEnchants(boolean hiddenEnchants) {
        ItemMeta itemMeta = this.getMeta();
        if (hiddenEnchants) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public Item setHiddenAttributes(boolean hiddenAttributes) {
        ItemMeta itemMeta = this.getMeta();
        if (hiddenAttributes) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        } else {
            itemMeta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public Item addEnchantment(Enchantment enchantment, int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public Item removeEnchantment(Enchantment enchantment) {
        this.itemStack.removeEnchantment(enchantment);
        return this;
    }

    public Item setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public int getAmount() {
        return this.itemStack.getAmount();
    }


    public Item setDurability(int durability) {
        this.itemStack.setDurability((short) durability);
        return this;
    }

    public int getDurability() {
        return this.itemStack.getDurability();
    }


    public Item setData(MaterialData materialData) {
        this.itemStack.setData(materialData);
        return this;
    }

    public ItemMeta getMeta() {
        return this.itemStack.getItemMeta();
    }

    public Item setSkullOwner(OfflinePlayer owner) {
        if (this.getMeta() instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) this.getMeta();
            skullMeta.setOwningPlayer(owner);
            this.itemStack.setItemMeta(skullMeta);
        }
        return this;
    }

    public OfflinePlayer getSkullOwner() {
        if (this.getMeta() instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) this.getMeta();
            return skullMeta.getOwningPlayer();
        }
        return null;
    }

    public Item setLeatherColor(Color color) {
        if (this.getMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta armorMeta = (LeatherArmorMeta) this.getMeta();
            armorMeta.setColor(color);
            this.itemStack.setItemMeta(armorMeta);
        }
        return this;
    }

    public Color getLeatherColor() {
        if (this.getMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta armorMeta = (LeatherArmorMeta) this.getMeta();
            return armorMeta.getColor();
        }
        return null;
    }

    public Item setPotionColor(Color color) {
        if (this.getMeta() instanceof PotionMeta) {
            PotionMeta potionMeta = (PotionMeta) this.getMeta();
            potionMeta.setColor(color);
            this.itemStack.setItemMeta(potionMeta);
        }
        return this;
    }


    public Item setMeta(ItemMeta itemMeta) {
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStack toItemStack() {
        return this.itemStack;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static boolean isNull(final Item item) {
        if (item == null) {
            return true;
        }
        if (item.itemStack == null) {
            return true;
        }
        return item.getType() == Material.AIR;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Item) {
            Item item = (Item) obj;
            return this.itemStack.equals(item.itemStack);
        }
        return false;
    }

    @Override
    public Item clone() {
        try {
            Item clone = (Item) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Item(this.itemStack.clone());
    }
}
