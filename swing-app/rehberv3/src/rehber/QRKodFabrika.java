package rehber;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class QRKodFabrika {
    int genişlik = 300;
    int yükseklik = 300;
    String veri;
    String dosyayolu;

    public QRKodFabrika(String veri, String ad) {
        this.veri = veri;
        this.dosyayolu = "C:\\Users\\10Pro.22h2\\Desktop\\QRKodlar\\" + ad + ".png";
    }

    public void QROluşturucu() {
        Map<EncodeHintType, Object> özellik = new HashMap();
        özellik.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        özellik.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        özellik.put(EncodeHintType.MARGIN, 1);

        try {
            BitMatrix matris = (new MultiFormatWriter()).encode(this.veri, BarcodeFormat.QR_CODE, this.genişlik, this.yükseklik, özellik);
            Path dosyayoluPath = (new File(this.dosyayolu)).toPath();
            MatrixToImageWriter.writeToPath(matris, "PNG", dosyayoluPath);
        } catch (IOException | WriterException e) {
            ((Exception)e).printStackTrace();
        }
    }
}
