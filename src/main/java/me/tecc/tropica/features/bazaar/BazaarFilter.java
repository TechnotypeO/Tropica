package me.tecc.tropica.features.bazaar;

public class BazaarFilter {
    private String search;
    private int type;

    public BazaarFilter(String search, int type) {

        this.search = search;
        this.type = type;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSearch() {
        return search;
    }

    public int getType() {
        return type;
    }
}
