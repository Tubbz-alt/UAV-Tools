package map;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jesimar
 */
public class CreateMap {
    
    private static final Statistics statistic = Statistics.getInstance();
    
    private double rateMediumOcuped = 0.50;
    private double rateMediumZoneN = 0.50;    
    private double rateMediumZoneP = 0.30;
    private double rateMediumZoneB = 0.20;    
    
    private double rateRealOcuped = 0.0;
    private double rateRealOcupedN = 0.0;
    private double rateRealOcupedP = 0.0;    
    private double rateRealOcupedB = 0.0;
    
    private int sizeN = 0;
    private int sizeP = 0;
    private int sizeB = 0;
    
    /*Unidade usada - trabalha em centimetros*/
    private final int UNITY = 100;
    
    private final DrawSpace drawSpace;
    private final Simulation simulation;
    
    public CreateMap(DrawSpace drawSpace, Simulation simulation){
        this.drawSpace = drawSpace;
        this.simulation = simulation;
    }
    
    public void randomMap() {
        for (int i = 0; i < 10; i++) {
            double xm = Math.random() * 10 - 5;
            double ym = Math.random() * (-10);
            while (Util.dist(xm, ym, 0, 0) < 2.5 || Util.dist(xm, ym, 0, -10) < 2.5) {
                xm = Math.random() * 10 - 5;
                ym = Math.random() * (-10);
            }

            Polygon poly = new Polygon();

            double length = (1.5 + UAV.rmd.nextGaussian() * 0.5);
            while (length < 0.01) {
                length = (1.5 + UAV.rmd.nextGaussian() * 0.5);
            }
            xm *= UNITY;
            ym *= UNITY;
            length *= UNITY;

            /*
             poly.addPoint((int)(xm-length/2), (int)(ym-length/2));
             poly.addPoint((int)(xm-length/2), (int)(ym+length/2));
             poly.addPoint((int)(xm+length/2), (int)(ym+length/2));
             poly.addPoint((int)(xm+length/2), (int)(ym-length/2));
             */
            double ang = UAV.rmd.nextDouble() * Math.PI;
            
            Util.add(poly, xm, ym, length / 2, ang);
            simulation.add(poly);
        }
        drawSpace.repaint();
    }
    
    public void randomMapWithoutIntersection() {                       
        rateMediumZoneN *= rateMediumOcuped;        
        rateMediumZoneP *= rateMediumOcuped;        
        rateMediumZoneB *= rateMediumOcuped;
        
        double dimX = 1000 * UNITY;
        double dimY = 1000 * UNITY;       

        List<Polygon> listPoly = new ArrayList<>();
        while (rateRealOcuped < rateMediumOcuped) {
            Polygon poly = null;
            double length;
            double xm;
            double ym;
            do {
                if (rateRealOcupedN < rateMediumZoneN) {
                    do {
                        xm = Math.random() * dimX - dimX / 2;
                        ym = Math.random() * dimY - dimY / 2;
                    } while (Util.dist(xm, ym, 0, 0) < 200 * UNITY);
                } else {
                    xm = Math.random() * dimX - dimX / 2;
                    ym = Math.random() * dimY - dimY / 2;
                }
                do {
                    length = (150 * UNITY + UAV.rmd.nextGaussian() * 50 * UNITY);
                } while (length < 25 * UNITY);
                double ang = UAV.rmd.nextDouble() * Math.PI;
                poly = new Polygon();
                Util.add(poly, xm, ym, length / 2, ang);
            } while (verifyIntersection(listPoly, poly, xm, ym));
            listPoly.add(poly);
            rateRealOcuped += length * length / (dimX * dimY);
            String type = "";
            if (rateRealOcupedN < rateMediumZoneN) {
                rateRealOcupedN += length * length / (dimX * dimY);
                type = "n";
                sizeN++;
            } else if (rateRealOcupedP < rateMediumZoneP) {
                rateRealOcupedP += length * length / (dimX * dimY);
                type = "p";
                sizeP++;
            } else if (rateRealOcupedB < rateMediumZoneB) {
                rateRealOcupedB += length * length / (dimX * dimY);
                type = "b";
                sizeB++;
            }
            simulation.add(poly, type);
        }        
        drawSpace.repaint();
        generateStatistic();
    }
    
    private boolean verifyIntersection(List<Polygon> listPoly, Polygon poly, 
            double xm, double ym) {
        boolean polyWithIntersection = false;
        for (Polygon p : listPoly) {
            for (int i = 0; i < poly.npoints; i++) {
                if (p.contains(poly.xpoints[i], poly.ypoints[i])) {
                    return true;
                }
            }
            for (int i = 0; i < p.npoints; i++) {
                if (poly.contains(p.xpoints[i], p.ypoints[i])) {
                    return true;
                }
            }
            if (p.contains(xm, ym) || polyWithIntersection) {
                return true;
            }
        }
        return false;
    }
    
    public void generateStatistic(){
        int id = Statistics.index;
        statistic.rateOcupationFixed = rateMediumOcuped;
        statistic.rateZoneNFixed = rateMediumZoneN / rateMediumOcuped;
        statistic.rateZonePFixed = rateMediumZoneP / rateMediumOcuped;
        statistic.rateZoneBFixed = rateMediumZoneB / rateMediumOcuped;
        
        statistic.rateRealOcupation[id] = rateRealOcuped;
        statistic.amountZoneTotal[id] = sizeN + sizeP + sizeB;
        statistic.amountZoneN[id] = sizeN;
        statistic.amountZoneP[id] = sizeP;
        statistic.amountZoneB[id] = sizeB;
        statistic.rateRealZoneN[id] = rateRealOcupedN / rateRealOcuped;
        statistic.rateRealZoneP[id] = rateRealOcupedP / rateRealOcuped;
        statistic.rateRealZoneB[id] = rateRealOcupedB / rateRealOcuped;
        Statistics.increment();
    }
    
}
