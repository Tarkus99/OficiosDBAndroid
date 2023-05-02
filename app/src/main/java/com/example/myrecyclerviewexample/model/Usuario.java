package com.example.myrecyclerviewexample.model;

public class Usuario {
    private int imagen;
    private String nombre;
    private String apellidos;
    private int oficio;

    public Usuario(int imagen, String nombre, String apellidos, int oficio) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.oficio = oficio;
    }

    public int getImagen() {
        return imagen;
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
