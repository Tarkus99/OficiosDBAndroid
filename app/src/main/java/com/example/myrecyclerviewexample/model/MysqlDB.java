package com.example.myrecyclerviewexample.model;

import android.util.Log;

import com.mysql.jdbc.exceptions.MySQLDataException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MysqlDB {

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return DriverManager.getConnection("jdbc:mysql://10.13.0.4:3306/java","gabriel","1234");
    }

    public List<Usuario> getAllUsers(){
        List<Usuario> usuarios = new ArrayList<>();

        try(Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM USUARIO ORDER BY 3")
        ){
            int id,oficio;
            String nombre,apellidos;
            while(rs.next()){
                id=rs.getInt("idUsuario");
                nombre = rs.getString("nombre");
                apellidos= rs.getString("apellidos");
                oficio = rs.getInt("idOficio");
                usuarios.add(new Usuario(id,nombre,apellidos,oficio));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public List<Oficio> getAllOficios() {
        List<Oficio> oficios = new ArrayList<>();

        try(Connection c = getConnection();
            Statement statement = c.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM OFICIO")
        ){

            int idOficio;
            String descripcion;

            while (rs.next()){
                idOficio = rs.getInt("idOficio");
                descripcion = rs.getString("descripcion");
                oficios.add(new Oficio(idOficio,descripcion));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return oficios;
    }

    public int insertUser(String name, String apellidos, int oficio){
        String sql =
                "INSERT INTO USUARIO(nombre, apellidos, idOficio)" +
                        " VALUES('" + name + "', '" + apellidos + "', "  + oficio +")";
        try(Connection c = getConnection();
            Statement st = c.createStatement();
        ){
           return st.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public int updateUser(String nombre, String apellidos, int profesion, int idUsuario){
        String sql = "UPDATE USUARIO SET nombre = '" + nombre + "', apellidos = '" + apellidos
                + "', idOficio = " + profesion + " WHERE idUsuario = " + idUsuario;
        Log.d("caca", sql);
        try(
                Connection c = getConnection();
                Statement st = c.createStatement();
                )
        {
            return st.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteUser(Usuario u){
        String sql = "DELETE FROM USUARIO WHERE idUsuario = " + u.getId();
        try (
                Connection c = getConnection();
                Statement st = c.createStatement()
                ){
            return st.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
}
