import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

public class LineThick extends JPanel implements Observer{
    private Model m;
    public int current_thickness;

    JToggleButton one;
    JToggleButton two;
    JToggleButton three;

    JToggleButton c_tb;

    public LineThick(Model m){
        this.m = m;
        this.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        current_thickness = 3;
        this.setLayout(new GridLayout(0,1));
        ImageIcon[] icons = new ImageIcon[3];
        for (int i=0; i<3; i++){
            icons[i]=new ImageIcon(this.getClass().getResource(String.valueOf(i+8) +".png"));
        }
        one = new JToggleButton(icons[0]);
        two = new JToggleButton(icons[1]);
        three = new JToggleButton(icons[2]);
        c_tb = one;

        one.setBorder(BorderFactory.createLoweredBevelBorder());
        two.setBorder(null);
        three.setBorder(null);

        this.add(one);
        this.add(two);
        this.add(three);

        one.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                c_tb.setBorder(null);
                one.setBorder(BorderFactory.createLoweredBevelBorder());
                c_tb = one;
                current_thickness = 3;
                m.thickness_bar_change(3);
            }
        });
        two.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                c_tb.setBorder(null);
                two.setBorder(BorderFactory.createLoweredBevelBorder());
                c_tb = two;
                current_thickness = 5;
                m.thickness_bar_change(5);
            }
        });
        three.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                c_tb.setBorder(null);
                three.setBorder(BorderFactory.createLoweredBevelBorder());
                c_tb = three;
                current_thickness = 7;
                m.thickness_bar_change(7);
            }
        });
    }
    @Override
    public void update(Observable arg0, Object arg1){
        if (m.chaning_thickness_bar){
            switch (m.thickness){
                case 3:
                    c_tb.setBorder(null);
                    one.setBorder(BorderFactory.createLoweredBevelBorder());
                    c_tb = one;
                    break;
                case 5:
                    c_tb.setBorder(null);
                    two.setBorder(BorderFactory.createLoweredBevelBorder());
                    c_tb = two;
                    break;
                case 7:
                    c_tb.setBorder(null);
                    three.setBorder(BorderFactory.createLoweredBevelBorder());
                    c_tb = three;
                    break;
            }
            m.chaning_thickness_bar = false;
        }
    }
}
