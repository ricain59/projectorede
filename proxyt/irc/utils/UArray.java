package irc.utils;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import irc.utils.UApp;

public class UArray
{
    /**
     * Cria um iterator apartir de uma array.
     * @param array array fonte.
     * @return Iterator do array.
     */
    public static Iterator iterator(Object[] array)
    {
        ArrayList<Object> toProcess = new ArrayList<Object>(Arrays.asList(array));
        return toProcess.iterator();
    }
    
    /**
     * Faz a interc��o entre dois arrays de Strings.
     * @param arrayOne Primeiro Array.
     * @param arrayTwo Segundo Array.
     * @return um novo array do tipo String com as Strings que existem em ambos os arrays.
     */
    public static String[] intersection(String[] arrayOne, String[] arrayTwo)
    {
        try {
            ArrayList<String> results = new ArrayList<String>();
            for(String one: arrayOne){
                for(String two: arrayTwo){
                    if (one == two){
                        results.add(one);
                    }   
                }
            }
            return results.toArray(new String[0]);
        }
        catch (Exception e){
            UApp.printErrorMessage(e);
            return new String[0];
        }
    }
}
