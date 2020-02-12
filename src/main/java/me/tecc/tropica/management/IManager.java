package me.tecc.tropica.management;

import java.util.List;

public interface IManager<T> {
    /**
     * Register a candidate to this manager.
     * @param t
     */
    void register(T t);
    void unregister(T t);
    List<T> getRegistrants();
}
