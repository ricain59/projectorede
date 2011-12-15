import java.util.Hashtable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Date;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Cache
{
    public String basePath = null;
    public Hashtable htable; 

    public Cache()
    {
        htable = new Hashtable();
        // Criação do directorio cache
        File cacheDir = new File("Cache");
        cacheDir.mkdirs();
        basePath = cacheDir.getAbsolutePath();
        File file = new File(basePath);;
        // obtem toda a lista de ficheiro exustente na cache
        String files[] = file.list();
    }

    /*
     * Verifica se o url pode ser inserido na cache (url com ? ou !)
     */
    public boolean IsCachable(String urlRecebido)
    {
        return (getFileName(urlRecebido) != null);
    }

    /*
     * Verifica se o url esta presente na cache
     */
    public boolean IsCached(String urlRecebido)
    {
        String filename = getFileName(urlRecebido);
        if (filename == null)
            return false;
        // Procura na hastable
        if (htable.get(filename) != null)
        {
            return true;
        }else{
            return false;
        }
    }
    
    /*
     * Esse método é chamado quando se acede a cache para ir buscar os ficheiros
     * necessario
     * E atualiza-se a data dos ficheiros.
     */
    public FileInputStream getFileInputStream(String urlRecebido)
    {
        FileInputStream fileStream = null;
        try
        {
            String filename = getFileName(urlRecebido);
            htable.put(filename,new Date());
            fileStream = new FileInputStream(filename);
        }
        catch (FileNotFoundException filenotfound)
        {
            try
            {
                System.out.println("File Not Found:"+getFileName(urlRecebido)+" "+filenotfound);
            }
            catch (Exception e) 
            {
            }
        }
        finally
        {
            return fileStream;
        }
    }

    /*
     * Método chamada quando queremos criar um novo objeto na cache 
     */
    public FileOutputStream getFileOutputStream(String urlRecebido)
    {
        FileOutputStream fileStream = null;
        String filename;
        try
        {
            filename = getFileName(urlRecebido);

            fileStream = new FileOutputStream(filename);
        }
        catch (IOException e)
        {
        }
        finally
        {
            return fileStream;
        }
    }   

    /*
     * Permite adicionar mas uma entrada na hastable
     */
    public synchronized void AddToTable(String rawUrl)
    {
        String filename = getFileName(rawUrl);
        htable.put(filename,new Date());
    }

    /*
     * Convert o url para uma nova string mas só se o url da para inserir na cache
     * Todos os url com ? , cgi-bin não são inseridos na cache
     */
    private String getFileName(String rawUrl)
    {
        String filename = basePath + File.separatorChar + rawUrl.substring(7).replace('/','@');
        if (filename.indexOf('?') != -1 || filename.indexOf("cgi-bin") != -1)
        {
            return null;
        }
        return filename;
    }   
}

