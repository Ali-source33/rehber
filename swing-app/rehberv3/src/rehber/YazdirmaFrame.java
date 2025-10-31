package rehber;

import javax.swing.*;
import java.awt.*;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import java.util.List;

public class YazdirmaFrame extends JFrame {
    private List<Kisi> secilenKisiler;
    private JTextField sayfaIcinAdetSayisiField;
    private ButtonGroup yazdirmaTipiGroup;

    public YazdirmaFrame(List<Kisi> secilenKisiler) {
        this.secilenKisiler = secilenKisiler;

        initComponents();
    }

    private void initComponents() {
        var stil = new Stil();

        setTitle("Yazdır");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel yazdirSecimPanel = new JPanel();
        yazdirSecimPanel.setLayout(new BoxLayout(yazdirSecimPanel, BoxLayout.Y_AXIS));

        JPanel turSecmePaneli = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        JPanel sayfaIcinAdetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        JPanel yazdirButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));

        JRadioButton kisiEtiketiBtn = new JRadioButton("Kişi Etiketi");
        kisiEtiketiBtn.setActionCommand("KisiEtiketiYazdirici");

        JRadioButton diplomatEtiketiBtn = new JRadioButton("Diplomat Etiketi");
        diplomatEtiketiBtn.setActionCommand("DiplomatEtiketiYazdirici");

        yazdirmaTipiGroup = new ButtonGroup();
        yazdirmaTipiGroup.add(kisiEtiketiBtn);
        yazdirmaTipiGroup.add(diplomatEtiketiBtn);

        sayfaIcinAdetSayisiField = new JTextField(10);

        sayfaIcinAdetPanel.add(new JLabel("Sayfa Şablon Sayısı"));
        sayfaIcinAdetPanel.add(sayfaIcinAdetSayisiField);

        JButton yazdirButton = new JButton("Yazdır");
        stil.styleButton(yazdirButton, new Color(150, 150, 150), Color.WHITE);
        yazdirButtonPanel.add(yazdirButton);

        turSecmePaneli.add(new JLabel("Şablon Seçiniz:"));
        turSecmePaneli.add(kisiEtiketiBtn);
        turSecmePaneli.add(diplomatEtiketiBtn);

        yazdirSecimPanel.add(turSecmePaneli);
        yazdirSecimPanel.add(sayfaIcinAdetPanel);
        yazdirSecimPanel.add(yazdirButtonPanel);

        add(yazdirSecimPanel, BorderLayout.CENTER);

        yazdirButton.addActionListener(e -> {
            try {
                ButtonModel selectedModel = yazdirmaTipiGroup.getSelection();
                if (selectedModel == null) {
                    JOptionPane.showMessageDialog(this, "Lütfen bir yazdırma tipi seçin!");
                    return;
                }

                String secim = selectedModel.getActionCommand();
                EtiketYazdırılabilir yazici = YazdiriciProvider.getYazdirici(secim);

                int sayfaBasiEtiket = Integer.parseInt(sayfaIcinAdetSayisiField.getText());
                yazici.Yazdır(secilenKisiler, sayfaBasiEtiket);

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Lütfen geçerli bir sayı giriniz.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Yazdırma sırasında hata oluştu: " + ex.getMessage());
            }
            this.dispose();
        });
    }
}
