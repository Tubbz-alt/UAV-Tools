package uav.mission_creator.struct.geom;

import uav.mission_creator.struct.UtilGeo;


/**
 *
 * @author Jesimar S. Arantes
 */
public class Point3D extends Point{
    
    private final String name;
    private final double x;
    private final double y;
    private final double h;
    
    public Point3D(double x, double y, double h) {
        this.name = "";
        this.x = x;
        this.y = y;
        this.h = h;
    }
    
    public Point3D(String name, double x, double y, double h) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.h = h;
    }
    
    public Point3D(PointGeo base, PointGeo point) {
        this.name = point.getName();       
        this.y = UtilGeo.convertGeoToY(base, point);
        this.x = UtilGeo.convertGeoToX(base, point);
        this.h = UtilGeo.convertGeoToZ(base, point);
    }
    
    public PointGeo parseToGeo(PointGeo base){
        return new PointGeo( 
            UtilGeo.converteXtoLongitude(base.getLng(), base.getLat(), x),
            UtilGeo.converteYtoLatitude(base.getLat(), y),
            base.getAlt() + h
        );
    }
    
    public Point3D minus(Point3D o){
        return new Point3D(x-o.x, y-o.y, h-o.h);
    }
    
    public double angle(){
        return Math.atan2(y, x);
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getH() {
        return h;
    }

    @Override
    public String toString() {
        return String.format("%.16g %.16g %.16g\n", x, y, h);
    }
    
    @Override
    public double distance(Point point) {
        Point3D p = (Point3D) point;
        return Math.sqrt((p.x - x)*(p.x - x) + (p.y - y)*(p.y - y) + (p.h - h)*(p.h - h));
    }
}
