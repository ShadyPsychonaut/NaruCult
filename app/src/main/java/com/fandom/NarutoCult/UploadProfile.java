package com.fandom.NarutoCult;

public class UploadProfile {

    private String username;
    private String favChar;
    private String imageUrl;
    private String favElem;

    public UploadProfile() {

    }

    public UploadProfile(String username, String favChar, String imageUrl, String favElem) {
        this.username = username;
        this.favChar = favChar;
        this.imageUrl = imageUrl;
        this.favElem = favElem;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setFavChar(String favChar) {
        this.favChar = favChar;
    }

    public String getFavChar() {
        return favChar;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setFavElem(String favElem) {
        this.favElem = favElem;
    }

    public String getFavElem() {
        return favElem;
    }
}
