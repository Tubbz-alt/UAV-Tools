package temperature.sensor.pc;

import java.util.Locale;

/**
 *
 * @author Jesimar Arantes.
 */
public class TemperatureSensorPC {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TemperatureSensorPC temperature = new TemperatureSensorPC();
        temperature.start();
    }
    
    private final double INIT_TEMPERATURE = 20.0;//in graus celsius
    private final double MAX_TEMPERATURE = 80.0;//in graus celsius
    private final double MIN_TEMPERATURE = 0.0;//in graus celsius
    private final double FREQUENCY = 10;//in hertz
    
    public TemperatureSensorPC() {
        Locale.setDefault(Locale.US);
    }
    
    private void start(){
        double temperature = INIT_TEMPERATURE;
        while (true){
            try {
                Thread.sleep((int)(1000.0/FREQUENCY));
            } catch (InterruptedException ex) {
                
            }
            if (Math.random() >= 0.5){
                temperature += 2.0*Math.random();
                if (temperature > MAX_TEMPERATURE){
                    temperature = MAX_TEMPERATURE;
                }
            }else{
                temperature -= 0.2*Math.random();
                if (temperature < MIN_TEMPERATURE){
                    temperature = MIN_TEMPERATURE;
                }
            }
            System.out.printf("%.2f\n", temperature);
        }
    }
}
