package rehber;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;


public class KisiKartPanel extends JPanel {
    private Kisi kisi;
    private int index;
    private KisiTableModel tableModel;
    private List<Grup> grupİsimleri;

    private JLabel QRLabel;
    private JLabel fotoLabel;
    private JButton fotoSecButton;
    private JTextField adField, soyadField, telefonField, emailField, grupField;
    private JTextArea adresArea;
    private JButton guncelleButton;

    public KisiKartPanel(Kisi kisi, int index, KisiTableModel tableModel) {
        this.kisi = kisi;
        this.index = index;
        this.tableModel = tableModel;
        initComponents();
        loadDataToFields();
        setupListeners();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        QRLabel = new JLabel();
        QRLabel.setPreferredSize(new Dimension(100,100));
        QRLabel.setMaximumSize(new Dimension(100,100));
        QRLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        QRLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(QRLabel);

        add(Box.createRigidArea(new Dimension(0,10)));

        fotoLabel = new JLabel();
        fotoLabel.setPreferredSize(new Dimension(100, 100));
        fotoLabel.setMaximumSize(new Dimension(100, 100));
        fotoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        fotoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(fotoLabel);

        fotoSecButton = new JButton("Fotoğraf Seç");
        fotoSecButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(fotoSecButton);

        add(Box.createVerticalStrut(20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        adField = new JTextField(15);
        soyadField = new JTextField(15);
        telefonField = new JTextField(15);
        emailField = new JTextField(20);
        grupField = new JTextField(20);
        grupField.setEditable(false);
        adresArea = new JTextArea(3,20);
        adresArea.setLineWrap(true);
        adresArea.setWrapStyleWord(true);

        gbc.gridx=0; gbc.gridy=0;
        formPanel.add(new JLabel("Ad:"), gbc);
        gbc.gridx=1;
        formPanel.add(adField, gbc);

        gbc.gridx=0; gbc.gridy++;
        formPanel.add(new JLabel("Soyad:"), gbc);
        gbc.gridx=1;
        formPanel.add(soyadField, gbc);

        gbc.gridx=0; gbc.gridy++;
        formPanel.add(new JLabel("Telefon:"), gbc);
        gbc.gridx=1;
        formPanel.add(telefonField, gbc);

        gbc.gridx=0; gbc.gridy++;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx=1;
        formPanel.add(emailField, gbc);

        gbc.gridx=0; gbc.gridy++;
        formPanel.add(new JLabel("Adres:"), gbc);
        gbc.gridx=1;
        formPanel.add(new JScrollPane(adresArea), gbc);

        gbc.gridx=0; gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Gruplar:"), gbc);
        gbc.gridx=1;
        formPanel.add(grupField, gbc);

        add(formPanel);

        guncelleButton = new JButton("Güncelle");
        guncelleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(15));
        add(guncelleButton);
    }

    private void loadDataToFields() {
        // QR Kod yükle
        ImageIcon iconqr = new ImageIcon("C:\\Users\\10Pro.22h2\\Desktop\\QRKodlar\\" + kisi.getTelefon() + ".png");
        Image scaledqr = iconqr.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        QRLabel.setIcon(new ImageIcon(scaledqr));

        // Fotoğraf yükle
        String localFoto = kisi.getFotoYolu();
        if (localFoto != null && !localFoto.isEmpty()) {
            File fotoDosya = new File(localFoto);
            if (fotoDosya.exists()) {
                ImageIcon icon = new ImageIcon(localFoto);
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                fotoLabel.setIcon(new ImageIcon(img));
                fotoLabel.setText("");
            } else {
                fotoLabel.setText("Fotoğraf bulunamadı");
                fotoLabel.setIcon(null);
                fotoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                fotoLabel.setVerticalAlignment(SwingConstants.CENTER);
            }
        } else {
            fotoLabel.setText("Fotoğraf Yok");
            fotoLabel.setIcon(null);
            fotoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            fotoLabel.setVerticalAlignment(SwingConstants.CENTER);
        }

        // Diğer alanları doldur (ad, soyad, telefon, email, adres, grup)
        adField.setText(kisi.getAd());
        soyadField.setText(kisi.getSoyad());
        telefonField.setText(kisi.getTelefon());
        emailField.setText(kisi.getEmail());
        adresArea.setText(kisi.getAdres());
        List<Grup> grupİsimleri = KisiDAO.getGruplarByKisiId(kisi.getId());
        String sonuc = grupİsimleri.stream().map(Grup::getAd).collect(Collectors.joining(", "));
        grupField.setText(sonuc);
    }


    private void setupListeners() {
        fotoSecButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getAbsolutePath();
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                fotoLabel.setIcon(new ImageIcon(img));
                fotoLabel.setText("");
                kisi.setFotoYolu(path);
            }
        });


        guncelleButton.addActionListener(e -> {
            try {
                String yeniAd = adField.getText().trim();
                String yeniSoyad = soyadField.getText().trim();
                String yeniTelefon = telefonField.getText().trim();
                String yeniEmail = emailField.getText().trim();
                String yeniAdres = adresArea.getText().trim();
                String yeniFotoYolu = kisi.getFotoYolu();

                if (yeniAd.isEmpty() || yeniSoyad.isEmpty() || yeniTelefon.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ad, Soyad, Telefon boş bırakılamaz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Lokal veritabanını güncelleme
                try (Connection conn = DriverManager.getConnection(
                        "jdbc:sqlserver://localhost:1433;databaseName=TelefonRehberiApp2;encrypt=true;trustServerCertificate=true;",
                        "sa",
                        "StrongPass1!")) {

                    String sql = "UPDATE Kisiler SET ad=?, soyad=?, telefon=?, email=?, adres=?, fotoYolu=? WHERE id=?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, yeniAd);
                        stmt.setString(2, yeniSoyad);
                        stmt.setString(3, yeniTelefon);
                        stmt.setString(4, yeniEmail);
                        stmt.setString(5, yeniAdres);
                        stmt.setString(6, yeniFotoYolu);
                        stmt.setInt(7, kisi.getId());
                        stmt.executeUpdate();
                    }
                }

                kisi.setAd(yeniAd);
                kisi.setSoyad(yeniSoyad);
                kisi.setTelefon(yeniTelefon);
                kisi.setEmail(yeniEmail);
                kisi.setAdres(yeniAdres);

                File fotoFile = new File(yeniFotoYolu);
                if (fotoFile.exists()) {
                    String ec2FotoUrl = PhotoUploader.uploadPhoto(fotoFile, kisi.getId());
                    if (ec2FotoUrl != null) {
                        kisi.setFotoYolu(ec2FotoUrl);
                    }
                }

                // EC2 veritabanını güncelle
                EC2Client.addOrUpdateContact(kisi);

                // Tabloyu güncelle
                tableModel.setValueAt(yeniAd, index, 0);
                tableModel.setValueAt(yeniSoyad, index, 1);
                tableModel.setValueAt(yeniTelefon, index, 2);

                JOptionPane.showMessageDialog(this, "Kişi bilgileri güncellendi.");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Güncelleme sırasında hata oluştu!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

    }
}
