import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

public class ColorBar extends JPanel implements Observer {
    private Model m;
    public Color current_color;
    JButton choose;
    JLabel chosen_color;
    JToggleButton blue;
    JToggleButton red;
    JToggleButton yellow;
    JToggleButton green;
    JToggleButton black;
    JToggleButton orange;
    JToggleButton current;

    public ColorBar(Model m){
        this.m = m;
        this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        this.setLayout(new GridLayout(0,2));
        ImageIcon[] icons = new ImageIcon[7];
        for (int i=0; i<7; i++){
            icons[i]=new ImageIcon(this.getClass().getResource(String.valueOf(i+1) +".png"));
        }

        choose = new JButton(icons[6]);
        chosen_color = new JLabel(" ");
        chosen_color.setBackground(Color.blue);
        chosen_color.setOpaque(true);
        chosen_color.setPreferredSize(new Dimension(23, 23));
        blue = new JToggleButton(icons[0]);
        red = new JToggleButton(icons[1]);
        yellow = new JToggleButton(icons[2]);
        green = new JToggleButton(icons[3]);
        black = new JToggleButton(icons[4]);
        orange = new JToggleButton(icons[5]);

        blue.setBorder(BorderFactory.createLoweredBevelBorder());
        red.setBorder(null);
        yellow.setBorder(null);
        green.setBorder(null);
        black.setBorder(null);
        orange.setBorder(null);

        current = blue;
        choose.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                Color c = JColorChooser.showDialog(null, "Choose a Color", Color.black);
                if (c != null) {
                    current_color = c;
                    chosen_color.setBackground(c);
                    m.color_bar_change(c);
                }
            }
        });
        blue.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                current.setBorder(null);
                blue.setBorder(BorderFactory.createLoweredBevelBorder());
                current = blue;
                current_color = Color.blue;
                chosen_color.setBackground(Color.blue);
                m.color_bar_change(current_color);
            }
        });
        red.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                current.setBorder(null);
                red.setBorder(BorderFactory.createLoweredBevelBorder());
                current = red;
                current_color = Color.red;
                chosen_color.setBackground(Color.red);
                m.color_bar_change(current_color);
            }
        });
        yellow.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                current.setBorder(null);
                yellow.setBorder(BorderFactory.createLoweredBevelBorder());
                current = yellow;
                current_color = Color.yellow;
                chosen_color.setBackground(Color.yellow);
                m.color_bar_change(current_color);
            }
        });
        black.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                current.setBorder(null);
                black.setBorder(BorderFactory.createLoweredBevelBorder());
                current = black;
                current_color = Color.black;
                chosen_color.setBackground(Color.black);
                m.color_bar_change(current_color);

            }
        });
        green.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                current.setBorder(null);
                green.setBorder(BorderFactory.createLoweredBevelBorder());
                current = green;
                current_color = Color.green;
                chosen_color.setBackground(Color.green);
                m.color_bar_change(current_color);
            }
        });
        orange.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                current.setBorder(null);
                orange.setBorder(BorderFactory.createLoweredBevelBorder());
                current = orange;
                current_color = Color.orange;
                chosen_color.setBackground(Color.orange);
                m.color_bar_change(current_color);
            }
        });

        this.add(choose);
        this.add(chosen_color);
        this.add(blue);
        this.add(red);
        this.add(yellow);
        this.add(green);
        this.add(black);
        this.add(orange);
    }
    @Override
    public void update(Observable arg0, Object arg1){
        if (m.changing_color_bar){
            chosen_color.setBackground(m.current_color);
            m.changing_color_bar = false;
        }
        repaint();
    }
}
