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
public class SymbolInstIf extends ArbreSymbols{
    String valor;
    TSB tsb; 
    SymbolExpLo e;
    SymbolCos c;
    SymbolInstIf2 i;
    Parser p;
    public SymbolInstIf(Parser p, SymbolExpLo e, SymbolCos c, SymbolInstIf2 i, M1 m1, int linea) throws TSBsINVALIDEXPR, TaulaSimbols.ErrorCompiladorNivellProfunditat {
        super(p, "InstIf",linea);

        p.taulaSimbols.sortirBloc();
        
        this.valor = e.valor;
        if(e.tsb != TSB.BOOLEAN){

                //gestionar error de que no es una expressio boleana
                throw new TSBsINVALIDEXPR();

        }
        this.e = e;
        this.c = c;
        this.i = i;
        this.p = p;
        
        this.tsb = TSB.BOOLEAN;
    }
    
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariRedefinicioEntrada {
        
        String s = e.C3A();
        Etiqueta e1 = p.generador.novaEtiqueta();
        Etiqueta e2 = p.generador.novaEtiqueta();
        Etiqueta e3 = p.generador.novaEtiqueta();
        
        p.generador.genera(TipusInstruccionsCTA.IFEQ,e2.toString(),s,"-1");
        p.generador.genera(TipusInstruccionsCTA.GOTO, e1.toString(),null,null);
        p.generador.genera(TipusInstruccionsCTA.SKIP,e2.toString(), null,null );
        c.C3A();
        
        //miram si hi ha un else o no
        if(i.Else){
            p.generador.genera(TipusInstruccionsCTA.GOTO,e3.toString(), null, null );
            p.generador.genera(TipusInstruccionsCTA.SKIP, e1.toString(), null, null);
            i.C3A();
            p.generador.genera(TipusInstruccionsCTA.SKIP, e3.toString(), null, null);
        }else{
            p.generador.genera(TipusInstruccionsCTA.SKIP,e1.toString() , null, null);
        }

        return "";
    }
}
