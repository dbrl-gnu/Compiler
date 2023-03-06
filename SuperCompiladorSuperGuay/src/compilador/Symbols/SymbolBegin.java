package compilador.Symbols;

import Codi3A.Generador;
import compilador.sintactic.Parser;
import compilador.ts.TaulaSimbols;

public class SymbolBegin extends ArbreSymbols {
    Parser p;
    SymbolLlistDecl llistDecl;
    
    public SymbolBegin( Parser p, SymbolLlistDecl llistDecl, int linea) {
        super(p, "SymbolBegin", linea);
        
        this.p = p;
        this.llistDecl = llistDecl;
    }
    public SymbolBegin( Parser p, int linea) {
        super(p, "SymbolBegin",linea);
        
        this.p = p;
    }
    
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariRedefinicioEntrada {
        if(llistDecl != null){
            llistDecl.C3A();
        }
        
        return "";
    }
}
