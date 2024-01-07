package tubes.pbo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;  // Import for the Statement class


public class Database {
    private Connection connection;

    public Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tubes_pbo", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("MySQL JDBC Driver tidak ditemukan.");
        }
    }

    public void addBarang(Merchandise mc) throws SQLException {
        String query = "INSERT INTO produk (barangID,namaBarang, hargaBarang, Stok) VALUES (?,?, ?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, mc.getId());
            pstmt.setString(2, mc.getNama());
            pstmt.setDouble(3, mc.getHarga());
            pstmt.setInt(4, mc.getStok());
            pstmt.executeUpdate();
        }
    }


    // Melihat semua barang
    public List<Merchandise> getAllBarang() throws SQLException {
        List<Merchandise> barangList = new ArrayList<>();
        String query = "SELECT * FROM produk;";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery(query)) {
            while (rs.next()) {
                barangList.add(
                        new Merchandise(rs.getInt("barangID"), rs.getString("namaBarang"), rs.getDouble("hargaBarang"),
                                rs.getInt("Stok")));
            }
        }
        return barangList;
    }

    // Menghapus barang
    public void deleteBarang(int id) throws SQLException {
        String query = "DELETE FROM produk WHERE barangID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // Memperbarui barang
    public void updateBarang(Merchandise mc) throws SQLException {
        String query = "UPDATE produk SET namaBarang = ?, hargaBarang = ?, stok = ? WHERE barangID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, mc.getNama());
            pstmt.setDouble(2, mc.getHarga());
            pstmt.setInt(3, mc.getStok());
            pstmt.setInt(4, mc.getId());
            pstmt.executeUpdate();
        }
    }

    // Mendapatkan barang berdasarkan ID
    public Merchandise getBarangByID(int id) throws SQLException {
        String query = "SELECT * FROM produk WHERE barangID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Merchandise(rs.getInt("barangID"), rs.getString("namaBarang"),
                            rs.getDouble("hargaBarang"), rs.getInt("Stok"));
                }
            }
        }
        return null;
    }

    public void addTransaksi(Struk struk) throws SQLException {
        String query = "INSERT INTO transaksi (namaPembeli, totalBeli, TotalHarga) VALUES (?, ?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, struk.getNamaPembeli());
            pstmt.setInt(2, struk.getJumlahBeli());
            pstmt.setDouble(3, struk.getTotalHarga());
            pstmt.executeUpdate();
    
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    struk.setTransaksiID(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained.");
                }
            }
        }
    }
    

    public void updateStokBarang(int barangID, int newStok) throws SQLException {
        String query = "UPDATE produk SET Stok = ? WHERE barangID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, newStok);
            pstmt.setInt(2, barangID);
            pstmt.executeUpdate();
        }
    }

    public void addTransaksiProduk(Struk struk) throws SQLException {
        String query = "INSERT INTO transaksi_produk (transaksiID, barangID) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, struk.getTransaksiID());
            pstmt.setInt(2, struk.getBarangID());
            pstmt.executeUpdate();
        }
    }

 
}
