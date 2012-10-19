package dk.muhko;

/**
 * User: mholm
 * Date: 11/10/12
 */
public class Malt {
    private String name;
    private String weight;
    private String ecb;

    public Malt(String name, String weight, String ecb) {
        this.name = name;
        this.weight = weight;
        this.ecb = ecb;
    }

    public String getName() {
        return name;
    }

    public String getWeight() {
        return weight;
    }

    public String getEcb() {
        return ecb;
    }

    public boolean include() {
        return !name.equals("None");
    }
}
