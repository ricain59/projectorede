package irc.utils;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Esta classe disponibiliza metodos para ler e escrever
 * mensagens na consola para interação com o utilizador.
 */
public class Console
{
    private static Scanner scanner;
    
    static
    {
        scanner = new Scanner(System.in);
    }
    
    // ESCREVER
    public static void println(Object obj) 
    {
        System.out.println(obj);
    }
    
    public static void print(Object obj)
    {
        System.out.print(obj);
    }
    
    public static PrintStream printf(String s, Object[] args)
    {
        return System.out.printf(s, args);
    }
    
    public static void printError(Exception e)
    {
        println("???? ERROR ????\n" + 
                "Ocurreu um erro em tempo de execução\n" + 
                e.getMessage() + "\n" +
                "Stack Trace:");
                e.printStackTrace();
        println("???????????????\n");
    }
   
    //LER
    public static String nextLine() {
        return scanner.nextLine();
    }
    
    public static float nextFloat()
    {
        return scanner.nextFloat();
    }
    
    public static int nextInt()
    {
        return scanner.nextInt();
    }
    
}
