package me.tecc.tropica.events;

import me.tecc.tropica.exceptions.MethodNotFoundException;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AbstractEventHandler implements Listener {
    /**
     * The events that this event handler handles.
     *
     * @see Event
     */
    protected List<Class<? extends Event>> supportedEvents;

    public AbstractEventHandler(Class<? extends Event>... supportedEvents) {
        // set supported events to supportedEvents as list
        this.supportedEvents = Arrays.asList(supportedEvents);
    }

    /**
     * Handles Spigot events and redirects them to the proper method.
     *
     * @param event The event that has occurred.
     *
     * @see Event
     * @see HandlerMethod
     */
    @EventHandler
    public final void onEvent(Event event) throws MethodNotFoundException, InvocationTargetException, IllegalAccessException {
        // check if this class supports specified event
        if (!supportedEvents.contains(event.getClass()))
            // if it does not, return
            return;

        // get handler methods in this class
        Map<Class<? extends Event>, Method> methods = getHandlerMethods(this.getClass());

        // get method associated with the event
        Method eventMethod = methods.get(event.getClass());

        if (eventMethod == null)
            throw new MethodNotFoundException("Handler method not found for event type '" + event.getEventName() + "'!");

        eventMethod.invoke(this, event);
    }

    /**
     * Gets all available candidates for handler methods.
     * These must be annotated with the {@link HandlerMethod}
     * annotation.
     * This is the only check it performs, though.
     * @param cls The class to check for handler methods.
     *            This class must extend
     *            {@link AbstractEventHandler}, due to
     *            limitations set by Technotype.
     * @return The candidates for handle methods
     */
    private static Map<Class<? extends Event>, Method> getHandlerMethods(Class<? extends AbstractEventHandler> cls) {
        // create method map
        Map<Class<? extends Event>, Method> methods = new HashMap<>();
        // loop through the classes methods
        for (Method m : cls.getMethods()) {
            // check if handler annotation is not present
            if (!m.isAnnotationPresent(HandlerMethod.class))
                // if handler annotation is not present, continue
                continue;

            // set variable for containing annotation
            HandlerMethod handlerAnnotation = m.getAnnotation(HandlerMethod.class);

            // add method as candidate because it is valid
            methods.put(handlerAnnotation.value(), m);
        }

        // return all candidates
        return methods;
    }

    /**
     * Indicates that the target method handles a the event specified by {@link HandlerMethod#value()}
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    protected @interface HandlerMethod {
        Class<? extends Event> value();
    }
}
