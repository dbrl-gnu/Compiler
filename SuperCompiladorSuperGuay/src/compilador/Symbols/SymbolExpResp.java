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

public class SymbolExpResp extends ArbreSymbols{
    TSB tsb;
    //String valor;
    Variable valor;
    boolean esConstant = false;
    Parser p;
    SymbolExpResp expResp;
    SymbolOResp oResp;
    SymbolExpAri expAri;
    
    public SymbolExpResp(Parser p, SymbolExpResp expResp, SymbolOResp oResp,  SymbolExpAri expAri, int linea) throws ErrorTipusVar {
        super(p, "ExpResp",linea);
        this.tsb = TSB.BOOLEAN;
        this.valor = expAri.varDesti;
        if(expResp.esConstant && expAri.esConstant){
            this.esConstant = true;
        }
            //si els dos operants no son del mateix tipus o es fa una comparacio
            //"aritmetica" i no es un int aleshores es dona un error
            if((expAri.tsb != expResp.tsb)|| 
                ((oResp.tipus != "==" || oResp.tipus != "!=")
                && (expAri.tsb != TSB.INTEGER || expResp.tsb != TSB.INTEGER))){
                

                    throw new ErrorTipusVar();

            }          
        this.p = p;
        this.expResp = expResp;
        this.oResp = oResp;
        this.expAri = expAri;
    }
    
    public SymbolExpResp(Parser p, SymbolExpAri expAri, int linea) {
        super(p, "ExpResp",linea);
        this.tsb = expAri.tsb;
        this.esConstant = expAri.esConstant;
        
        this.p = p;
        this.expAri = expAri;
    }
    
    public String C3A() throws ErrorNDimArr, ErrorTipusVar, TaulaSimbols.ErrorUsuariEntradaNoDefinida {
        if(expResp != null){
            
          
            
            Variable t = p.generador.novaVar(TSB.BOOLEAN,esConstant);
            Etiqueta e1 = p.generador.novaEtiqueta();
            Etiqueta e2 = p.generador.novaEtiqueta();
            TipusInstruccionsCTA c = null;
            switch(oResp.tipus){
                case "<" -> c = TipusInstruccionsCTA.IFLT;
                case ">" -> c = TipusInstruccionsCTA.IFGT;
                case "!=" -> c = TipusInstruccionsCTA.IFNEQ;
                case "==" -> c = TipusInstruccionsCTA.IFEQ;
                case "<=" -> c = TipusInstruccionsCTA.IFLTE;
                case ">=" -> c = TipusInstruccionsCTA.IFGTE;   
            }
            String s = expResp.C3A();
            String s2 = expAri.C3A();
            p.generador.genera(c, e1.toString(), s, s2);
            p.generador.genera(TipusInstruccionsCTA.COPY, t.toString(), "0", null);
            p.generador.genera(TipusInstruccionsCTA.GOTO, e2.toString(), null, null);
            p.generador.genera(TipusInstruccionsCTA.SKIP, e1.toString(), null, null);
            p.generador.genera(TipusInstruccionsCTA.COPY, t.toString(), "-1", null);
            p.generador.genera(TipusInstruccionsCTA.SKIP, e2.toString(), null, null);
            return t.toString();
            
        }else{
            return expAri.C3A();
        }
        
        
     
    }
}
