package rehber;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class KisiManager {
    private ArrayList<Kisi> kisiler = new ArrayList<>();
    private KisiTableModel tableModel;

    public KisiManager(ArrayList kisiler,KisiTableModel tableModel) {
        this.tableModel = tableModel;
        this.kisiler=kisiler;
        veritabanindanKisileriYukle();
    }

    private void veritabanindanKisileriYukle() {
        kisiler.clear();
        kisiler.addAll(KisiDAO.getTumKisiler());
        tableModel.fireTableDataChanged();
    }

    public boolean kisiEkle(String ad, String soyad, String telefon) {
        if (ad.isEmpty() || soyad.isEmpty() || telefon.isEmpty()) return false;

        for (Kisi mevcut : kisiler) {
            if (mevcut.getTelefon().equals(telefon)) return false;
        }

        Kisi yeniKisi = new Kisi(0, ad, soyad, telefon, "", "", "", 1);
        int yeniId = KisiDAO.kisiEkle(yeniKisi);

        QRKodFabrika qrKodFabrika = new QRKodFabrika(
                "http://51.20.181.136:8080/kisi.html?id="+yeniId, telefon
        );
        qrKodFabrika.QROluşturucu();

        if (yeniId != -1) {
            yeniKisi.setId(yeniId);
            kisiler.add(yeniKisi);
            tableModel.fireTableDataChanged();
            return true;
        }
        return false;   
    }




    private static final Gson gson = new Gson();

    public boolean kisiEkleService(Kisi kisi) {
        try {


            if (kisi.getAd() == null || kisi.getSoyad() == null || kisi.getTelefon() == null) {
                System.out.println("Gerekli alanlar boş, gönderilemiyor.");
                return false;
            }

            kisi.setEmail(null);
            kisi.setAdres(null);
            kisi.setFotoYolu(null);

            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            String json = gson.toJson(kisi);


            final String BASE_URL = "http://51.20.181.136:8080/api/contacts/add";
            // POST isteği
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes("utf-8"));
            }

            int responseCode = conn.getResponseCode();
            System.out.println("HTTP Response Code: " + responseCode);

            return responseCode == 200 || responseCode == 201;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }



    public void kisiSilById(int[] ids) {
        List<Kisi> silinecekKisiler = new ArrayList<>();

        for (int id : ids) {
            Kisi kisi = kisiler.stream()
                    .filter(k -> k.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (kisi != null) {
                File dosya = new File("C:\\Users\\10Pro.22h2\\Desktop\\QRKodlar\\" + kisi.getTelefon() + ".png");
                if (dosya.exists()) dosya.delete();

                KisiDAO.kisiSilById(id);
                silinecekKisiler.add(kisi);
            }
        }

        // Local listeden sil
        kisiler.removeAll(silinecekKisiler);
        tableModel.setKisiler(kisiler);
        tableModel.fireTableDataChanged();
    }



    public static boolean kisiSilService(int[] ids) {
        boolean basarili = true;

        for (int id : ids) {
            HttpURLConnection conn = null;
            try {
                final String BASE_URL = "http://51.20.181.136:8080/api/contacts/delete/";
                URL url = new URL(BASE_URL + id);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                int responseCode = conn.getResponseCode();
                System.out.println("DELETE ID " + id + " ResponseCode: " + responseCode);

                // Stream okuma sadece debug için
                try (InputStream is = (responseCode >= 200 && responseCode < 400) ? conn.getInputStream() : conn.getErrorStream()) {
                    if (is != null) {
                        while (is.read() != -1) {}
                    }
                }

                // Response code kontrolü
                if (responseCode != 200 && responseCode != 204) {
                    basarili = false;
                    System.out.println("ID " + id + " silinemedi, responseCode=" + responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
                basarili = false;
            } finally {
                if (conn != null) conn.disconnect();
            }
        }

        System.out.println("kisiSilService return: " + basarili);
        return basarili;
    }

}
