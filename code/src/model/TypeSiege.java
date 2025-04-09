package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class TypeSiege {
    private int id;
    private String designation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void insert(Connection conn) throws SQLException {
        String query = "INSERT INTO Type_siege (designation) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, this.designation);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    public static TypeSiege getById(Connection conn, int id) throws SQLException {
        String query = "SELECT * FROM Type_siege WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    TypeSiege type = new TypeSiege();
                    type.setId(rs.getInt("id"));
                    type.setDesignation(rs.getString("designation"));
                    return type;
                }
            }
        }
        return null;
    }

    public static List<TypeSiege> getAll(Connection conn) throws SQLException {
        List<TypeSiege> types = new ArrayList<>();
        String query = "SELECT * FROM Type_siege";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                TypeSiege type = new TypeSiege();
                type.setId(rs.getInt("id"));
                type.setDesignation(rs.getString("designation"));
                types.add(type);
            }
        }
        return types;
    }

    public void update(Connection conn) throws SQLException {
        String query = "UPDATE Type_siege SET designation = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, this.designation);
            stmt.setInt(2, this.id);
            stmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String query = "DELETE FROM Type_siege WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        }
    }
}
