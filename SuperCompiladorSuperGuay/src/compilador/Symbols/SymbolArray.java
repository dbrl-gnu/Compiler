package compilador.Symbols;
import Codi3A.Etiqueta;
import Codi3A.Generador;
import Codi3A.TipusInstruccionsCTA;
import Codi3A.Variable;
import compilador.sintactic.Parser;
import compilador.ts.*;

import java.time.temporal.ValueRange;
import java.util.Stack;

/**
 *
 * @author 34601
 */
public class SymbolArray extends ArbreSymbols {
    int numero;
    String tipus;
    //String valor;
    Variable valor;
    String id;
    int nivell = 0;
    Parser p;
    SymbolArray a;
    SymbolExpAri e;
    Stack aux = new Stack();

    public SymbolArray(Parser p, SymbolArray a, SymbolExpAri e, int linea){
        super(p, "Array",linea);
        if(e.tsb != TSB.INTEGER){
            try{
                
            throw new ErrorTipusVar();
            }catch(Exception e){
                System.out.println("Eerror");
                System.exit(1);
            }
        }

        this.p = p;
        this.a = a;
        this.e = e;
        this.id = a.id;
        this.nivell = a.nivell +1;
    }

    //en cas de que sigui l'ID
    public SymbolArray(Parser p, String s, int linea){
        super(p, "Array", linea);
        this.tipus = "id";
        this.id = s;
        //Descripcio d =(Descripcio) p.taulaSimbols.consultarID(s);

        this.p = p;
    }
    DArray darr;
    Variable t3;
    public String[] C3A(int primer, int nbytes, boolean dec) throws ErrorNDimArr, ErrorTipusVar, TaulaSimbols.ErrorUsuariEntradaNoDefinida {
        Variable t = null;
        String sa[];
        
        //si no és un array aleshores tornam simplement el nom
        if(a == null){

            return new String[] {id,null};
        }
        Variable arrr = p.generador.getVariable(a.id);

        //miram que no intentem accedir a una dimensio que no existeix
        if(dec){
            if(arrr.dim <nivell){
                throw new ErrorNDimArr();
            }
        }
        //a n'aquest int guardarem un int que representa la profunditat en la que sa troba 
        //la declaració de l'array que s'està mirant
        int auxaux;
        if(arrr != null){
            auxaux =arrr.proc;
        }else{
            auxaux = p.generador.getProcedimentActual();
        }
        
        t = p.generador.novaVar(TSB.INTEGER, e.esConstant);
        String se = e.C3A();//in
        
        //si no és una declaració miram que l'index de cada dimensió no surti d'aquesta dimensió, si així ho fa l'enviam a una subrutina
        //que donarà un error en temps d'execusió
        if(dec){
            Etiqueta e1 = p.generador.novaEtiqueta();
            p.generador.genera(TipusInstruccionsCTA.IFLT, e1.toString(), se, a.id+"_"+auxaux+"$"+primer);
            p.generador.genera(TipusInstruccionsCTA.GOTO, "error", null, null);
            p.generador.genera(TipusInstruccionsCTA.SKIP, e1.toString(), null, null);
        }else{
            //si és una declaració, aleshores anam guardant en unes variables la grandaria de cada dimensió
            t3 = p.generador.novaVar(TSB.INTEGER, e.esConstant);
            p.generador.genera(TipusInstruccionsCTA.COPY, a.id+"_"+auxaux+"$"+primer, se, null);
        }
        //si la producció de l'esquerra no te una expresio resp vol dir que és el darrer element de l'array, així que ja tancam
        if(a.e == null){
            //si primer és igual a 1 vol dir que és també l'element de més a la dreta, per lo que si entra aqui 
            //significa que és un array d'una dimensió
            if(primer == 1){
                p.generador.genera(TipusInstruccionsCTA.PROD, t.toString(), se, Integer.toString(nbytes)); //tx = t(x-1) * nbytes
                return new String[] {id,t.toString()};
            }
            return new String[] {id,se};
        }
        sa = a.C3A(primer+1, nbytes, dec);

        p.generador.genera(TipusInstruccionsCTA.PROD, t.toString(), sa[1], a.id+"_"+auxaux+"$"+primer); //tn = in * d(n+1)
        Variable t2 = p.generador.novaVar(TSB.INTEGER, e.esConstant);
        p.generador.genera(TipusInstruccionsCTA.ADD, t2.toString(), t.toString(), se); //t(n+1) = tn + i(n+1)
        return new String[] {id,t2.toString()};

    }
}
