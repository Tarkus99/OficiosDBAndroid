package com.example.myrecyclerviewexample.model;

import android.content.Context;

import com.example.myrecyclerviewexample.API.Connector;
import com.example.myrecyclerviewexample.DetailedView;
import com.example.myrecyclerviewexample.GestionarPreferencias;
import com.example.myrecyclerviewexample.MainActivity;
import com.example.myrecyclerviewexample.base.BaseActivity;
import com.example.myrecyclerviewexample.base.Parameters;

import java.lang.reflect.Parameter;
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

    public List<Empleado> getEmpleados(Context context){
        if (empleados.isEmpty()){
            empleados = Connector.getConector().getAsList(Empleado.class,
                    Parameters.PREFIJO +
                            GestionarPreferencias.getInstance().getIpConnection(context)+
                          Parameters.SUFIJO +
                          "usuarios");
            empleados.sort(Empleado::compareTo);
        }
        return empleados;
    }

    public List<Oficio> getOficios(Context context) {
        if (oficios.isEmpty()) {
            oficios = Connector.getConector().getAsList(Oficio.class,
                    Parameters.PREFIJO +
                            GestionarPreferencias.getInstance().getIpConnection(context)+
                            Parameters.SUFIJO +
                    "oficios");
        }
        return oficios;
    }

    public boolean updateUser(Empleado u, Context context){
        Empleado e;
        e = Connector.getConector().put(Empleado.class, u,
                Parameters.PREFIJO +
                        GestionarPreferencias.getInstance().getIpConnection(context)+
                        Parameters.SUFIJO +
                "usuarios");
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

    public boolean deleteUser(int id, Context context){
        Empleado e;
        e = Connector.getConector().delete(Empleado.class,
                Parameters.PREFIJO +
                        GestionarPreferencias.getInstance().getIpConnection(context)+
                        Parameters.SUFIJO +
                "usuarios/"+id);
        if (e!=null){
            empleados.remove(new Empleado(id, "", "", 0));
            return true;
        }
        return false;
    }

    public boolean createUser(Empleado empleado, Context context){
        Empleado e = Connector.getConector().post(Empleado.class, empleado,
                Parameters.PREFIJO +
                        GestionarPreferencias.getInstance().getIpConnection(context)+
                        Parameters.SUFIJO +
                "usuarios");
        if (e!=null){
            addUser(empleado);
            return true;
        }
        return false;
    }
}
