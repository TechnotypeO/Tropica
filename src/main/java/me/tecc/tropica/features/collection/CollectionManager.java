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

public class CollectionManager {
    private static CollectionManager collectionManager;
    private final CollectionCommand collectionCommand;

    public CollectionManager() {
        collectionManager = this;
        this.collectionCommand = new CollectionCommand();

        Tropica.getTropica().getCommand("collection").setExecutor(collectionCommand);
    }

    public static CollectionManager getInstance() {
        return collectionManager;
    }

    public void handleEvent(Player player, ItemStack itemStack) {
        final Material material = itemStack.getType();
        final UUID uuid = player.getUniqueId();
        final String path = material.name() + "." + uuid.toString();
        final int amount = itemStack.getAmount();
        final String originalName;

        net.minecraft.server.v1_15_R1.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack.clone());
        originalName = itemStack1.getItem().g(itemStack1).getLegacyString();

        CollectionContainer.getInstance().getAsync(path, (double)0,
                new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {

                        double collection = (double) o;

                        if (collection < 1) {
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
                        collection += amount;
                        CollectionContainer.getInstance().setAsync(path, collection, null);
                    }
                });
    }
}
