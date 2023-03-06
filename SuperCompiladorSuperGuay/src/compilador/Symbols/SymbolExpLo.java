package compilador.Symbols;

import Codi3A.Etiqueta;
import Codi3A.Generador;
import Codi3A.TipusInstruccionsCTA;
import Codi3A.Variable;
import compilador.ts.TSB;
import compilador.sintactic.Parser;
import compilador.ts.TaulaSimbols;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SymbolExpLo extends ArbreSymbols{
    TSB tsb;
    String valor;
    public Parser p;
    public SymbolExpLo expLo;
    public SymbolExpAnd expAnd;
    
    boolean esConstant = false;
    public SymbolExpLo(Parser p, SymbolExpLo expLo, String op, SymbolExpAnd expAnd, int linea) throws ErrorTipusVar {
        super(p, "ExpLo",linea);
        
        this.tsb = expAnd.tsb;
        this.valor = expAnd.valor;
        if (expLo.esConstant && expAnd.esConstant){
            this.esConstant = true;
        }
            if(expLo.tsb != TSB.BOOLEAN || expAnd.tsb != TSB.BOOLEAN){

                    throw new ErrorTipusVar();

            }        
        this.p = p;
        this.expLo = expLo;

        this.expAnd = expAnd;
    }
    public SymbolExpLo(Parser p, SymbolExpAnd expAnd, int linea) throws ErrorTipusVar {
        super(p, "ExpLo",linea);
        
        this.tsb = expAnd.tsb;
        this.valor = expAnd.valor;
        this.esConstant = expAnd.esConstant;
        if(expAnd.expResp.tsb != expAnd.tsb){
            throw new ErrorTipusVar();
        }
        this.p = p;
        this.expAnd = expAnd;
    }
    
    public String C3A() throws ErrorNDimArr, ErrorTipusVar, TaulaSimbols.ErrorUsuariEntradaNoDefinida {

        Variable t;
        //si te una expresio logia vol que es mes que nomes una expresio and
        if(expLo != null){
            //miram antes de fer una "o" logica que les dues parts siguin booleans

            t = p.generador.novaVar(TSB.BOOLEAN,esConstant);
            String s = expLo.C3A();
            String s2 = expAnd.C3A();
            p.generador.genera(TipusInstruccionsCTA.OR, t.toString(), s, s2);
            return t.toString();
        }else{//si nomes es una expresio and ja ens encarregarem de que torni nomes una variable
            return expAnd.C3A();  
        }
    }
}
