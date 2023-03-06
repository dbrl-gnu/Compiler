package Codi3A;

import java.util.ArrayList;
import compilador.ts.TSB;

public class Procediment {

    public int nProcediment; // valor enter que l’identifica
    public int caller;
    public String nom;
    public  String nomOG;
    public TSB retorn;
    ArrayList <Parametre> parametres;
            
    public int ocupVL;
    public int despVar;


    public Procediment(String nom,int nProcediment,int caller, TSB retorn, ArrayList<Parametre> parametres) {
        this.nomOG = nom;
        if (!nom.equals("main")) {
            nom = nom+"_"+nProcediment;

        }else {
            this.nom = nom;
        }

        this.nProcediment = nProcediment;
        this.caller = caller;
        this.retorn = retorn;
        this.parametres = parametres;
        
        this.ocupVL = 0;
    }

    @Override
    public String toString() {
        String s = "\n";
        if (parametres != null) {
            for (int i = 0; i < parametres.size(); i++) {
                s += "\t" + parametres.get(i).toString() + "\n";
            }
        }
        return "Procediment : " + nomOG + " amb id :" + nProcediment + ", es de tipus :" + retorn +" Parmetres:"+ s;
    }



}
