package irc.utils;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

public class UObject
{
    
    /**
     * Serializa para um ficheiro um objecto que serializavel.
     * @param obj
     * @param file Caminho do ficheiro de destino.
     * @return true se o serializer for executado com sucesso, caso contr�rio false.
     */
    public static boolean serialize(Object obj, String file)
    {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            return true;
        }
        catch (Exception e) {
            UApp.printErrorMessage(e);
            return false;
        }
    }
    
    /**
     * Deserializa um objecto que anteriormente foi serializado para um ficheiro.
     * @param obj
     * @param file Caminho do ficheiro de destino.
     */
    public static Object deserialize(String file)
    {
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Serializable deserializerObject = (Serializable)ois.readObject();
            ois.close();
            return deserializerObject;
        }
        catch (Exception e) {
            UApp.printErrorMessage(e);
            return null;
        }
    }

}
