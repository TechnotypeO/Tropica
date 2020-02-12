package me.tecc.tropica.management;

import java.util.ArrayList;
import java.util.List;

public class Supervisor implements IManager<IManager<?>> {
    private static final Supervisor instance = new Supervisor();

    public Supervisor() {
    }

    /**
     * The
     */
    private ArrayList<IManager<?>> registeredManagers = new ArrayList<>();


    @Override
    public void register(IManager<?> manager) {

    }

    @Override
    public void unregister(IManager<?> manager) {

    }

    @Override
    public List<IManager<?>> getRegistrants() {
        return registeredManagers;
    }

    public static Supervisor getInstance() {
        return instance;
    }
}
