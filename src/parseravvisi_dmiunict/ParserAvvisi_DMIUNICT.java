package parseravvisi_dmiunict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.text.DateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;

public class ParserAvvisi_DMIUNICT {
    
    static String[] linkArchivio = { // Link archivi avvisi dei CdL
        "http://web.dmi.unict.it/corsi/l-31/avvisi/",   // Triennale Informatica
        "http://web.dmi.unict.it/corsi/l-35/avvisi/", // Triennale Matematica
        "http://web.dmi.unict.it/corsi/lm-18/avvisi/", // Magistrale Informatica
        "http://web.dmi.unict.it/corsi/lm-40/avvisi/" // Magistrale Matematica
    };
    static String[] fileName = {
        "archivioAvvisi.dat",
        "avviso.dat"       
    };


    public static void main(String[] args) throws IOException {
        String pathData = leggiPath();
        Document doc[] = new Document[4]; // 0, html tr. inf || 1, html  tr. mat || 2, html mag. inf || 3, html mag. matematica
        Parser parser;
        FileManager fm = new FileManager();
        File fileX=null, fileY=null;

        try {
            fileX = fm.file(pathData+fileName[0]);
        } catch(FileException e) {
            System.err.println("Il file "+pathData+fileName[0]+" non è stato creato a causa di qualche errore");
            e.printStackTrace();
        } 
        try {
            fileY = fm.file(pathData+fileName[1]);
        } catch(FileException e) {
            System.err.println("Il file "+pathData+fileName[0]+" non è stato creato a causa di qualche errore");
            e.printStackTrace();
        }   
        
        // Gestione estrapolazione documenti HTML    
        for (int i=0; i<doc.length; i++) {
        try {
            doc[i] = Jsoup.connect(linkArchivio[i]).get();
        } catch(Exception e) {
            e.printStackTrace(); 
            }
        }
        // Parsing + Estrapolazione link
        parser = new Parser();
        for (int i=0; i<doc.length; i++) 
            parser.generaLink(i,doc[i].getElementsByClass("views-field views-field-php").toString());
            
        for (int i=0; i<parser.getAvvisi().size(); i++) {
            Avviso x = (Avviso) parser.getAvvisi().get(i);
            String nomeCdl = x.getNomeCdL();
            String link = x.getLink().toString();
            JsonObject json = javax.json.Json.createObjectBuilder()
            .add(x.getNomeCdL(),x.getLink().toString())
            .build();
            String contenuto = FileManager.leggi(fileX);
    
            if(!contenuto.contains(json.toString())) {
                FileManager.scrivi(fileX,json.toString(),contenuto);
                //Update 26/11/2017
                String newsCompleta = new String();
      
                Document news = Jsoup.connect(x.getStrLink()).get();
                newsCompleta = getNews(news);
                FileManager.scrivi(fileY,"<b>["+x.getNomeCdL()+"]</b>"+"\n"+newsCompleta);
            }
        }
        

    }
            public static String getNews(Document news) {
            String newsCompleta = new String();
            
            String titolo = "<b>"+news.getElementsByClass("page-title").text()+"</b>\n";
            String link = news.baseUri()+"\n";
            String contenuto = news.getElementsByClass("content").get(1).text();
            newsCompleta = link+titolo+contenuto;
            return newsCompleta;
        }
            
        public static String leggiPath()  {
                    String path=null;
                    FileManager fm = new FileManager();
                    File f = null;
                    try {
                        f = fm.file("config");
                        FileReader fr = new FileReader(f);
                        BufferedReader br = new BufferedReader(fr);
                        path = br.readLine();
                    } catch (FileException ex) {
                        ex.printStackTrace();
                    } catch (FileNotFoundException ex) {
                    Logger.getLogger(ParserAvvisi_DMIUNICT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ParserAvvisi_DMIUNICT.class.getName()).log(Level.SEVERE, null, ex);
        }
                    return path;
                    
        }
}
 