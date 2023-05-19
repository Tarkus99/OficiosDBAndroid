package com.example.myrecyclerviewexample.model;

import com.example.myrecyclerviewexample.API.Connector;
import com.example.myrecyclerviewexample.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private static Model model;
    private List<Empleado> empleados;
    private List<Oficio> oficios;

    private Model() {
        empleados = new ArrayList<>();
        oficios = new ArrayList<>();
    }

    public static Model getInstance() {
        if (model == null)
            model = new Model();

        return model;
    }
    public void addUser(Empleado u){
        empleados.add(u);
        empleados.sort(Empleado::compareTo);
    }

    public List<Empleado> getEmpleados(){
        if (empleados.isEmpty()){
            empleados = Connector.getConector().getAsList(Empleado.class, "usuarios");
            empleados.sort(Empleado::compareTo);
        }
        return empleados;
    }

    public List<Oficio> getOficios() {
        if (oficios.isEmpty()) {
            oficios = Connector.getConector().getAsList(Oficio.class, "oficios");
        }
        return oficios;
    }

    public boolean updateUser(Empleado u){
        Empleado e;
        e = Connector.getConector().put(Empleado.class, u, "usuarios");
        if(e!=null){
            e = empleados.get(empleados.indexOf(u));
            e.setNombre(u.getNombre());
            e.setApellidos(u.getApellidos());
            e.setIdOficio(u.getIdOficio());
            empleados.sort(Empleado::compareTo);
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteUser(int id){
        Empleado e;
        e = Connector.getConector().delete(Empleado.class, "usuarios/"+id);
        if (e!=null){
            empleados.remove(new Empleado(id, "", "", 0));
            return true;
        }
        return false;
    }

    public boolean createUser(Empleado empleado){
        Empleado e = Connector.getConector().post(Empleado.class, empleado, "usuarios");
        if (e!=null){
            addUser(empleado);
            return true;
        }
        return false;
    }
}
