package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Locale;
import lib.iCallBack;
import lib.sPanelDraw;

/**
 *
 * @author marcio
 */
public class DrawSpace extends sPanelDraw {

    public static final int GRID_NOT = 0;
    public static final int GRID_BEFORE = 1;
    public static final int GRID_AFTER = 2;

    public static final int CLICK_SELECT = 0;
    public static final int CLICK_NEW = 1;
    public static final int CLICK_DEL = 2;
    public static final int CLICK_POINT = 3;

    public final Cursor CursorHand = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    public final Cursor CursorDefault = Cursor.getDefaultCursor();

    public LinkedList<Polygon> selected = new LinkedList<>();   
    public LinkedList<Point> listPoint = new LinkedList<>();
    private Polygon Cur = null;

    private int grid = GRID_NOT;
    private int click = CLICK_SELECT;

    public final Simulation sim;
    private final iCallBack cb_save;

    public DrawSpace(iCallBack save, Color default_color, Simulation sim) {
        super(default_color);
        this.sim = sim;
        this.cb_save = save;
    }

    public void printRect() {

    }
    
    public void savePoint() throws FileNotFoundException {
        PrintStream output = new PrintStream(new File("./points-start.txt"));
        output.println("<points start>");
        output.println(listPoint.size());        
        output.println("<px, py, py, vx, vy, vz>");
        Point pLast = null;
        float vx = 24;
        float vy = 0;
        for (Point p : listPoint) {            
            float px = (float)(p.getX()/100.0);
            float py = (float)(p.getY()/100.0);
            float pz = 100;
            float vz = 0; 
            if (pLast != null){
                float pxLast = (float)(pLast.getX()/100.0);
                float pyLast = (float)(pLast.getY()/100.0);
                double angle = Math.atan2(py - pyLast, px - pxLast);
                vx = (float)(Math.cos(angle) * 24);
                vy = (float)(Math.sin(angle) * 24);
            }
            output.println(String.format(Locale.ENGLISH,"%.2f,%.2f,%.2f,%.2f,%.2f,%.2f", 
                    px, py, pz, vx,vy,vz));
            pLast = p;
        }
        output.close();
    }
    
    public void clear() {
        sim.clear();
        selected.clear();
        Cur = null;
        repaint();
    }

    public String change_grid() {
        grid = (grid + 1) % 3;
        repaint();
        return "Grid [ " + grid + " ]";
    }

    public String change_click() {
        click = (click + 1) % 4;
        Cur = null;
        repaint();
        if (click == CLICK_SELECT) {
            setCursor(CursorDefault);
            return "Select";
        } else if (click == CLICK_NEW) {
            setCursor(CursorHand);
            return "New-Poly";
        } else if (click == CLICK_DEL) {
            setCursor(CursorDefault);
            return "Del-Poly";
        } else if (click == CLICK_POINT) {
            setCursor(CursorDefault);
            return "Click-Point";
        }
        return "error";
    }

    @Override
    protected void MouseClicked(MouseEvent e, double X, double Y) throws Throwable {
        if (click == CLICK_SELECT) {
            if (!e.isControlDown()) {
                selected.clear();
            }
            boolean cursor = false;
            for (Polygon poly : sim.poly()) {
                if (poly.contains(X, Y)) {
                    cursor = true;
                    if (selected.contains(poly)) {
                        selected.remove(poly);
                    } else {
                        selected.addLast(poly);
                    }
                }
            }
            if (cursor) {
                setCursor(CursorHand);
            } else {
                selected.clear();
                setCursor(CursorDefault);
            }
            repaint();
        } else if (click == CLICK_NEW) {
            if (e.isControlDown()) {
                if (Cur != null) {
                    sim.add(Cur);
                    cb_save.main();
                    Cur = null;
                }
            } else if (e.isShiftDown()) {
                Cur = null;
            } else {
                if (Cur == null) {
                    Cur = new Polygon();
                }
                Cur.addPoint((int) X, (int) Y);
            }
            repaint();
        } else if (click == CLICK_DEL) {
            sim.del(X, Y);
            repaint();
        } else if (click == CLICK_POINT) {
            listPoint.add(new Point((int)X, (int)Y));
            repaint();
        }
    }

    @Override
    protected void MouseMoved(MouseEvent e, double X, double Y) {
        if (click == CLICK_SELECT) {
            boolean cursor = false;
            for (Polygon poly : sim.poly()) {
                if (poly.contains(X, Y)) {
                    cursor = true;
                }
            }
            if (cursor) {
                setCursor(CursorHand);
            } else {
                setCursor(CursorDefault);
            }
        }
    }
    
    public static final int Grid = 100;

    private double xo, yo;

    @Override
    protected void MousePressed(MouseEvent e, double X, double Y) {
        if (click == CLICK_SELECT) {
            //mark(X, Y, 10); 
            xo = X;
            yo = Y;
            for (Polygon poly : sim.poly()) {
                if (poly.contains(X, Y)) {
                    Cur = poly;
                }
            }
        }
    }

