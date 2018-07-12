package map;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import lib.iCallBack;
import lib.sFrame;
import lib.sPanel;
import lib.sTabbedAbstract;
import lib.sTabbedPane;

/**
 *
 * @author marcio
 */
public class CreateMapArtificial {
    private static final UIManager.LookAndFeelInfo[] LookAndFeels = UIManager.getInstalledLookAndFeels();
    private static final int begin_index_look_and_feel = 1;
    
    private static sFrame frame;
    
    private static sTabbedPane tabbedPane;
    private static sTabbedAbstract tabFile;
    private static sTabbedAbstract tabMap;
    private static sTabbedAbstract tabView;
    private static sTabbedAbstract tabControl;
    private static sTabbedAbstract tabLayout;
    private static sTabbedAbstract tabHelp;
        
    private static JProgressBar progressBar;
    
    private static DrawSpace draw;
    
    private static Simulation sim;
    private static UAV uav;
    private static JFileChooser fileChooserOpenSave;
    private static JFileChooser fileChooserOpenSavePoint;
            
    private static double vx = 0;
    private static double vy = 0;
    private final static double max = 3;
    private final static double steps = 10;
        
    private static iCallBack repaint = new iCallBack() {
        @Override
        public void main() {
            draw.repaint();
        }
    };
    
    private static iCallBack save = new iCallBack() {
        @Override
        public void main() {
            tabFile.Enabled("Save");
        }
    };
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        uav = new UAV(100, 2000);
        sim = new Simulation(repaint, uav);
        
        tabbedPane = new sTabbedPane(){
            @Override
            public void Config(int w, int h) {
                super.setPreferredSize(new Dimension(w, h)); 
                for(Component comp : this.getComponents()){
                    if(comp instanceof sTabbedAbstract){
                        sTabbedAbstract panel = (sTabbedAbstract) comp;
                        panel.Config(w, h);
                    }else if(comp instanceof sPanel){
                        sPanel panel = (sPanel) comp;
                        panel.Config(w, h);
                    }
                }
            }
        };
        
