import javax.swing.*;
import javax.swing.event.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;
import java.io.*;

public class View extends JPanel implements Observer{
    private Model m;

    public double scale = 1;
    public Tool_enum current_tool;

    public Dot draw_start;
    public Dot drag_start;
    public List<PaintObject> PArray = new ArrayList<PaintObject>();
    boolean selected;
    boolean is_resizing;
    int selected_index;
    int resize_index;
    boolean is_drawing;
    boolean is_dragging;

    boolean is_full_size = true;
    Dot painting_point;

    //Constructor
    public View(Model m){
        this.m = m;
        this.current_tool = Tool_enum.Rectangle.Rectangle;
        selected = false;
        is_drawing = false;
        is_dragging = false;
        is_resizing = false;
        this.addKeyListener(new TAdapter());
        this.layoutView();
        this.registerControllers();
        this.setBackground(Color.white);
    }
    private void layoutView() {
        this.setPreferredSize(new Dimension(1400, 1000));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    /** Register event Controllers for mouse clicks and motion. */
    private void registerControllers() {
        MouseInputListener mil = new MController();
        this.addMouseListener(mil);
        this.addMouseMotionListener(mil);
    }

    public int is_select(int x, int y){
        for (int i  = 0; i < PArray.size(); i++){
            if (is_contain(PArray.get(i), x, y)){
                selected = true;
                return i;
            }
        }
        selected = false;
        return -1;
    }

    public boolean is_contain(PaintObject p, int x, int y){
        switch (p.type){
            case 2:
                int x1 = p.Rec_NW.x-7;
                int y1 = p.Rec_NW.y-7;
                int x2 = p.Rec_SE.x+7;
                int y2 = p.Rec_SE.y+7;

                if (x1 <= x && x <= x2 && y1 <= y && y <= y2){
                    return true;
                }
                break;
            case 3:
                int r = p.Cir_r / 2;
                int xc = p.Cir_center.x + r;
                int yc = p.Cir_center.y + r;
                if ((xc - x)*(xc - x) + (yc - y)*(yc - y) <= (r+7) * (r+7)){
                    return true;
                }
                break;
            case 1:
                x1 = p.line_end1.x;
                y1 = p.line_end1.y;
                x2 = p.line_end2.x;
                y2 = p.line_end2.y;

                Line2D line = new Line2D.Float(x1, y1, x2, y2);
                int boxX = x - 3;
                int boxY = y - 3;
                if (line.intersects(boxX, boxY, 6, 6)){
                    return true;
                }
                else {
                    return false;
                }
        }

        return false;
    }

    private int resizable(int x, int y, PaintObject p){
        switch (p.type){
            case 1:
                int x1 = p.line_end1.x;
                int y1 = p.line_end1.y;
                int x2 = p.line_end2.x;
                int y2 = p.line_end2.y;
                if (in_rect(x1, y1, x, y)){
                    return 1;
                }
                if (in_rect(x2, y2, x, y)){
                    return 2;
                }
                break;
            case 2:
                x1 = p.Rec_NW.x;
                y1 = p.Rec_NW.y;
                x2 = p.Rec_NE.x;
                y2 = p.Rec_NE.y;
                int x3 = p.Rec_SE.x;
                int y3 = p.Rec_SE.y;
                int x4 = p.Rec_SW.x;
                int y4 = p.Rec_SW.y;
                if (in_rect(x1, y1, x, y)){
                    return 3;
                }
                if (in_rect(x2, y2, x, y)){
                    return 4;
                }
                if (in_rect(x3, y3, x, y)){
                    return 5;
                }
                if (in_rect(x4, y4, x, y)){
                    return 6;
                }
                break;
            case 3:
                int r = p.Cir_r;
                x1 = p.Cir_center.x + r/2;
                y1 = p.Cir_center.y;
                x2 = p.Cir_center.x;
                y2 = p.Cir_center.y + r/2;
                x3 = p.Cir_center.x + r/2;
                y3 = p.Cir_center.y + r;
                x4 = p.Cir_center.x + r;
                y4 = p.Cir_center.y + r/2;
                if (in_rect(x1, y1, x, y)){
                    return 7;
                }
                if (in_rect(x2, y2, x, y)){
                    return 7;
                }
                if (in_rect(x3, y3, x, y)){
                    return 7;
                }
                if (in_rect(x4, y4, x, y)){
                    return 7;
                }
                break;
        }
        return -1;
    }

    private boolean in_rect(int rec_x, int rec_y, int x, int y){
        if (rec_x-7 <= x && x <= rec_x+7 && rec_y-7 <= y && y <= rec_y+7){
            return true;
        }
        else {
            return false;
        }
    }
    private class MController extends MouseInputAdapter {

        public void mousePressed(MouseEvent e) {
            drag_start = new Dot(e.getX(), e.getY());
            if (selected){
                resize_index = resizable(e.getX(), e.getY(), PArray.get(selected_index));
                if (resize_index == -1){
                    is_resizing = false;
                }
                else{
                    is_resizing = true;
                }
            }
            if (current_tool == Tool_enum.Line || current_tool == Tool_enum.Rectangle || current_tool == Tool_enum.Circle) {
                draw_start = new Dot(e.getX(), e.getY());
                is_drawing = true;
            } else {
                selected_index = is_select(e.getX(), e.getY());
                setFocusable(true);
                requestFocusInWindow();
            }
            if (current_tool == Tool_enum.Eraser && selected){
                PArray.remove(selected_index);
                selected = false;
            }
            if (current_tool == Tool_enum.Fill && selected){
                PArray.get(selected_index).fill = m.current_color;
                PArray.get(selected_index).is_filled = true;
                selected = false;
            }
            if (selected){
                m.change_color_bar(PArray.get(selected_index).color);
                m.change_thickness_bar(PArray.get(selected_index).tickness);
            }
            repaint();
        }

        public void mouseReleased(MouseEvent e){
            is_resizing = false;
            if (current_tool == Tool_enum.Rectangle){
                PaintObject rec = new PaintObject(2, m.thickness, m.current_color);
                int x1 = draw_start.x;
                int x2 = e.getX();
                int y1 = draw_start.y;
                int y2 = e.getY();
                rec.Rec_NW = new Dot(x1, y1);
                rec.Rec_NE = new Dot(x2, y1);
                rec.Rec_SW = new Dot(x1, y2);
                rec.Rec_SE = new Dot(x2, y2);

                Double temp_x1;
                Double temp_y1;
                Double temp_x2;
                Double temp_y2;
                temp_x1 = x1 / scale;
                temp_y1 = y1 / scale;
                temp_x2 = x2 / scale;
                temp_y2 = y2 / scale;
                int real_x1 = temp_x1.intValue();
                int real_y1 = temp_y1.intValue();
                int real_x2 = temp_x2.intValue();
                int real_y2 = temp_y2.intValue();
                rec.real_Rec_NW = new Dot(real_x1, real_y1);
                rec.real_Rec_NE = new Dot(real_x2, real_y1);
                rec.real_Rec_SW = new Dot(real_x1, real_y2);
                rec.real_Rec_SE = new Dot(real_x2, real_y2);

                PArray.add(0, rec);
                is_drawing = false;
                is_dragging = false;
                repaint();
            }
            if (current_tool == Tool_enum.Line) {
                PaintObject line = new PaintObject(1, m.thickness, m.current_color);
                int x1 = draw_start.x;
                int x2 = e.getX();
                int y1 = draw_start.y;
                int y2 = e.getY();
                line.line_end1 = new Dot(x1, y1);
                line.line_end2 = new Dot(x2, y2);
                Double temp_x1;
                Double temp_y1;
                Double temp_x2;
                Double temp_y2;
                temp_x1 = x1 / scale;
                temp_y1 = y1 / scale;
                temp_x2 = x2 / scale;
                temp_y2 = y2 / scale;
                int real_x1 = temp_x1.intValue();
                int real_y1 = temp_y1.intValue();
                int real_x2 = temp_x2.intValue();
                int real_y2 = temp_y2.intValue();

                line.real_line_end1 = new Dot(real_x1, real_y1);
                line.real_line_end2 = new Dot(real_x2, real_y2);

                PArray.add(0, line);
                is_drawing = false;
                is_dragging = false;
                repaint();
            }
            if (current_tool == Tool_enum.Circle){
                PaintObject circle = new PaintObject(3, m.thickness, m.current_color);
                int x1 = draw_start.x;
                int y1 = draw_start.y;
                int x2 = e.getX();
                int y2 = e.getY();
                int width = Math.abs(x2 - x1);
                int height = Math.abs(y2 - y1);
                circle.Cir_center = new Dot(x1, y1);
                if (height > width){
                    circle.Cir_r = height;
                }
                else {
                    circle.Cir_r = width;
                }
                Double temp_x1;
                Double temp_y1;
                temp_x1 = x1 / scale;
                temp_y1 = y1 / scale;
                int real_x1 = temp_x1.intValue();
                int real_y1 = temp_y1.intValue();

                circle.real_Cir_center = new Dot(real_x1, real_y1);
                Double temp_r = circle.Cir_r / scale;
                int real_r = temp_r.intValue();
                circle.real_Cir_r = real_r;

                PArray.add(0, circle);
                is_drawing = false;
                is_dragging = false;
                repaint();
            }
        }
        /**
         * The mouse moved. Check if it's over the dragable regions and adjust
         * the cursor accordingly.
         */
        public void mouseMoved(MouseEvent e) {
        }

        /** The user is dragging the mouse. Resize appropriately. */
        public void mouseDragged(MouseEvent e) {
            //Drawing
            if (is_drawing){
                painting_point = new Dot(e.getX(), e.getY());
                is_dragging = true;
            }

            //Move
            if (selected && !is_resizing){
                int updated_x = e.getX() - drag_start.x;
                int updated_y = e.getY() - drag_start.y;
                drag_start.x = e.getX();
                drag_start.y = e.getY();
                PaintObject temp = PArray.get(selected_index);
                int type = temp.type;
                switch (type){
                    case 1:
                        PArray.get(selected_index).line_end1.x += updated_x;
                        PArray.get(selected_index).line_end1.y += updated_y;
                        PArray.get(selected_index).line_end2.x += updated_x;
                        PArray.get(selected_index).line_end2.y += updated_y;
                        break;
                    case 2:
                        PArray.get(selected_index).Rec_SE.x += updated_x;
                        PArray.get(selected_index).Rec_SE.y += updated_y;
                        PArray.get(selected_index).Rec_NW.x += updated_x;
                        PArray.get(selected_index).Rec_NW.y += updated_y;
                        PArray.get(selected_index).Rec_SW.x += updated_x;
                        PArray.get(selected_index).Rec_SW.y += updated_y;
                        PArray.get(selected_index).Rec_NE.x += updated_x;
                        PArray.get(selected_index).Rec_NE.y += updated_y;
                        break;
                    case 3:
                        PArray.get(selected_index).Cir_center.x += updated_x;
                        PArray.get(selected_index).Cir_center.y += updated_y;
                        break;
                }
            }

            //Resize
            if (selected && is_resizing){
                switch (resize_index){
                    case 1:
                        PArray.get(selected_index).line_end1.x = e.getX();
                        PArray.get(selected_index).line_end1.y = e.getY();
                        break;
                    case 2:
                        PArray.get(selected_index).line_end2.x = e.getX();
                        PArray.get(selected_index).line_end2.y = e.getY();
                        break;
                    case 3:
                        PArray.get(selected_index).Rec_NW.x = e.getX();
                        PArray.get(selected_index).Rec_NW.y = e.getY();
                        PArray.get(selected_index).Rec_NE.y = e.getY();
                        PArray.get(selected_index).Rec_SW.x = e.getX();
                        break;
                    case 4:
                        PArray.get(selected_index).Rec_NE.x = e.getX();
                        PArray.get(selected_index).Rec_NE.y = e.getY();
                        PArray.get(selected_index).Rec_SE.x = e.getX();
                        PArray.get(selected_index).Rec_NW.y = e.getY();
                        break;
                    case 5:
                        PArray.get(selected_index).Rec_SE.x = e.getX();
                        PArray.get(selected_index).Rec_SE.y = e.getY();
                        PArray.get(selected_index).Rec_NE.x = e.getX();
                        PArray.get(selected_index).Rec_SW.y = e.getY();
                        break;
                    case 6:
                        PArray.get(selected_index).Rec_SW.x = e.getX();
                        PArray.get(selected_index).Rec_SW.y = e.getY();
                        PArray.get(selected_index).Rec_NW.x = e.getX();
                        PArray.get(selected_index).Rec_SE.y = e.getY();
                        break;
                    case 7:
                        int x = PArray.get(selected_index).Cir_center.x;
                        int y = PArray.get(selected_index).Cir_center.y;
                        int width = Math.abs(x - e.getX());
                        int height = Math.abs(y - e.getY());
                        if (width > height) {
                            PArray.get(selected_index).Cir_r = width;
                        }
                        else {
                            PArray.get(selected_index).Cir_r = height;
                        }
                        break;
                }
            }

            repaint();
        } // mouseDragged
    } // MController

    public void update_sclae(){
        for (int i = 0; i <PArray.size(); i++){
            int type = PArray.get(i).type;
            Double temp;

            switch (type){
                case 1:
                    temp = PArray.get(i).real_line_end1.x * this.scale;
                    PArray.get(i).line_end1.x = temp.intValue();
                    temp = PArray.get(i).real_line_end1.y * this.scale;
                    PArray.get(i).line_end1.y = temp.intValue();
                    temp = PArray.get(i).real_line_end2.x * this.scale;
                    PArray.get(i).line_end2.x = temp.intValue();
                    temp = PArray.get(i).real_line_end2.y * this.scale;
                    PArray.get(i).line_end2.y = temp.intValue();
                    break;
                case 2:
                    temp = PArray.get(i).real_Rec_NW.x * this.scale;
                    PArray.get(i).Rec_NW.x = temp.intValue();
                    temp = PArray.get(i).real_Rec_NW.y * this.scale;
                    PArray.get(i).Rec_NW.y = temp.intValue();
                    temp = PArray.get(i).real_Rec_SE.x * this.scale;
                    PArray.get(i).Rec_SE.x = temp.intValue();
                    temp = PArray.get(i).real_Rec_SE.y * this.scale;
                    PArray.get(i).Rec_SE.y = temp.intValue();
                    temp = PArray.get(i).real_Rec_SW.x * this.scale;
                    PArray.get(i).Rec_SW.x = temp.intValue();
                    temp = PArray.get(i).real_Rec_SW.y * this.scale;
                    PArray.get(i).Rec_SW.y = temp.intValue();
                    temp = PArray.get(i).real_Rec_NE.x * this.scale;
                    PArray.get(i).Rec_NE.x = temp.intValue();
                    temp = PArray.get(i).real_Rec_NE.y * this.scale;
                    PArray.get(i).Rec_NE.y = temp.intValue();
                    break;
                case 3:
                    temp = PArray.get(i).real_Cir_center.x * this.scale;
                    PArray.get(i).Cir_center.x = temp.intValue();
                    temp = PArray.get(i).real_Cir_center.y * this.scale;
                    PArray.get(i).Cir_center.y = temp.intValue();
                    temp = PArray.get(i).real_Cir_r * this.scale;
                    PArray.get(i).Cir_r = temp.intValue();
                    break;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Insets insets = this.getInsets();
        double width_scale = (this.getWidth() - insets.left - insets.right) / 802.0;
        double height_scale = (this.getHeight() - insets.top - insets.bottom) / 602.0;
        double pre_s = this.scale;
        this.scale = Math.min(width_scale, height_scale);

        if (pre_s != this.scale) {
            update_sclae();
        }

        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < PArray.size(); i++){
            int type = PArray.get(i).type;
            Color color = PArray.get(i).color;
            int tickness = PArray.get(i).tickness;
            boolean is_filled = PArray.get(i).is_filled;
            Color filled_color = PArray.get(i).fill;
            switch (type){
                case 1:
                    int x1 = PArray.get(i).line_end1.x;
                    int y1 = PArray.get(i).line_end1.y;
                    int x2 = PArray.get(i).line_end2.x;
                    int y2 = PArray.get(i).line_end2.y;
                    g2.setColor(color);
                    g2.setStroke(new BasicStroke(tickness));
                    g2.draw(new Line2D.Float(x1, y1, x2, y2));
                    break;
                case 2:
                    x1 = PArray.get(i).Rec_NW.x;
                    y1 = PArray.get(i).Rec_NW.y;
                    x2 = PArray.get(i).Rec_SE.x;
                    y2 = PArray.get(i).Rec_SE.y;
                    int width = x2 - x1;
                    int height = y2 - y1;
                    g2.setStroke(new BasicStroke(tickness));

                    if (is_filled){
                        g2.setColor(filled_color);
                        g2.fill(new Rectangle2D.Float(x1, y1, width, height));
                    }
                    g2.setColor(color);
                    g2.draw(new Rectangle2D.Float(x1, y1, width, height));
                    break;
                case 3:
                    int r = PArray.get(i).Cir_r;
                    x1 = PArray.get(i).Cir_center.x;
                    y1 = PArray.get(i).Cir_center.y;

                    Ellipse2D.Double circle = new Ellipse2D.Double(x1, y1, r, r);
                    if (is_filled){
                        g2.setColor(filled_color);
                        g2.fill(circle);
                    }
                    g2.setColor(color);
                    g2.setStroke(new BasicStroke(tickness));
                    g2.draw(circle);
                    break;
            }
        }

        if (selected){
            PaintObject temp = PArray.get(selected_index);
            int type = temp.type;
            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(3));
            switch (type){
                case 1:
                    g.drawRect(temp.line_end1.x - 5, temp.line_end1.y - 5, 10, 10);
                    g.drawRect(temp.line_end2.x - 5, temp.line_end2.y - 5, 10, 10);
                    break;
                case 2:
                    g.drawRect(temp.Rec_NW.x - 5, temp.Rec_NW.y - 5, 10, 10);
                    g.drawRect(temp.Rec_NE.x - 5, temp.Rec_NE.y - 5, 10, 10);
                    g.drawRect(temp.Rec_SW.x - 5, temp.Rec_SW.y - 5, 10, 10);
                    g.drawRect(temp.Rec_SE.x - 5, temp.Rec_SE.y - 5, 10, 10);
                    break;
                case 3:
                    int r = temp.Cir_r;
                    g.drawRect(temp.Cir_center.x + r/2 - 5, temp.Cir_center.y - 5, 10, 10);
                    g.drawRect(temp.Cir_center.x - 5, temp.Cir_center.y + r/2 - 5, 10, 10);
                    g.drawRect(temp.Cir_center.x + r/2 - 5, temp.Cir_center.y + r - 5, 10, 10);
                    g.drawRect(temp.Cir_center.x + r - 5, temp.Cir_center.y + r/2 - 5, 10, 10);
                    break;
            }
        }

        if (is_dragging){
            int x1 = draw_start.x;
            int y1 = draw_start.y;
            int x2 = painting_point.x;
            int y2 = painting_point.y;
            int width = x2 - x1;
            int height = y2 - y1;
            int r;
            if (width > height){
                r = width;
            }
            else {
                r = height;
            }
            g2.setColor(m.current_color);
            g2.setStroke(new BasicStroke(m.thickness));
            if (current_tool == Tool_enum.Line){
                g2.draw(new Line2D.Float(x1, y1, x2, y2));
            }
            else if (current_tool == Tool_enum.Rectangle) {
                g2.draw(new Rectangle2D.Float(x1, y1, width, height));
            } else if (current_tool == Tool_enum.Circle) {
                Ellipse2D.Double circle = new Ellipse2D.Double(x1, y1, r, r);
                g2.draw(circle);
            }
        }

    }

    @Override
    public void update(Observable arg0, Object arg1){
        if (m.tool_changing){
            current_tool = m.tool_type;
            m.tool_changing = false;
            selected = false;
        }
        is_full_size = m.is_full_size;

        if (is_full_size){
            this.setPreferredSize(new Dimension(1400, 1000));
        }
        else{
            this.setPreferredSize(null);
        }
        if (m.color_bar_changing && selected){
            PArray.get(selected_index).color = m.current_color;
            m.color_bar_changing = false;
        }
        if (m.thickness_bar_changing && selected){
            PArray.get(selected_index).tickness =  m.thickness;
            m.thickness_bar_changing = false;
        }
        if (m.new_file){
            m.new_file = false;
            PArray.clear();
            selected = false;
        }
        if (m.save_file){
            m.save_file = false;
            save_file();
        }
        if (m.load_file){
            m.load_file = false;
            PArray.clear();
            selected = false;
            load_file();
        }
        repaint();
    }

    public void load_file(){
        String a = "";
        JFileChooser c = new JFileChooser();
        int retrival = c.showOpenDialog(null);
        if (retrival == JFileChooser.APPROVE_OPTION) {
            BufferedReader reader = null;
            try{
                reader = new BufferedReader(new FileReader(c.getSelectedFile()));
                String text = null;
                while ((text = reader.readLine()) != null){
                    List<String> l = Arrays.asList(text.split(","));

                    int tp = Integer.parseInt(l.get(2));
                    int tn = Integer.parseInt(l.get(3));
                    Color cl = new Color(Integer.parseInt(l.get(4)));
                    PaintObject p = new PaintObject(tp, tn, cl);

                    Boolean b = Boolean.parseBoolean(l.get(0));
                    if (!b){
                        p.is_filled = false;
                    }
                    else{
                        p.is_filled = true;
                        p.fill = new Color(Integer.parseInt(l.get(1)));
                    }
                    switch (p.type){
                        case 1:
                            int x1 = Integer.parseInt(l.get(5));
                            int y1 = Integer.parseInt(l.get(6));
                            int x2 = Integer.parseInt(l.get(7));
                            int y2 = Integer.parseInt(l.get(8));
                            p.line_end1 = new Dot(x1, y1);
                            p.line_end2 = new Dot(x2, y2);
                            p.real_line_end1 = new Dot(x1, y1);
                            p.real_line_end2 = new Dot(x2, y2);
                            break;
                        case 2:
                            x1 = Integer.parseInt(l.get(5));
                            y1 = Integer.parseInt(l.get(6));
                            x2 = Integer.parseInt(l.get(7));
                            y2 = Integer.parseInt(l.get(8));
                            int x3 = Integer.parseInt(l.get(9));
                            int y3 = Integer.parseInt(l.get(10));
                            int x4 = Integer.parseInt(l.get(11));
                            int y4 = Integer.parseInt(l.get(12));
                            p.Rec_NW = new Dot(x1, y1);
                            p.Rec_NE = new Dot(x2, y2);
                            p.Rec_SW = new Dot(x3, y3);
                            p.Rec_SE = new Dot(x4, y4);
                            p.real_Rec_NW = new Dot(x1, y1);
                            p.real_Rec_NE = new Dot(x2, y2);
                            p.real_Rec_SW = new Dot(x3, y3);
                            p.real_Rec_SE = new Dot(x4, y4);
                            break;
                        case 3:
                            int x = Integer.parseInt(l.get(5));
                            int y = Integer.parseInt(l.get(6));
                            p.Cir_center = new Dot(x, y);
                            p.real_Cir_center = new Dot(x, y);
                            p.real_Cir_r = Integer.parseInt(l.get(7));
                            break;
                    }
                    PArray.add(PArray.size(), p);
                }
                update_sclae();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                }
            }
        }
    }

