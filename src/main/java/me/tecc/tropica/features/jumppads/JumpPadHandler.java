package me.tecc.tropica.features.jumppads;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.items.Item;
import me.tecc.tropica.storage.JumpPadContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

public class JumpPadHandler implements Listener {
    private final Collection<JumpPad> jumpPadCollection = new ArrayList<>();
    private final Map<JumpPad, JumpPadRunnable> runnableMap = new HashMap<>();
    private final Map<UUID, Long> cooldown = new HashMap<>();
    private JumpPadContainer container = JumpPadContainer.getInstance();

    public JumpPadHandler() {
        Bukkit.getPluginManager().registerEvents(this, Tropica.getTropica());
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent e) {
        if (e.getItemInHand() != null) {
            Item item = new Item(e.getItemInHand());

            if (item.getName().equalsIgnoreCase(TUtil.toColor("&aJump Pad &7(Place Down)"))) {

                if (e.isCancelled()) {
                    return;
                }

                int x, y, z;

                x = e.getBlock().getX();
                y = e.getBlock().getY();
                z = e.getBlock().getZ();
                World world = e.getBlock().getWorld();

                final JumpPad jumpPad = new JumpPad(x, y, z, world);
                this.registerJumpPad(jumpPad);
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
       if (e.getBlock().getType() == Material.SLIME_BLOCK) {
           if (e.isCancelled()) {
               return;
           }
           int x, y, z;

           x = e.getBlock().getX();
           y = e.getBlock().getY();
           z = e.getBlock().getZ();
           World world = e.getBlock().getWorld();

           final JumpPad jumpPad = this.isJumpPad(x, y, z, world);
           if (jumpPad != null) {
                this.unregisterJumpPad(jumpPad);
           }
       }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (player.getLocation().getBlock().getRelative(0, -1, 0).getType() == Material.SLIME_BLOCK) {
            if (cooldown.containsKey(player.getUniqueId())) {
                long l = cooldown.get(player.getUniqueId());
                long current = System.currentTimeMillis();
                long difference = current - l;

                if (difference >= 2000) {
                    cooldown.remove(player.getUniqueId());
                } else {
                    return;
                }
            }
            cooldown.put(player.getUniqueId(), System.currentTimeMillis());

            Block b = player.getLocation().getBlock().getRelative(0, -1, 0);
            JumpPad jumpPad = isJumpPad(b.getX(), b.getY(), b.getZ(), b.getWorld());

            if (jumpPad != null) {
                jumpPad.launch(player);
            }
        }
    }

    private JumpPad isJumpPad(int x, int y, int z, World world) {
        for (JumpPad jumpPad : jumpPadCollection) {
            if (jumpPad.getX() == x && jumpPad.getY() == y && jumpPad.getZ() == z) {
                if (jumpPad.getWorld().getName().equalsIgnoreCase(world.getName())) {
                    return jumpPad;
                }
            }
        }

        return null;
    }

    private void unregisterJumpPad(JumpPad jumpPad) {
        jumpPadCollection.remove(jumpPad);

        container.getAsync("list", new ArrayList<>(), o -> {
            List<String> list = (List<String>) o;
            list.remove(jumpPad.toString());

            container.setAsync("list", list, null);
        });

        if (runnableMap.containsKey(jumpPad)) {
            runnableMap.get(jumpPad).cancel();
            runnableMap.remove(jumpPad);
        }
    }

    private void registerJumpPad(final JumpPad jumpPad) {
        jumpPadCollection.add(jumpPad);

        container.getAsync("list", new ArrayList<>(), o -> {
            List<String> list = (List<String>) o;
            list.add(jumpPad.toString());

            container.setAsync("list", list, null);
        });

       initializeParticles(jumpPad);
    }

    private void initializeParticles(JumpPad jumpPad) {
        if (!runnableMap.containsKey(jumpPad)) {

            JumpPadRunnable jumpPadRunnable = new JumpPadRunnable(jumpPad.getLocation());
            jumpPadRunnable.runTaskTimer(Tropica.getTropica(), 0L, 3L);
            runnableMap.put(jumpPad, jumpPadRunnable);
        }
    }

    public void initializationProcess() {
        container.getAsync("list", new ArrayList<>(), o ->{
            List<String> list = (List<String>) o;

            for (String s : list) {
                JumpPad jumpPad = JumpPad.parse(s);
                jumpPadCollection.add(jumpPad);
                initializeParticles(jumpPad);
            }
        });
    }
}
