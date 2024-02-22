package com.gerson.ardoak;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface VinosDao {
    @Query("SELECT * FROM tablaVinos")
    List<Vino> getAllVinos();

    @Insert
    void insertVino(Vino vino);
}
