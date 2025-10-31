package rehber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GrupEklemeFrame extends JFrame {
    private JTextField grupIsmi;
    private JButton grupEkleButton;
    private JComboBox<Grup> grupComboBox,altPanelGrupComboBox;
    private GrupManager grupManager;
    private Stil stil;

    public GrupEklemeFrame(JComboBox<Grup> grupComboBox,JComboBox<Grup> altPanelGrupComboBox) {
        this.grupComboBox = grupComboBox;
        this.grupManager = new GrupManager();
        this.altPanelGrupComboBox = altPanelGrupComboBox;
        this.stil = new Stil();

        WievComponents();
    }

    private void WievComponents() {
        setTitle("Grup Ekleme");
        setSize(280, 150);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel grupIsmiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        grupIsmi = new JTextField(15);
        grupIsmiPanel.add(new JLabel("Grup İsmi:"));
        grupIsmiPanel.add(grupIsmi);

        JPanel grupEklePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        grupEkleButton = new JButton("Grup Ekle");
        stil.styleButton(grupEkleButton, new Color(0, 255, 0), Color.WHITE);
        grupEklePanel.add(grupEkleButton);

        add(grupIsmiPanel, BorderLayout.NORTH);
        add(grupEklePanel, BorderLayout.EAST);

        grupIsmi.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (grupIsmi.getText().length() >= 30) {
                    e.consume();
                }
            }
        });

        grupEkleButton.addActionListener(e -> {
            String yeniGrupAdi = grupIsmi.getText().trim();
            if (yeniGrupAdi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Grup adı boş olamaz.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Grup yeniGrup = grupManager.grupEkle(yeniGrupAdi);

            if (yeniGrup != null) {
                grupComboBox.addItem(yeniGrup);
                altPanelGrupComboBox.addItem(yeniGrup);
                JOptionPane.showMessageDialog(this, "Grup başarıyla eklendi.");
                grupIsmi.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Grup eklenirken hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
