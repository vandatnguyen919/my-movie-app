package com.example.data.source.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastCrewResponse {

    @SerializedName("id")
    private int movieId;

    @SerializedName("cast")
    private List<CastCrew> casts;

    @SerializedName("crew")
    private List<CastCrew> crews;

    public CastCrewResponse() {
    }

    public CastCrewResponse(int movieId, List<CastCrew> casts, List<CastCrew> crews) {
        this.movieId = movieId;
        this.casts = casts;
        this.crews = crews;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public List<CastCrew> getCasts() {
        return casts;
    }

    public void setCasts(List<CastCrew> casts) {
        this.casts = casts;
    }

    public List<CastCrew> getCrews() {
        return crews;
    }

    public void setCrews(List<CastCrew> crews) {
        this.crews = crews;
    }
}
