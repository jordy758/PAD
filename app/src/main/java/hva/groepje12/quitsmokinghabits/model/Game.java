package hva.groepje12.quitsmokinghabits.model;

import android.graphics.drawable.Drawable;

public class Game {
    private String gameName;
    private String gameDescription;
    private String packageName;
    private Drawable gameIcon;


    public Game(String gameName, String gameDescription, String packageName, Drawable gameIcon) {
        this.gameName = gameName;
        this.gameDescription = gameDescription;
        this.packageName = packageName;
        this.gameIcon = gameIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameDescription() {
        return gameDescription;
    }

    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
    }

    public Drawable getGameIcon() {
        return gameIcon;
    }

    public void setGameIcon(Drawable gameIcon) {
        this.gameIcon = gameIcon;
    }
}
