package irc.utils;

public class UNumber
{

    /**
     * Arredonda um n�mero de preci��o simples para um dado n�mero de casas decimais.
     * @param value N�mero do tipo float.
     * @param places N�mero de casas decimais.
     * @return um n�mero float aredontado a n casas d�cimais.
     */
    public static float round(float value, int places)
    {
        float p = (float)Math.pow(10, places);
        value = value * p;
        float tmp = Math.round(value);
        return (float)tmp / p;
    }

    /**
     * Filtra um n�mero inteiro a um intervalo de n�merico.
     * @param value inteiro a filtrar.
     * @param min M�nimo.
     * @param max M�ximo.
     * @return um inteiro int filtrado pelo intervalo. Caso o n�mero ultrapasse o m�nimo ou m�ximo assume por defeito um dos limites.
     */
    public static int filterBetween(int value, int min, int max)
    {
        
        if (value < min) {
            return min;
        }
        
        if (value > max ){
            return max;
        }
        
        return value;
        
    }
    
    /**
     * Filtra um n�mero inteiro a um m�nimo.
     * @param value inteiro a filtrar.
     * @param min M�nimo.
     * @return um inteiro int filtrado pelo n�mero m�nimo. Caso o n�mero seja menor que o m�nimo assume por defeito o limite m�nimo.
     */
    public static int filterMin(int value, int min)
    {
        return filterBetween(value, min, Integer.MAX_VALUE);
    }
    
    /**
     * Filtra um n�mero inteiro a um m�ximo.
     * @param value inteiro a filtrar.
     * @param max M�ximo.
     * @return um inteiro int filtrado pelo n�mero m�ximo. Caso o n�mero seja maoir que o m�ximo assume por defeito o limite m�ximo.
     */
    public static int filterMax(int value, int max)
    {
        return filterBetween(value, Integer.MIN_VALUE, max);
    }
    
    /**
     * Filtra um n�mero de precis�o simples a um intervalo de n�merico.
     * @param value n�mero a filtrar.
     * @param min M�nimo.
     * @param max M�ximo.
     * @return um n�mero float filtrado pelo intervalo. Caso o n�mero ultrapasse o m�nimo ou m�ximo assume por defeito um dos limites.
     */
    public static float filterBetween(float value, float min, float max)
    {
        if (value < min) {
            return min;
        }
        
        if (value > max ){
            return max;
        }
        return value;
    }
    
    /**
     * Filtra um n�mero a um m�nimo.
     * @param value n�mero a filtrar.
     * @param min M�nimo.
     * @return um m�mero float filtrado pelo n�mero m�nimo. Caso o n�mero seja menor que o m�nimo assume por defeito o limite m�nimo.
     */
    public static float filterMin(float value, float min)
    {
        return filterBetween(value, min, Float.MAX_VALUE);
    }
    
    /**
     * Filtra um n�mero a um m�ximo.
     * @param value n�mero a filtrar.
     * @param max M�ximo.
     * @return um inteiro float filtrado pelo n�mero m�ximo. Caso o n�mero seja maoir que o m�ximo assume por defeito o limite m�ximo.
     */
    public static float filterMax(float value, float max)
    {
        return filterBetween(value, Float.MIN_VALUE, max);
    }
    
}
