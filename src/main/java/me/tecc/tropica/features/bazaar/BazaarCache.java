package me.tecc.tropica.features.bazaar;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import me.tecc.tropica.items.Item;
import me.tecc.tropica.items.sCoder;

import java.util.concurrent.TimeUnit;

public class BazaarCache {
    public static LoadingCache<String, Item> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Item>() {
                @Override
                public Item load(String s) throws Exception {
                    return new Item(sCoder.itemStackArrayFromBase64(s)[0]).clone();
                }
            });


}
