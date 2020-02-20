package me.tecc.tropica.commands;

import org.bukkit.command.CommandExecutor;

public abstract class AbstractCommand implements CommandExecutor {
    public AbstractCommand(String name) {
        this(name, "tropica.default");
    }
    public AbstractCommand(String name, String requiredPermission) {
        
    }
}
