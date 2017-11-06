package parseravvisi_dmiunict;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    
    public FileManager() {
        // File manager che inserisce dati tramite una pila LIFO 
    }
    
    public File file(String path) throws FileException {
       
        File file = null;
        try {
            file = new File(path);

            if (file.exists());
                
            else if (file.createNewFile());
            
            else
                throw new FileException(); // Lancia eccezione da gestire 

        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception e) {
            
        }
        
        return file;
    }
    public static String leggi(File file) throws FileNotFoundException, IOException {
        String contenuto = new String();
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line=br.readLine();

        for (; !(line==null); line=br.readLine()) {
            contenuto += line+"\n";
        }
        return contenuto;
    }
    
    public static void scrivi(File file, String string, String contenuto) throws IOException {
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(string+"\n"+contenuto);
        bw.flush();
        bw.close();
    }
    public static void scrivi(File file, String string) throws IOException {
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(string+"\n");
        bw.flush();
        bw.close();
    }
    
    public static void delete(File file) {
        file.delete();
    }
    public static void clean(File file) throws IOException {
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("");
        bw.flush();
        bw.close();
    }
}
