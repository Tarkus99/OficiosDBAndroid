package com.example.myrecyclerviewexample.model;

public class Oficio {
    private int idOficio;
    private String descripcion;
    public Oficio(int idOficio, String descripcion) {
        this.idOficio=idOficio;
        this.descripcion=descripcion;
    }

    public int getIdOficio() {
        return idOficio;
    }

    public void setIdOficio(int idOficio) {
        this.idOficio = idOficio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
