package me.tecc.tropica.management;

import me.tecc.tropica.events.AbstractEventHandler;

import java.util.ArrayList;
import java.util.List;

public class EventHandlerManager implements IManager<AbstractEventHandler> {
    private static EventHandlerManager instance = new EventHandlerManager();
    private List<AbstractEventHandler> registeredEventHandlers;

    public EventHandlerManager() {
        registeredEventHandlers = new ArrayList<>();
    }

    @Override
    public void register(AbstractEventHandler abstractEventHandler) {
        // add event handler to list
        registeredEventHandlers.add(abstractEventHandler);
    }

    @Override
    public void unregister(AbstractEventHandler abstractEventHandler) {
        registeredEventHandlers.remove(abstractEventHandler);
    }

    @Override
    public List<AbstractEventHandler> getRegistrants() {
        return registeredEventHandlers;
    }

    public static EventHandlerManager getInstance() {
        return instance;
    }
}
