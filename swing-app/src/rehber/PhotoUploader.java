package rehber;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class PhotoUploader {

    private static final String PHOTO_UPLOAD_URL = "http://51.20.181.136:8080/api/photos/upload";

    public static String uploadPhoto(File file, int contactId) throws Exception {
        if (!file.exists()) return null;

        String boundary = "*****" + System.currentTimeMillis() + "*****";
        HttpURLConnection conn = (HttpURLConnection) new URL(PHOTO_UPLOAD_URL).openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
            // contactId parametresini ekleme
            out.writeBytes("--" + boundary + "\r\n");
            out.writeBytes("Content-Disposition: form-data; name=\"contactId\"\r\n\r\n");
            out.writeBytes(String.valueOf(contactId));
            out.writeBytes("\r\n");

            // dosya parametresini ekleme
            out.writeBytes("--" + boundary + "\r\n");
            out.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n");
            out.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
            out.write(Files.readAllBytes(file.toPath()));
            out.writeBytes("\r\n--" + boundary + "--\r\n");
            out.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) response.append(line);
                return new org.json.JSONObject(response.toString()).getString("url");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Fotoğraf yükleme başarısız. Kod: " + responseCode);
            return null;
        }
    }
}
