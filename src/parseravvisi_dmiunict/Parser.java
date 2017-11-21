package parseravvisi_dmiunict;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Parser {
 
    protected static String LINK = "http://web.dmi.unict.it"; // Base del sito, quindi verrà LINK + risultato parsing depurato 
    private String[] conditionManagment = {
        
        "<div",
        "class=\"views-field",
        "views-field-php\">",
        "<span",
        "class=\"field-content\"><a"    
    }; // Array di stringhe per individuare il link || Core del parser || 
    private ArrayList<Avviso> avvisi;
   
    public Parser() {
        avvisi = new ArrayList();
    }
   
    public void generaLink(int idCdL, String str) {
       
        ArrayList<String> arrayString = new ArrayList();
        StringTokenizer sToken = new StringTokenizer(str);
       
        //return 
                sistemaLink(idCdL,parsingToken(sToken));
    }
 
     public ArrayList parsingToken (StringTokenizer sToken) {
       
        ArrayList<String> arrayString = new ArrayList();
        int i=0;
        while (i<conditionManagment.length && sToken.hasMoreTokens()) {
                String str = sToken.nextToken();
                if (i>=conditionManagment.length-1) {
                    arrayString.add(sToken.nextToken());
                    i=0;
                }
               
                if (str.equals(conditionManagment[i])) 
                    i++;
                
               
                else 
                    i=0;
        }
        return arrayString;
       
    }
   
    public void sistemaLink(int idCdL,ArrayList<String> arrayString)  {
        
        String x = new String();
        
        for (int i=0; i<arrayString.size(); i++) {
            
            x = arrayString.get(i); // Array contente link sporco dall'href
            StringTokenizer stx = new StringTokenizer(x,"\" ");
           
            while (stx.hasMoreTokens())
                if(stx.nextToken().equals("href=")) {
                    //arrayLink.add(this.LINK+stx.nextToken());
                    try {
                        creaAvviso(idCdL,new URL(LINK+stx.nextToken()));
                    } catch (Exception e) {
                        System.err.println("Errore (Exception)");
                    }
                }
        }
       
    }
    
    public void creaAvviso(int idCdL, URL link) {
        avvisi.add(new Avviso(idCdL,link));
    }
    public ArrayList getAvvisi() {
        return avvisi;
    }
    
}