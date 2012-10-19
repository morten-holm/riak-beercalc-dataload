package dk.muhko;

/**
 * User: mholm
 * Date: 11/10/12
 */
public class Hop {
    private String name;
    private String weight;
    private String alpha;
    private String boil;

    public Hop(String name, String weight, String alpha, String boil) {
        this.name = name;
        this.weight = weight;
        this.alpha = alpha;
        this.boil = boil;
    }

    public String getName() {
        return name;
    }

    public String getWeight() {
        return weight;
    }

    public String getAlpha() {
        return alpha;
    }

    public String getBoil() {
        return boil;
    }

    public boolean include() {
        return !name.equals("None");
    }
}
