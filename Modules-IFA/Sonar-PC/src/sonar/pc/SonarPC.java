package sonar.pc;

import java.util.Locale;

/**
 *
 * @author Jesimar Arantes.
 */
public class SonarPC {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SonarPC sonar = new SonarPC();
        sonar.start();
    }
    
    private final double INIT_DIST = 1.5;//in meters
    private final double MAX_DIST = 3.0;//in meters
    private final double MIN_DIST = 0.0;//in meters
    private final double FREQUENCY = 10;//in hertz
    
    public SonarPC() {
        Locale.setDefault(Locale.US);
    }
    
    private void start(){
        double dist = INIT_DIST;
        while (true){
            try {
                Thread.sleep((int)(1000.0/FREQUENCY));
            } catch (InterruptedException ex) {
                
            }
            if (Math.random() >= 0.5){
                dist += 0.2*Math.random();
                if (dist > MAX_DIST){
                    dist = MAX_DIST;
                }
            }else{
                dist -= 0.2*Math.random();
                if (dist < MIN_DIST){
                    dist = MIN_DIST;
                }
            }
            System.out.printf("%.2f\n", dist);
        }
    }
}
