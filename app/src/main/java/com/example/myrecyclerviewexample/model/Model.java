package com.example.myrecyclerviewexample.model;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private static Model model;
    private List<Usuario> list;

    private Model(){
        list = new ArrayList<>();
    }


    public static Model getInstance(){
        if(model==null)
            model = new Model();

        return model;
    }

    public List<Usuario> getUsuarios() {
        MysqlDB mysqlDB = new MysqlDB();
        list = mysqlDB.getAllUsers();
        return list;
    }
    public List<Oficio> getOficios(){

    }
}
