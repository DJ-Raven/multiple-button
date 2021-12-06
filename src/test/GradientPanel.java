package test;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GradientPanel extends JPanel {

    public GradientPanel() {
        setOpaque(false);
    }

    @Override
    public void paint(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setPaint(new GradientPaint(0, 0, new Color(100, 65, 165), 0, getWidth(), new Color(42, 8, 69)));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paint(grphcs);
    }
}
