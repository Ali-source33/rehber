package rehber;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TelefonRehberiApp {
    private JComboBox<Grup> grupComboBox;
    private JComboBox<Grup> altPanelGrupComboBox;
    private JFrame frame;
    private JTextField adField, soyadField, telefonField ,aramaField;
    private JTable table;
    private KisiTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private ArrayList<Kisi> kisiler;
    private List<Kisi> secilenKisiler= new ArrayList<>();
    private GrupManager grupManager;
    private int tumKisilerGrupId = 1;
    private List<Grup> grupİsimleri;

    public TelefonRehberiApp() {
        kisiler = new ArrayList<>();
        Stil stil = new Stil();

        // Modern renkler
        Color arkaPlan = new Color(245, 248, 255);
        Color butonRenk = new Color(51, 153, 255);
        Color butonYazi = Color.WHITE;
        Font genelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font baslikFont = new Font("Segoe UI", Font.BOLD, 16);

        frame = new JFrame("Rehber");
        frame.setSize(1030, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        ImageIcon imgIcon = new ImageIcon(TelefonRehberiApp.class.getResource("/İmage/icon.png"));
        frame.setIconImage(imgIcon.getImage());

        // Üst panel
        JPanel girisPaneli = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JPanel ustPanel = new JPanel(new BorderLayout());
        JPanel yazdırPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 10));
        yazdırPanel.setBackground(arkaPlan);
        ustPanel.setBackground(arkaPlan);

        adField = new JTextField(10);
        soyadField = new JTextField(10);
        telefonField = new JTextField(10);
        JButton ekleButton = new JButton("Ekle");
        JButton yazdirButton = new JButton("yazdır");
        yazdirButton.setPreferredSize(new Dimension(200, 30));
        stil.styleButton(yazdirButton, new Color(150, 150, 150), butonYazi);
        stil.styleButton(ekleButton, butonRenk, butonYazi);

        stil.styleTextField(adField);
        stil.styleTextField(soyadField);
        stil.styleTextField(telefonField);
        //Telefon numarası sadece rakamlardan oluşmasını sağlama ve 11 rakamdan oluşmasını sağlama
        telefonField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
                if (telefonField.getText().length() >= 11) {
                    e.consume();
                }
            }
        });

        // ad ve soyadın sadece harflerden oluşması ve uzunluk sınırlaması
        adField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume();
                }
                if (adField.getText().length() >= 15) {
                    e.consume();
                }
            }
        });

        soyadField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume();
                }
                if (soyadField.getText().length() >= 15) {
                    e.consume();
                }
            }
        });

        //ÜST PANEL
        girisPaneli.add(new JLabel("Ad:"));
        girisPaneli.add(adField);
        girisPaneli.add(new JLabel("Soyad:"));
        girisPaneli.add(soyadField);
        girisPaneli.add(new JLabel("Telefon:"));
        girisPaneli.add(telefonField);
        girisPaneli.add(ekleButton);
        yazdırPanel.add(yazdirButton, BorderLayout.EAST);
        ustPanel.add(girisPaneli, BorderLayout.WEST);
        ustPanel.add(yazdırPanel, BorderLayout.EAST);

        //COMBOBOX PANEL
        JPanel comboboxpanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JPanel grupbuttonpanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        comboboxpanel.setMaximumSize(new Dimension(300, 50));
        grupbuttonpanel.setMaximumSize(new Dimension(300, 50));

        girisPaneli.setBackground(arkaPlan);
        grupComboBox = new JComboBox<>();
        grupComboBox.setEditable(false);

        altPanelGrupComboBox = new JComboBox<>();
        altPanelGrupComboBox.setPreferredSize(new Dimension(158, 25));
        altPanelGrupComboBox.setMaximumSize(new Dimension(158, 25));

        grupComboBox.setPreferredSize(new Dimension(158, 25));
        JButton grupEkleButton = new JButton("(+) Grup Ekle");
        stil.styleButton(grupEkleButton, new Color(102, 255, 0), butonYazi);
        JButton grupSilButton = new JButton("(-) Grup    Sil");
        stil.styleButton(grupSilButton, new Color(255, 77, 77), butonYazi);

        comboboxpanel.add(new JLabel("Gruplar:"));
        comboboxpanel.add(grupComboBox);
        grupbuttonpanel.add(grupEkleButton);
        grupbuttonpanel.add(grupSilButton);
        JPanel grupAnaPanel = new JPanel();
        grupAnaPanel.setLayout(new BoxLayout(grupAnaPanel, BoxLayout.Y_AXIS));

        grupAnaPanel.add(grupbuttonpanel);
        grupAnaPanel.add(comboboxpanel);
        grupAnaPanel.add(Box.createVerticalStrut(200));

        JPanel grupKişiAnaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        grupKişiAnaPanel.setLayout(new BoxLayout(grupKişiAnaPanel, BoxLayout.Y_AXIS));

        JPanel grubaKisiEklemeÇıkarmaPaneli = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JPanel kişiComboBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JPanel altGrupComboBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JPanel grubaEklemeComboboxlarıPaneli = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        grubaEklemeComboboxlarıPaneli.setLayout(new BoxLayout(grubaEklemeComboboxlarıPaneli, BoxLayout.Y_AXIS));
        grubaEklemeComboboxlarıPaneli.add(kişiComboBoxPanel);

        grubaEklemeComboboxlarıPaneli.add(altGrupComboBoxPanel);

        altGrupComboBoxPanel.add(new JLabel("Gruplar:"));
        altGrupComboBoxPanel.add(altPanelGrupComboBox);

        JButton grubaEkleButton = new JButton("(+) Gruba Ekle");
        stil.styleButton(grubaEkleButton, new Color(102, 255, 0), butonYazi);
        JButton gruptanÇıkarButton = new JButton("(-) Gruptan Çıkar");
        stil.styleButton(gruptanÇıkarButton, new Color(255, 77, 77), butonYazi);

        grubaKisiEklemeÇıkarmaPaneli.add(grubaEkleButton);
        grubaKisiEklemeÇıkarmaPaneli.add(gruptanÇıkarButton);
        grubaKisiEklemeÇıkarmaPaneli.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        altGrupComboBoxPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        grupKişiAnaPanel.add(grubaKisiEklemeÇıkarmaPaneli);
        grupKişiAnaPanel.add(grubaEklemeComboboxlarıPaneli);
        grupKişiAnaPanel.add(altGrupComboBoxPanel);
        grupKişiAnaPanel.add(kişiComboBoxPanel);

        JPanel eastPaneli = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        eastPaneli.setLayout(new BoxLayout(eastPaneli, BoxLayout.Y_AXIS));

        eastPaneli.add(grupAnaPanel);
        eastPaneli.add(grupKişiAnaPanel);

        //PANELLERİ EKLEME
        frame.add(eastPaneli, BorderLayout.EAST);
        frame.add(ustPanel, BorderLayout.NORTH);

        // Tablo
        kisiler = new ArrayList<>();
        tableModel = new KisiTableModel(kisiler);


        table = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        //Kişileri tabloya ekleme
        KisiManager KisiManager = new KisiManager(kisiler, tableModel);

        //GRUP LİSTESİ
        grupManager = new GrupManager();
        ArrayList<Grup> gruplar = grupManager.getTümGruplar();
        for (Grup grup : gruplar) {
            grupComboBox.addItem(grup);
            altPanelGrupComboBox.addItem(grup);
        }

        //TABLO AYARLARI
        table.setRowHeight(28);
        table.setFont(genelFont);
        table.getTableHeader().setFont(baslikFont);
        table.getTableHeader().setBackground(new Color(220, 235, 250));

        table.getColumn("⋮").setCellRenderer(new ButtonRenderer());
        table.getColumn("⋮").setCellEditor(new ButtonEditor(new JCheckBox(), kisiler, (KisiTableModel) table.getModel()));
        TableRowSorter<KisiTableModel> rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Alt panel Arama, Silme ve Hepsini Seç butonu
        JPanel altPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        altPanel.setBackground(arkaPlan);

        aramaField = new JTextField(20);
        stil.styleTextField(aramaField);

        altPanel.add(new JLabel("Ara:"));
        altPanel.add(aramaField);

        JButton silButton = new JButton("Seçilen Kişiyi Sil");
        stil.styleButton(silButton, new Color(255, 77, 77), butonYazi);
        JButton hepsiniseç = new JButton("Hepsini seç");
        stil.styleButton(hepsiniseç, new Color(102, 255, 0), butonYazi);

        altPanel.add(hepsiniseç);
        altPanel.add(silButton);

        frame.add(altPanel, BorderLayout.SOUTH);

        //Güncel seçilen kişiler
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                secilenKisiler.clear();
                int[] selectedRows = table.getSelectedRows();

                for (int row : selectedRows) {
                    int modelIndex = table.convertRowIndexToModel(row);
                    if (modelIndex >= 0 && modelIndex < kisiler.size()) {
                        Kisi kisi = kisiler.get(modelIndex);
                        secilenKisiler.add(kisi);
                    } else {
                        System.err.println("Geçersiz modelIndex: " + modelIndex + ", kisiler.size(): " + kisiler.size());
                    }
                }
            }
        });

        // Ekle butonu
        ekleButton.addActionListener(e -> {
            String ad = adField.getText().trim();
            String soyad = soyadField.getText().trim();
            String telefon = telefonField.getText().trim();

            boolean eklendi = KisiManager.kisiEkle(ad, soyad, telefon);

            // EC2'ye gönderme
            Kisi yeniKisi = new Kisi(0, ad, soyad, telefon, "", "", "", 1);
            boolean servisEklendi = KisiManager.kisiEkleService(yeniKisi);

            if (!eklendi || !servisEklendi) {
                JOptionPane.showMessageDialog(frame,
                        "Kişi eklenemedi. Lütfen bilgileri kontrol edin veya telefon numarası zaten kayıtlı.",
                        "Uyarı", JOptionPane.WARNING_MESSAGE);
                return;
            }

            tableModel.fireTableDataChanged();

            adField.setText("");
            soyadField.setText("");
            telefonField.setText("");
        });


        //Yazdır Buton Lambda
        yazdirButton.addActionListener(e -> {
            if (secilenKisiler == null || secilenKisiler.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Lütfen önce yazdırılacak kişileri seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                return;
            }
            YazdirmaFrame yazdirmaFrame = new YazdirmaFrame(secilenKisiler);
            yazdirmaFrame.setVisible(true);
        });

        //GRUP EKLE BUTTON LAMBDA
        grupEkleButton.addActionListener(e -> {
            GrupEklemeFrame frame = new GrupEklemeFrame(grupComboBox, altPanelGrupComboBox);
            frame.setVisible(true);
        });

        //GRUP SİL BUTTON LAMBDA
        grupSilButton.addActionListener(e -> {
            Grup secilenGrup = (Grup) grupComboBox.getSelectedItem();

            if (secilenGrup != null) {
                int onay = JOptionPane.showConfirmDialog(null,
                        secilenGrup.getAd() + " grubunu silmek istediğinize emin misiniz?",
                        "Grup Silme", JOptionPane.YES_NO_OPTION);

                if (onay == JOptionPane.YES_OPTION) {
                    GrupManager.grupSil(secilenGrup.getId());
                    GrupManager.grupComboBoxuYenile(grupComboBox, altPanelGrupComboBox);
                    JOptionPane.showMessageDialog(null, "Grup silindi.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen silinecek bir grup seçin.");
            }
        });


        //Grup comboboxların lambdası
        grupComboBox.addActionListener(e -> {
            Grup seciliGrup = (Grup) grupComboBox.getSelectedItem();
            if (seciliGrup == null) return;

            List<Kisi> kisiler;

            if (seciliGrup.getId() == tumKisilerGrupId) {
                kisiler = KisiDAO.getTumKisiler();
            } else {
                kisiler = KisiDAO.getKisilerByGrupId(seciliGrup.getId());
            }
            tableModel.setKisiler(kisiler);
            tableModel.fireTableDataChanged();
            table.clearSelection();
        });


        //Gruba Ekleme
        grubaEkleButton.addActionListener(e -> {
            Grup secilenGrup = (Grup) altPanelGrupComboBox.getSelectedItem();
            if (secilenGrup == null) {
                JOptionPane.showMessageDialog(null, "Lütfen önce bir grup seçin.");
                return;
            }

            List<Kisi> secilen = new ArrayList<>();
            int[] seciliSatirlar = table.getSelectedRows();
            for (int row : seciliSatirlar) {
                int modelIndex = table.convertRowIndexToModel(row);
                secilen.add(tableModel.getKisiAt(modelIndex));
            }

            if (secilen.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Lütfen önce tablodan kişiler seçin.");
                return;
            }

            List<Integer> kisiIdList = secilen.stream()
                    .map(Kisi::getId)
                    .filter(id -> !KisiDAO.kisiGrubaEkliMi(id, secilenGrup.getId()))
                    .toList();

            if (kisiIdList.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Seçilen kişilerin hepsi zaten bu gruba ekli.");
                return;
            }
            if (secilenGrup.getId() == 1) {
                JOptionPane.showMessageDialog(null, "Seçilen kişiler zaten Tüm Kişiler grubuna ekli.");
                return;
            } else {
                KisiDAO.grubaEkle(secilenGrup.getId(), kisiIdList);
                JOptionPane.showMessageDialog(null, "Yeni kişiler başarıyla gruba eklendi.");
            }
        });

        //Gruptan Çıkarma
        gruptanÇıkarButton.addActionListener(e -> {
            Grup secilenGrup = (Grup) altPanelGrupComboBox.getSelectedItem();
            if (secilenGrup == null) {
                JOptionPane.showMessageDialog(null, "Lütfen önce bir grup seçin.");
                return;
            }

            if (secilenGrup.getId() == tumKisilerGrupId) {
                JOptionPane.showMessageDialog(null, "Tüm Kişiler grubundan kişi çıkarılamaz.");
                return;
            }

            List<Kisi> secilen = new ArrayList<>();
            int[] seciliSatirlar = table.getSelectedRows();
            for (int row : seciliSatirlar) {
                int modelIndex = table.convertRowIndexToModel(row);
                secilen.add(tableModel.getKisiAt(modelIndex));
            }

            if (secilen.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Lütfen önce tablodan kişiler seçin.");
                return;
            }

            List<Integer> kisiIdList = secilen.stream()
                    .map(Kisi::getId)
                    .toList();

            KisiDAO.gruptanCikar(secilenGrup.getId(), kisiIdList);
            Grup seciliGrup = (Grup) grupComboBox.getSelectedItem();
            if (seciliGrup == null) return;

            List<Kisi> kisiler;

            if (seciliGrup.getId() == tumKisilerGrupId) {
                kisiler = KisiDAO.getTumKisiler();
            } else {
                kisiler = KisiDAO.getKisilerByGrupId(seciliGrup.getId());
            }

            tableModel.setKisiler(kisiler);
            tableModel.fireTableDataChanged();
            table.clearSelection();

            JOptionPane.showMessageDialog(null, "Seçilen kişiler gruptan çıkarıldı.");
        });

        // ALT PANEL SİL BUTONUNUN DURUMA GÖRE TEPKİSİ
        table.getSelectionModel().addListSelectionListener(e ->
        {
            if (e.getValueIsAdjusting()) return;

            int secimSayisi = table.getSelectedRowCount();

            if (secimSayisi == 0) {
                silButton.setEnabled(false);
                silButton.setText("Seçilen Kişiyi Sil");
            } else if (secimSayisi == 1) {
                silButton.setEnabled(true);
                silButton.setText("Seçilen Kişiyi Sil");
            } else {
                silButton.setEnabled(true);
                silButton.setText("Seçilenleri Sil");
            }
        });

        // Arama filtresi
        aramaField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filtrele();
            }

            public void removeUpdate(DocumentEvent e) {
                filtrele();
            }

            public void changedUpdate(DocumentEvent e) {
                filtrele();
            }

            private void filtrele() {
                String text = aramaField.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        // HEPSİNİ SEÇ BUTONU LAMBDA
        hepsiniseç.addActionListener(e ->
        {
            int rowCount = table.getRowCount();
            if (rowCount == table.getSelectedRowCount()) {
                table.clearSelection();
            } else if (rowCount > 0) {
                table.setRowSelectionInterval(0, rowCount - 1);
            }
        });

        // kisi silme lambda'sı
        silButton.addActionListener(e -> {
            if (secilenKisiler.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Silmek için en az bir kişi seçin.");
                return;
            }

            int onay = JOptionPane.showConfirmDialog(frame,
                    secilenKisiler.size() + " kişi silinecek. Emin misiniz?",
                    "Kişi Silme",
                    JOptionPane.YES_NO_OPTION);
            if (onay != JOptionPane.YES_OPTION) return;

            int[] ids = secilenKisiler.stream().mapToInt(Kisi::getId).toArray();

            boolean remoteOK = KisiManager.kisiSilService(ids);

            if (remoteOK) {
                KisiManager.kisiSilById(ids);

                secilenKisiler.clear();
                table.clearSelection();
                tableModel.fireTableDataChanged();

                JOptionPane.showMessageDialog(frame, "Seçili kişiler hem local hem sunucudan silindi.");
            } else {
                JOptionPane.showMessageDialog(frame, "Sunucuda silme işlemi sırasında hata oluştu.");
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        }

    }