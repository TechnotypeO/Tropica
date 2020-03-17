package me.tecc.tropica.events;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class MessageEventHandler extends AbstractEventHandler {
    /**
     * Instantiates a new MessageEventHandler.
     * This class can handle anything that extends AsyncPlayerChatEvent.
     */
    public MessageEventHandler() {
        // call super with all supported events
        super(AsyncPlayerChatEvent.class);
    }

    @HandlerMethod(AsyncPlayerChatEvent.class)
    public void onPlayerChatEvent(AsyncPlayerChatEvent event) {


    }
}
