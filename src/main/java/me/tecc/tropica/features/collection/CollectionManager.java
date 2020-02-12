package me.tecc.tropica.features.collection;

import me.tecc.tropica.storage.CollectionContainer;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.UUID;
import java.util.function.Consumer;

public class CollectionManager {
    private static CollectionManager collectionManager;

    public CollectionManager() {
        collectionManager = this;
    }

    public static CollectionManager getInstance() {
        return collectionManager;
    }

    public void handleEvent(EntityPickupItemEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity.getType() == EntityType.PLAYER) {

            final Material material = event.getItem().getItemStack().getType();
            final UUID uuid = livingEntity.getUniqueId();
            final String path = material.name() + "." + uuid.toString();
            final int amount = event.getItem().getItemStack().getAmount();

            CollectionContainer.getInstance().getAsync(path, (double)0,
                    new Consumer<Object>() {
                        @Override
                        public void accept(Object o) {

                            double collection = (double) o;
                            collection += amount;

                            CollectionContainer.getInstance().setAsync(path, collection, null);
                        }
                    });
        }
    }
}
