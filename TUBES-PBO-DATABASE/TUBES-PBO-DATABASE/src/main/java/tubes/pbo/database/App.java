package tubes.pbo.database;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class App {
    private static Scanner scanner = new Scanner(System.in);
    private static final Database dbHelper = new Database();

    public static void main(String[] args)

    {
        Transaksi transaksi = new Transaksi();

        // collection framework menggunakan hashmap untuk mengelola admin
        HashMap<String, String> admin = new HashMap<String, String>();
        admin.put("rodes", "123");
        admin.put("rio", "123");
        admin.put("zel", "123");

        Scanner i = new Scanner(System.in);
        String iadm;
        System.out.println("Driver Ready");
        System.out.print("Admin     : ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        // method string lowercase
        iadm = i.nextLine().toLowerCase();

        // string manipulasi menggunakan method equals
        if (admin.containsKey(username) && admin.get(username).equals(password)) {
            int option;

            // perulangan untuk menu
            do {
                System.out.println("\nMenu:");
                System.out.println("1. Tambah Barang");
                System.out.println("2. Lihat Semua Barang");
                System.out.println("3. Ubah Barang");
                System.out.println("4. Hapus Barang");
                System.out.println("5. Transaksi");
                System.out.println("6. Keluar");
                System.out.print("Pilih opsi: ");
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    // fungsi untuk menu
                    case 1:
                        tambahBarang();
                        break;
                    case 2:
                        lihatBarang();
                        break;
                    case 3:
                        editBarang();
                        break;
                    case 4:
                        hapusBarang();
                        break;
                    case 5:
                        transaksi.transaksi();
                        break;
                    case 6:
                        System.out.println("Keluar.");
                        break;
                    default:
                        System.out.println("Opsi Tidak");
                }
                // jika inputan bukan atau sama 6 maka break
            } while (option != 6);

        } else {
            System.out.println("Akun tidak tersedia. Program hanya dapat diakses oleh admin yang terdaftar");
        }
        i.close();

    }

    // method menambahkan barang
    private static void tambahBarang() {
        System.out.print("Nama Barang: ");
        String nama = scanner.nextLine();
        System.out.print("Harga Barang: ");
        double harga = scanner.nextDouble();
        System.out.print("Stok Barang: ");
        int stok = scanner.nextInt();
        scanner.nextLine(); // Mengonsumsi newline yang tersisa

        // membuat objek dari clas merchen
        Merchandise merchandise = new Merchandise(0, nama, harga, stok);
        // menggunakan method tambah barang di kelas merchend
        merchandise.tambahBarang();
    }

    // method edit barang
    private static void editBarang() {

        System.out.print("Masukkan ID barang yang akan diperbarui: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            // Mengambil objek Merchandise berdasarkan ID
            Merchandise merchandise = dbHelper.getBarangByID(id);
            if (merchandise == null) {
                System.out.println("Barang dengan ID " + id + " tidak ditemukan.");
                return;
            }

            System.out.println("Mengedit Barang dengan ID: " + id);
            System.out.print("Nama Barang baru: ");
            String nama = scanner.nextLine();
            System.out.print("Harga Barang baru: ");
            double harga = scanner.nextDouble();
            System.out.print("Stok Barang baru: ");
            int stok = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Update informasi pada objek merchandise
            merchandise.setNama(nama);
            merchandise.setHarga(harga);
            merchandise.setStok(stok);

            // Update barang di database
            dbHelper.updateBarang(merchandise);
            System.out.println("Barang berhasil diperbarui.");
        } catch (SQLException e) {
            System.out.println("Gagal memperbarui barang: " + e.getMessage());
        }
    }

    private static void hapusBarang() {
        System.out.print("Masukkan ID barang yang akan dihapus: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            // mengambil data database method deletebarang dengan ID
            dbHelper.deleteBarang(id);
            System.out.println("Barang berhasil dihapus.");
        } catch (SQLException e) {
            System.out.println("Gagal menghapus barang: " + e.getMessage());
        }
    }

    // metho untuk melihat semua barang
    private static void lihatBarang() {
        try {

            // mengambil data dari database getAll barang, di inisiasikan ke baralng list
            // dng tipe data list
            List<Merchandise> barangList = dbHelper.getAllBarang();
            // jika barang kosong
            if (barangList.isEmpty()) {
                System.out.println("Tidak ada barang.");
                return;
            }

            System.out.println("Daftar Barang:");
            // perulangan mengiterasi setia barang di list di inisiasi ke objek barang
            for (Merchandise barang : barangList) {
                // mengambil data
                System.out.printf("ID: %d, Nama: %s, Harga: %.2f, Stok: %d\n",
                        barang.getId(), barang.getNama(), barang.getHarga(), barang.getStok());
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil data barang: " + e.getMessage());
        }
    }

}
