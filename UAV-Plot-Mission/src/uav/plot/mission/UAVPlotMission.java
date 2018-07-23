package uav.plot.mission;

import uav.plot.mission.readers.ReaderRoute;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;
import javax.swing.JFrame;
import marte.swing.graphics.pkg2d.navigation.sFrame;
import marte.swing.graphics.pkg2d.navigation.sPanelDraw;
import uav.plot.mission.readers.ReaderConfig;
import uav.plot.mission.readers.ReaderMap;

/**
 *
 * @author Jesimar Arantes
 */
public class UAVPlotMission {

    private static final double CONST_UNIT = 0.01; //in centimeters
    
    private static final Color COLOR_NFZ = new Color(97, 97, 97);
    private static final Color COLOR_PENALTY = new Color(255, 103, 61);
    private static final Color COLOR_BONUS = new Color(103, 167, 255);
    private static final Color COLOR_ROUTE = Color.RED;
    
    public static int toUnit(double value){
        return Math.round((float)(value/CONST_UNIT));
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        sFrame frame = new sFrame("UAV-Plot-Mission");
        
        ReaderConfig reader = ReaderConfig.getInstance();
        reader.read();
        
        String pathMap = reader.getFileMap(); 
        if (pathMap.equals("")){
            System.out.println("Você precisa informar o nome do arquivo de mapa");
            System.exit(0);
        }
        ReaderMap map = new ReaderMap(pathMap);
        map.read();
        
        String pathRoute = reader.getFileRoute();
        ReaderRoute route = new ReaderRoute(pathRoute);
        if (!pathRoute.equals("")){
            route.read();
        }
        
        sPanelDraw draw = new sPanelDraw(Color.WHITE) {
            @Override
            protected void paintDynamicScene(Graphics2D g2) {
                //Draw Map
                g2.setColor(COLOR_NFZ);
                for (int i = 0; i < map.getSizeNFZ(); i++){
                    g2.fillPolygon(
                            Arrays.stream(map.getVetorX_NFZ(i)).mapToInt((v)->toUnit(v)).toArray(), 
                            Arrays.stream(map.getVetorY_NFZ(i)).mapToInt((v)->toUnit(v)).toArray(), 
                            map.getVetorX_NFZ(i).length);
                }
                g2.setColor(COLOR_PENALTY);
                for (int i = 0; i < map.getSizePenalty(); i++){
                    g2.fillPolygon(
                            Arrays.stream(map.getVetorX_Penalty(i)).mapToInt((v)->toUnit(v)).toArray(), 
                            Arrays.stream(map.getVetorY_Penalty(i)).mapToInt((v)->toUnit(v)).toArray(), 
                            map.getVetorX_Penalty(i).length);
                }
                g2.setColor(COLOR_BONUS);
                for (int i = 0; i < map.getSizeBonus(); i++){
                    g2.fillPolygon(
                            Arrays.stream(map.getVetorX_Bonus(i)).mapToInt((v)->toUnit(v)).toArray(), 
                            Arrays.stream(map.getVetorY_Bonus(i)).mapToInt((v)->toUnit(v)).toArray(), 
                            map.getVetorX_Bonus(i).length);
                }
                
                //Draw Routes
                g2.setColor(COLOR_ROUTE);                
                for (int i = 0; i < route.getRoute3D().size(); i++){
                    g2.fillOval(
                            toUnit(route.getRoute3D().getPosition3D(i).getX())-15, 
                            toUnit(route.getRoute3D().getPosition3D(i).getY())-15, 
                            30, 
                            30
                    );
                    if (i+1 < route.getRoute3D().size()){
                        g2.drawLine(
                                toUnit(route.getRoute3D().getPosition3D(i).getX()), 
                                toUnit(route.getRoute3D().getPosition3D(i).getY()), 
                                toUnit(route.getRoute3D().getPosition3D(i+1).getX()), 
                                toUnit(route.getRoute3D().getPosition3D(i+1).getY())
                        );
                    }
                }
            }
        };
        
        frame.add(draw);
        frame.setSize(1000, 700);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        draw.Config(900, 600);
        draw.restart_system();
        
        int xInit = Arrays.stream(map.getVetorX_NFZ())
                        .mapToInt(
                                (d)->Arrays.stream(d)
                                        .mapToInt(v->(int)v)
                                        .min().getAsInt()
                        ).min().getAsInt();
        int yInit = Arrays.stream(map.getVetorY_NFZ())
                        .mapToInt(
                                (d)->Arrays.stream(d)
                                        .mapToInt(v->(int)v)
                                        .min().getAsInt()
                        ).min().getAsInt();
        int xFinal = Arrays.stream(map.getVetorX_NFZ())
                        .mapToInt(
                                (d)->Arrays.stream(d)
                                        .mapToInt(v->(int)v)
                                        .max().getAsInt()
                        ).max().getAsInt();
        int yFinal = Arrays.stream(map.getVetorY_NFZ())
                        .mapToInt(
                                (d)->Arrays.stream(d)
                                        .mapToInt(v->(int)v)
                                        .max().getAsInt()
                        ).max().getAsInt();
        
        draw.goTo(
                toUnit(xInit),
                toUnit(yInit), 
                toUnit(xFinal-xInit),
                toUnit(yFinal-yInit)
        );
        draw.repaint();
    }    
}
