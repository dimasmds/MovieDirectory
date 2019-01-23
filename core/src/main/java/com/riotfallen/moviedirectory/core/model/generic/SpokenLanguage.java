package com.riotfallen.moviedirectory.core.model.generic;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpokenLanguage implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public SpokenLanguage() {
    }

    private SpokenLanguage(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<SpokenLanguage> CREATOR = new Parcelable.Creator<SpokenLanguage>() {
        @Override
        public SpokenLanguage createFromParcel(Parcel source) {
            return new SpokenLanguage(source);
        }

        @Override
        public SpokenLanguage[] newArray(int size) {
            return new SpokenLanguage[size];
        }
    };
}
