package compilador.Symbols;

import Codi3A.Generador;
import Codi3A.TipusInstruccionsCTA;
import compilador.sintactic.Parser;
import compilador.ts.TaulaSimbols;

public class SymbolBuit extends ArbreSymbols{
    Parser p;
    SymbolCapVoid capVoid;
    SymbolCos cos;
    
    public SymbolBuit(Parser p, SymbolCapVoid capVoid, SymbolCos cos, int linea) throws TaulaSimbols.ErrorCompiladorNivellProfunditat {
        super(p, "Buit", linea);
        
        p.taulaSimbols.sortirBloc();

        this.p = p;
        this.capVoid = capVoid;
        this.cos = cos;
    }
    
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariRedefinicioEntrada {
        capVoid.C3A();
        if (capVoid.cf2.id.equals("main")){
            p.generador.genera(TipusInstruccionsCTA.SKIP,capVoid.cf2.id, null,null);
        }else {
            p.generador.genera(TipusInstruccionsCTA.SKIP,capVoid.cf2.id+"__"+p.generador.getProcedimentActual(), null,null);
        }

        p.generador.genera(TipusInstruccionsCTA.PMB,capVoid.cf2.id+"__"+p.generador.getProcedimentActual(),null,null);
        cos.C3A();
        p.generador.genera(TipusInstruccionsCTA.RTN, null, null, null);


        p.generador.closeProcedimiento(p.generador.getProcediment(capVoid.cf2.id).caller);




        return "";
    }
}
