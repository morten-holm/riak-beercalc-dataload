package dk.muhko;

/**
 * User: mholm
 * Date: 11/10/12
 */
public class Fermentation {
    private String temperature;
    private String duration;

    public Fermentation(String temperature, String duration) {
        this.temperature = temperature;
        this.duration = duration;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
