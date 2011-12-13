package irc.utils;

import java.util.Date;
import java.text.SimpleDateFormat;
import irc.utils.UEnum.partOfDay;

/**
 * Class com todos os mêtodos necessários para a manipulação de Datas.
 */
public class UDate
{
    /**
     * Calcula a idade conforme a data de nascimento fornecida.
     * @param dateOfBirth Data de nascimento.
     * @return um inteiro com a dirença de anos entre a data dateOfBirth e a data actual do computador.
     */
    public static int age(Date dateOfBirth)
    {
        Date now = new Date();
        int myage = now.getYear() - dateOfBirth.getYear();
        
        if (dateOfBirth.getMonth() > now.getMonth()
           || (dateOfBirth.getMonth() == now.getMonth()
           && dateOfBirth.getDate() > now.getDate())){
               myage--;
        }
        
        return myage;
    }
    
    /**
     * Calcula a idade conforme a data de nascimento fornecida.
     * @param year Ano da data.
     * @param month Mês da Data.
     * @param day Dia da Data.
     * @return um inteiro com a dirença de anos entre a data dateOfBirth e a data actual do computador.
     */
    public static int age(int year, int month, int day)
    { 
        return age(new Date(year, month, day));
    }
    
    /**
     * Formata o objecto Date para o formato da String format.
     * @param date objecto Date que vai ser formato.
     * @param format formato da String.
     * @return A data no formato desejado.
     */
    public static String format(Date date, String format)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
    
    /**
     * Calcula a diferenca entre datas por partes do dia.
     * @param start Data de início.
     * @param end Data de fim.
     * @param part Parte do dia.
     */
    public static long diff(Date start, Date end, partOfDay part)
    {
        return (long) ((end.getTime() - start.getTime()) / part.dividerPer());
    }
    
    /**
     * Calcula a diferenca entre datas em milisegundos.
     * @param start Data de início.
     * @param end Data de fim.
     */
    public static long diffMilliSeconds(Date start, Date end)
    {
        return diff(start, end, partOfDay.MILLISECOND);
    }
    
    /**
     * Calcula a diferenca entre datas em segundos.
     * @param start Data de início.
     * @param end Data de fim.
     */
    public static long diffSeconds(Date start, Date end)
    {
        return diff(start, end, partOfDay.SECOND);
    }
    
    /**
     * Calcula a diferenca entre datas em minutos.
     * @param start Data de início.
     * @param end Data de fim.
     */
    public static long diffMinutes(Date start, Date end)
    {
        return diff(start, end, partOfDay.MINUTE);
    }
    
    /**
     * Calcula a diferenca entre datas em horas.
     * @param start Data de início.
     * @param end Data de fim.
     */
    public static long diffHours(Date start, Date end)
    {
        return diff(start, end, partOfDay.HOUR);
    }
    
    /**
     * Calcula a diferenca entre datas em dias.
     * @param start Data de início.
     * @param end Data de fim.
     */
    public static long diffDays(Date start, Date end)
    {
        return diff(start, end, partOfDay.DAY);
    }
    
    /**
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @return
     */
    public static Date convertDate(int year, int month, int day, int hour, int minute)
    {
        return new Date(convertYear(year), convertMonth(month), convertDay(day), convertHour(hour), convertMinute(minute));
    }
    
    /**
     * Converte um inteiro de um ano para o formato da Classe Date.
     * @param year Ano.
     * @return um inteiro que representa uma ano.
     */
    public static int convertYear(int year)
    {
       return UNumber.filterBetween(year - 1900, 0, Integer.MAX_VALUE);
    }
    
    /**
     * Converte um inteiro de um mês para o formato da Classe Date.
     * @param month Mês.
     * @return um inteiro que representa uma mês compreendido entre 0 e 11.
     */
     public static int convertMonth(int month)
    {
       return UNumber.filterBetween(month - 1, 0, 11);
    }
    
    /**
     * Converte um inteiro de um dia para o formato da Classe Date.
     * @param day Dia.
     * @return Retorna um inteiro compreendido entre 0 e 31.
     */
    public static int convertDay(int day)
    {
       return UNumber.filterBetween(day, 1, 31);
    }
    
    /**
     * Converte um inteiro de uma hora para o formato da Classe Date.
     * @param hour Hora.
     * @return Retorna um inteiro compreendido entre 0 e 23.
     */
    public static int convertHour(int hour)
    {
       return UNumber.filterBetween(hour, 0, 23);
    }
    
    /**
     * Converte um inteiro de um minuto para o formato da Classe Date.
     * @param minute Minuto.
     * @return Retorna um inteiro compreendido entre 0 e 59.
     */
    public static int convertMinute(int minute)
    {
       return UNumber.filterBetween(minute, 0, 59);
    }
    
    /**
     * Retorna a data maior de duas datas.
     * @param value1 Primeira Data.
     * @param value2 Segunda Data.
     * @return a data maior.
     */
    public static Date max(Date value1, Date value2)
    {
        if (value1.before(value2))
            return value2;
        else 
            return value1;
    }
    
    /**
     * Retorna a data menor de duas datas.
     * @param value1 Primeira Data.
     * @param value2 Segunda Data.
     * @return a data menor.
     */
    public static Date min(Date value1, Date value2)
    {
        if (value1.after(value2))
            return value2;
        else 
            return value1;
    }
        
    /**
     * Retorna a data menor de um array de datas.
     * @param value1 Primeira Data.
     * @param value2 Segunda Data.
     * @return a data menor.
     */
    public static Date min(Date[] dates)
    {
        if (dates == null || dates.length == 0){
            return null;
        }
        else {
            Date result = dates[0];
            for(Date d: dates){
                result = min(result, d);
            }
            return result;
        }
    }
    
    /**
     * Retorna a data maoir de um array de datas.
     * @param value1 Primeira Data.
     * @param value2 Segunda Data.
     * @return a data maoir.
     */
    public static Date max(Date[] dates)
    {
        if (dates == null || dates.length == 0){
            return null;
        }
        else {
            Date result = dates[0];
            for(Date d: dates){
                result = max(result, d);
            }
            return result;
        }
    }
    
    /**
     * Confirmar se uma data se encontra dentro de um intervalo de datas.
     * @param date Data a comparar.
     * @param start Data de Início.
     * @param end Data de Fim.
     * @return true caso a data se encontre entre as datas de Início e de Fim, Caso contrário false.
     */
    public static boolean between(Date date, Date start, Date end)
    {
        return (start.before(date) && date.before(end));
    }
    
}
