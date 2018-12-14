package uav.mission_creator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import uav.mission_creator.file.ReaderFileConfigRoute;
import uav.mission_creator.struct.UtilGeo;
import uav.mission_creator.struct.geom.PointGeo;

/**
 *
 * @author Jesimar S. Arantes
 */
public class RouteGeoTo3D {

    private final ReaderFileConfigRoute config;    
   
    public static void main(String[] args) throws FileNotFoundException, IOException {        
        RouteGeoTo3D main = new RouteGeoTo3D();
        main.exec();
    }

    public RouteGeoTo3D() throws IOException {
        config = new ReaderFileConfigRoute();
    }
    
    public void exec() throws FileNotFoundException{
        Locale.setDefault(Locale.ENGLISH);
        
        String dir = config.getDirRoute3D();
        //p1: -21.99968206310259;-47.93965555555555;0.000000000000000
        //p2: -21.99502163051751;-47.93965555555555;0.000000000000000
        //geo: -47.93965555555555,-21.99734645634928,0
        double lat1 = -21.99968206310259;
        double lng1 = -47.93965555555555;
        double lat2 = -21.99502163051751;
        double lng2 = -47.93965555555555;
              
        double lonBase = -47.93965555555555;
        double latBase = -21.99734645634928;
        double altBase = 0.0;
        PointGeo pGeo = new PointGeo(lonBase, latBase, altBase);
        
        double x1 = UtilGeo.convertGeoToX(pGeo, lng1);
        double y1 = UtilGeo.convertGeoToY(pGeo, lat1);
        double x2 = UtilGeo.convertGeoToX(pGeo, lng2);
        double y2 = UtilGeo.convertGeoToY(pGeo, lat2);
        
        System.out.println("x1: " + x1);
        System.out.println("y1: " + y1);
        System.out.println("x2: " + x2);
        System.out.println("y2: " + y2);
        
    }
}
