package me.tecc.tropica.features.homes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.features.playerData.PlayerWrapper;
import me.tecc.tropica.texts.Text;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class HomeHandler implements CommandExecutor, Listener {
    private static HomeHandler homeHandler;

    public HomeHandler() {
        homeHandler = this;

        Bukkit.getPluginManager().registerEvents(this, Tropica.getTropica());
        Tropica.getTropica().getCommand("sethome").setExecutor(this);
        Tropica.getTropica().getCommand("delhome").setExecutor(this);
        Tropica.getTropica().getCommand("home").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("sethome")) {
                if (args.length >= 1) {
                    PlayerWrapper playerWrapper = new PlayerWrapper(player);
                    String homeName = args[0];

                    JsonArray jsonArray;
                    if (playerWrapper.getJsonObject().has("homes")) {
                        jsonArray = playerWrapper.getJsonArray("homes");
                    } else {
                        jsonArray = new JsonArray();
                    }

                    boolean hasHome = false;

                    for (JsonElement jsonElement : jsonArray) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        if (jsonObject.get("name").getAsString().equalsIgnoreCase(homeName)) {
                            hasHome = true;
                            break;
                        }
                    }

                    if (hasHome) {
                        new Text("&c&lOOPS! &7This home already exists!").send(player);
                    } else {
                        Location loc = player.getLocation();

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("name", args[0]);
                        jsonObject.addProperty("x", loc.getX());
                        jsonObject.addProperty("y", loc.getY());
                        jsonObject.addProperty("z", loc.getZ());
                        jsonObject.addProperty("yaw", loc.getYaw());
                        jsonObject.addProperty("pitch", loc.getPitch());
                        jsonObject.addProperty("world", loc.getWorld().getName());

                        jsonArray.add(jsonObject);

                        new Text("&a&lSUCCESS! &7Your home was successfully set!").send(player);
                    }
                    playerWrapper.setJsonArray("homes", jsonArray);

                } else {
                    new Text("&c&lOOPS! &7Missing arguments: &f/sethome (text)&7!").send(player);
                }
                return true;
            }

            if (command.getName().equalsIgnoreCase("delhome")) {
                if (args.length >= 1) {
                    PlayerWrapper playerWrapper = new PlayerWrapper(player);
                    String homeName = args[0];

                    JsonArray jsonArray;
                    if (playerWrapper.getJsonObject().has("homes")) {
                        jsonArray = playerWrapper.getJsonArray("homes");
                    } else {
                        jsonArray = new JsonArray();
                    }

                    boolean hasHome = false;
                    JsonObject home = null;

                    for (JsonElement jsonElement : jsonArray) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        if (jsonObject.get("name").getAsString().equalsIgnoreCase(homeName)) {
                            hasHome = true;
                            home = jsonObject;
                            break;
                        }
                    }

                    if (!hasHome) {
                        new Text("&c&lOOPS! &7This home doesn't exist!").send(player);
                    } else {
                        jsonArray.remove(home);
                        new Text("&a&lSUCCESS! &7Your home was successfully removed!").send(player);
                    }

                    playerWrapper.setJsonArray("homes", jsonArray);
                } else {
                    new Text("&c&lOOPS! &7Missing arguments: &f/delhome (text)&7!").send(player);
                }
                return true;
            }
            if (command.getName().equalsIgnoreCase("home")) {
                PlayerWrapper playerWrapper = new PlayerWrapper(player);

                JsonArray jsonArray;
                if (playerWrapper.getJsonObject().has("homes")) {
                    jsonArray = playerWrapper.getJsonArray("homes");
                } else {
                    new Text("&c&lOOPS! &7You don't have any homes yet!").send(player);
                    return true;
                }

                if (args.length >= 1) {
                    String homeName = args[0];

                    JsonObject home = null;

                    for (JsonElement jsonElement : jsonArray) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        if (jsonObject.get("name").getAsString().equalsIgnoreCase(homeName)) {
                            home = jsonObject;
                            break;
                        }
                    }

                    if (home == null) {
                        new Text("&c&lOOPS! &7This home doesn't exist!").send(player);
                    } else {
                        Location loc;

                        double x, y, z;
                        float yaw, pitch;
                        World world;

                        x = home.get("x").getAsDouble();
                        y = home.get("y").getAsDouble();
                        z = home.get("z").getAsDouble();

                        yaw = home.get("yaw").getAsFloat();
                        pitch = home.get("pitch").getAsFloat();

                        world = Bukkit.getWorld(home.get("world").getAsString());

                        loc = new Location(world, x, y, z, yaw, pitch);
                        player.teleport(loc);

                        new Text("&a&lSUCCESS! &7Teleported to your home!").send(player);
                    }


                } else {
                    TextComponent textComponent = new TextComponent(TUtil.toColor("&c&lOOPS! &7Here's list of your homes:"));
                    TextComponent empty = new TextComponent(TUtil.toColor("\n"));

                    for (JsonElement jsonElement : jsonArray) {
                       JsonObject jsonObject = jsonElement.getAsJsonObject();
                       String name = jsonObject.get("name").getAsString();
                       TextComponent textComponent1 = new TextComponent(TUtil.toColor("&9✥ &6"+name.toUpperCase()));
                       textComponent1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home "+name));
                       textComponent1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                               new ComponentBuilder(TUtil.toColor("&7Click here to teleport to &6"+name+"&7 home!")).create()));

                       textComponent.addExtra(empty);
                       textComponent.addExtra(textComponent1);
                    }

                    player.spigot().sendMessage(textComponent);
                }
                return true;
            }
        }
        return true;
    }


    public static HomeHandler getInstance() {
        return homeHandler;
    }
}
