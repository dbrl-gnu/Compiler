package compilador.Symbols;

import Codi3A.Generador;
import compilador.sintactic.Parser;
import compilador.ts.TaulaSimbols;

public class SymbolLlistDecl extends ArbreSymbols{
    public Parser p;
    public SymbolLlistDecl llistDecl;
    public SymbolDecl decl;
    
    public SymbolLlistDecl(Parser p, SymbolLlistDecl llistDecl, SymbolDecl decl, int linea) {
        super(p, "LlistDecl",linea);
        
        this.p = p;
        this.llistDecl = llistDecl;
        this.decl = decl;
    }
    public SymbolLlistDecl(Parser p, SymbolDecl decl, int linea) {
        super(p, "LlistDecl",linea);

        this.p = p;
        this.decl = decl;
    }
    
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariRedefinicioEntrada {
        if(llistDecl != null){
            llistDecl.C3A();
        }
        decl.C3A();
        
        
        return "";
    }
}
