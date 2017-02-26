import java.awt.*;
import java.util.Observable;

public class Model extends Observable{
    public Tool_enum tool_type;
    public boolean tool_changing = false;

    public boolean is_full_size = true;

    public Color current_color = Color.blue;
    public boolean changing_color_bar = false;
    public boolean color_bar_changing = false;

    public int thickness = 3;
    public boolean chaning_thickness_bar = false;
    public boolean thickness_bar_changing = false;

    public boolean new_file = false;
    public boolean save_file = false;
    public boolean load_file = false;

    //Constructor
    public Model(){
        tool_type = Tool_enum.Select;
        setChanged();
    }

    public void change_tool_type(Tool_enum t){
        tool_changing = true;
        this.tool_type = t;
        setChanged();
        notifyObservers();
    }

    public void change_view_type(boolean is_full_size){
        this.is_full_size = is_full_size;
        setChanged();
        notifyObservers();
    }

    public void change_color_bar(Color c){
        this.current_color = c;
        changing_color_bar = true;
        setChanged();
        notifyObservers();
    }

    public void color_bar_change(Color c){
        this.current_color = c;
        color_bar_changing = true;
        setChanged();
        notifyObservers();
    }

    public void change_thickness_bar(int t){
        this.thickness = t;
        chaning_thickness_bar = true;
        setChanged();
        notifyObservers();
    }

    public void thickness_bar_change(int t){
        this.thickness = t;
        thickness_bar_changing = true;
        setChanged();
        notifyObservers();
    }

    public void new_file(){
        new_file = true;
        setChanged();
        notifyObservers();
    }

    public void save_file(){
        save_file = true;
        setChanged();
        notifyObservers();
    }

    public void load_file(){
        load_file = true;
        setChanged();
        notifyObservers();
    }
}
