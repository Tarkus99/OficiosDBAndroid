package com.example.myrecyclerviewexample.model;

import java.io.Serializable;

public class Usuario implements Serializable, Comparable<Usuario> {
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setOficio(int oficio) {
        this.oficio = oficio;
    }

    public int getId() {
        return id;
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

    @Override
    public int compareTo(Usuario u) {
        return apellidos.compareTo(u.getApellidos());
    }
    @Override
    public boolean equals(Object o){
        Usuario aux;
        if (o instanceof Usuario){
            aux = (Usuario) o;
            return this.getId()==aux.getId();
        }
        return false;
    }
}
