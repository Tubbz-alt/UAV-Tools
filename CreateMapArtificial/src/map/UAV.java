package map;

import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Locale;
import java.util.Random;

/**
 *
 * @author marcio
 */
public final class UAV {
    
    public final double A2[][] = new double[][]{
        {1,     1,  0,      0},
        {0,     1,  0,      0},
        {0,     0,  1,      1},
        {0,     0,  0,      1}
    };
    
    public final double B2[][] = new double[][]{
        {   0.5,    0       },
        {   1,      0       },
        {   0,      0.5     },
        {   0,      1       }
    };
    
    public final double B2_[][] = new double[][]{
        {0.5/B2[0][0],  0.5/B2[1][0],   0,              0},
        {0,             0,              0.5/B2[0][0],   0.5/B2[1][0]},
    };
    
    public final double G2[] = new double[]{
        0, 0, 0, 0
    };
    
    public final double A[][] = new double[][]{
        {1, 0.7869, 0,      0},
        {0, 0.6065, 0,      0},
        {0,      0, 1, 0.7869},
        {0,      0, 0, 0.6065}
    };
    
    public final double B[][] = new double[][]{
        {0.2131, 0     },
        {0.3935, 0     },
        {     0, 0.2131},
        {     0, 0.3935}
    };
    
    public final double G[] = new double[]{
        0, 0, 0.49, 0.98
    };
    
    public final double B_[][] = new double[][]{
        {0.5/B[0][0],  0.5/B[1][0],   0,              0},
        {0,             0,              0.5/B[0][0],   0.5/B[1][0]},
    };
    
    public final double Q[][] = new double[][]{
        {0.03555, 0,         0,      0       },
        {0,      0.06320,    0,      0       },
        {0,      0,         0.03555, 0       },
        {0,      0,         0,      0.06320  }
    };
    
    public final double P0[][] = new double[][]{
        {0.0025,     0,      0,          0},
        {0, 0.00000025,      0,          0},
        {0,          0, 0.0025,          0},
        {0,          0,      0, 0.00000025}
    };
    
    public final int size;
    public final int T;
    
    private double Xt[][]; //Real path    : px, vx, py, vy
    private double It[][]; //Ideal path   : px, vx, py, vy
    private double Ut[][]; //control path : Vx, Vy
    private double URt[][]; //control path : Vx, Vy
    
    public final static Random rmd = new Random();
    private int time;
    
    public UAV(int size, int T) {
        this.size = size;
        this.T = T;
        this.Xt = new double[T][4];
        this.It = new double[T][4];
        this.Ut = new double[T][2];
        this.URt = new double[T][2];
        this.start();
    }
    
    private final static int horizon = 100;
    private final static int RAND_SPIRAL = 1;
    private final static int FEED_BACK = 1;
    private final static int LEARNING = 0;          //0 - not | 1 - cplex | 2 - optimal
    private final static int DIRECIONAL_FORCE = 1;  //gravity and wind
    
