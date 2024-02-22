package com.gerson.ardoak;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tablaVinos")
public class Vino {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    public String nombre;
    public String origen;
    public String tipo;
    public double precio;
    public String imagePath;

    public Vino() { }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {

        this.imagePath = imagePath;
    }
}
