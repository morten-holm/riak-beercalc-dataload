package dk.muhko;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mholm
 * Date: 11/10/12
 */
public class Beer {
    private String name;
    private String brewer;
    private List<Malt> malts = new ArrayList<Malt>();
    private List<Hop> hops = new ArrayList<Hop>();
    private List<Fermentation> fermentations = new ArrayList<Fermentation>();
    private String yeast;
    private String volume;
    private String efficiency;
    private String mashSchedule;
    private String boilVolume;
    private String attenuation;
    private String originalGravity;
    private String finalGravity;
    private String comments;

    public Beer(String name, String brewer, String yeast) {
        this.name = name;
        this.brewer = brewer;
        this.yeast = yeast;
    }

    public String getName() {
        return name;
    }

    public String getYeast() {
        return yeast;
    }

    public String getBrewer() {
        return brewer;
    }

    public void addMalt(Malt malt) {
        this.malts.add(malt);
    }

    public List<Malt> getMalts() {
        return malts;
    }

    public void addHop(Hop hop) {
        this.hops.add(hop);
    }

    public List<Hop> getHops() {
        return hops;
    }

    public void addFermentation(Fermentation fermentation) {
        this.fermentations.add(fermentation);
    }

    public List<Fermentation> getFermentations() {
        return fermentations;
    }
}
