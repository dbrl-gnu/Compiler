package compilador.Symbols;

import Codi3A.Generador;
import compilador.sintactic.Parser;
import compilador.ts.TaulaSimbols;

/**
 *
 * @author 34601
 */
public class SymbolInstR extends ArbreSymbols{
    public SymbolInstIf i;
    public SymbolInstWhile w;
    public SymbolInstDoWhile d;
    public SymbolCridada c;
    public SymbolRetu r;
    public SymbolIo io;
    public Parser p;
    public int tipus;
    
    public SymbolInstR(Parser p, SymbolInstIf i, int linea) throws TaulaSimbols.ErrorCompiladorNivellProfunditat {
        super(p, "InstR",linea);
        
        p.taulaSimbols.sortirBloc();
        
        this.p = p;
        this.i = i;
        tipus = 0;
    }
    
    public SymbolInstR(Parser p, SymbolInstWhile w, int linea) throws TaulaSimbols.ErrorCompiladorNivellProfunditat {
        super(p, "InstR",linea);

        p.taulaSimbols.sortirBloc();
        
        this.p = p;
        this.w = w;
        tipus = 1;
    }
    
    public SymbolInstR(Parser p, SymbolInstDoWhile d, int linea) throws TaulaSimbols.ErrorCompiladorNivellProfunditat {
        super(p, "InstR",linea);
        p.taulaSimbols.sortirBloc();

        this.p = p;
        this.d = d;
        tipus = 2;
    }
    
    public SymbolInstR(Parser p, SymbolCridada c, int linea){
        super(p, "InstR",linea);

        this.p = p;
        this.c = c;
        tipus = 3;
    }

    
    public SymbolInstR(Parser p, SymbolIo i, int linea){
        super(p, "InstR",linea);

        this.p = p;
        this.io = i;
        tipus = 5;
    }
    
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariRedefinicioEntrada {
        switch(tipus){
            case 0: i.C3A();
                break;
            case 1: w.C3A();
                break;
            case 2: d.C3A();
                break;
            case 3: c.C3A();
                break;
            case 5: io.C3A();
                break;
        }
        
        
        return "";
    }
    
    
}
