package tubes.pbo.database;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Merchandise extends Barang implements Template{
   
    private static Scanner scanner = new Scanner(System.in);
    private static final Database db= new Database();

    //konstruktor merchen
    public Merchandise(int id, String nama, double harga, int stok) {
        super(id,nama, harga, stok);
    }

    // Getters dan Setters
    public int getId() { return super.getId(); }
    public String getNama() { return super.getNama(); }
    public double getHarga() { return super.getHarga(); }
    public int getStok() { return super.getStok(); }
 

    // menerpakan method yang ada pada interface templet
    @Override 

    // method tambah barang
    public void tambahBarang() {
        try {
            // menambah ke database add barang yang inputany dari properties kelas ini
            db.addBarang(this); 
            System.out.println("Barang berhasil ditambahkan.");
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan barang: " + e.getMessage());
        }
    }

    @Override
    // method hapus barang
  public  void hapusBarang(){
    System.out.print("Masukkan ID barang yang akan dihapus: ");
    //mengambil inputan
    int id = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    try {
        //memperbarui database dengan id
        db.deleteBarang(id);
        System.out.println("Barang berhasil dihapus.");
    } catch (SQLException e) {
        System.out.println("Gagal menghapus barang: " + e.getMessage());
    }
    }
    @Override
    // method edit barang
    public void editBarang() {
        System.out.print("Masukkan ID barang yang akan diperbarui: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 
    
        System.out.print("Nama Barang baru: ");
        String nama = scanner.nextLine();
        System.out.print("Harga Barang baru: ");
        double harga = scanner.nextDouble();
        System.out.print("Stok Barang baru: ");
        int stok = scanner.nextInt();
        scanner.nextLine();
    
        // membuat objek mc dari class merechen
        Merchandise mc = new Merchandise(id,nama, harga, stok);
    
        try {
            // mengapdate data barang ke database
            db.updateBarang(mc);
            System.out.println("Barang berhasil diperbarui.");
        } catch (SQLException e) {
            System.out.println("Gagal memperbarui barang: " + e.getMessage());
        }
    }
    
    @Override
    // method untuk melihat barang
  public  void lihatBarang(){
    try {
        // mengmabil data semua barang  di inisiasikan ke baranglist  dengan tipe data List 
            List<Merchandise> barangList = db.getAllBarang();
            // jika barang kosong
            if (barangList.isEmpty()) {
                System.out.println("Tidak ada barang.");
                return;
            }
            System.out.println("Daftar Barang:");
            for (Barang barang : barangList) {
                System.out.printf("ID: %d, Nama: %s, Harga: %.2f, Stok: %d\n", barang.getId(), barang.getNama(), barang.getHarga(), barang.getStok());
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil data barang: " + e.getMessage());
        }    }

}
