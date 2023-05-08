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
    @Override
    public boolean equals (Object o){
        Oficio aux;
        if (o instanceof Oficio){
            aux = (Oficio) o;
            return this.idOficio==aux.getIdOficio();
        }
        return false;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
