package me.tecc.tropica.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.TileEntitySkull;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class HeadManager {
    /**
     * A method which sets a certain skull texture at a certain block/location
     * @param skinUrl the url of the texture
     * @param block the target block/location
     */
    @SuppressWarnings("unused")
    public static void setSkullUrl(final String skinUrl, final Block block) {
        block.setType(Material.PLAYER_HEAD);
        Skull skullData = (Skull)block.getState();
        skullData.setSkullType(SkullType.PLAYER);

        TileEntitySkull skullTile = (TileEntitySkull)((CraftWorld)block.getWorld()).getHandle().getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
        if (skullTile != null) {
            skullTile.setGameProfile(getNonPlayerProfile(skinUrl));
        }
        block.getState().update(true);
    }

    /**
     * A method which sets a certain skull texture at a certain block/location with
     * additionally included rotation for example: 1
     *
     * @param skinUrl the url of the texture
     * @param block the target block/location
     * @param rotation the rotation option
     */
    @SuppressWarnings({"unused", "deprecation"})
    public static void setSkullUrl(final String skinUrl, final Block block, final int rotation) {
        block.setType(Material.PLAYER_HEAD);
        Skull skullData = (Skull) block.getState();
        skullData.setSkullType(SkullType.PLAYER);
        skullData.getData().setData((byte) rotation);
        skullData.update(true);

        TileEntitySkull skullTile = (TileEntitySkull) ((CraftWorld) block.getWorld()).getHandle()
                .getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
        if (skullTile != null) {
            skullTile.setGameProfile(getNonPlayerProfile(skinUrl));
        }
    }


    private static GameProfile getNonPlayerProfile(final String skinURL) {
        GameProfile newSkinProfile = new GameProfile(UUID.randomUUID(), null);
        newSkinProfile.getProperties().put("textures", new Property("textures", skinURL));
        return newSkinProfile;
    }

    /**
     * Creates an item stack of custom head/skull
     * @param url provided and valid url which must be the value encoded string
     *            you can use {https://minecraft-heads.com/custom-heads} website to
     *            find your custom head and after clicking the chosen one scroll down and copy
     *            the field {Value} and paste it as your 'url' into the parameter.
     * @return the custom head item
     */
    public static ItemStack createSkull(final String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        if (url.isEmpty())
            return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");

        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();

        }
        head.setItemMeta(headMeta);

        return head;
    }
}
