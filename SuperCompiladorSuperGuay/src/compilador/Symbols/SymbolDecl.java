package compilador.Symbols;

import Codi3A.Generador;
import compilador.sintactic.Parser;
import compilador.ts.TaulaSimbols;


public class SymbolDecl extends ArbreSymbols{
    Parser p;
    SymbolVar var;
    SymbolAssig assig;
    SymbolBuit buit;
    SymbolFull full;
    int tipus;
    public SymbolDecl(Parser p, SymbolVar var, int linea) {
        super(p, "Decl",linea);
        
        this.p = p;
        this.var = var;
        tipus = 0;
    }

    public SymbolDecl(Parser p, SymbolAssig assig, int linea) {
        super(p, "Decl",linea);

        this.p = p;
        this.assig = assig;
        tipus = 1;
        
    }
    public SymbolDecl(Parser p, SymbolBuit buit, int linea) {
        super(p, "Decl",linea);
        
        this.p = p;
        this.buit = buit;
        tipus = 2;
    }
    public SymbolDecl(Parser p, SymbolFull full, int linea) {
        super(p, "Decl",linea);
        
        this.p = p;
        this.full = full;
        tipus = 3;
    }
    
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariRedefinicioEntrada {
        switch(tipus){
            case 0-> var.C3A();
            case 1-> assig.C3A();
            case 2-> buit.C3A();
            case 3->full.C3A();
        }
        
        
        return "";
    }

}
