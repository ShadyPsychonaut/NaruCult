package com.fandom.NarutoCult.main_tabs.people_tab.people_activity.your_views_fragment;

import com.google.firebase.database.Exclude;

public class UploadMyView {

    private String message;
    private String mKey;

    public UploadMyView(){
        //Empty constructor req
    }

    public UploadMyView(String mess){
        message = mess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key){
        mKey = key;
    }

}
