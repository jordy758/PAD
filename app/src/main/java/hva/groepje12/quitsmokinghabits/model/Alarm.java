package hva.groepje12.quitsmokinghabits.model;

public class Alarm {
    private int id;
    private String time;

    public int getId() {
        return id;
    }

    public String getTime() {
        return time.substring(0, 5);
    }

    public void setTime(String time) {
        this.time = time;
    }
}
