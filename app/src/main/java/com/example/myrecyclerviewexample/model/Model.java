package com.example.myrecyclerviewexample.model;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private static Model model;
    private List<Usuario> usuarios;
    private List<Oficio> oficios;

    private Model() {
        usuarios = new ArrayList<>();
        oficios = new ArrayList<>();
    }

    public static Model getInstance() {
        if (model == null)
            model = new Model();

        return model;
    }

    public List<Usuario> getUsuarios() {
        if (usuarios.isEmpty()) {
            MysqlDB mysqlDB = new MysqlDB();
            usuarios = mysqlDB.getAllUsers();
        }
        return usuarios;
    }

    public List<Oficio> getOficios() {
        if (oficios.isEmpty()) {
            MysqlDB mysqlDB = new MysqlDB();
            oficios = mysqlDB.getAllOficios();
        }
        return oficios;
    }

    public int insertUser(String nombre, String apellidos, int oficio){
        MysqlDB mysqlDB = new MysqlDB();
        return mysqlDB.insertUser(nombre, apellidos, oficio);
    }

    public void addUser(Usuario u){
        usuarios.add(u);
    }
}
