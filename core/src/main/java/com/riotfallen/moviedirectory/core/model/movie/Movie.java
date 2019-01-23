package com.riotfallen.moviedirectory.core.model.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.riotfallen.moviedirectory.core.model.generic.Genre;
import com.riotfallen.moviedirectory.core.model.generic.ProductionCompany;
import com.riotfallen.moviedirectory.core.model.generic.SpokenLanguage;

import java.util.List;

public class Movie implements Parcelable {

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("production_companies")
    @Expose
    private List<ProductionCompany> productionCompanies = null;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("runtime")
    @Expose
    private Integer runtime;
    @SerializedName("spoken_languages")
    @Expose
    private List<SpokenLanguage> spokenLanguages = null;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;


    public String getBackdropPath() {
        return backdropPath;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public Integer getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public List<SpokenLanguage> getSpokenLanguages() {
        return spokenLanguages;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getVideo() {
        return video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.backdropPath);
        dest.writeTypedList(this.genres);
        dest.writeValue(this.id);
        dest.writeString(this.overview);
        dest.writeString(this.posterPath);
        dest.writeTypedList(this.productionCompanies);
        dest.writeString(this.releaseDate);
        dest.writeValue(this.runtime);
        dest.writeTypedList(this.spokenLanguages);
        dest.writeString(this.title);
        dest.writeValue(this.video);
        dest.writeValue(this.voteAverage);
        dest.writeValue(this.voteCount);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.backdropPath = in.readString();
        this.genres = in.createTypedArrayList(Genre.CREATOR);
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.productionCompanies = in.createTypedArrayList(ProductionCompany.CREATOR);
        this.releaseDate = in.readString();
        this.runtime = (Integer) in.readValue(Integer.class.getClassLoader());
        this.spokenLanguages = in.createTypedArrayList(SpokenLanguage.CREATOR);
        this.title = in.readString();
        this.video = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
        this.voteCount = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
