package rehber;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private int currentRow;
    private JTable currentTable;
    private ArrayList<Kisi> kisilerRef;
    private TelefonRehberiApp appRef;

    public ButtonEditor(JCheckBox checkBox, ArrayList<Kisi> kisilerRef,KisiTableModel tableModel) {
        super(checkBox);
        this.kisilerRef = kisilerRef;
        this.button = new JButton("⋮");

        this.button.addActionListener(e -> {
            if (this.currentTable != null) {
                int modelRow = this.currentTable.convertRowIndexToModel(this.currentRow);
                Kisi secilenKisi = kisilerRef.get(modelRow);

                JFrame kart = new JFrame("Kişi Kartı");
                kart.setSize(520, 620);
                kart.setLocationRelativeTo(null);

                KisiKartPanel kisiKartPanel = new KisiKartPanel(secilenKisi, modelRow, tableModel);
                kart.add(kisiKartPanel);

                kart.setVisible(true);
            }
        });

    }
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.currentRow = row;
        this.currentTable = table;
        return this.button;
    }
}