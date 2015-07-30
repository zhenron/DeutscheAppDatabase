package xingke.deutscheappdatabase;

/**
 * Created by Xingke on 28/07/2015.
 */
public class Statistic {
    private int id;
    private String artikel;
    private String kasus;
    private String genus;
    private String dek_art;
    private String dek_adj;
    private String dek_sub;
    private int occurrences;
    private int rights;
    private int wrongs;
    private double success_rate;

    public Statistic() {
    }

    public Statistic(String artikel, String kasus, String genus, String dek_art, String dek_adj, String dek_sub) {
        this.artikel = artikel;
        this.kasus = kasus;
        this.genus = genus;
        this.dek_art = dek_art;
        this.dek_adj = dek_adj;
        this.dek_sub = dek_sub;
        this.occurrences = 0;
        this.rights = 0;
        this.wrongs = 0;
        this.success_rate = 0;
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

    public String getKasus() {
        return kasus;
    }

    public void setKasus(String kasus) {
        this.kasus = kasus;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getDek_art() {
        return dek_art;
    }

    public void setDek_art(String dek_art) {
        this.dek_art = dek_art;
    }

    public String getDek_adj() {
        return dek_adj;
    }

    public void setDek_adj(String dek_adj) {
        this.dek_adj = dek_adj;
    }

    public String getDek_sub() {
        return dek_sub;
    }

    public void setDek_sub(String dek_sub) {
        this.dek_sub = dek_sub;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }

    public int getRights() {
        return rights;
    }

    public void setRights(int rights) {
        this.rights = rights;
    }

    public int getWrongs() {
        return wrongs;
    }

    public void setWrongs(int wrongs) {
        this.wrongs = wrongs;
    }

    public double getSuccess_rate() {
        return success_rate;
    }

    public void setSuccess_rate(double success_rate) {
        this.success_rate = success_rate;
    }
}
