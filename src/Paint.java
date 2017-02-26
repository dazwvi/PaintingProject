import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Paint extends JFrame {

    //Menu
    JMenuBar jmb;
    JCheckBoxMenuItem ViewFull;
    JCheckBoxMenuItem ViewFit;
    JMenuItem new_file;
    JMenuItem load_file;
    JMenuItem save_file;

    //ToolPalette
    JPanel toolholder;
    Box vertBox;
    JPanel toolBox;
    JButton current;
    JButton jt1;
    JButton jt2;
    JButton jt3;
    JButton jt4;
    JButton jt5;
    JButton jt6;


    //MVC
    Model m;
    View vf;
    ColorBar cb;
    LineThick lt;

    public Paint(){
        super();
        this.setTitle("JSketch");
        this.setSize(800, 600);
        this.getContentPane().setLayout(new BorderLayout());

        // MVC
        m = new Model();
        vf = new View(m);
        cb = new ColorBar(m);
        lt = new LineThick(m);
        m.addObserver(vf);
        m.addObserver(cb);
        m.addObserver(lt);
        m.notifyObservers();

        doMenuBar();
        doToolPalette();

        JScrollPane scrollPane = new JScrollPane(vf);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        current = jt1;
        jt1.setBorder(null);
        jt2.setBorder(null);
        jt3.setBorder(null);
        jt4.setBorder(null);
        jt5.setBorder(null);
        jt6.setBorder(null);


        //Mouse listener
        jt1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                m.change_tool_type(Tool_enum.Select);
                current.setBorder(null);
                jt1.setBorder(BorderFactory.createLoweredBevelBorder());
                current = jt1;
            }
        });
        jt2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                m.change_tool_type(Tool_enum.Eraser);
                current.setBorder(null);
                jt2.setBorder(BorderFactory.createLoweredBevelBorder());
                current = jt2;
            }
        });
        jt3.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                m.change_tool_type(Tool_enum.Line);
                current.setBorder(null);
                jt3.setBorder(BorderFactory.createLoweredBevelBorder());
                current = jt3;
            }
        });
        jt4.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                m.change_tool_type(Tool_enum.Circle);
                current.setBorder(null);
                jt4.setBorder(BorderFactory.createLoweredBevelBorder());
                current = jt4;
            }
        });
        jt5.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                m.change_tool_type(Tool_enum.Rectangle);
                current.setBorder(null);
                jt5.setBorder(BorderFactory.createLoweredBevelBorder());
                current = jt5;
            }
        });
        jt6.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                m.change_tool_type(Tool_enum.Fill);
                current.setBorder(null);
                jt6.setBorder(BorderFactory.createLoweredBevelBorder());
                current = jt6;
            }
        });
    }

    private void doMenuBar(){
        jmb = new JMenuBar();

        JMenu file = new JMenu("File");
        new_file = new JMenuItem("New");
        load_file = new JMenuItem("Load");
        save_file = new JMenuItem("Save");
        new_file.addActionListener(new MenuItemHandler());
        load_file.addActionListener(new MenuItemHandler());
        save_file.addActionListener(new MenuItemHandler());
        file.add(new_file);
        file.add(load_file);
        file.add(save_file);

        JMenu view = new JMenu("View");
        ViewFull = new JCheckBoxMenuItem("Full-size-view", true);
        ViewFit = new JCheckBoxMenuItem("Fit-size-view");
        ViewFull.addActionListener(new MenuItemHandler());
        ViewFit.addActionListener(new MenuItemHandler());
        view.add(ViewFull);
        view.add(ViewFit);

        jmb.add(file);
        jmb.add(view);
        this.setJMenuBar(jmb);
    }

    public class MenuItemHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            if (cmd == "Full-size-view"){
                ViewFit.setState(false);
                m.change_view_type(true);
            }
            if (cmd == "Fit-size-view"){
                ViewFull.setState(false);
                m.change_view_type(false);
            }
            if (cmd == "New"){
                m.new_file();
            }
            if (cmd == "Load"){
                m.load_file();
            }
            if (cmd == "Save"){
                m.save_file();
            }
        }
    }

    private void doToolPalette(){
        toolholder = new JPanel();
        toolholder.setLayout(new BorderLayout());

        vertBox = Box.createVerticalBox();
        toolBox = new JPanel();
        toolBox.setLayout(new GridLayout(0,2));

        ImageIcon[] icons = new ImageIcon[6];
        for (int i=0; i<6; i++){
            icons[i]=new ImageIcon(this.getClass().getResource(String.valueOf(i+1) +".GIF"));
        }
        jt1 = new JButton(icons[0]);
        jt2 = new JButton(icons[1]);
        jt3 = new JButton(icons[2]);
        jt4 = new JButton(icons[3]);
        jt5 = new JButton(icons[4]);
        jt6 = new JButton(icons[5]);

        ButtonGroup bg1 = new ButtonGroup();
        bg1.add(jt1);
        bg1.add(jt2);
        bg1.add(jt3);
        bg1.add(jt4);
        bg1.add(jt5);
        bg1.add(jt6);

        toolBox.add(jt1);
        toolBox.add(jt2);
        toolBox.add(jt3);
        toolBox.add(jt4);
        toolBox.add(jt5);
        toolBox.add(jt6);
        vertBox.add(toolBox);

        JLabel c_label = new JLabel(" ");
        JLabel c_label1 = new JLabel(" ");
        vertBox.add(c_label);
        vertBox.add(cb);
        vertBox.add(c_label1);
        vertBox.add(lt);

        toolholder.add(vertBox, BorderLayout.NORTH);

        this.add(toolholder, BorderLayout.WEST);
    }
}
