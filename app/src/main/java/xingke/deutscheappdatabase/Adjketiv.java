package xingke.deutscheappdatabase;

/**
 * Created by Xingke on 26/07/2015.
 */
public class Adjketiv {
    private int id;
    private String adjektiv;

    public Adjketiv() {
    }

    public Adjketiv(String adjektiv) {
        this.adjektiv = adjektiv;
    }

    public Adjketiv(int id, String adjektiv) {
        this.id = id;
        this.adjektiv = adjektiv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdjektiv() {
        return adjektiv;
    }

    public void setAdjektiv(String adjektiv) {
        this.adjektiv = adjektiv;
    }
}