    public boolean control(double Vx, double Vy) throws Exception{
        if(DIRECIONAL_FORCE==0){
            for(int i=0; i<4; i++){
                G[i] = 0;
            }
        }
        if(LEARNING==2){
            for(int i=0; i<4; i++){
                System.arraycopy(A[i], 0, A2[i], 0, 4);
                System.arraycopy(B[i], 0, B2[i], 0, 2);
                G2[i] = G[i];
            }
        }
        if(time<T){
            /*if(time%200<50){
                Ut[time][0] = Math.cos(2*Math.PI*(time%200)/50.0);
                Ut[time][1] = Math.sin(2*Math.PI*(time%200)/50.0);;
            }else{
                Ut[time][0] = Vx;
                Ut[time][1] = Vy;
            }*/
            if(RAND_SPIRAL==0){
                Ut[time][0] = Vx;
                Ut[time][1] = Vy;
                
                Ut[time][0] -= G2[0]*Math.PI*0.32/(B2[0][0]+B2[0][1]);
                Ut[time][1] -= G2[2]*Math.PI*0.32/(B2[2][0]+B2[2][1]);
            }else{
                double a = 0;
                double b = 0.15*(time)/horizon;
                double ang = 3*Math.PI*Math.sqrt(30*time)/horizon;
                double r = b;
                Ut[time][0] = r*Math.cos(ang);
                Ut[time][1] = r*Math.sin(ang);

                Ut[time][0] -= G2[0]*Math.PI*0.34/(B2[0][0]+B2[0][1]);
                Ut[time][1] -= G2[2]*Math.PI*0.34/(B2[2][0]+B2[2][1]);
            }
            
            Ut[time][0] = Math.max(Ut[time][0], -3);
            Ut[time][1] = Math.min(Ut[time][1], +3);
            
            real(Xt[time+1] ,  Xt[time], URt[time], It[time], Ut[time]);
            
            ideal(It[time+1] ,  It[time], Ut[time]);

            time++;
            if(LEARNING==1 && time%horizon==0){
                cplex();
                /*for(int i=0; i<4; i++){
                    for(int j=0; j<4; j++){
                        A2[i][j] = A[i][j];
                    }
                    for(int j=0; j<2; j++){
                        B2[i][j] = B[i][j];
                    }
                    if(time>10*horizon){
                        G2[i] = G[i];
                    }
                    //G2[i] = G[i];
                }*/
            }
            return true;
        }
        return false;
    }
    
    public void start(){
        this.time = 0;
        for(int i=0; i<4; i++){
            Xt[time][i] = rmd.nextGaussian()*P0[i][i]; 
        }
        for(int i=0; i<4; i++){
            It[time][i] = 0;
        }
        if(LEARNING==2){
            for(int i=0; i<4; i++){
                for(int j=0; j<2; j++){
                    Kt[j][i] = 0;
                    for(int n=0; n<4; n++){
                        Kt[j][i] += B_[j][n] * A[n][i];
                    }
                    Kt[j][i] *= -1;
                    //Kt[j][i] = 0;
                }
            }
            System.out.println("---------------------[K optimal]----------------------");
        }else{
            for(int i=0; i<4; i++){
                for(int j=0; j<2; j++){
                    Kt[j][i] = 0;
                    for(int n=0; n<4; n++){
                        Kt[j][i] += B2_[j][n] * A2[n][i];
                    }
                    Kt[j][i] *= -1;
                    //Kt[j][i] = 0;
                }
            }
            System.out.println("---------------------[K used]----------------------");
        }
        for(int i=0; i<2; i++){
            for(int j=0; j<4; j++){
                System.out.printf("%9g ", Kt[i][j]);
            }
            System.out.println();
        }
        //time++;
    }
    
    private void update() throws Exception{
        boolean solve = cplexKt();
        //boolean solve = true;
        if(solve){
            for(int i=0; i<4; i++){
                for(int j=0; j<4; j++){
                    A2[i][j] = AR[i][j];
                }
                for(int j=0; j<2; j++){
                    B2[i][j] = BR[i][j];
                }
                //if(time>3*horizon){
                    G2[i] = GR[i];
                //}
            }
        }
        /*for(int t=0; t<time; t++){
            ideal(It[t+1] ,  It[t], Ut[t]);
        }*/
    }
    
