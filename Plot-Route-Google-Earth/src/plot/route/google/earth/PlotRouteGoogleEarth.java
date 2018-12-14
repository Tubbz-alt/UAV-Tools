package plot.route.google.earth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 *
 * @author Jesimar S. Arantes
 */
public class PlotRouteGoogleEarth {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        String nameRoute = "route-uav";
        String route = "";
        File file = new File("route.txt");
        Scanner sc = new Scanner(file);
        String line = sc.nextLine();
        while(sc.hasNext()){
            line = sc.nextLine();
            line = line.replace(",", ".");
            String lines[] = line.split("\t");
            route += lines[1] + "," +  lines[0] +  "," + lines[2] + " ";
        }
        System.out.println("\t\t<Placemark>");
        System.out.println("\t\t\t<name>" + nameRoute + "</name>");
        System.out.println("\t\t\t<styleUrl>#msn_ylw-pushpin</styleUrl>");
        System.out.println("\t\t\t<LineString>");
        System.out.println("\t\t\t\t<tessellate>1</tessellate>");
        System.out.println("\t\t\t\t<coordinates>");
        System.out.println("\t\t\t\t\t" + route);
        System.out.println("\t\t\t\t</coordinates>");
        System.out.println("\t\t\t</LineString>");
        System.out.println("\t\t</Placemark>");
        
        PrintStream print = new PrintStream(new File("out-route.txt"));
        print.println("\t\t<Placemark>");
        print.println("\t\t\t<name>" + nameRoute + "</name>");
        print.println("\t\t\t<styleUrl>#msn_ylw-pushpin</styleUrl>");
        print.println("\t\t\t<LineString>");
        print.println("\t\t\t\t<tessellate>1</tessellate>");
        print.println("\t\t\t\t<coordinates>");
        print.println("\t\t\t\t\t" + route);
        print.println("\t\t\t\t</coordinates>");
        print.println("\t\t\t</LineString>");
        print.println("\t\t</Placemark>");
        print.close();
        
        /**
         * <Placemark>
         *     <name>route-uav</name>
         *     <styleUrl>#msn_ylw-pushpin</styleUrl>
         *     <LineString>
         *         <tessellate>1</tessellate>
         *         <coordinates>
         *             -47.9332138,-22.0016943,0 -47.9332138,-22.0016943,0 
         *         </coordinates>
         *     </LineString>
         * </Placemark>
         */
    }
    
}
