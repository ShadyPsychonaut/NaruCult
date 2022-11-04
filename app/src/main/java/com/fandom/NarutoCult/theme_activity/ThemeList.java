package com.fandom.NarutoCult.theme_activity;

public class ThemeList {

    private String title;
    private int icon;
    private String trackUrl;

    public ThemeList() {

    }

    public ThemeList (String title, String trackUrl, int icon) {
        this.title = title;
        this.trackUrl = trackUrl;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }

    public String getTrackUrl() {
        return trackUrl;
    }
}
