package parseravvisi_dmiunict;

import java.io.File;
import java.io.IOException;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.text.DateFormat;
import javax.json.JsonObject;

public class ParserAvvisi_DMIUNICT {
    static String pathData = "data\\";
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
        
        Document doc[] = new Document[4]; // 0, html tr. inf || 1, html  tr. mat || 2, html mag. inf || 3, html mag. matematica
        Parser parser;
        String inLink = inLink();
        FileManager fm = new FileManager();
        File fileX=null, fileY=null;
        try {
            fileX = fm.file(pathData+fileName[0]);
        } catch(FileException e) {
            System.err.println("Il file X non è stato creato a causa di qualche errore");
            //Sarebbe giusto che in futuro faccio qualcosa che mi avvisa
        } 
        try {
            fileY = fm.file(pathData+fileName[1]);
        } catch(FileException e) {
            System.err.println("Il file Y non è stato creato a causa di qualche errore");
            //Sarebbe giusto che in futuro faccio qualcosa che mi avvisa
        }   
        
        // Gestione estrapolazione documenti HTML    
        for (int i=0; i<doc.length; i++) {
        try {
            doc[i] = Jsoup.connect(linkArchivio[i]).get();
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
            JsonObject json = javax.json.Json.createObjectBuilder()
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
 
 
