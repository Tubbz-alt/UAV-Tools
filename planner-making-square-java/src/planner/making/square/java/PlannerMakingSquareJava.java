//Author: Jesimar da Silva Arantes
//Date: 08/10/2018
//Last Update: 08/10/2018
//Description: Code that generates a square-shaped route in Java.
//Descricao: Código que gera uma rota em formato de quadrado em Java.

package planner.making.square.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * @author Jesimar S. Arantes
 */
public class PlannerMakingSquareJava {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
	System.out.println("#G-Path-Planner [Java]");
	System.out.println("#Planejador que faz uma rota em formato de quadrado");
        PrintStream out = new PrintStream(new File("output.txt"));
	out.println("0.0 0.0");
        out.println("0.0 2.0");
        out.println("0.0 4.0");
        out.println("0.0 6.0");
        out.println("0.0 8.0");
        out.println("0.0 10.0");
        out.println("2.0 10.0");
        out.println("4.0 10.0");
        out.println("6.0 10.0");
        out.println("8.0 10.0");
        out.println("10.0 10.0");
        out.println("10.0 8.0");
        out.println("10.0 6.0");
        out.println("10.0 4.0");
        out.println("10.0 2.0");
        out.println("10.0 0.0");
        out.println("8.0 0.0");
        out.println("6.0 0.0");
        out.println("4.0 0.0");
        out.println("2.0 0.0");
        out.println("0.0 0.0");
	out.close();
        System.out.println("#Rota terminada");
    }
    
}
