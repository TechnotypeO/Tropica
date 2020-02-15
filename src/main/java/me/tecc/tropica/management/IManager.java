package me.tecc.tropica.management;

import java.util.List;

public interface IManager<T> {
    /**
     * Register a candidate to this manager.
     * @param t The candidate to register.
     */
    void register(T t);

    /**
     * Unregister a candidate from this manager.
     * @param t The candidate to unregister.
     */
    void unregister(T t);

    /**
     * Gets all the current registrants
     * @return All registrants.
     */
    List<T> getRegistrants();
}
