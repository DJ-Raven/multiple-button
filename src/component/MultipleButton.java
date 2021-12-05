package component;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MultipleButton extends JPanel {

    public EventItemSelected getEvent() {
        return event;
    }

    public void setEvent(EventItemSelected event) {
        this.event = event;
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
        initItem();
    }

    public Color getEffectColor() {
        return effectColor;
    }

    public void setEffectColor(Color effectColor) {
        this.effectColor = effectColor;
        int red = effectColor.getRed();
        int green = effectColor.getGreen();
        int blue = effectColor.getBlue();
        colors = new Color[]{new Color(red, green, blue, 70), new Color(red, green, blue, 20), new Color(red, green, blue, showLine)};
    }

    private List items = new ArrayList();
    private Color effectColor;
    private Point point;
    private float[] dist = {0.0f, 0.5f, 1.0f};
    private Color[] colors;
    private int itemWidth = 100;
    private int itemHeight = 100;
    private int space = 4;
    private int lineSize = 4;
    private int showLine = 0;
    private EventItemSelected event;

    public MultipleButton() {
        init();
    }

    private void init() {
        setOpaque(false);
        setEffectColor(new Color(255, 255, 255));
        setForeground(new Color(205, 205, 205));
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                point = me.getPoint();
                checKMouse();
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent me) {
                point = me.getPoint();
                checKMouse();
                repaint();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent me) {
                point = null;
                checKMouse();
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if (event != null && point != null) {
                    int index = getMouseOver();
                    if (index >= 0) {
                        event.selected(index);
                    }
                }
            }
        });
        setLayout(new WrapLayout(WrapLayout.LEFT, space, space));
    }

    private void initItem() {
        removeAll();
        for (Object d : items) {
            add(createLabe(d));
        }
        repaint();
        revalidate();
    }

    private JLabel createLabe(Object data) {
        JLabel label = new JLabel(data.toString());
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 14));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(getForeground());
        label.setPreferredSize(new Dimension(itemWidth, itemHeight));
        return label;
    }

    private void checKMouse() {
        if (point == null) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else {
            int index = getMouseOver();
            if (index == -1) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            } else {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }
    }

    @Override
    public void paint(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        Shape mouseOver = null;
        int width = getWidth();
        int height = getHeight();
        int size = 150;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int x = space;
        int y = space;
        for (int i = 0; i < items.size(); i++) {
            Shape s = createShap(x, y, itemWidth, itemHeight);
            if (mouseOver == null && isMouseOver(x, y, itemWidth, itemHeight)) {
                mouseOver = s;
            }
            g2d.fill(s);
            x += space + itemWidth;
            if (x + space + itemWidth > width) {
                x = space;
                y += space + itemHeight;
            }
        }
        g2d.setComposite(AlphaComposite.SrcIn);
        if (point != null) {
            RadialGradientPaint p = new RadialGradientPaint(point, size, dist, colors);
            g2d.setPaint(p);
        } else {
            g2d.setColor(new Color(255, 255, 255, showLine));
        }
        g2d.fillRect(0, 0, width, height);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        if (mouseOver != null) {
            g2d.setColor(effectColor);
            g2d.fill(mouseOver);
        }
        g2d.dispose();
        g2.drawImage(img, 0, 0, null);
        g2.dispose();
        super.paint(grphcs);
    }

    private Shape createShap(int x, int y, int widht, int height) {
        Area area = new Area(new Rectangle2D.Double(x, y, widht, height));
        area.subtract(new Area(new Rectangle2D.Double(x + lineSize, y + lineSize, widht - lineSize * 2, height - lineSize * 2)));
        return area;
    }

    private boolean isMouseOver(int x, int y, int width, int height) {
        boolean over = false;
        if (point != null) {
            if (new Rectangle2D.Double(x, y, width, height).contains(point)) {
                over = true;
            }
        }
        return over;
    }

    private int getMouseOver() {
        int width = getWidth();
        int x = space;
        int y = space;
        for (int i = 0; i < items.size(); i++) {
            if (isMouseOver(x, y, itemWidth, itemHeight)) {
                return i;
            }
            x += space + itemWidth;
            if (x + space + itemWidth > width) {
                x = space;
                y += space + itemHeight;
            }
        }
        return -1;
    }
}
