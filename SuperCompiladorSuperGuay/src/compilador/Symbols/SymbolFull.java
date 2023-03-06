package compilador.Symbols;

import Codi3A.Generador;
import Codi3A.TipusInstruccionsCTA;
import compilador.sintactic.Parser;
import compilador.ts.TaulaSimbols;

public class SymbolFull extends ArbreSymbols{
    Parser p;
    SymbolCapFull_1 cf;
    SymbolCos cos;
    SymbolRetu retu;
    
    public SymbolFull(Parser p, SymbolCapFull_1 symbolCapFull1, SymbolCos cos, SymbolRetu retu, int linea) throws ErrorRetornIncorrecte, TaulaSimbols.ErrorCompiladorNivellProfunditat {
        super(p, "Full",linea);
        p.taulaSimbols.sortirBloc();
        
        this.p = p;
        this.cf = symbolCapFull1;
        this.cos = cos;
        this.retu = retu;
        if (cf.tVar.tsb != retu.tsb){

                throw new ErrorRetornIncorrecte();

        }
    }
    
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariRedefinicioEntrada{
        cf.C3A();
        if (cf.cf2.id.equals("main")){
            p.generador.genera(TipusInstruccionsCTA.SKIP,cf.cf2.id, null,null);
        }else{
            p.generador.genera(TipusInstruccionsCTA.SKIP,cf.cf2.id+"__"+p.generador.getProcedimentActual(), null,null);
        }

        p.generador.genera(TipusInstruccionsCTA.PMB,cf.cf2.id+"__"+p.generador.getProcedimentActual(),null,null);
        cos.C3A();
        retu.C3A();

            p.generador.closeProcedimiento(p.generador.getProcediment(cf.cf2.id).caller);

        return "";
    }
}
