package forms;

import java.awt.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/* Класс движущейся полосы с информацией о текущем пользователе */
class PanelCurUser extends JPanel implements Runnable {
    Font font = new Font("Segoe Print", Font.BOLD, 13);
    int step = 2; //смещение
    int startX = 0;
    private String text;

    PanelCurUser(String text) {
        setBackground(new Color(237, 246, 229));
        setPreferredSize(new Dimension(410, 20));
        setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(70);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);
        g2d.drawString(this.getText(), startX - this.getWidth(), this.getY() + 5);
        g2d.drawString(this.getText(), startX += step, this.getY() + 5);
        if (startX >= this.getWidth()) {
            startX = startX - this.getWidth() - step;
        }
        g2d.dispose();
    }
}
