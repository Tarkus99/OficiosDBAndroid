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

    public List<Empleado> getEmpleados(){
        if (empleados.isEmpty()){
            empleados = Connector.getConector().getAsList(Empleado.class, "usuarios");
        }
        return empleados;
    }

    public List<Oficio> getOficios() {
        if (oficios.isEmpty()) {
            oficios = Connector.getConector().getAsList(Oficio.class, "oficios");
        }
        return oficios;
    }

    public boolean insertUser(Empleado u){
        MysqlDB mysqlDB = new MysqlDB();
        Empleado aux  = mysqlDB.insertUser(u);
        if (aux!=null) {
            empleados.add(aux);
            empleados.sort(Empleado::compareTo);
            return true;
        }
        return false;
    }

    public boolean insertUserWithId(Empleado u){
        MysqlDB mysqlDB = new MysqlDB();
        Empleado aux = mysqlDB.insertUserWithId(u);
        if (aux!=null) {
            empleados.add(aux);
            empleados.sort(Empleado::compareTo);
            return true;
        }
        return false;
    }

    public int updateUser(Empleado u){
        int result;
        Empleado aux;
        MysqlDB mysqlDB = new MysqlDB();
        result = mysqlDB.updateUser(u);
        if(result != 0){
            aux = empleados.get(empleados.indexOf(u));
            aux.setNombre(u.getNombre());
            aux.setApellidos(u.getApellidos());
            aux.setIdOficio(u.getIdOficio());
            return 1;
        }else{
            return 0;
        }
    }

    public void deleteUser(Empleado u){
        boolean result;
        MysqlDB mysqlDB = new MysqlDB();
        result = mysqlDB.deleteUser(u);
        if (result){
            empleados.remove(u);
        }
    }

    public void addUser(Empleado u){
        empleados.add(u);
    }
}
