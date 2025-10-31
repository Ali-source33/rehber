package rehber;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KisiDAO {

    public static int kisiEkle(Kisi kisi) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=TelefonRehberiApp2;encrypt=true;trustServerCertificate=true;",
                "sa",
                "StrongPass1!")) {

            String sql = "INSERT INTO Kisiler (ad, soyad, telefon, email, adres, group_id) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, kisi.getAd());
                stmt.setString(2, kisi.getSoyad());
                stmt.setString(3, kisi.getTelefon());
                stmt.setString(4, kisi.getEmail());
                stmt.setString(5, kisi.getAdres());
                stmt.setInt(6, kisi.getGrupId());

                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static boolean kisiGrubaEkliMi(int kisiId, int grupId) {
        String sql = "SELECT COUNT(*) FROM KisiGrup WHERE kisi_id = ? AND grup_id = ?";
        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=TelefonRehberiApp2;encrypt=true;trustServerCertificate=true;",
                "sa",
                "StrongPass1!");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kisiId);
            ps.setInt(2, grupId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Gruba Ekleme
    public static void grubaEkle(int grupId, List<Integer> kisiIdList) {
        String sql = "INSERT INTO KisiGrup (kisi_id, grup_id) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=TelefonRehberiApp2;encrypt=true;trustServerCertificate=true;",
                "sa",
                "StrongPass1!"))
        {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql))
            {
                for (int kisiId : kisiIdList)
                {
                    if (!kisiGrubaEkliMi(kisiId, grupId)) {
                        ps.setInt(1, kisiId);
                        ps.setInt(2, grupId);
                        ps.addBatch();
                    }
                }
                ps.executeBatch();
            }
            conn.commit();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    // Gruptan çıkar
    public static void gruptanCikar(int grupId, List<Integer> kisiIdList) {
        String sql = "DELETE FROM KisiGrup WHERE grup_id = ? AND kisi_id = ?";

        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=TelefonRehberiApp2;encrypt=true;trustServerCertificate=true;",
                "sa",
                "StrongPass1!"))
        {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (int kisiId : kisiIdList) {
                    ps.setInt(1, grupId);
                    ps.setInt(2, kisiId);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Kisi> getKisilerByGrupId(int grupId) {
        List<Kisi> kisiler = new ArrayList<>();
        String sql = "SELECT k.id, k.ad, k.soyad, k.telefon, k.email, k.adres, k.fotoYolu, kg.grup_id " +  // <-- burada eklendi
                "FROM Kisiler k " +
                "JOIN KisiGrup kg ON k.id = kg.kisi_id " +
                "WHERE kg.grup_id = ?";

        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=TelefonRehberiApp2;encrypt=true;trustServerCertificate=true;",
                "sa",
                "StrongPass1!");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, grupId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Kisi kisi = new Kisi(
                        rs.getInt("id"),
                        rs.getString("ad"),
                        rs.getString("soyad"),
                        rs.getString("telefon"),
                        rs.getString("email"),
                        rs.getString("adres"),
                        rs.getString("fotoYolu"),
                        rs.getInt("grup_id")
                );
                kisiler.add(kisi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kisiler;
    }

    // Belirli bir kişinin tüm gruplarını getir
    public static List<Grup> getGruplarByKisiId(int kisiId) {
        List<Grup> gruplar = new ArrayList<>();
        String sql = "SELECT g.id, g.ad FROM Gruplar g " +
                "JOIN KisiGrup kg ON g.id = kg.grup_id " +
                "WHERE kg.kisi_id = ?";

        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=TelefonRehberiApp2;encrypt=true;trustServerCertificate=true;",
                "sa",
                "StrongPass1!");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, kisiId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                gruplar.add(new Grup(rs.getInt("id"), rs.getString("ad")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gruplar;
    }

    public static ArrayList<Kisi> getTumKisiler() {
        ArrayList<Kisi> kisiler = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=TelefonRehberiApp2;encrypt=true;trustServerCertificate=true;",
                "sa",
                "StrongPass1!")) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Kisiler");

            while (rs.next()) {
                Kisi k = new Kisi(
                        rs.getInt("id"),
                        rs.getString("ad"),
                        rs.getString("soyad"),
                        rs.getString("telefon"),
                        rs.getString("email"),
                        rs.getString("adres"),
                        rs.getString("fotoYolu"),
                        rs.getInt("group_id")
                );
                kisiler.add(k);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kisiler;
    }

    public static boolean kisiSilById(int id) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=TelefonRehberiApp2;encrypt=true;trustServerCertificate=true;",
                "sa",
                "StrongPass1!")) {

            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement("DELETE FROM KisiGrup WHERE kisi_id = ?")) {
                ps1.setInt(1, id);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement("DELETE FROM Kisiler WHERE id = ?")) {
                ps2.setInt(1, id);
                int rows = ps2.executeUpdate();

                conn.commit();
                return rows > 0;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}