    @Override
    protected void MouseDragged(MouseEvent e, double X, double Y) {
        if (click == CLICK_SELECT) {
            /*if (Cur != null) {
             Cur.translate((int)(X-xo), (int)(Y-yo));
             //Cur.mov(X, Y, Grid);
                
             }
             for (Polygon gr : selected) {
             Cur.translate((int)(X-xo), (int)(Y-yo));
             //gr.mov(X, Y, Grid);
             }*/
            mov(X, Y);
            //this.xo = (int)X;
            //this.yo = (int)Y;
            repaint();
        }
    }

    private void mov(double X, double Y) {
        int dx = (int) (X - xo);
        int dy = (int) (Y - yo);
        if (dx != 0 || dy != 0) {
            xo += dx;
            yo += dy;
            if (Cur != null && !selected.contains(Cur)) {
                Cur.translate(dx, dy);
            }
            for (Polygon gr : selected) {
                /*for(int i=0; i<gr.npoints; i++){
                 gr.xpoints[i] = (int)(X-xo);
                 gr.ypoints[i] = (int)(Y-yo);
                 }*/
                gr.translate(dx, dy);
            }
            cb_save.main();
        }
    }

    @Override
    protected void MouseReleased(MouseEvent e, double X, double Y) {
        if (click == CLICK_SELECT) {
            /*if (Cur != null) {
             Cur.translate((int)(X-xo), (int)(Y-yo));
             }
             //Cur.mov(X, Y, Grid);
             for (Polygon gr : selected) {
             Cur.translate((int)(X-xo), (int)(Y-yo));
             //gr.mov(X, Y, Grid);
             }*/
            mov(X, Y);
            //this.xo = (int)X;
            //this.yo = (int)Y;
            Cur = null;
            cb_save.main();
        }
    }

    @Override
    protected void paintComponentBefore(Graphics2D g2) {
        paintRuler(g2, sim.size);
        if (grid == GRID_NOT) {
            g2.setColor(Color.WHITE);
            g2.fillRect(10, 10, 20, 20);

            g2.setColor(Color.BLACK);
            g2.drawRect(10, 10, 20, 20);
        } else if (grid == GRID_BEFORE) {
            g2.setColor(Color.WHITE);
            g2.fillRect(10, 10, 20, 20);

            g2.setColor(Color.BLACK);
            g2.drawRect(10, 10, 10, 10);
            g2.drawRect(10, 20, 10, 10);
            g2.drawRect(20, 10, 10, 10);
            g2.drawRect(20, 20, 10, 10);

            g2.setColor(Color.ORANGE);
            g2.fillRect(15, 15, 11, 11);
        } else {
            g2.setColor(Color.WHITE);
            g2.fillRect(10, 10, 20, 20);

            g2.setColor(Color.ORANGE);
            g2.fillRect(15, 15, 11, 11);

            g2.setColor(Color.BLACK);
            g2.drawRect(10, 10, 10, 10);
            g2.drawRect(10, 20, 10, 10);
            g2.drawRect(20, 10, 10, 10);
            g2.drawRect(20, 20, 10, 10);
        }
        sim.paintControl(g2);
    }

    @Override
    protected void paintComponentAfter(Graphics2D g2) {
        if (grid == GRID_BEFORE) {
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1));
            int xi = sim.lower(Xi());
            int xf = sim.uper(Xf());
            int yi = sim.lower(Yi());
            int yf = sim.uper(Yf());
            for (int x = xi; x <= xf; x += sim.size) {
                g2.drawLine(x, yi, x, yf);
            }
            for (int y = yi; y <= yf; y += sim.size) {
                g2.drawLine(xi, y, xf, y);
            }
        }
        
        int unity = 100;
        
        g2.drawRect(-50 * unity, -50 * unity, 100 * unity, 100 * unity);
        g2.drawRect(-600 * unity, -600 * unity, 1200 * unity, 1200 * unity);

        sim.paint(g2);
        if (Cur != null) {
            g2.setColor(Color.PINK);
            g2.fillPolygon(Cur);
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2));
            g2.drawPolygon(Cur);
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2));
            Simulation.normalPoly(g2, Cur);
        }
        for (Polygon p : selected) {
            g2.setColor(Color.PINK);
            g2.fillPolygon(p);
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2));
            g2.drawPolygon(p);
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2));
            Simulation.normalPoly(g2, p);
        }
        
        for (Point p : listPoint) {
            int rx = 400;
            int ry = 400;
            g2.setColor(Color.RED);
            g2.fillOval((int)p.getX()-rx, (int)p.getY()-ry, 2*rx, 2*ry);            
            g2.setStroke(new BasicStroke(2));
        }
        
        sim.paintUAV(g2);

        if (grid == GRID_AFTER) {
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1));
            int xi = sim.lower(Xi());
            int xf = sim.uper(Xf());
            int yi = sim.lower(Yi());
            int yf = sim.uper(Yf());
            for (int x = xi; x <= xf; x += sim.size) {
                g2.drawLine(x, yi, x, yf);
            }
            for (int y = yi; y <= yf; y += sim.size) {
                g2.drawLine(xi, y, xf, y);
            }
        }
    }

}
