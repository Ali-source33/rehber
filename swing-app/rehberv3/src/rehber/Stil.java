package rehber;

import javax.swing.*;
import java.awt.*;

public class Stil
{
    //TEXT FİELD STİLİ
    void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(120, 30));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    //BUTON STİLİ
    void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(140, 30));
    }
}
