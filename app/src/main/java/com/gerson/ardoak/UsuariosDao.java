package com.gerson.ardoak;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UsuariosDao {

    @Insert
    long insert(Usuario usuario);
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND password = :password")
    Usuario login(String correo, String password);

    @Query("SELECT COUNT(*) FROM usuarios WHERE nombre = :nombreUsuario AND password = :contrasena")
    boolean existeUsuario(String nombreUsuario, String contrasena);
}
