package compilador.Symbols;

import Codi3A.Etiqueta;
import Codi3A.Generador;
import Codi3A.TipusInstruccionsCTA;
import compilador.ts.TSB;
import compilador.sintactic.Parser;
import compilador.ts.TaulaSimbols;

/**
 *
 * @author 34601
 */
public class SymbolInstDoWhile extends ArbreSymbols{
    String valor;
    TSB tsb;
    public Parser p;
    public SymbolCos c;
    public SymbolExpLo e;
    
    public SymbolInstDoWhile(Parser p,  SymbolCos c, SymbolExpLo e, M1 m, int linea) throws TSBsINVALIDEXPR, TaulaSimbols.ErrorCompiladorNivellProfunditat {
        super(p, "InstWhile",linea);
        
        p.taulaSimbols.sortirBloc();
        
        this.valor = e.valor;
        if(e.tsb != TSB.BOOLEAN){

                //gestionar error de que no es una expressio boleana
                throw new TSBsINVALIDEXPR();

        }
                
        this.tsb = TSB.BOOLEAN;

        this.p = p;
        this.c = c;
        this.e = e;
    }
    
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariRedefinicioEntrada {
        Etiqueta e1 = p.generador.novaEtiqueta();
        Etiqueta e2 = p.generador.novaEtiqueta();
        Etiqueta e3 = p.generador.novaEtiqueta();
        
        p.generador.genera(TipusInstruccionsCTA.SKIP, e1.toString(), null, null);
        c.C3A();
        String s = e.C3A();
        p.generador.genera(TipusInstruccionsCTA.IFEQ, e1.toString(), s, "-1");

        return "";
    }
}
