package irc.utils;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class UString
{
    /**
     * Retorna uma nova String replicando todas as ocurrencias da String oldValue pela String newValue.
     * @param value String de origem.
     * @param oldValue String antiga.
     * @param newValue Nova String.
     * @return uma nova String replicando a String antiga pela nova String.
     */
    public static String replaceAll(String value, String oldValue, String newValue)
    {
        String result = value;
        while (result.indexOf(oldValue) != -1){
            result = result.replace(oldValue, newValue);
        }
        return result;
    }
    
    /**
     * Retorna uma nova String sem espa�os, sem acentos e em mai�sculas.
     * @param value String de origem.
     * @return String sem acentos, sem espa�os e em mai�sculas.
     */
    public static String upperCaseWithoutAccents(String value)
    {
        String result = value;
        result = processaAlias(result);
        result = result.replace("_", "").toUpperCase();
        return result;
    }
    
    /**
     * Retorna uma nova String com os espa�os trocados por '_', sem acentos e em min�sculas.
     * @param value String de origem.
     * @return String sem acentos, sem espa�os e em min�sculas.
     */
    public static String processaAlias(String value)
    {
        String result = value;
        result = replaceAll(result, "  ", " ");
        result = result.trim();
        result = result.toLowerCase();
        result = removeAccents(result);
        result = result.replace(" ", "_");
        
        return result;
    }
    
    /**
     * Substitui um caract�res com acento pelo mesmo mas sem acento gr�fico.
     * @param value String de origem.
     * @return String sem acentos.
     */
    public static String removeAccents(String value)
    {
        String result = "";
        
        for (char ch: value.toCharArray()){            
            String c = Character.toString(ch);
            c = c.toLowerCase();
            c = c.replace("�", "a");
            c = c.replace("�", "a");
            c = c.replace("�", "a");
            c = c.replace("�", "a");
            c = c.replace("�", "a");
            c = c.replace("�", "e");
            c = c.replace("�", "e");
            c = c.replace("�", "e");
            c = c.replace("�", "e");
            c = c.replace("�", "i");
            c = c.replace("�", "i");
            c = c.replace("�", "i");
            c = c.replace("�", "i");
            c = c.replace("�", "o");
            c = c.replace("�", "o");
            c = c.replace("�", "o");
            c = c.replace("�", "o");
            c = c.replace("�", "o");
            c = c.replace("�", "u");
            c = c.replace("�", "u");
            c = c.replace("�", "u");
            c = c.replace("�", "u");
            c = c.replace("�", "c");
            c = c.replace("�", "n");
            c = c.replace("�", "y");
            c = c.replace("�", "y");
            c = Character.isLowerCase(ch) ? c.toLowerCase() : c.toUpperCase();
              
            result += c;
        }
        
        return result;
    }

    /**
     * Junta as String do Array com de separador dados como par�metro.
     * @param source Array do tipo String.
     * @param delimiter separador para os elementos do Array.
     * @return uma nova String.
     */
    public static String join(String[] source, String delimiter)
    {
        String result = "";
        for(String v: source){
            if (!result.equals(""))
                result += delimiter;
            result += v;
        }
        return result;
    }
    
    /**
     * Cria uma String, repetindo n vezes o valor inserido.
     * @param value String a ser repetida.
     * @param number N�mero de vezes que a String ser� repetida.
     */
    public static String repeat(String value, int number)
    {
        String result = "";
        for (int i=0; i< number; i++) {
            result += value;
        }
        return result;
    }
    
    /**
     * Tranforma um String num formato de data e converte para o tipo Date.
     * @param value String que representa uma Data.
     * @param format Formato da data que foi introduzida.
     * @return um objecto do tipo Date da convers�o da String para Date. Caso a String value n�o esteja no formato correcto, devolve null.
     */
    public static Date toDate(String value, String format){
        DateFormat df = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = df.parse(value);
        } catch (Exception e) {
            UApp.printErrorMessage(e);
            return null;
        }
        finally{
            return date;
        }
    }
    
}
