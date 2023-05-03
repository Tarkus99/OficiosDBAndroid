package com.example.myrecyclerviewexample.model;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int id;
    private String nombre;
    private String apellidos;
    private int oficio;

    public Usuario(int id, String nombre, String apellidos, int oficio) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.oficio = oficio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public int getOficio() {
        return oficio;
    }
}
