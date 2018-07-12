/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 *
 * @author jesimar
 */
public class Statistics {

    private static Statistics instance = null;

    private static final int SIZE = 100;

    public static int index = 0;

    public double rateOcupationFixed;
    public double rateZoneNFixed;
    public double rateZonePFixed;
    public double rateZoneBFixed;

    public double[] rateRealOcupation = new double[SIZE];
    public double[] rateRealZoneN = new double[SIZE];
    public double[] rateRealZoneP = new double[SIZE];
    public double[] rateRealZoneB = new double[SIZE];

    public double[] amountZoneTotal = new double[SIZE];
    public double[] amountZoneN = new double[SIZE];
    public double[] amountZoneP = new double[SIZE];
    public double[] amountZoneB = new double[SIZE];

    private Statistics() {
                
    }

    public static Statistics getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new Statistics();
            return instance;
        }
    }

    public void printStatisticsAll() {
        System.out.println("rateOcupationFixed = " + rateOcupationFixed);
        System.out.println("rateZoneNFixed = " + rateZoneNFixed);
        System.out.println("rateZonePFixed = " + rateZonePFixed);
        System.out.println("rateZoneBFixed = " + rateZoneBFixed);
        for (int i = 0; i < index; i++) {
            System.out.println("----------" + i + "----------");

            System.out.println("    rateRealOcupation = " + rateRealOcupation[i]);
            System.out.println("    rateRealZoneN = " + rateRealZoneN[i]);
            System.out.println("    rateRealZoneP = " + rateRealZoneP[i]);
            System.out.println("    rateRealZoneB = " + rateRealZoneB[i]);

            System.out.println("    amountZoneTotal = " + amountZoneTotal[i]);
            System.out.println("    amountZoneN = " + amountZoneN[i]);
            System.out.println("    amountZoneP = " + amountZoneP[i]);
            System.out.println("    amountZoneB = " + amountZoneB[i]);
        }
    }
    
    public void printStatistics(){
        DescriptiveStatistics dsRateRealOcupation = new DescriptiveStatistics(rateRealOcupation);
        DescriptiveStatistics dsRateRealZoneN = new DescriptiveStatistics(rateRealZoneN);
        DescriptiveStatistics dsRateRealZoneP = new DescriptiveStatistics(rateRealZoneP);
        DescriptiveStatistics dsRateRealZoneB = new DescriptiveStatistics(rateRealZoneB);
        DescriptiveStatistics dsAmountZoneTotal = new DescriptiveStatistics(amountZoneTotal);
        DescriptiveStatistics dsAmountZoneN = new DescriptiveStatistics(amountZoneN);
        DescriptiveStatistics dsAmountZoneP = new DescriptiveStatistics(amountZoneP);
        DescriptiveStatistics dsAmountZoneB = new DescriptiveStatistics(amountZoneB);
                
        System.out.println("----------Statistics----------\n");
        
        System.out.println("rateOcupationFixed = " + rateOcupationFixed);
        System.out.println("rateZoneNFixed = " + rateZoneNFixed);
        System.out.println("rateZonePFixed = " + rateZonePFixed);
        System.out.println("rateZoneBFixed = " + rateZoneBFixed);
        
        System.out.println("\nRate Real Ocupation");
        printData(dsRateRealOcupation);
        
        System.out.println("\nRate Real Zone N");
        printData(dsRateRealZoneN);
        
        System.out.println("\nRate Real Zone P");
        printData(dsRateRealZoneP);
        
        System.out.println("\nRate Real Zone B");
        printData(dsRateRealZoneB);       
        
        System.out.println("\nAmount Zone Total");
        printData(dsAmountZoneTotal);
        
        System.out.println("\nAmount Zone N");
        printData(dsAmountZoneN);
        
        System.out.println("\nAmount Zone P");
        printData(dsAmountZoneP);
        
        System.out.println("\nAmount Zone B");
        printData(dsAmountZoneB);
    }
    
    private void printData(DescriptiveStatistics ds){
        System.out.println("    Media: " + ds.getMean());
        System.out.println("    Variance: " + ds.getPopulationVariance());
        System.out.println("    Desvio Padrao: " + ds.getStandardDeviation());
        System.out.println("    Min: " + ds.getMin());
        System.out.println("    Max: " + ds.getMax());
    }
    
    public static void increment(){
        if (index < SIZE-1){
            index++;
        }else {
            index = 0;
        }
    }
   
}
