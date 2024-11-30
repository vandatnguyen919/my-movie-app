package com.example.domain.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ReminderDto {

    private int movieId;
    private long timestamp;
    private MovieDto movieDto;

    public ReminderDto() {
    }

    public ReminderDto(int movieId, long timestamp) {
        this.movieId = movieId;
        this.timestamp = timestamp;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public MovieDto getMovieDto() {
        return movieDto;
    }

    public void setMovieDto(MovieDto movieDto) {
        this.movieDto = movieDto;
    }

    public String getMovieTitle() {
        return movieDto.getTitle();
    }

    public String getMovieReleaseYear() {
        return movieDto.getReleaseYear();
    }

    public double getMovieVoteAverage() {
        return movieDto.getVoteAverage();
    }

    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public String getMoviePosterPathUrl() {
        return movieDto.getPosterPathUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReminderDto that = (ReminderDto) o;
        return movieId == that.movieId && timestamp == that.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, timestamp);
    }
}
