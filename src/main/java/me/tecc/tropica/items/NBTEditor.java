package me.tecc.tropica.items;


import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTEditor {
    public static NBTTagCompound getTag(org.bukkit.inventory.ItemStack item) {
        net.minecraft.server.v1_16_R3.ItemStack itemNms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag;
        if (itemNms.hasTag()) tag = itemNms.getTag();
        else tag = new NBTTagCompound();
        return tag;
    }

    public static org.bukkit.inventory.ItemStack setTag(org.bukkit.inventory.ItemStack item, NBTTagCompound tag) {
        net.minecraft.server.v1_16_R3.ItemStack itemNms = CraftItemStack.asNMSCopy(item);
        itemNms.setTag(tag);
        return CraftItemStack.asBukkitCopy(itemNms);
    }

    public static org.bukkit.inventory.ItemStack addString(org.bukkit.inventory.ItemStack item, String name, String value) {
        NBTTagCompound tag = getTag(item);
        tag.setString(name, value);
        return setTag(item, tag);
    }

    public static org.bukkit.inventory.ItemStack removeString(org.bukkit.inventory.ItemStack item, String name) {
        NBTTagCompound tag = getTag(item);
        if (tag.hasKey(name)) {
            tag.remove(name);
        }
        return setTag(item, tag);
    }

    public static boolean hasString(org.bukkit.inventory.ItemStack item, String name) {
        NBTTagCompound tag = getTag(item);
        return tag.hasKey(name);
    }

    public static String getString(org.bukkit.inventory.ItemStack item, String name) {
        NBTTagCompound tag = getTag(item);
        return tag.getString(name);
    }

    public static org.bukkit.inventory.ItemStack addInteger(org.bukkit.inventory.ItemStack item, String name, int value) {
        NBTTagCompound tag = getTag(item);
        tag.setInt(name, value);
        return setTag(item, tag);
    }

    public static org.bukkit.inventory.ItemStack removeInteger(org.bukkit.inventory.ItemStack item, String name) {
        NBTTagCompound tag = getTag(item);
        if (tag.hasKey(name)) {
            tag.remove(name);
        }
        return setTag(item, tag);
    }

    public static boolean hasInteger(org.bukkit.inventory.ItemStack item, String name) {
        NBTTagCompound tag = getTag(item);
        return tag.hasKey(name);
    }

    public static int getInteger(org.bukkit.inventory.ItemStack item, String name) {
        NBTTagCompound tag = getTag(item);
        return tag.getInt(name);
    }

    public static ItemStack createGameItem(Material mat, int amount) {
        ItemStack itemStack = new ItemStack(mat, amount);
        itemStack = NBTEditor.addInteger(itemStack, "nonce", 1);


        return itemStack;
    }
}
