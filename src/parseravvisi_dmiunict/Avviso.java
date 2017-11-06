package parseravvisi_dmiunict;

import java.net.URL;

public class Avviso {
    
    private String nomeCdL; // Nome facoltà;
    private URL link; 
    
    public Avviso(String nomeCdL, URL link) {
        this.nomeCdL = nomeCdL;
        this.link = link;
    }
    public Avviso(int idCdL, URL link) {
        this.nomeCdL = getStringToId(idCdL);
        this.link = link;
    }
       
    public String getNomeCdL() {
        return this.nomeCdL;
    }
    public URL getLink() {
        return this.link;
    }

    public void setNomeCdL(String nomeCdL) {
        this.nomeCdL = nomeCdL;
    }

    public void setLink(URL link) {
        this.link = link;
    }
    
    @Override
    public String toString() {
        String x = 
                "CdL: "+this.getNomeCdL()+"  ||  "+
                link; // link == link.toString(); (Semmai un giorno lo scordassi)
        return x;
    }
    
    public String getStringToId(int idCdL) {
        String nomeCdL = new String();
        switch (idCdL) {
            case 0: 
                nomeCdL="Informatica L-31";
                break;
            case 1:
                nomeCdL="Matematica L-35";
                break;
            case 2:
                nomeCdL="Informatica LM-18";
                break;
            case 3: 
                nomeCdL="Matematica LM-40";
                break;
        }
        return nomeCdL;
    }
}
