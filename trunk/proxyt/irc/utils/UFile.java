package irc.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import irc.utils.UApp;

public class UFile
{
    /**
     * Sustitui num ficheiro todo o conte�do existente pelo novo conte�do dado.
     * @param file Caminho do ficheiro.
     * @param text Novo conte�do para o ficheiro.
     * @return true caso consiga escrever no ficheiro correctament, caso contr�rio false.
     */
    public static boolean writeAllText(String file, String text)
    {
        try{
            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(text);
            out.close();
            return true;
        }
        catch (Exception e){
            UApp.printErrorMessage(e);
            return false;
        }
    }
    
    /**
     * L� o conte�do do ficheiro e retorna um String com esse conte�do.
     * @param file Caminho do ficheiro.
     */
    public static String readAllText(String file)
    {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = "";
            String text = in.readLine();
            while ((line = in.readLine()) != null) {
                text += "\n" + line;
            }
            in.close();
            if (text == null){
                text = "";
            }
            return text;
        }
        catch(Exception e) {
            UApp.printErrorMessage(e);
            return "";
        }
    }
}
