package com.example.myrecyclerviewexample.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

        return DriverManager.getConnection("jdbc:mysql://10.13.0.4:3306/java", "gabriel", "1234");
    }

    public List<Empleado> getAllUsers() {
        List<Empleado> empleados = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM USUARIO ORDER BY 3")
        ) {
            int id, oficio;
            String nombre, apellidos;
            while (rs.next()) {
                id = rs.getInt("idUsuario");
                nombre = rs.getString("nombre");
                apellidos = rs.getString("apellidos");
                oficio = rs.getInt("idOficio");
                empleados.add(new Empleado(id, nombre, apellidos, oficio));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empleados;
    }

    public List<Oficio> getAllOficios() {
        List<Oficio> oficios = new ArrayList<>();

        try (Connection c = getConnection();
             Statement statement = c.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM OFICIO")
        ) {

            int idOficio;
            String descripcion;

            while (rs.next()) {
                idOficio = rs.getInt("idOficio");
                descripcion = rs.getString("descripcion");
                oficios.add(new Oficio(idOficio, descripcion));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return oficios;
    }

    public Empleado insertUser(Empleado u) {
        String sql =
                "INSERT INTO USUARIO(nombre, apellidos, idOficio)" +
                        " VALUES(?,?,?)";
        try (Connection c = getConnection();
             PreparedStatement pst = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            int pos = 0;
            pst.setString(++pos, u.getNombre());
            pst.setString(++pos, u.getApellidos());
            pst.setInt(++pos, u.getIdOficio());

            if (pst.executeUpdate() == 0)
                throw new SQLException("No se ha podido insertar el usuario.");

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setIdEmpleado(rs.getInt(1));
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Empleado insertUserWithId(Empleado u) {
        String sql =
                "INSERT INTO USUARIO VALUES(?,?,?,?)";
        try (Connection c = getConnection();
             PreparedStatement pst = c.prepareStatement(sql);
        ) {
            int pos = 0;
            pst.setInt(++pos, u.getIdEmpleado());
            pst.setString(++pos, u.getNombre());
            pst.setString(++pos, u.getApellidos());
            pst.setInt(++pos, u.getIdOficio());

            if (pst.executeUpdate() == 0)
                throw new SQLException("No se ha podido insertar el usuario.");
            return u;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public int updateUser(Empleado u) {
        String sql = "UPDATE USUARIO SET nombre = '" + u.getNombre() + "', apellidos = '" + u.getApellidos()
                + "', idOficio = " + u.getIdOficio() + " WHERE idUsuario = " + u.getIdEmpleado();
        try (
                Connection c = getConnection();
                Statement st = c.createStatement();
        ) {
            return st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean deleteUser(Empleado u) {
        String sql = "DELETE FROM USUARIO WHERE idUsuario = ?";
        try (
                Connection c = getConnection();
                PreparedStatement pst = c.prepareStatement(sql);
        ) {
            int pos = 0;
            pst.setInt(++pos, u.getIdEmpleado());
            return pst.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
