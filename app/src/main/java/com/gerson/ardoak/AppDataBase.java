package com.gerson.ardoak;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = {Usuario.class, Vino.class}, version = 4)
@TypeConverters(Converters.class)
public abstract class AppDataBase extends RoomDatabase {
    public abstract UsuariosDao usuariosDao();
    public abstract VinosDao vinosDao();

    private static AppDataBase INSTANCE;

    public static AppDataBase obtainInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDataBase.class, "app_database")
                    .fallbackToDestructiveMigration()  // Migración destructiva
                    .build();
        }
        return INSTANCE;
    }
}
