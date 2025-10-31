package rehber;
public class Grup {
    private int id;
    private String ad;

    public Grup(int id, String ad) {
        this.id = id;
        this.ad = ad;
    }

    public int getId() {
        return id;
    }

    public String getAd() {
        return ad;
    }

    @Override
    public String toString() {
        return ad;
    }
}
