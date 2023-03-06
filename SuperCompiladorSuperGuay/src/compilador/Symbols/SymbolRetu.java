package compilador.Symbols;

import Codi3A.Generador;
import Codi3A.TipusInstruccionsCTA;
import compilador.sintactic.Parser;
import compilador.ts.TSB;
import compilador.ts.TaulaSimbols;

public class SymbolRetu extends ArbreSymbols{
    public Parser p;
    public SymbolExpResp er;
    public TSB tsb;
    public SymbolRetu(Parser p, SymbolExpResp er, int linea) {
        super(p, "Retu",linea);
                
        this.p = p;
        this.er = er;
        this.tsb = er.tsb;
    }
    
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariEntradaNoDefinida {
        
        String s = er.C3A();

        p.generador.genera(TipusInstruccionsCTA.RTN, s, null, null);

        return "";
    }
}
