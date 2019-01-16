package com.riotfallen.moviedirectory.core.model.generic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpokenLanguage {

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }
}
