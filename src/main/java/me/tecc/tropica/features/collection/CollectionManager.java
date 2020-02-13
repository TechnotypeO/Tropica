package me.tecc.tropica.features.collection;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.storage.CollectionContainer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * This class has been made to handle collections in more
 * depth way like controlling the collect items and increase
 * collections.
 *
 * This class also works together with:
 * {@link CollectionFeature}
 */
public class CollectionManager {
    // the instance
    private static CollectionManager collectionManager;
    // the feature
    private final CollectionFeature collectionCommand;

    /**
     * Constructor of the class.
     * Registers the command and create new
     * instance of {@link CollectionFeature}
     */
    public CollectionManager() {
        collectionManager = this;
        this.collectionCommand = new CollectionFeature();

        // registration of the command
        Tropica.getTropica().getCommand("collection").setExecutor(collectionCommand);
    }

    /**
     * A method that handles the semi-event which is called
     * in {@link me.tecc.tropica.events.BasicEventHandler}.
     *
     * Responsible for controlling the increasement of
     * player collections and proper way of storing them.
     * Sends a fancy message on encountering a new item.
     *
     * @see Consumer
     * @see me.tecc.tropica.storage.AbstractContainer
     *
     * @param player the target player
     * @param itemStack the item
     */
    public void handleEvent(Player player, ItemStack itemStack) {
        // getting basic data
        final Material material = itemStack.getType();
        final UUID uuid = player.getUniqueId();
        final String path = material.name() + "." + uuid.toString();
        final int amount = itemStack.getAmount();
        final String originalName;

        // getting the original name of item
        net.minecraft.server.v1_15_R1.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack.clone());
        originalName = itemStack1.getItem().g(itemStack1).getLegacyString();

        // getting the collection
        CollectionContainer.getInstance().getAsync(path, (double)0,
                new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        // the amount of collection
                        double collection = (double) o;

                        // check for the new item
                        if (collection < 1) {
                            // fancy message and sounds
                            TextComponent textComponent = new TextComponent(TUtil.toColor(
                                    "\n&r &r &r &r &r &a&l◆ &2&lNEW COLLECTION UNLOCKED &a&l◆\n"+
                                            "&r &r &r&7You've unlocked &f&n"+originalName+"\n"+
                                            "\n"+
                                            "&r &r &r&7[&eClick here to open your collection menu&7]\n"
                            ));
                            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new ComponentBuilder(TUtil.toColor("&bClick here to open!")).create()));
                            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/collection "+material.toString()));

                            player.spigot().sendMessage(textComponent);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 2.0f);
                        }
                        // increasing the collection
                        collection += amount;

                        // saving process
                        CollectionContainer.getInstance().setAsync(path, collection, null);
                    }
                });
    }

    /**
     * Returns the instance of this class
     * @return instance of {@link CollectionManager}
     */
    public static CollectionManager getInstance() {
        return collectionManager;
    }
}
