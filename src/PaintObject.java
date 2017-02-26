import java.awt.*;

public class PaintObject {
    public Boolean is_filled;
    public Color fill = Color.blue;
    public int type; // 1 - Line, 2 - Rectangle, 3 - Circle
    public int tickness;
    public int real_tickness;
    public Color color;

    //Fields for line
    public Dot line_end1;
    public Dot line_end2;
    public Dot real_line_end1;
    public Dot real_line_end2;

    //Fields for Rectangle;
    public Dot Rec_NW;
    public Dot Rec_NE;
    public Dot Rec_SW;
    public Dot Rec_SE;
    public Dot real_Rec_NW;
    public Dot real_Rec_NE;
    public Dot real_Rec_SW;
    public Dot real_Rec_SE;

    //Fields for Circle
    public Dot Cir_center;
    public int Cir_r;
    public Dot real_Cir_center;
    public int real_Cir_r;

    //Constructor
    public PaintObject(int tp, int tn, Color c){
        this.is_filled = false;
        this.type = tp;
        this.tickness = tn;
        this.color = c;
        this.real_tickness = tn;
    }
}