        //============== Creating menu File ====================================
        //Create, Add objects and define events
        tabFile = new sTabbedAbstract(Color.WHITE) {
            @Override
            public void ActionPerformed(ActionEvent e, String item, int index) throws Exception {
                MenuFileEvent(e, item, index);
            }
        };
        tabFile.addMenuItem(
            "New Map",  KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK),
            "Open Map", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK),
            "Open Map2", KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK),
            "Save",         KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), false,
            "Save As",      KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK), false,
            "Save Point",      KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK), false,
            "------------",
            "Exit"
        );
        tabbedPane.addTab("File",tabFile);
        
        
        //============== Creating menu View ====================================
        //Create, Add objects and define events
        tabView = new sTabbedAbstract(Color.WHITE) {
            @Override
            public void ActionPerformed(ActionEvent e, String item, int index) throws Exception {
                MenuViewEvent(e, item, index);
            }
        };
        tabView.addMenuItem(
            "Grid",  KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK)
        );
        tabbedPane.addTab("View",tabView);
        
        
        //============== Creating menu Map ====================================
        //Create, Add objects and define events
        tabMap = new sTabbedAbstract(Color.WHITE) {
            @Override
            public void ActionPerformed(ActionEvent e, String item, int index) throws Exception {
                MenuMapEvent(e, item, index);
            }
        };
        tabMap.addMenuItem(
            "Select",  KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK),
            "Random",  KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK),
            "Random2",  KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK),
            "Clear",   KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK),
            "Gen-500", KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK),
            "Gen-100", KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK),
            "Gen-10000", KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK),
            "Exec C&A", KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK)
        );
        tabbedPane.addTab("Map",tabMap);
        
        //============== Creating menu Control ====================================
        //Create, Add objects and define events
        tabControl = new sTabbedAbstract(Color.WHITE) {
            
            @Override
            public void ActionPerformed(ActionEvent e, String item, int index) throws Exception {
                MenuControlEvent(e, item, index);
            }
            public void norm(double dx, double dy){
                vx += dx;
                vy += dy;
                    
                double v = Math.sqrt((vx)*(vx) + (vy)*(vy));
                if(v>max){
                    vx = vx*max/v;
                    vy = vy*max/v;
                }
                System.out.println(Math.sqrt((vx)*(vx) + (vy)*(vy)));
            }
            @Override
            public void keyPressed(KeyEvent e, String item, int index) {
                if(item.equals("Start")){
                    if(e.getKeyCode()==KeyEvent.VK_DOWN){
                        norm(0, +max/steps);
                    }else if(e.getKeyCode()==KeyEvent.VK_UP){
                        norm(0, -max/steps);
                    }else if(e.getKeyCode()==KeyEvent.VK_LEFT){
                        norm(-max/steps, 0);
                    }else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                        norm(+max/steps, 0);
                    }
                }
            }
        };
        tabControl.addMenuItem(
            "Start",  KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK)
        );
        tabbedPane.addTab("Control",tabControl);
        
        
        //============== Creating menu Look And Feel ===========================
        //Create objects
        tabLayout = new sTabbedAbstract(Color.WHITE){
            @Override
            public void ActionPerformed(ActionEvent e, String item, int index) throws Exception {
                MenuLookAndFeellEvent(e, item, index);
            }
        };
        tabLayout.addRadioButtonMenuItem(begin_index_look_and_feel, LookAndFeels);
        tabbedPane.addTab("Look and Feel", tabLayout);

        //============== Creating menu Help ====================================
        //Create and Add objects
        tabHelp = new sTabbedAbstract(Color.WHITE){
            @Override
            public void ActionPerformed(ActionEvent e, String item, int index) throws Exception {
                MenuHelpEvent(e, item, index);
            }
        };
        tabHelp.addMenuItem(
            "About"
        );
        tabbedPane.addTab("Help", tabHelp);
        
        
        progressBar = new JProgressBar(0, 1000){
            @Override
            public void setValue(int n) {
                super.setValue(n);
                progressBar.repaint();
            }
        };
        progressBar.setValue(0);
        progressBar.setBorderPainted(true);
        progressBar.setStringPainted(true);
        
        
        draw = new DrawSpace(save, Color.WHITE, sim);
        
        frame = new sFrame(new FlowLayout(FlowLayout.CENTER, 0, 0), "Simple UAV Simulation"){
            @Override
            public void Config(int w, int h) {
                setSize(w, h);
                tabbedPane.Config(w-30, 100);
                progressBar.setPreferredSize(new Dimension(w-30, 23));
                draw.Config(w-30, h-170);
            }
        };
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                frame.Config(frame.getWidth(), frame.getHeight());
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
        
        
        frame.add(tabbedPane);
        frame.add(progressBar);
        frame.add(draw);
        
        frame.Config(1000, 700);
        setLookAndFeel(LookAndFeels[begin_index_look_and_feel].getClassName());
        frame.setVisible(true);
        
        fileChooserOpenSave = new JFileChooser("./");
        fileChooserOpenSave.setMultiSelectionEnabled(false);
        fileChooserOpenSave.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().matches(".*\\.sgl");
            }
            @Override
            public String getDescription() {
                return "Files .sgl";
            }
        });
        
    }
    public static void setLookAndFeel(String lookAndFeelds){
        try {
            UIManager.setLookAndFeel(lookAndFeelds);
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
    
    private static void MenuFileEvent(ActionEvent e, String item, int index) throws Exception {
        if(item.equals("New Map")){
            NewMap();
        }else if(item.equals("Open Map")){
            OpenMap();
        }else if (item.equals("Open Map2")){
            OpenMap2();
        }else if(item.equals("Save")){
            Save();
        }else if(item.equals("Save As")){
            SaveAs();
        }else if(item.equals("Save Point")){
            SavePoint();
        }else if(item.equals("Exit")){
            Exit();
        }else{
            throw new Exception("Event from iten '"+item+"' not is implemented yet");
        }
    }

    private static void NewMap() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static void OpenMap() throws FileNotFoundException {
        int R = fileChooserOpenSave.showOpenDialog(null);
        if(R == JFileChooser.APPROVE_OPTION){
            sim.open(fileChooserOpenSave.getSelectedFile());

            progressBar.setValue(0);
            progressBar.setString("");

            tabFile.Enabled("Save As");
            tabFile.Enabled("Save Point");
            tabFile.Disabled("Save");

            frame.setTitle("Simple UAV Simulation - " + sim);
            draw.repaint();
        }
    }
    
    private static void OpenMap2() throws FileNotFoundException {
        int R = fileChooserOpenSave.showOpenDialog(null);
        if(R == JFileChooser.APPROVE_OPTION){
            sim.open2(fileChooserOpenSave.getSelectedFile());

            progressBar.setValue(0);
            progressBar.setString("");

            tabFile.Enabled("Save As");
            tabFile.Enabled("Save Point");
            tabFile.Disabled("Save");

            frame.setTitle("Simple UAV Simulation - " + sim);
            draw.repaint();
        }
    }

    private static void Save() throws FileNotFoundException {        
        sim.save();
        tabFile.Disabled("Save");
    }

    private static void SaveAs() throws FileNotFoundException {
        int R = fileChooserOpenSave.showSaveDialog(null);
        if(R == JFileChooser.APPROVE_OPTION){
            final File file = fileChooserOpenSave.getSelectedFile();
            if(file.exists()){
                R = JOptionPane.showConfirmDialog(null, "Has another project call '"+file.getName()+"'\n" +
                        "Do you like replace it", "Conflict", JOptionPane.OK_CANCEL_OPTION);
            }else{
                R = JOptionPane.OK_OPTION;
            }
            if(R == JOptionPane.OK_OPTION){
                progressBar.setValue(0);
                progressBar.setString("");

                sim.setFile(file);
                sim.save();

                tabFile.Disabled("Save");
                frame.setTitle("Simple UAV Simulation - " + sim);
            }
        }
    }
    
    private static void SavePoint() throws FileNotFoundException {                       
        draw.savePoint();     
    }

    private static void Exit() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    
    private static void MenuViewEvent(ActionEvent e, String item, int index) throws Exception {
        if(item.equals("Grid")){
            Grid(e);
        }else{
            throw new Exception("Event from iten '"+item+"' not is implemented yet");
        }
    }
    private static void Grid(ActionEvent e) {
        JButton btm = (JButton) e.getSource();
        String s = draw.change_grid();
        btm.setText(s);
    }
    
    private static void MenuMapEvent(ActionEvent e, String item, int index) throws Exception {
        if(item.equals("Select")){
            Click(e);
        }else if(item.equals("Random")){
            Random(e);
        }else if (item.equals("Random2")){
            Random2(e);
        }else if(item.equals("Clear")){
            Clear(e);
        }else if(item.equals("Gen-500")){
            Gen500(e);
        }else if(item.equals("Gen-100")){
            Gen100(e);
        }else if(item.equals("Gen-10000")){
            Gen10000(e);
        }else if(item.equals("Exec C&A")){
            sim.save();
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        exec("java -jar CA.jar", new File("./"), true, true);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }else{
            throw new Exception("Event from iten '"+item+"' not is implemented yet");
        }
    }
    private static boolean ok;
    private static boolean exec(String cmd, File dir_IA, final boolean print, final boolean error) throws IOException, InterruptedException{
        //args = new String[]{"copy", "C:/ProOF/JAR/", "java_v001", "D:/fFramework/ProOF/ProOFLanguage_vA/work_space/code/"};
        //args = new String[]{"compile", "D:/fFramework/ProOF/ProOFLanguage_vA/work_space/code/java_v001/", "java_v001"};
        //args = new String[]{"execute", "D:/fFramework/ProOF/ProOFLanguage_vA/work_space/code/java_v001/", "java_v001"};
        final Process comp = Runtime.getRuntime().exec(cmd, null, dir_IA);
        
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Scanner sc = new Scanner(comp.getInputStream());
                while(sc.hasNextLine()){
                    if(print)System.out.print(sc.nextLine()+"\n");
                }
                sc.close();
            }
        });
        
        ok = true;
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Scanner sc = new Scanner(comp.getErrorStream());
                while(sc.hasNextLine()){
                    ok = false;
                    if(error)System.err.print("err:"+sc.nextLine()+"\n");
                }
                sc.close();
            }
        });
        comp.waitFor();
        comp.destroy();
        return ok;
    }
    
    private static void Click(ActionEvent e) {
        JButton btm = (JButton) e.getSource();
        String s = draw.change_click();
        btm.setText(s);
    }
    
    private static void Random(ActionEvent e) {
        sim.mapClassified = false;
        CreateMap createMap = new CreateMap(draw, sim);
        createMap.randomMap();
        save.main();
    }
    
    private static void Random2(ActionEvent e) {
        sim.mapClassified = true;
        CreateMap createMap = new CreateMap(draw, sim);
        createMap.randomMapWithoutIntersection();
        Statistics.getInstance().printStatistics();
        save.main();
    }
    
    private static void Clear(ActionEvent e) {
        draw.clear();
    }
    
    private static void Gen500(ActionEvent e) throws FileNotFoundException, InterruptedException {
        sim.mapClassified = false;
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for(int i=0; i<500; i++){
                        final int index = i;
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    progressBar.setValue(2*index);
                                    draw.clear();
                                    CreateMap createMap = new CreateMap(draw, sim);                                                                           
                                    createMap.randomMap();                                    
                                    sim.setFile(new File("./gen500/PPDCP_"+index/100+""+(index/10)%10+""+index%10+".sgl"));
                                    sim.save();
                                    frame.setTitle("Simple UAV Simulation - " + sim);
                                    if(index==500-1){
                                        progressBar.setValue(0);
                                        JOptionPane.showMessageDialog(frame, "500 randomic maps generated");
                                    }
                                }catch (Throwable ex) {
                                    ex.printStackTrace();
                                    JOptionPane.showMessageDialog(frame, "error");
                                }
                            }
                        });
                        Thread.sleep(50);
                    }
                }catch (Throwable ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "error");
                }
            }            
        });                            
    }
    
    private static void Gen100(ActionEvent e) throws FileNotFoundException, InterruptedException {
        final int gen = 100;
        sim.mapClassified = true;
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for(int i=0; i<gen; i++){
                        final int index = i;
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    progressBar.setValue(10*index);
                                    draw.clear();
                                    CreateMap createMap = new CreateMap(draw, sim);                                    
                                    createMap.randomMapWithoutIntersection();                                    
                                    sim.setFile(new File("./gen100/PPCCS_"+index/100+""+(index/10)%10+""+index%10+".sgl"));
                                    sim.save();
                                    frame.setTitle("Simple UAV Simulation - " + sim);
                                    if(index==gen-1){
                                        progressBar.setValue(0);
                                        JOptionPane.showMessageDialog(frame, 
                                                "100 randomic maps without intersection generated");
                                    }
                                }catch (Throwable ex) {
                                    ex.printStackTrace();
                                    JOptionPane.showMessageDialog(frame, "error");
                                }
                            }
                        });
                        Thread.sleep(50);
                    }
                    Statistics.getInstance().printStatistics();
                }catch (Throwable ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "error");
                }
            }            
        });                            
    }
    
    private static void Gen10000(ActionEvent e) throws FileNotFoundException, InterruptedException {
        final int gen = 10000;
        sim.mapClassified = true;
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for(int i=0; i<gen; i++){
                        final int index = i;
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    progressBar.setValue(index/10);
                                    draw.clear();
                                    CreateMap createMap = new CreateMap(draw, sim);                                    
                                    createMap.randomMapWithoutIntersection();                                    
                                    sim.setFile(new File("./gen10000/I6_PPCCS_"+
                                            (index/10000)+""+(index/1000)%10+""+
                                            (index/100)%10+""+(index/10)%10+""+
                                            index%10+".sgl"));
                                    sim.save();
                                    frame.setTitle("Simple UAV Simulation - " + sim);
                                    if(index==gen-1){
                                        progressBar.setValue(0);
                                        JOptionPane.showMessageDialog(frame, 
                                                "10000 randomic maps without intersection generated");
                                    }
                                }catch (Throwable ex) {
                                    ex.printStackTrace();
                                    JOptionPane.showMessageDialog(frame, "error");
                                }
                            }
                        });
                        Thread.sleep(50);
                    }
                    Statistics.getInstance().printStatistics();
                }catch (Throwable ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "error");
                }
            }            
        });                            
    }
    
    private static int start = 0;
    private static void MenuControlEvent(ActionEvent e, String item, int index) throws Exception {
        if(item.equals("Start")){
            Start(e);
        }else{
            throw new Exception("Event from iten '"+item+"' not is implemented yet");
        }
    }
    private static void Start(final ActionEvent e) {
        if(start==0){
            start = 1;
            sim.start(0);
            uav.start();
            draw.repaint();

            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(start==1 && sim.clock()){
                        try {
                            Thread.sleep(100);
                            vx = uav.rmd.nextGaussian();
                            vy = uav.rmd.nextGaussian();
                            
                            uav.control(vx, vy);
                        } catch (Exception ex) {
                            Logger.getLogger(CreateMapArtificial.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        draw.repaint();
                    }
                    JButton btm = (JButton) e.getSource();
                    btm.setText("Start");
                    btm.setEnabled(true);
                }
            });
            th.start();
            JButton btm = (JButton) e.getSource();
            btm.setText("Stop");
        }else{
            JButton btm = (JButton) e.getSource();
            btm.setEnabled(false);
            start = 0;
        }
    }
    
    private static void MenuLookAndFeellEvent(ActionEvent e, String item, int index) {
        setLookAndFeel(LookAndFeels[index].getClassName());
    }

    private static void MenuHelpEvent(ActionEvent e, String item, int index) throws Exception {
        if(item.equals("About")){
            About();
        }else{
            throw new Exception("Event from iten '"+item+"' not is implemented yet");
        }
    }
    private static void About() {

    }
}
