package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import lib.iCallBack;

/**
 *
 * @author marcio
 */
public class Simulation {

    public final int size;
    public final int T;

    private final iCallBack repaint;
    private final UAV uav;

    private final ArrayList<Polygon> Pti[];
    private final ArrayList<String> typeZone[];

    private int time = 0;

    private File file = new File("./temp.sgl");

    public boolean mapClassified = false;

    @Override
    public String toString() {
        return file.getName();
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Simulation(iCallBack repaint, UAV uav) {
        this.repaint = repaint;
        this.uav = uav;
        this.size = uav.size;
        this.T = uav.T;
        this.Pti = new ArrayList[T];        
        for (int t = 0; t < T; t++) {
            this.Pti[t] = new ArrayList<>();
        }

        this.typeZone = new ArrayList[T];
        for (int t = 0; t < T; t++) {
            this.typeZone[t] = new ArrayList<>();
        }
    }

    public void save() throws FileNotFoundException {
        PrintStream output = new PrintStream(file);
        output.println("<number of polygons>");
        output.println(Pti[0].size());
        if (mapClassified) {
            int sizeN = 0;
            int sizeP = 0;
            int sizeB = 0;
            for (String str : typeZone[0]) {
                switch (str) {
                    case "n":
                        sizeN++;
                        break;
                    case "p":
                        sizeP++;
                        break;
                    case "b":
                        sizeB++;
                        break;
                }
            }
            output.println("<number of zona n>");
            output.println(sizeN);
            output.println("<number of zona p>");
            output.println(sizeP);
            output.println("<number of zona b>");
            output.println(sizeB);
        }
        int i = 0;
        for (Polygon p : Pti[0]) {
            if (mapClassified) {
                output.println("<x..., y..., n = " + p.npoints + ", id = " + (i) + ", type = " + typeZone[0].get(i) + ">");
                output.println(toString(p.xpoints, p.npoints));
                output.println(toString(p.ypoints, p.npoints));
            } else {
                output.println("<x..., y..., n = " + p.npoints + ", id = " + (i) + ">");
                output.println(toString(p.xpoints, p.npoints));
                output.println(toString(p.ypoints, p.npoints));
            }
            i++;
        }
        output.close();
    }

    public void open(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        sc.nextLine();
        int N = Integer.parseInt(sc.nextLine());
        for (int j = 0; j < N; j++) {
            sc.nextLine();
            int x[] = toVectorInt(sc.nextLine());
            int y[] = toVectorInt(sc.nextLine());
            add(new Polygon(x, y, x.length));
        }
        sc.close();
    }
    
    public void open2(File file) throws FileNotFoundException {
        mapClassified = true;
        Scanner sc = new Scanner(file);
        sc.nextLine();
        int N = Integer.parseInt(sc.nextLine());
        sc.nextLine();
        int n = Integer.parseInt(sc.nextLine());
        sc.nextLine();
        int p = Integer.parseInt(sc.nextLine());
        sc.nextLine();
        int b = Integer.parseInt(sc.nextLine());
        for (int j = 0; j < n; j++) {
            typeZone[time].add("n");
            sc.nextLine();
            int x[] = toVectorInt(sc.nextLine());
            int y[] = toVectorInt(sc.nextLine());
            add(new Polygon(x, y, x.length));
        }
        for (int j = 0; j < p; j++) {
            typeZone[time].add("p");
            sc.nextLine();
            int x[] = toVectorInt(sc.nextLine());
            int y[] = toVectorInt(sc.nextLine());
            add(new Polygon(x, y, x.length));
        }
        for (int j = 0; j < b; j++) {
            typeZone[time].add("b");
            sc.nextLine();
            int x[] = toVectorInt(sc.nextLine());
            int y[] = toVectorInt(sc.nextLine());
            add(new Polygon(x, y, x.length));
        }
        sc.close();
    }

    public ArrayList<Polygon> poly() {
        return Pti[time];
    }

    public void add(Polygon poly, int t) {
        Pti[t].add(poly);
    }

    public void add(Polygon poly) {
        for (int t = 0; t < T; t++) {
            add(poly, t);
        }
    }

    public void add(Polygon poly, int t, String type) {
        Pti[t].add(poly);
        typeZone[t].add(type);
    }

    public void add(Polygon poly, String type) {
        for (int t = 0; t < T; t++) {
            add(poly, t, type);
        }
    }

    public void clear() {
        for (int t = 0; t < T; t++) {
            Pti[t].clear();
            typeZone[t].clear();
        }
    }

    public void start(int t) {
        if (t >= 0 && t < T) {
            this.time = t;
            repaint.main();
        }
    }

    public boolean clock() {
        if (time < T - 1) {
            time++;
            repaint.main();
            return true;
        }
        return false;
    }

    public int grid(double v) {
        return size * ((int) ((v) / size));
    }

    public int lower(double v) {
        return grid(v - size + 1);
    }

    public int uper(double v) {
        return grid(v + size - 1);
    }

    public void paint(Graphics2D g2) {
        if (mapClassified) {
            int i = 0;
            for (Polygon p : Pti[time]) {
                switch (typeZone[time].get(i)) {
                    case "n":
                        g2.setColor(new Color(97, 97, 97));
                        break;
                    case "p":
                        g2.setColor(new Color(255, 103, 61));
                        break;
                    case "b":
                        g2.setColor(new Color(103, 167, 255));
                        break;
                    default:
                        g2.setColor(Color.BLACK);
                }
                g2.fillPolygon(p);
                i++;
            }
        } else {
            g2.setColor(Color.ORANGE);
            for (Polygon p : Pti[time]) {
                g2.fillPolygon(p);
            }
        }

        g2.setStroke(new BasicStroke(2));
        int i = 0;
        for (Polygon p : Pti[time]) {
            g2.setColor(Color.RED);
            g2.drawPolygon(p);
            g2.setColor(Color.BLACK);
            g2.drawString(String.format("[%d]", i), center(p.xpoints), center(p.ypoints));
            i++;
        }
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(2));
        for (Polygon p : Pti[time]) {
            normalPoly(g2, p);
        }
    }

