package xingke.deutscheappdatabase;

/**
 * Created by Xingke on 26/07/2015.
 */
public class Substantiv {
    private int id;
    private String artikel;
    private String substantiv;

    public Substantiv() {
    }

    public Substantiv(String artikel, String substantiv) {
        this.artikel = artikel;
        this.substantiv = substantiv;
    }

    public Substantiv(int id, String artikel, String substantiv) {
        this.id = id;
        this.artikel = artikel;
        this.substantiv = substantiv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtikel() {
        return artikel;
    }

    public void setArtikel(String artikel) {
        this.artikel = artikel;
    }

    public String getSubstantiv() {
        return substantiv;
    }

    public void setSubstantiv(String substantiv) {
        this.substantiv = substantiv;
    }
}
