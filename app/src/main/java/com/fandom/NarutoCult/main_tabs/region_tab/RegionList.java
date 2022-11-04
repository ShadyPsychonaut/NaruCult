package com.fandom.NarutoCult.main_tabs.region_tab;

public class RegionList {

    private String regionName;
    private String regionCountry;
    private String imgLink;
    private String symLink;

    public RegionList() {

    }

    public RegionList(String regionName, String regionCountry, String imgLink, String symLink) {
        this.regionName = regionName;
        this.regionCountry = regionCountry;
        this.imgLink = imgLink;
        this.symLink = symLink;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getRegionCountry() {
        return regionCountry;
    }

    public String getImgLink() {
        return imgLink;
    }

    public String getSymLink() {
        return symLink;
    }
}
