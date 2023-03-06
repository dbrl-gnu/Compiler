package compilador.Symbols;

import Codi3A.Generador;
import Codi3A.TipusInstruccionsCTA;
import Codi3A.Variable;
import compilador.ts.TSB;
import compilador.sintactic.Parser;
import compilador.ts.TaulaSimbols;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SymbolExpAnd extends ArbreSymbols{
    TSB tsb;
    String valor;
    Parser p;
    SymbolExpAnd expAnd;
    SymbolExpResp expResp;
    
    boolean esConstant = false;
    public SymbolExpAnd(Parser p, SymbolExpAnd expAnd, String op ,SymbolExpResp expResp, int linea) throws ErrorTipusVar {
        super(p, "ExpAnd",linea);

        this.tsb = expResp.tsb;
        if (expAnd.esConstant && expResp.esConstant){
            this.esConstant = true;
        }
        if(expResp.tsb != TSB.BOOLEAN || expAnd.tsb != TSB.BOOLEAN){

            throw new ErrorTipusVar();

        }        
        this.p = p;
        this.expAnd = expAnd;
        this.expResp = expResp;
    }
    public SymbolExpAnd(Parser p, SymbolExpResp expResp, int linea){
        super(p, "ExpAnd",linea);
        this.tsb = expResp.tsb;  
        this.esConstant = expResp.esConstant;

        this.p = p;
        this.expResp = expResp;
    }
    
    public String C3A() throws ErrorNDimArr, ErrorTipusVar, TaulaSimbols.ErrorUsuariEntradaNoDefinida {
        if(expAnd != null){
            //miram antes de fer una "i" logica que les dues parts siguin booleans

            Variable t = p.generador.novaVar(TSB.BOOLEAN,esConstant);
            String s2 = expAnd.C3A();
            String s = expResp.C3A();
            p.generador.genera(TipusInstruccionsCTA.AND, t.toString(), s, s2);
            return t.toString();
        }else{//si nomes es una expresio ja ens encarregarem de que torni  nomes una variable en el seguent nivell
            return expResp.C3A();
        }
    }
}
