package rehber;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class KisiTableModel extends AbstractTableModel {
    private final String[] kolonlar = {"Ad", "Soyad", "Telefon", "⋮"};
    private List<Kisi> kisiler;

    public KisiTableModel(List<Kisi> kisiler) {
        this.kisiler = kisiler;}

    public Kisi getKisiAt(int rowIndex) {
        return kisiler.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return kisiler.size();
    }

    @Override
    public int getColumnCount() {
        return kolonlar.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Kisi k = kisiler.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> k.getAd();
            case 1 -> k.getSoyad();
            case 2 -> k.getTelefon();
            case 3 -> "⋮";
            default -> null;
        };
    }

    public void setKisiler(List<Kisi> yeniKisiler)
    {
        this.kisiler = yeniKisiler;
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        return kolonlar[column];
    }
}