    private void real(double R[], double X[], double UR[], double It[], double U[]){
        double Y[] = new double[4];
        /*double Kt[][] = new double[2][4];
        for(int i=0; i<4; i++){
            for(int j=0; j<2; j++){
                Kt[j][i] = 0;
                for(int n=0; n<4; n++){
                    Kt[j][i] += B_[j][n] * A[n][i];
                }
                Kt[j][i] *= -1;
                //Kt[j][i] = 0;
            }
        }*/
        for(int i=0; i<4; i++){
            Y[i] = X[i] + rmd.nextGaussian() * Q[i][i];
        }
        
        for(int i=0; i<4; i++){
            R[i] = 0;
            for(int j=0; j<4; j++){
                R[i] += A[i][j] * X[j];
            }
            for(int j=0; j<2; j++){
                double u = 0;
                for(int n=0; n<4; n++){
                    u += Kt[j][n]*(Y[n] - It[n]);
                }
                if(FEED_BACK==1){
                    UR[j] = U[j] + u;
                }else{
                    UR[j] = U[j];
                }
                
            }
            for(int j=0; j<2; j++){
                UR[j] = Math.max(UR[j], -3);
                UR[j] = Math.min(UR[j], +3);
                
                R[i] += B[i][j] * UR[j];
            }
            //R[i] = It[i];
            
            R[i] += G[i]; //gravidade e vento direcional
        }
        //System.out.println("R2 = "+R[2]);
        R[1] = Math.max(R[1], -3);
        R[1] = Math.min(R[1], +3);
        R[3] = Math.max(R[3], -3);
        R[3] = Math.min(R[3], +3);
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                double v = rmd.nextGaussian();
                R[i] += v*Q[i][j];
            }
        }
    }
    
    public static void main(String args[]){
        Random rmd = new Random();
        double m = 0;
        int tot = 1000000;
        for(int i=0; i<tot; i++){
            double v = rmd.nextGaussian();
            m += Math.abs(v);
        }
        System.out.println("avg   = "+m/tot);
        System.out.println("error = "+Math.abs(m/tot-1/Math.sqrt(Math.PI/2)));//0.79788456080286535587989211986876
    }
    
    private void ideal(double R[], double X[], double U[]){
        for(int i=0; i<4; i++){
            R[i] = 0;
            for(int j=0; j<4; j++){
                R[i] += A2[i][j] * X[j];
            }
            for(int j=0; j<2; j++){
                R[i] += B2[i][j] * U[j];
            }
            R[i] += G2[i];
        }
        R[1] = Math.max(R[1], -3);
        R[1] = Math.min(R[1], +3);
        R[3] = Math.max(R[3], -3);
        R[3] = Math.min(R[3], +3);
    }
    
    public final double AR[][] = new double[4][4];
    public final double BR[][] = new double[4][2];
    public final double GR[] = new double[4];
    public final double QR[][] = new double[4][4];
    
    public final double Kt[][] = new double[2][4];
    
    private boolean cplexKt() throws Exception{
        final double M = 100.0;
        
        double dx[][] = new double[time-1][4];
        for(int i=0; i<4; i++){
            double max = 0;
            for(int t=0; t<time-1; t++){
                dx[t][i] = Xt[t][i]-It[t][i];
                max = Math.max(max, Math.abs(dx[t][i]));
            }
            for(int t=0; t<time-1; t++){
                dx[t][i] = dx[t][i]*3/max;
            }
        }
        
        IloCplex cplex = new IloCplex();
        IloNumVar K[][] = new IloNumVar[2][4];
        IloNumVar D[][] = new IloNumVar[time-1][4];
        
        for(int i=0; i<2; i++){
            for(int j=0; j<4; j++){
                K[i][j] = cplex.numVar(Integer.MIN_VALUE, Integer.MAX_VALUE, "K"+i+","+j);
            }
        }
        for(int t=0; t<time-1; t++){
            for(int i=0; i<4; i++){
                D[t][i] = cplex.numVar(0, Integer.MAX_VALUE, "D"+t+","+i);
            }
        }
        
        for(int t=0; t<time-1; t++){
            for(int i=0; i<4; i++){
                IloNumExpr exp = null;
                for(int j=0; j<4; j++){
                    for(int k=0; k<2; k++){
                        if(exp==null){
                            exp = cplex.prod(K[k][j], BR[i][k]*(dx[t][j]));
                        }else{
                            exp = cplex.sum(exp, cplex.prod(K[k][j], BR[i][k]*(dx[t][j])));
                        }
                    }
                }
                double v = 0;
                for(int j=0; j<4; j++){
                    v += AR[i][j] * (dx[t][j]);
                }
                exp = cplex.sum(exp, v);
                cplex.addGe(D[t][i], cplex.prod(+1,exp));
                cplex.addGe(D[t][i], cplex.prod(-1,exp));
            }
        }
        
        IloNumExpr obj = null;
        for(int t=0; t<time-1; t++){
            for(int j=0; j<4; j++){
                if(obj==null){
                    obj = cplex.prod(cplex.prod(D[t][j], D[t][j]), t+1);
                }else{
                    obj = cplex.sum(obj, cplex.prod(cplex.prod(D[t][j], D[t][j]), t+1));
                }
            }
        }
        cplex.addMinimize(obj);
        
        cplex.exportModel("Kt.lp");
        cplex.setOut(null);
        if(cplex.solve()){
            System.out.println("Status = "+cplex.getStatus());
            System.out.println("Obj    = "+cplex.getObjValue());

            
            System.out.println("---------------------[K]----------------------");
            for(int i=0; i<2; i++){
                for(int j=0; j<4; j++){
                    Kt[i][j] = cplex.getValue(K[i][j]);
                    if(Math.abs(Kt[i][j])<0.0001){
                        Kt[i][j] = 0;
                    }
                    System.out.printf("%9g ", Kt[i][j]);
                }
                System.out.println();
            }
            System.out.println("---------------------[K-r]----------------------");
            for(int i=0; i<2; i++){
                for(int j=0; j<4; j++){
                    double v = 0;
                    for(int n=0; n<4; n++){
                        v += B_[i][n] * A[n][j];
                    }
                    v *= -1;
                    if(Math.abs(v)<0.0001){
                        v = 0;
                    }
                    System.out.printf("%9g ", v);
                }
                System.out.println();
            }
            System.out.println("---------------------[iB]----------------------");
            for(int t=0; t<time-1; t++){
                for(int j=0; j<4; j++){
                    double v = cplex.getValue(D[t][j]);
                    if(Math.abs(v)<0.0001){
                        v = 0;
                    }
                    System.out.printf("%9g ", v);
                }
                System.out.println();
            }
            cplex.end();
            return true;
        }else{
            System.out.println("Status = "+cplex.getStatus());
            System.out.println("---------------------[K]----------------------");
            for(int i=0; i<2; i++){
                for(int j=0; j<4; j++){
                    System.out.printf("%9g ", Kt[i][j]);
                }
                System.out.println();
            }
            cplex.end();
            return false;
        } 
    }
    
    private void cplex() throws Exception{
        IloCplex cplex = new IloCplex();
        IloNumVar A[][] = new IloNumVar[4][4];
        IloNumVar B[][] = new IloNumVar[4][2];
        IloNumVar G[]   = new IloNumVar[4];
        IloNumVar D[][] = new IloNumVar[time-1][4];
        IloNumVar Z[][] = new IloNumVar[4][6];
        IloNumVar Y[][] = new IloNumVar[4][6];
        IloIntVar W[][] = new IloIntVar[4][6];
        
        for(int i=0; i<4; i++){
            G[i] = cplex.numVar(Integer.MIN_VALUE, Integer.MAX_VALUE, "G"+i);
        }
        for(int i=0; i<4; i++){
            for(int j=0; j<6; j++){
                Z[i][j] = cplex.numVar(0, Integer.MAX_VALUE, "Z"+i+","+j);
                Y[i][j] = cplex.numVar(0, Integer.MAX_VALUE, "Y"+i+","+j);
                W[i][j] = cplex.boolVar("W"+i+","+j);
            }
        }
        
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                A[i][j] = cplex.numVar(Integer.MIN_VALUE, Integer.MAX_VALUE, "A"+i+","+j);
            }
        }
        for(int i=0; i<4; i++){
            for(int j=0; j<2; j++){
                B[i][j] = cplex.numVar(Integer.MIN_VALUE, Integer.MAX_VALUE,  "B"+i+","+j);
            }
        }
        
        for(int t=0; t<time-1; t++){
            for(int j=0; j<4; j++){
                D[t][j] = cplex.numVar(0, Integer.MAX_VALUE, "D"+j+","+t);
            }
        }
        
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                cplex.addGe(Z[i][j], cplex.sum(cplex.prod(A[i][j], +1), cplex.prod(Y[i][j], -1)));
                cplex.addGe(Z[i][j], cplex.sum(cplex.prod(A[i][j], -1), cplex.prod(Y[i][j], +1)));
            }
            for(int j=0; j<2; j++){
                cplex.addGe(Z[i][j+4], cplex.sum(cplex.prod(B[i][j], +1), cplex.prod(Y[i][j+4], -1)));
                cplex.addGe(Z[i][j+4], cplex.sum(cplex.prod(B[i][j], -1), cplex.prod(Y[i][j+4], +1)));
            }
            
            for(int j=0; j<4; j++){
                cplex.addGe(Y[i][j], cplex.prod(A[i][j], +1));
                cplex.addGe(Y[i][j], cplex.prod(A[i][j], -1));
            }
            for(int j=0; j<2; j++){
                cplex.addGe(Y[i][j+4], cplex.prod(B[i][j], +1));
                cplex.addGe(Y[i][j+4], cplex.prod(B[i][j], -1));
            }
            
            for(int j=0; j<6; j++){
                cplex.addGe(Y[i][j], cplex.prod(W[i][j], 0));
                cplex.addLe(Y[i][j], cplex.prod(W[i][j], 100));
            }
            /*
            for(int j=0; j<4; j++){
                cplex.addGe(Z[i][j], cplex.sum(cplex.prod(A[i][j], +1), -AR[i][j]));
                cplex.addGe(Z[i][j], cplex.sum(cplex.prod(A[i][j], -1), +AR[i][j]));
            }
            for(int j=0; j<2; j++){
                cplex.addGe(Z[i][j+4], cplex.sum(cplex.prod(B[i][j], +1), -BR[i][j]));
                cplex.addGe(Z[i][j+4], cplex.sum(cplex.prod(B[i][j], -1), +BR[i][j]));
            }
             */
            /*for(int j=0; j<4; j++){
                cplex.addLe(A[i][j], cplex.prod(W[i][j], 10));
                cplex.addGe(A[i][j], cplex.prod(W[i][j], -10));
            }
            for(int j=0; j<2; j++){
                cplex.addLe(B[i][j], cplex.prod(W[i][j+4], 10));
                cplex.addGe(B[i][j], cplex.prod(W[i][j+4], -10));
            }*/
        }
        
        for(int t=0; t<time-1; t++){
            for(int i=0; i<4; i++){
                IloNumExpr exp = cplex.prod(G[i], +1);
                for(int j=0; j<2; j++){
                    exp = cplex.sum(exp, cplex.prod(B[i][j], URt[t][j]));
                }
                for(int j=0; j<4; j++){
                    exp = cplex.sum(exp, cplex.prod(A[i][j], Xt[t][j]));
                }
                exp = cplex.sum(D[t][i], exp);
                cplex.addGe(exp, Xt[t+1][i]);
            }
        }
        for(int t=0; t<time-1; t++){
            for(int i=0; i<4; i++){
                IloNumExpr exp = cplex.prod(G[i], -1);
                for(int j=0; j<2; j++){
                    exp = cplex.sum(exp, cplex.prod(B[i][j], -URt[t][j]));
                }
                for(int j=0; j<4; j++){
                    exp = cplex.sum(exp, cplex.prod(A[i][j], -Xt[t][j]));
                }
                exp = cplex.sum(D[t][i], exp);
                cplex.addGe(exp, -Xt[t+1][i]);
            }
        }
                
        IloNumExpr exp1 = null;
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                if(exp1==null){
                    exp1 = W[i][j];
                }else{
                    exp1 = cplex.sum(exp1, W[i][j]);
                }
            }
        }
        IloNumExpr exp2 = null;
        for(int i=0; i<4; i++){
            for(int j=0; j<2; j++){
                if(exp2==null){
                    exp2 = W[i][j+4];
                }else{
                    exp2 = cplex.sum(exp2, W[i][j+4]);
                }
            }
        }
        cplex.addGe(exp1, 2);
        cplex.addGe(exp2, 2);
        
        IloNumExpr obj = null;
        for(int t=0; t<time-1; t++){
            for(int i=0; i<4; i++){
                double weigth;
                if(t>time-1-horizon){
                    weigth = 1;
                    //weigth = (t+1.0)/time;
                    //weigth = Math.sqrt((t+1.0)/time);
                    //weigth = 1/Math.pow(1.25,time-1-t);
                    //weigth = 1;
                }else{
                    weigth = 1;
                    //weigth = (t+1.0)/time;
                    //weigth = Math.sqrt((t+1.0)/time);
                    //weigth = 1/Math.pow(1.25,time-1-horizon-t);
                }
                if(obj==null){
                    //obj = cplex.prod(D[t][i], D[t][i]);
                    obj = cplex.prod(D[t][i],  weigth);
                }else{
                    //obj = cplex.sum(obj, cplex.prod(D[t][i], D[t][i]));
                    obj = cplex.sum(obj, cplex.prod(D[t][i], weigth));
                }
            }
        }
        for(int i=0; i<4; i++){
            for(int j=0; j<6; j++){
                obj = cplex.sum(obj, cplex.prod(Z[i][j], 1000));
                obj = cplex.sum(obj, cplex.prod(Y[i][j], -2));
                obj = cplex.sum(obj, cplex.prod(W[i][j], 1));
                obj = cplex.sum(obj, cplex.prod(cplex.prod(Y[i][j], Y[i][j]), 1));
            }
        }
                
        cplex.addMinimize(obj);
        //cplex.exportModel("UAV.lp");
        cplex.setOut(null);
        if(cplex.solve()){
            System.out.println("Status = "+cplex.getStatus());
            System.out.println("Obj    = "+cplex.getObjValue());
            System.out.println("---------------------[RA]----------------------");
            for(int i=0; i<4; i++){
                for(int j=0; j<4; j++){
                    AR[i][j] = cplex.getValue(A[i][j]);
                    if(Math.abs(AR[i][j])<0.0001){
                        AR[i][j] = 0;
                    }
                    System.out.printf("%9g ", AR[i][j]);
                }
                System.out.println();
            }
            System.out.println("---------------------[RB]----------------------");
            for(int i=0; i<4; i++){
                for(int j=0; j<2; j++){
                    BR[i][j] = cplex.getValue(B[i][j]);
                    if(Math.abs(BR[i][j])<0.0001){
                        BR[i][j] = 0;
                    }
                    System.out.printf("%9g ", BR[i][j]);
                }
                System.out.println();
            }
            System.out.println("---------------------[G]----------------------");
            for(int i=0; i<4; i++){
                GR[i] = cplex.getValue(G[i]);
                if(Math.abs(GR[i])<0.0001){
                    GR[i] = 0;
                }
                System.out.printf("%9g ", GR[i]);
            }
            System.out.println();
            System.out.println("---------------------[RQ]----------------------");
            for(int i=0; i<4; i++){
                QR[i][i] = 0;
                for(int t=0; t<time-1; t++){
                    double erro = -Xt[t+1][i];
                    for(int j=0; j<4; j++){
                        erro += AR[i][j] * Xt[t][j];
                    }
                    for(int j=0; j<2; j++){
                        erro += BR[i][j] * URt[t][j];
                    }
                    erro = Math.abs(erro);
                    QR[i][i] += erro;
                }
                QR[i][i] = QR[i][i]*Math.sqrt(Math.PI/2) /(time-1);
                for(int j=0; j<4; j++){
                    if(Math.abs(QR[i][j])<0.0001){
                        QR[i][j] = 0;
                    }
                    System.out.printf("%9g ", QR[i][j]);
                }
                System.out.println();
            }
            System.out.println("---------------------[W]----------------------");
            for(int i=0; i<4; i++){
                for(int j=0; j<6; j++){
                    System.out.printf("%9d ", cplex.getValue(W[i][j]) < 0.5 ? 0 : 1);
                }
                System.out.println();
            }
            System.out.println("---------------------[Z]----------------------");
            for(int i=0; i<4; i++){
                for(int j=0; j<6; j++){
                    System.out.printf("%9g ", cplex.getValue(Z[i][j]));
                }
                System.out.println();
            }
            System.out.println("---------------------[Y]----------------------");
            for(int i=0; i<4; i++){
                for(int j=0; j<6; j++){
                    System.out.printf("%9g ", cplex.getValue(Y[i][j]));
                }
                System.out.println();
            }
            update();
        }else{
            System.out.println("Status = "+cplex.getStatus());
        }
        cplex.end();
    }
        
    private Color control = new Color(205, 205, 255);
    public void paintControl(Graphics2D g2){
        g2.setColor(control);
        g2.fillRect(40, 40, 100, 160);
        g2.setColor(Color.BLACK);
        g2.drawRect(40, 40, 100, 100);
        g2.drawRect(40, 40, 100, 120);
        g2.drawRect(40, 40, 100, 140);
        g2.drawRect(40, 40, 100, 160);
        g2.drawOval(90-4, 90-4, 8, 8);
        
        if(time>0){
            g2.setColor(Color.BLUE);
            g2.drawLine(90, 90, (int)(90+Ut[time-1][0]*40/3), (int)(90+Ut[time-1][1]*40/3));
            g2.fillOval((int)(90+Ut[time-1][0]*40/3)-2, (int)(90+Ut[time-1][1]*40/3)-2, 5, 5);
            double vi = Math.sqrt(Ut[time-1][0]*Ut[time-1][0] + Ut[time-1][1]*Ut[time-1][1]);
            g2.drawString(String.format(Locale.ENGLISH, "%3.1f", vi), 70-20, 157);
            
            g2.setColor(Color.RED);
            g2.drawLine(90, 90, (int)(90+URt[time-1][0]*40/3), (int)(90+URt[time-1][1]*40/3));
            g2.fillOval((int)(90+URt[time-1][0]*40/3)-2, (int)(90+URt[time-1][1]*40/3)-2, 5, 5);
            
            double vr = Math.sqrt(URt[time-1][0]*URt[time-1][0] + URt[time-1][1]*URt[time-1][1]);
            g2.drawString(String.format(Locale.ENGLISH, "%3.1f m/s", vr), 100-20, 157);
            
        }
        g2.setColor(Color.BLACK);
        g2.drawString(String.format(Locale.ENGLISH, "%3d clock", time), 90-20, 177);
        g2.drawString(String.format(Locale.ENGLISH, "%3.1f max", max_erro), 90-20, 197);
    }
    
    double max_erro = 0;
    
    public void paint(Graphics2D g2, int t) {
        t--;
        g2.setStroke(new BasicStroke(2));
        
        g2.setColor(Color.GREEN);
        for(int i=0; i<t; i++){
            g2.fillOval((int)(It[i][0]*size)-2, (int)(It[i][2]*size)-2, 4, 4);
            g2.drawLine((int)(It[i][0]*size), (int)(It[i][2]*size), (int)(It[i+1][0]*size), (int)(It[i+1][2]*size));
        }
        g2.setColor(Color.ORANGE);
        for(int i=0; i<t; i++){
            g2.fillOval((int)(Xt[i][0]*size)-2, (int)(Xt[i][2]*size)-2, 4, 4);
            g2.drawLine((int)(Xt[i][0]*size), (int)(Xt[i][2]*size), (int)(Xt[i+1][0]*size), (int)(Xt[i+1][2]*size));
        }
        
        g2.setStroke(new BasicStroke(1));
        
        if(t>=0){
            g2.setColor(Color.GREEN);
            g2.fillOval((int)(It[t][0]*size)-5, (int)(It[t][2]*size)-5, 10, 10);
            g2.setColor(Color.BLACK);
            g2.drawOval((int)(It[t][0]*size)-5, (int)(It[t][2]*size)-5, 10, 10);
            
            g2.setColor(Color.ORANGE);
            g2.fillOval((int)(Xt[t][0]*size)-5, (int)(Xt[t][2]*size)-5, 10, 10);
            g2.setColor(Color.BLACK);
            g2.drawOval((int)(Xt[t][0]*size)-5, (int)(Xt[t][2]*size)-5, 10, 10);
            
            double x1 = It[t][0];
            double y1 = It[t][2];

            double rx = 0;
            double ry = 0;
            for(int j=0; j<4; j++){
                rx += Q[0][j];
                ry += Q[2][j];
            }
            double cx = Math.sqrt(2*rx)*erf_inv(1-2*0.001);
            double cy = Math.sqrt(2*ry)*erf_inv(1-2*0.001);

            g2.setColor(Color.RED);
            //double cx = erf_inv(1-2*DELTA);
            //double cy = erf_inv(1-2*DELTA);
            g2.drawOval((int)(x1*100-cx*100), (int)(y1*100-cy*100), (int)(2*cx*100), (int)(2*cy*100));
            g2.drawOval((int)(Xt[t][0]*100-cx*100), (int)(Xt[t][2]*100-cy*100), (int)(2*cx*100), (int)(2*cy*100));
            
        }
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        for(int s=horizon; s<t; s+=horizon){
            int x1 = (int)(It[s][0]*100);
            int y1 = (int)(It[s][2]*100);
            int x2 = (int)(Xt[s][0]*100);
            int y2 = (int)(Xt[s][2]*100);
            g2.drawLine(x1, y1, x2, y2);
        }
        if(t>2*horizon){
            double max = -1;
            int s_max = -1;
            for(int s=2*horizon; s<t; s++){
                int x1 = (int)(It[s][0]*100);
                int y1 = (int)(It[s][2]*100);
                int x2 = (int)(Xt[s][0]*100);
                int y2 = (int)(Xt[s][2]*100);
                double v = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
                if(v>max){
                    max = v;
                    s_max = s;
                }
            }
            g2.setColor(Color.RED);
            int x1 = (int)(It[s_max][0]*100);
            int y1 = (int)(It[s_max][2]*100);
            int x2 = (int)(Xt[s_max][0]*100);
            int y2 = (int)(Xt[s_max][2]*100);
            g2.drawLine(x1, y1, x2, y2);
            
            double rx = 0;
            double ry = 0;
            for(int j=0; j<4; j++){
                rx += Q[0][j];
                ry += Q[2][j];
            }
            double cx = Math.sqrt(2*rx)*erf_inv(1-2*0.001);
            double cy = Math.sqrt(2*ry)*erf_inv(1-2*0.001);
            max_erro = max/(50*(cx+cy));
            //double cx = erf_inv(1-2*DELTA);
            //double cy = erf_inv(1-2*DELTA);
            g2.drawOval((int)((x1+x2)/2-cx*100), (int)((y1+y2)/2-cy*100), (int)(2*cx*100), (int)(2*cy*100));
            g2.setColor(Color.BLACK);
            g2.drawString(String.format("[%1.2f]", max_erro), (int)((x1+x2)/2), (int)((y1+y2)/2));
        }
        g2.setColor(Color.BLACK);
    }
    
    private static double p(double x, double y){
        return Math.pow(x, y);
    }
    
    private static double erf_inv(double z){
        final double PI = Math.PI;
        return 0.5 * Math.sqrt(PI) * (z + PI*p(z,3)/12 + 7*p(PI,2)*p(z,5)/480 + 127*p(PI,3)*p(z,7)/40320 + 4369*p(PI,4)*p(z,9)/5806080 + 34807*p(PI,5)*p(z,11)/182476800);
    }
}