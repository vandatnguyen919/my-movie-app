package com.example.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.domain.utils.Constants;

import java.util.Objects;

public class MovieDto implements Parcelable {

    private int id;
    private String title;
    private String overview;
    private String releaseDate;
    private double voteAverage;
    private String backdropPath;
    private String posterPath;
    private boolean adult;
    private boolean isFavorite;

    //    private Long reminderTimestamp;

    public MovieDto() {
    }

    public MovieDto(int id, String title, String overview, String releaseDate, double voteAverage, String backdropPath, String posterPath, boolean adult) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.adult = adult;
    }

    protected MovieDto(Parcel in) {
        id = in.readInt();
        title = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
        backdropPath = in.readString();
        posterPath = in.readString();
        adult = in.readByte() != 0;
    }

    public static final Creator<MovieDto> CREATOR = new Creator<MovieDto>() {
        @Override
        public MovieDto createFromParcel(Parcel in) {
            return new MovieDto(in);
        }

        @Override
        public MovieDto[] newArray(int size) {
            return new MovieDto[size];
        }
    };

    public String getPosterPathUrl() {
        return Constants.IMAGE_BASE_URL + posterPath;
    }

    public String getBackdropPathUrl() {
        return Constants.IMAGE_BASE_URL + backdropPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate.replace("-", "/");
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseYear() {
        return releaseDate.split("-")[0];
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
        dest.writeString(backdropPath);
        dest.writeString(posterPath);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieDto movieDto = (MovieDto) o;
        return id == movieDto.id && Double.compare(voteAverage, movieDto.voteAverage) == 0 && adult == movieDto.adult && isFavorite == movieDto.isFavorite && Objects.equals(title, movieDto.title) && Objects.equals(overview, movieDto.overview) && Objects.equals(releaseDate, movieDto.releaseDate) && Objects.equals(backdropPath, movieDto.backdropPath) && Objects.equals(posterPath, movieDto.posterPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, overview, releaseDate, voteAverage, backdropPath, posterPath, adult, isFavorite);
    }
}
