package com.example.data.source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.data.source.local.dao.MovieDao;
import com.example.data.source.local.dao.ReminderDao;
import com.example.data.source.local.entity.MovieEntity;
import com.example.data.source.local.entity.ReminderEntity;

@Database(entities = {MovieEntity.class, ReminderEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

    public abstract ReminderDao reminderDao();
}
