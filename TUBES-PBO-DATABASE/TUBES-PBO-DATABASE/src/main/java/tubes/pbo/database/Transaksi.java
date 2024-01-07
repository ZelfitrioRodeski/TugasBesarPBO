package tubes.pbo.database;

import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Transaksi {
    private static Scanner scanner = new Scanner(System.in);
    private static final Database db = new Database();
    private int transaksiID;

    Date date = new Date();

    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss z");
    SimpleDateFormat sdfDate = new SimpleDateFormat("E, dd/MM/yyyy");

    // method untuk proint struk

    private static void printStruk(Struk struk, String namaBarang, double hargaSatuan, int jumlahBeli) {
        // mainpulasi string
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd/MM/yyyy HH:mm:ss");
        System.out.println("\nSTRUK TRANSAKSI");
        System.out.println("Tanggal: " + sdf.format(new Date()));
        System.out.println("Nama Pembeli: " + struk.getNamaPembeli());
        System.out.println("Nama Barang: " + namaBarang);
        System.out.println("Harga Satuan: " + hargaSatuan);
        System.out.println("Jumlah Beli: " + jumlahBeli);
        System.out.println("Total Harga: " + struk.getTotalHarga());
        System.out.println("Terima Kasih!");
    }

    // method untuk transaksi
    public void transaksi() {
        System.out.print("Nama Pembeli: ");
        String namaPembeli = scanner.nextLine();
        System.out.print("ID Barang: ");
        int barangID = scanner.nextInt();
        System.out.print("Jumlah Beli: ");
        int jumlah = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            // mengambil data dari database dengan id , kemudian di isi dengan objek mc
            Merchandise mc = db.getBarangByID(barangID);
            // jika mc kosong
            if (mc == null) {
                System.out.println("Barang tidak ditemukan.");
                return;
            }
            // jika inputan user besar dri tok yang ada
            if (jumlah > mc.getStok()) {
                System.out.println("Stok tidak cukup.");
                return;
            }
            //  logika perhitungan 
            double totalHarga = mc.getHarga() * jumlah;
            Struk struk = new Struk(namaPembeli, barangID, totalHarga, jumlah);

            db.addTransaksi(struk); // tambah kan data
            db.addTransaksiProduk(struk); // update transaksi
            db.updateStokBarang(barangID, mc.getStok() - jumlah); // update stok

            printStruk(struk, mc.getNama(), mc.getHarga(), jumlah);

        } catch (SQLException e) {
            System.out.println("Gagal melakukan transaksi: " + e.getMessage());
        }
    }

}
