package rehber;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class GrupManager {

    private ArrayList<Grup> gruplar = new ArrayList<>();
    public GrupManager()
    {
        gruplariYukle();
    }

    public ArrayList<Grup> getGruplar() {
        return gruplar;
    }

    private void gruplariYukle() {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=TelefonRehberiApp2;encrypt=true;trustServerCertificate=true;",
                "sa", "StrongPass1!");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Gruplar")) {

            gruplar.clear();

            while (rs.next()) {
                int id = rs.getInt("id");
                String ad = rs.getString("ad");
                gruplar.add(new Grup(id, ad));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void grupSil(int grupId) {
        String deleteKisiGrupSQL = "DELETE FROM KisiGrup WHERE grup_id = ?";
        String deleteGrupSQL = "DELETE FROM Gruplar WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=TelefonRehberiApp2;encrypt=true;trustServerCertificate=true;",
                "sa",
                "StrongPass1!")) {

            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(deleteKisiGrupSQL);
                 PreparedStatement ps2 = conn.prepareStatement(deleteGrupSQL)) {

                ps1.setInt(1, grupId);
                ps1.executeUpdate();

                ps2.setInt(1, grupId);
                ps2.executeUpdate();

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void grupComboBoxuYenile(JComboBox grupComboBox,JComboBox altGrupComboBox)
    {
        grupComboBox.removeAllItems();
        altGrupComboBox.removeAllItems();

        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=TelefonRehberiApp2;encrypt=true;trustServerCertificate=true;",
                "sa",
                "StrongPass1!"))
        {
            String sql = "SELECT *FROM Gruplar";
            try(PreparedStatement stmt= conn.prepareStatement(sql);ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    int grupid = rs.getInt("id");
                    String grupadi = rs.getString("ad");
                    Grup grup = new Grup(grupid,grupadi);
                    grupComboBox.addItem(grup);
                    altGrupComboBox.addItem(grup);
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
    }

    // GRUP EKLEME
    public Grup grupEkle(String grupAdi)
    {
        grupAdi = grupAdi.trim();

        if (grupAdi.isEmpty()) {
            System.out.println("Grup adı boş olamaz.");
            return null;
        }

        String sql = "INSERT INTO Gruplar (ad) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=TelefonRehberiApp2;encrypt=true;trustServerCertificate=true;",
                "sa", "StrongPass1!");
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, grupAdi);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Grup eklenemedi.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int yeniId = generatedKeys.getInt(1);
                    Grup yeniGrup = new Grup(yeniId, grupAdi);
                    gruplar.add(yeniGrup);
                    return yeniGrup;
                } else {
                    throw new SQLException("Grup ID'si alınamadı.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Grup> getTümGruplar() {
        ArrayList<Grup> gruplar = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=TelefonRehberiApp2;encrypt=true;trustServerCertificate=true;",
                "sa",
                "StrongPass1!")) {

            String sql = "SELECT * FROM Gruplar";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String isim = rs.getString("ad");
                    gruplar.add(new Grup(id, isim));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gruplar;
    }
}
