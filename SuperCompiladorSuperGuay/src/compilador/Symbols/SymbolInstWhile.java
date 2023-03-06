package compilador.Symbols;

import Codi3A.Etiqueta;
import Codi3A.Generador;
import Codi3A.TipusInstruccionsCTA;
import compilador.ts.TSB;
import compilador.sintactic.Parser;
import compilador.ts.TaulaSimbols;

import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author 34601
 */
public class SymbolInstWhile extends ArbreSymbols{
    String valor;
    public Parser p;
    public SymbolExpLo e;
    public SymbolCos c;
    
    public SymbolInstWhile(Parser p, SymbolExpLo e, SymbolCos c, M1 m1, int linea) throws TSBsINVALIDEXPR, TaulaSimbols.ErrorCompiladorNivellProfunditat {
        super(p, "InstWhile",linea);
        
        p.taulaSimbols.sortirBloc();
        
        this.valor = e.valor;
        if(e.tsb != TSB.BOOLEAN){

                throw new TSBsINVALIDEXPR();

        }
        
        this.p = p;
        this.e = e;
        this.c = c;
    }
    
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariRedefinicioEntrada {
        
        Etiqueta e1 = p.generador.novaEtiqueta();
        Etiqueta e2 = p.generador.novaEtiqueta();
        Etiqueta e3 = p.generador.novaEtiqueta();
        
        p.generador.genera(TipusInstruccionsCTA.SKIP, e1.toString(), null, null);
        String s = e.C3A();
        p.generador.genera(TipusInstruccionsCTA.IFEQ, e2.toString(), s, "-1");
        p.generador.genera(TipusInstruccionsCTA.GOTO, e3.toString(), null, null);
        p.generador.genera(TipusInstruccionsCTA.SKIP, e2.toString(), null, null);
        c.C3A();
        p.generador.genera(TipusInstruccionsCTA.GOTO, e1.toString(), null, null);
        p.generador.genera(TipusInstruccionsCTA.SKIP, e3.toString(), null, null);
        return "";
    }
}
