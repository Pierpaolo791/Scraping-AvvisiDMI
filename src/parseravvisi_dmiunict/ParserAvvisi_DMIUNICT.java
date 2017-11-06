package parseravvisi_dmiunict;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.StringTokenizer;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.json.*;
public class ParserAvvisi_DMIUNICT {
    static String pathData = "..\\..\\data"; // config 
    static String[] linkArchivio = { // Link archivi avvisi dei CdL
        "http://web.dmi.unict.it/Didattica/Laurea%20Triennale%20in%20Informatica%20L-31/Avvisi/Archivio%20Avvisi/",   // Triennale Informatica
        "http://web.dmi.unict.it/Didattica/Laurea%20Triennale%20in%20Matematica%20L-35/Avvisi/Archivio%20avvisi/", // Triennale Matematica
        "http://web.dmi.unict.it/Didattica/Laurea%20Magistrale%20in%20Informatica%20LM-18/Avvisi/Archivio%20Avvisi/", // Magistrale Informatica
        "http://web.dmi.unict.it/Didattica/Laurea%20Magistrale%20in%20Matematica%20LM-40/Avvisi/Archivio%20avvisi/" // Magistrale Matematica
    };

   
    public static void main(String[] args) throws InterruptedException, IOException {
       
        Document doc[] = new Document[4]; // 0, html tr. inf || 1, html  tr. mat || 2, html mag. inf || 3, html mag. matematica
        Parser parser;
        String inLink = "201710";
        FileManager fm = new FileManager();
        File fileX=null, fileY=null;
        try {
            fileX = fm.file("dataX");
        } catch(FileException e) {
            System.err.println("Il file X non è stato creato a causa di qualche errore");
            //Sarebbe giusto che in futuro faccio qualcosa che mi avvisa
        } 
        try {
            fileY = fm.file("dataY");
        } catch(FileException e) {
            System.err.println("Il file Y non è stato creato a causa di qualche errore");
            //Sarebbe giusto che in futuro faccio qualcosa che mi avvisa
        }   
        
        // Gestione estrapolazione documenti HTML    
        for (int i=0; i<doc.length; i++) {
        try {
            doc[i] = Jsoup.connect(linkArchivio[i]+inLink).get();
        } catch(Exception e) {
            System.err.println("Errore (Exception)"); 
            // Le possibili cause di errore è la connessione lenta, un giorno la gestirò
            }
        }
            
            
            
        // Parsing + Estrapolazione link
        parser = new Parser();
        for (int i=0; i<doc.length; i++) 
            parser.generaLink(i,doc[i].toString());
            
        for (int i=0; i<parser.getAvvisi().size(); i++) {
            Avviso x = (Avviso) parser.getAvvisi().get(i); // Casting da Object ad Avvisi -- Vediamo che succede
            String nomeCdl = x.getNomeCdL();
            String link = x.getLink().toString();
            JsonObject json = Json.createObjectBuilder()
            .add(nomeCdl,link)
            .build();
            String contenuto = FileManager.leggi(fileX);
    
            if(!contenuto.contains(json.toString())) {
                FileManager.scrivi(fileX,json.toString(),contenuto);
                FileManager.scrivi(fileY,json.toString());
            }
                    
                    
        }

    }
    public static String inLink() {
         
        String inLink;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
        Date today = calendar.getTime();
        DateFormat dateFormat =DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
        String data = dateFormat.format(today); // gg/mm/aa
        String mese = data.substring(3,5); // mm
        String anno = data.substring(6,8); // gg
        inLink = "20"+anno+mese; // 20 + mm + gg 
        
        return inLink;
           
    }
}
 