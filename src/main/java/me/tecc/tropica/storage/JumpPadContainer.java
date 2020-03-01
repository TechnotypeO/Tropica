package me.tecc.tropica.storage;

import org.bukkit.plugin.Plugin;

public class JumpPadContainer extends AbstractContainer {
    private static JumpPadContainer jumpPadContainer;

    public JumpPadContainer(Plugin plugin, String filename) {
        super(plugin, filename);

        jumpPadContainer = this;
    }

    public static JumpPadContainer getInstance() {
        return jumpPadContainer;
    }
}
