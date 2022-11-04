package com.fandom.NarutoCult.main_tabs.people_tab;

public class PeopleItem {
    private String imageResource;
    private String text1, text2, you_link, quo1, quo2, quo3, quo4;

    public PeopleItem(String imageRes, String txt1, String txt2, String you, String q1, String q2, String q3, String q4){
        imageResource = imageRes;
        text1 = txt1;
        text2 = txt2;
        you_link = you;
        quo1 = q1;
        quo2 = q2;
        quo3 = q3;
        quo4 = q4;
    }

    public String getImageResource(){
        return imageResource;
    }

    public String getText1(){
        return text1;
    }

    public String getText2() {
        return text2;
    }

    public String getYouLink() {
        return you_link;
    }

    public String getQuo1() {
        return quo1;
    }

    public String getQuo2() {
        return quo2;
    }

    public String getQuo3() {
        return quo3;
    }

    public String getQuo4() {
        return quo4;
    }
}
