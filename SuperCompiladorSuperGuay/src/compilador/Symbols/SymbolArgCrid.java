package compilador.Symbols;
import Codi3A.Generador;
import Codi3A.Parametre;
import Codi3A.Procediment;
import Codi3A.Variable;

import compilador.sintactic.Parser;
import compilador.ts.DProcediment;
import compilador.ts.Descripcio;
import compilador.ts.TSB;
import compilador.ts.TaulaSimbols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
/**
 *
 * @author 34601
 */
public class SymbolArgCrid extends ArbreSymbols{
    String tipus;
    Variable valor;
    int numero;
    TSB tsb;
    Parser p;
    SymbolArgCrid a;
    SymbolOpernd o;
    Stack llistaArgs = new Stack();
    static ArrayList <Parametre> llistaDeParametres = new ArrayList<>();
    Parametre parametre;
    //FER
    //HashMap<String, ArrayList<Parametre>> parametres = new HashMap<>();

    
    public SymbolArgCrid(Parser p, SymbolArgCrid a, SymbolOpernd o, int linea) throws TaulaSimbols.ErrorUsuariEntradaNoDefinida {
        super(p,"ArgCrid", linea);
        
        this.valor = o.valor;
        this.numero = o.numero;
        this.tsb = o.tsb;

        this.p = p;
        this.a = a;
        this.o = o;
        //aqui tambe falta molt, gestionar sa recursivitat i tal
        if(o.tsb == TSB.BOOLEAN) {// es un bool
            parametre = new Parametre(o.s, o.tsb, 0);
        } else if(o.a != null) {
            parametre = new Parametre(o.a.id, o.tsb, 0);
        } else if (o.i != null){// un numero
            parametre = new Parametre(Integer.toString(o.numero), o.tsb, 0);
        }else if (o.c != null){
            DProcediment dProcediment = (DProcediment) p.taulaSimbols.consultarID(o.c.id);
            parametre = new Parametre(o.c.id,dProcediment.retorn,0);
        }
    }
    
    
    //cas en el que sigui el darrer(o primer segons com es miri) que s'analitza
    public SymbolArgCrid(Parser p, SymbolOpernd o, int linea) throws TaulaSimbols.ErrorUsuariEntradaNoDefinida {
        super(p,"ArgCrid",linea);

        this.valor = o.valor;
        this.numero = o.numero;
        this.tsb = o.tsb;

        this.p = p;
        this.o = o;

        if(o.tsb == TSB.BOOLEAN) {// es un bool
            parametre = new Parametre(o.s, o.tsb, 0);
        } else if(o.a != null) {
            parametre = new Parametre(o.a.id, o.tsb, 0);
        } else if (o.i != null){// un numero
            parametre = new Parametre(Integer.toString(o.numero), o.tsb, 0);
        }else if (o.c != null){
            DProcediment dProcediment = (DProcediment) p.taulaSimbols.consultarID(o.c.id);
            parametre = new Parametre(o.c.id,dProcediment.retorn,0);
        }
    }
    
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariEntradaNoDefinida { //nomes anam empilant els arguments
        if(a != null){//si no és el primer en empilar copia la pila de l'anterior"
            llistaArgs = a.llistaArgs;
        }
        String s = o.C3A();
        
        llistaArgs.push(s);
        return "";
    }

    
    public Parametre getParametre(int index){
        return llistaDeParametres.get(index);
    }
    
    public void borrarArguments(){
        this.llistaDeParametres.clear();
    }
}