    public static int center(int vet[]) {
        double avg = 0;
        for (int v : vet) {
            avg += v;
        }
        avg /= vet.length;
        return (int) (avg + 0.5);
    }

    public static void normalPoly(Graphics2D g2, Polygon poly) {
        if (poly.npoints == 1) {
            g2.drawOval(poly.xpoints[0] - 5, (int) poly.ypoints[0] - 5, 10, 10);
            return;
        }
        for (int i = 0; i < poly.npoints; i++) {
            int k = (i + 1) % poly.npoints;
            double x1 = poly.xpoints[i];
            double y1 = poly.ypoints[i];
            double x2 = poly.xpoints[k];
            double y2 = poly.ypoints[k];

            double a1 = -(y2 - y1);
            double a2 = +(x2 - x1);
            double b = a1 * x1 + a2 * y1;

            g2.drawOval((int) x1 - 5, (int) y1 - 5, 10, 10);

            double S = 30 / Math.sqrt(a1 * a1 + a2 * a2);
            g2.drawLine((int) ((x1 + x2) / 2), (int) ((y1 + y2) / 2), (int) ((x1 + x2) / 2 + S * a1), (int) ((y1 + y2) / 2 + S * a2));
            g2.fillOval((int) ((x1 + x2) / 2 - 5), (int) ((y1 + y2) / 2 - 5), 10, 10);
            g2.fillOval((int) ((x1 + x2) / 2 + S * a1 - 5), (int) ((y1 + y2) / 2 + S * a2 - 5), 10, 10);
        }
    }

    public void paintUAV(Graphics2D g2) {
        uav.paint(g2, time);
    }

    public void paintControl(Graphics2D g2) {
        uav.paintControl(g2);
    }

    private static int[] toVectorInt(String line) {
        String[] sa = line.split(",");
        int[] result = new int[sa.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (int) (100 * Double.parseDouble(sa[i]));
        }
        return result;
    }

    private static String toString(int valor) {
        return "" + valor / 100.0;
    }

    private static String toString(int valores[], int n) {
        String Resp = "" + toString(valores[0]);
        for (int i = 1; i < n; i++) {
            Resp += "," + toString(valores[i]);
        }
        return Resp;
    }

    public void del(double X, double Y) {
        LinkedList<Polygon> list = new LinkedList<>();
        for (Polygon poly : poly()) {
            if (poly.contains(X, Y)) {
                list.add(poly);
            }
        }
        for (Polygon p : list) {
            for (int t = 0; t < T; t++) {
                Pti[t].remove(p);
            }
        }
    }

}
