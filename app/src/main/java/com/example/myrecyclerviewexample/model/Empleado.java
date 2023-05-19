package com.example.myrecyclerviewexample.model;

import java.io.Serializable;

public class Empleado implements Serializable, Comparable<Empleado> {
    private Integer idEmpleado;
    private String nombre;
    private String apellidos;
    private int idOficio;

    public Empleado(int idEmpleado, String nombre, String apellidos, int oficio) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.idOficio = oficio;
    }

    public Empleado(String nombre, String apellidos, int oficio) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.idOficio = oficio;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setIdOficio(int idOficio) {
        this.idOficio = idOficio;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public int getIdOficio() {
        return idOficio;
    }

    @Override
    public int compareTo(Empleado u) {
        return apellidos.compareTo(u.getApellidos());
    }
    @Override
    public boolean equals(Object o){
        Empleado aux;
        if (o instanceof Empleado){
            aux = (Empleado) o;
            return this.getIdEmpleado()==aux.getIdEmpleado();
        }
        return false;
    }
}
