package compilador.Symbols;

import Codi3A.Parametre;
import compilador.sintactic.Parser;
import compilador.ts.DVariable;
import compilador.ts.TSB;
import java.util.ArrayList;

public class SymbolArgs extends ArbreSymbols{
    Parser p;
    SymbolTVar tVar;
    SymbolArray a;
    SymbolArgs args;
    ArrayList <Parametre> parametres = new ArrayList<>();

    
    Parametre parametre;
    
    public SymbolArgs(Parser p, SymbolTVar tVar, SymbolArray a, int linea) {
        super(p, "SymbolArgs",linea);
        
        this.a = a;
        this.p = p;
        this.tVar = tVar;

        //TOCAR DIMENSIO, ALOMILLOR ES PER ARRAYS I NO ES 0
        //llistaDeParametres.add(new Parametre(a.id,tVar.tsb,0));
        parametre = new Parametre(a.id, tVar.tsb, 0);
    }
    
    public SymbolArgs(Parser p, SymbolArgs args, SymbolTVar tVar,SymbolArray a, int linea) {
        super(p, "SymbolArgs", linea);
        
        this.p = p;
        this.tVar = tVar;
        this.args = args;
        this.a = a;

        //llistaDeParametres.add(new Parametre(a.id,tVar.tsb,0));
        parametre = new Parametre(a.id, tVar.tsb, 0);
    }
    
    public String C3A(){

        if(args != null){
            parametres = args.parametres;
        }

        int dim = a.nivell;
        String[] vdim = new String[dim];
        for(int rec = 0; rec< dim; rec ++){
            vdim[rec] = (String)a.aux.pop();
        }
        //p.generador.novaVar(tVar.tsb,a.id,false,dim,vdim);
        Parametre v = p.generador.nouPar(tVar.tsb, a.id , a.nivell);
        parametres.add(v);
        return "";
    }


}
