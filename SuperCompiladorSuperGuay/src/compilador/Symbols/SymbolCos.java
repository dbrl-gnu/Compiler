package compilador.Symbols;

import Codi3A.Generador;
import compilador.sintactic.Parser;
import compilador.ts.TaulaSimbols;

/**
 *
 * @author 34601
 */
public class SymbolCos extends ArbreSymbols{
    Parser p;
    SymbolInstR i;
    SymbolCos c;
    SymbolVar v;
    SymbolAssig a;
    int tipus;
    
    //cas en que sigui una instruccio
    public SymbolCos(Parser p, SymbolInstR i, SymbolCos c, int linea){
        super(p, "Cos",linea);
        
        this.p = p;
        this.i = i;
        this.c = c;
        tipus = 0;
    }
    
    //cas en el que sigui una variable
    public SymbolCos(Parser p, SymbolVar v, SymbolCos c, int linea){
        super(p, "Cos",linea);
        
        this.p = p;
        this.v = v;
        this.c = c;
        tipus = 1;
    }
    
    //cas en el que sigui una assignacio
    public SymbolCos(Parser p, SymbolAssig a, SymbolCos c, int linea){
        super(p, "Cos", linea);

        this.p = p;
        this.a = a;
        this.c = c;
        tipus = 2;
    }
    
    //cas en el que no volguem posar res mes
    public SymbolCos(Parser p, int linea){
        super(p, "Cos", linea);
        
        this.p = p;
        tipus = 3;
    }
    
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariRedefinicioEntrada {
        
        switch(tipus){
            case 0 -> {
                i.C3A();
                c.C3A();
            }
            case 1 -> {
                v.C3A();
                c.C3A();
            }
            case 2 -> {
                a.C3A();
                c.C3A();
            }
            
        }
        
        return "";
    }
}
