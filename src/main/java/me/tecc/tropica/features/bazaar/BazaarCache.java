package me.tecc.tropica.features.bazaar;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import me.tecc.tropica.items.Item;
import me.tecc.tropica.items.sCoder;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

public class BazaarCache {
    public static LoadingCache<String, Item> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Item>() {
                @Override
                public Item load(String s) throws Exception {
                    return new Item(sCoder.itemStackArrayFromBase64(s)[0]).clone();
                }
            });

    public static LoadingCache<String, Item> shop = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.HOURS)
            .build(new CacheLoader<String, Item>() {
                @Override
                public Item load(String s) throws Exception {
                    Item item = new Item(sCoder.itemStackArrayFromBase64(s)[0]).clone();
                    String originalName;

                    if (!item.getMeta().hasDisplayName()) {
                        net.minecraft.server.v1_15_R1.ItemStack itemStack1 = CraftItemStack.asNMSCopy(new ItemStack(item.getType()).clone());
                        originalName = "&a"+ itemStack1.getItem().g(itemStack1).getLegacyString();
                    } else {
                        originalName = item.getName();
                    }

                    return item.setName(originalName);
                }
            });
}
