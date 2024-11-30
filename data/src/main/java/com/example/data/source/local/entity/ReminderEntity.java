package com.example.data.source.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminder")
public class ReminderEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int movieId;
    private long timestamp;

    public ReminderEntity() {
    }

    public ReminderEntity(int movieId, long timestamp) {
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
}
