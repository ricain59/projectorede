package irc.utils;

public class UEnum
{
    /**
     * Enumera��o das v�rias partes do dia.
     */    
    public enum partOfDay
    {
        DAY,
        HOUR,
        MINUTE,
        SECOND,
        MILLISECOND;
        
        /**
         * Retorna um inteiro com o divisor por milisegundos para um dado parte do dia.
         */
        public long dividerPer()
        {
            switch(this){
                case MILLISECOND:   return 1;
                case SECOND:  return 1000;
                case MINUTE:  return 1000*60;
                case HOUR: return 1000*60*60;
                case DAY: return 1000*60*60*24;
            }
            throw new AssertionError("Unknown op: " + this);
        }
    }
    
    /**
     * Enumera��o para tipo de ordena��o, Ascendente ou Descendente.
     */
    public enum SortOrder
    {
        NONE,
        ASCENDING,
        DESCENDING;
        
        /**
         * Retorna o inteiro que pode ser usado nas interfaces do tipo Comparator, para ordenar objectos por ordem crescente ou decrescente.<br/>
         * Caso o item da enumera��o for NONE n�o deve ordenar.
         */
        public int value()
        {
            switch(this) {
                case NONE:   return 0;
                case ASCENDING:  return 1;
                case DESCENDING:  return -1;
            }
            throw new AssertionError("Unknown op: " + this);
        }
    }
    
}
