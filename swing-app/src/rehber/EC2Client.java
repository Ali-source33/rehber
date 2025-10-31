package rehber;

import org.json.JSONObject;

import javax.swing.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EC2Client {

    private static final String BASE_URL = "http://51.20.181.136:8080/api/contacts";

    public static void addOrUpdateContact(Kisi kisi) throws Exception {
        String endpoint = (kisi.getId() == 0) ? "/add" : "/update/" + kisi.getId();
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject json = new JSONObject();
        json.put("ad", kisi.getAd());
        json.put("soyad", kisi.getSoyad());
        json.put("telefon", kisi.getTelefon());
        json.put("email", kisi.getEmail());
        json.put("adres", kisi.getAdres());
        json.put("photoUrl", kisi.getFotoYolu());

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.toString().getBytes());
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            JOptionPane.showMessageDialog(null, "İşlem başarıyla tamamlandı.");
        } else {
            JOptionPane.showMessageDialog(null, "EC2 güncelleme başarısız. Kod: " + responseCode);
        }
    }
}
