package rehber;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.File;
import java.util.List;

public class DiplomatEtiketiYazdirici implements EtiketYazdırılabilir {

    @Override
    public void Yazdır(List<Kisi> kisiler, int sayfaBasiEtiketAdedi) {
        if (kisiler == null || kisiler.isEmpty()) return;

        PrinterJob job = PrinterJob.getPrinterJob();

        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
                int toplamSayfa = (int) Math.ceil((double) kisiler.size() / sayfaBasiEtiketAdedi);
                if (pageIndex >= toplamSayfa) return NO_SUCH_PAGE;

                Graphics2D g2d = (Graphics2D) g;

                double marginLeft = 20;
                double marginTop = 20;

                g2d.translate(pf.getImageableX() + marginLeft, pf.getImageableY() + marginTop);
                g2d.setFont(new Font("Arial", Font.PLAIN, 10));

                double usableWidth = pf.getImageableWidth() - 2 * marginLeft;
                double usableHeight = pf.getImageableHeight() - 2 * marginTop;

                int startIndex = pageIndex * sayfaBasiEtiketAdedi;
                int endIndex = Math.min(startIndex + sayfaBasiEtiketAdedi, kisiler.size());
                int etiketSayisi = endIndex - startIndex;

                int paddingX = 10;
                int paddingY = 10;

                int enIyiKolon = 1;
                int enIyiSatir = etiketSayisi;
                double enYuksekOran = 0;

                for (int kolon = 1; kolon <= etiketSayisi; kolon++) {
                    int satir = (int) Math.ceil((double) etiketSayisi / kolon);
                    double genislik = (usableWidth - (kolon - 1) * paddingX) / kolon;
                    double yukseklik = (usableHeight - (satir - 1) * paddingY) / satir;

                    double oran = genislik / yukseklik;

                    if (genislik > 60 && yukseklik > 60 && oran > enYuksekOran) {
                        enYuksekOran = oran;
                        enIyiKolon = kolon;
                        enIyiSatir = satir;
                    }
                }

                int etiketGenislik = (int) ((usableWidth - (enIyiKolon - 1) * paddingX) / enIyiKolon);
                int etiketYukseklik = (int) ((usableHeight - (enIyiSatir - 1) * paddingY) / enIyiSatir);

                for (int i = startIndex; i < endIndex; i++) {
                    Kisi kisi = kisiler.get(i);
                    int pozisyon = i - startIndex;

                    int x = (pozisyon % enIyiKolon) * (etiketGenislik + paddingX);
                    int y = (pozisyon / enIyiKolon) * (etiketYukseklik + paddingY);

                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.fillRect(x, y, etiketGenislik, etiketYukseklik);

                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, y, etiketGenislik, etiketYukseklik);

                    int yazY = y + 15;
                    g2d.drawString("Ad Soyad: " + kontrol(kisi.getAd()) + " " + kontrol(kisi.getSoyad()), x + 10, yazY);
                    yazY += 15;
                    g2d.drawString("Telefon: " + kontrol(kisi.getTelefon()), x + 10, yazY);
                    yazY += 15;
                    g2d.drawString("E-posta: " + kontrol(kisi.getEmail()), x + 10, yazY);
                    yazY += 15;
                    g2d.drawString("Adres: " + kontrol(kisi.getAdres()), x + 10, yazY);
                    yazY += 15;
                    g2d.drawString("Gönderen: MBB - MERSİN", x + 10, yazY);

                    String dosyaYolu = kisi.getFotoYolu();

                    int fotoGenişlik = etiketGenislik /5;
                    int fotoYükseklik = etiketYukseklik/5*4;

                    if (dosyaYolu != null && !dosyaYolu.isEmpty()) {
                        File dosya = new File(dosyaYolu);

                        try {
                            BufferedImage img = ImageIO.read(dosya);
                            if (img != null) {

                                g2d.drawImage(img, x + etiketGenislik - fotoGenişlik-10, y + 10, fotoGenişlik, fotoYükseklik, null);
                            } else {
                                g2d.drawString("[Fotoğraf Yüklenemedi]", x + etiketGenislik - fotoGenişlik-10, y + 30);
                            }
                        } catch (Exception ex) {
                            g2d.drawString("[Fotoğraf Yüklenemedi]", x + etiketGenislik - fotoGenişlik-10, y + 30);
                        }
                    } else {
                        g2d.drawString("[Fotoğraf Yok]", x + etiketGenislik - fotoGenişlik-10, y + 30);
                    }
                }
                return PAGE_EXISTS;
            }

            private String kontrol(String veri) {
                return (veri == null || veri.trim().isEmpty()) ? "boş" : veri;
            }
        });

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }
}