    public void save_file(){
        String a = "";
        JFileChooser c = new JFileChooser();
        int retrival = c.showSaveDialog(null);
        if (retrival == JFileChooser.APPROVE_OPTION) {
            try(FileWriter fw = new FileWriter(c.getSelectedFile())) {
                for (int i = 0; i < PArray.size(); i++){
                    PaintObject to_write = PArray.get(i);
                    a = to_write.is_filled.toString() + ",";
                    a += String.valueOf(to_write.fill.getRGB());
                    a += ",";
                    a += String.valueOf(to_write.type);
                    a += ",";
                    a += String.valueOf(to_write.real_tickness);
                    a += ",";
                    a += String.valueOf(to_write.color.getRGB());
                    a += ",";

                    switch (to_write.type){
                        case 1:
                            a += String.valueOf(to_write.real_line_end1.x);
                            a += ",";
                            a += String.valueOf(to_write.real_line_end1.y);
                            a += ",";
                            a += String.valueOf(to_write.real_line_end2.x);
                            a += ",";
                            a += String.valueOf(to_write.real_line_end2.y);
                            break;
                        case 2:
                            a += String.valueOf(to_write.real_Rec_NW.x);
                            a += ",";
                            a += String.valueOf(to_write.real_Rec_NW.y);
                            a += ",";
                            a += String.valueOf(to_write.real_Rec_NE.x);
                            a += ",";
                            a += String.valueOf(to_write.real_Rec_NE.y);
                            a += ",";
                            a += String.valueOf(to_write.real_Rec_SW.x);
                            a += ",";
                            a += String.valueOf(to_write.real_Rec_SW.y);
                            a += ",";
                            a += String.valueOf(to_write.real_Rec_SE.x);
                            a += ",";
                            a += String.valueOf(to_write.real_Rec_SE.y);
                            break;
                        case 3:
                            a += String.valueOf(to_write.real_Cir_center.x);
                            a += ",";
                            a += String.valueOf(to_write.real_Cir_center.y);
                            a += ",";
                            a += String.valueOf(to_write.real_Cir_r);
                            break;
                    }
                    a += System.getProperty("line.separator");
                    fw.write(a);
                    a = "";
                }
                fw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_ESCAPE:
                    selected = false;
                    repaint();
                    break;
            }
        }
    }
}
