package map;

import java.awt.Polygon;

/**
 *
 * @author jesimar
 */
public class Util {
    
    public static double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    
    public static void add(Polygon poly, double xm, double ym, double R, double ang) {
        double x, y;

        x = (-R) * Math.cos(ang) - (-R) * Math.sin(ang);
        y = (-R) * Math.sin(ang) + (-R) * Math.cos(ang);
        poly.addPoint((int) (x + xm), (int) (y + ym));

        x = (-R) * Math.cos(ang) - (+R) * Math.sin(ang);
        y = (-R) * Math.sin(ang) + (+R) * Math.cos(ang);
        poly.addPoint((int) (x + xm), (int) (y + ym));

        x = (+R) * Math.cos(ang) - (+R) * Math.sin(ang);
        y = (+R) * Math.sin(ang) + (+R) * Math.cos(ang);
        poly.addPoint((int) (x + xm), (int) (y + ym));

        x = (+R) * Math.cos(ang) - (-R) * Math.sin(ang);
        y = (+R) * Math.sin(ang) + (-R) * Math.cos(ang);
        poly.addPoint((int) (x + xm), (int) (y + ym));
    }
    
}
