//Author: Jesimar da Silva Arantes
//Date: 08/10/2018
//Last Update: 08/10/2018
//Description: Code that generates a line-shaped route in Java.
//Descricao: Código que gera uma rota em formato de linha em Java.

package replanner.making.line.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * @author Jesimar S. Arantes
 */
public class ReplannerMakingLineJava {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
	System.out.println("#G-Path-Replanner [Java]");
	System.out.println("#Planejador que faz uma rota em formato de linha");
        PrintStream out = new PrintStream(new File("output.txt"));
	out.println("0.0 0.0");
        out.println("0.0 2.0");
        out.println("0.0 4.0");
        out.println("0.0 6.0");
        out.println("0.0 8.0");
        out.println("0.0 10.0");
	out.close();
        System.out.println("#Rota terminada");
    }
    
}

