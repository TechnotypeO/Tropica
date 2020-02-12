package me.tecc.tropica.management;

public abstract class AbstractManager<T> {
    public abstract void register(T t);
    public abstract void unregister(T t);

    public <T extends AbstractManager<?>> T getInstance() {

    }
}
