package me.tecc.tropica.features.items;

import me.tecc.tropica.items.HeadManager;
import me.tecc.tropica.items.Item;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public enum TropicaItems {
    TROPICAL_BACKPACK(new Item(HeadManager.createSkull(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6L" +
                    "y90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E2YWQ4YWQ5MTNkZWYxM2JkNT" +
                    "c0MTY1NWU3N2QxMzRlYjFiNTdmMDI5NzBkYWE2YjMzMDgyNzU0ZDFhZmZjNCJ9fX0="))
        .setName("&aTropical Backpack &7(Right Click)")
        .setLore(new String[]{
        "&9Information!",
                "&7This tropical backpack can",
                "&7hold up to some items.",
                "",
                "&eRight click to open!"})
    ),


    JUMP_PAD(new Item(Material.SLIME_BLOCK, 1,
            "&aJump Pad &7(Place Down)",
            "&9Information!",
            "&7Place this down and jump on it",
            "&7to be launched into the air.",
            "",
            "&r &r &r &r &b◆&3&l COOL TIP &b◆",
            "&7Look at a certain direction while",
            "&7jumping to specify the destination.",
            "",
            "&ePlace on the ground!"
    ).addEnchantment(Enchantment.ARROW_INFINITE, 1).setHideflags(true)),

    ELEVATOR(new Item(Material.IRON_BLOCK, 1,
            "&fElevator &7(Place Down)",
            "Place at least 2 of these",
            "to travel up and down with them.",
            "",
            "&9&lNOTE:",
            "&aSneak &7to travel downwards, or &fJump",
            "&7to travel upwards!"));

    private Item item;

    TropicaItems(final Item item) {
        this.item = item;
    }

    public Item item() {
        return item;
    }
}
