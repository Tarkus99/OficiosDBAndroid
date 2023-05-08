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

    public boolean insertUser(Usuario u){
        MysqlDB mysqlDB = new MysqlDB();
        Usuario aux  = mysqlDB.insertUser(u);
        if (aux!=null) {
            usuarios.add(aux);
            usuarios.sort(Usuario::compareTo);
            return true;
        }
        return false;
    }

    public boolean insertUserWithId(Usuario u){
        MysqlDB mysqlDB = new MysqlDB();
        Usuario aux = mysqlDB.insertUserWithId(u);
        if (aux!=null) {
            usuarios.add(aux);
            usuarios.sort(Usuario::compareTo);
            return true;
        }
        return false;
    }

    public int updateUser(Usuario u){
        int result;
        Usuario aux;
        MysqlDB mysqlDB = new MysqlDB();
        result = mysqlDB.updateUser(u);
        if(result != 0){
            aux = usuarios.get(usuarios.indexOf(u));
            aux.setNombre(u.getNombre());
            aux.setApellidos(u.getApellidos());
            aux.setOficio(u.getOficio());
            return 1;
        }else{
            return 0;
        }
    }

    public void deleteUser(Usuario u){
        boolean result;
        MysqlDB mysqlDB = new MysqlDB();
        result = mysqlDB.deleteUser(u);
        if (result){
            usuarios.remove(u);
        }
    }

    public void addUser(Usuario u){
        usuarios.add(u);
    }
}
