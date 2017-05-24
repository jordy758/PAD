package hva.groepje12.quitsmokinghabits.model;

import android.support.annotation.NonNull;

import java.util.Date;

public class Goal implements Comparable<Goal> {
    private int id;
    private String goal;
    private double price;
    private Date achieved_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getFormattedPrice() {
        return Format.formatDoubleToPrice(price);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getAchievedAt() {
        return achieved_at;
    }

    @Override
    public int compareTo(@NonNull Goal o) {
        if (getAchievedAt() == null) {
            return -1;
        }

        if (o.getAchievedAt() == null) {
            return 1;
        }

        return o.getAchievedAt().compareTo(getAchievedAt());
    }
}
