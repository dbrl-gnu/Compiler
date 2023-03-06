package compilador.Symbols;

import Codi3A.Generador;
import Codi3A.TipusInstruccionsCTA;
import compilador.sintactic.Parser;
import compilador.ts.DVariable;
import compilador.ts.Descripcio;
import compilador.ts.TaulaSimbols;

public class SymbolIo extends ArbreSymbols{
    String tipusIO;
    Parser p;
    SymbolTIo t;
    SymbolOpernd o;
    
    public SymbolIo(Parser p, SymbolTIo t, SymbolOpernd o, int linea) throws ErrorConst, TaulaSimbols.ErrorUsuariEntradaNoDefinida {
        super(p, "Io",linea);
        this.tipusIO = t.tipus;
        
        this.p = p;
        this.t = t;
        this.o = o;
        if(o.a != null){
            Descripcio descripcio = p.taulaSimbols.consultarID(o.a.id);

            if (descripcio instanceof DVariable){
                DVariable dVariable = (DVariable) descripcio;


                if(dVariable.cte && t.tipus.equals("ET")){
                    throw new ErrorConst();
                }
            }
        }
    }
    
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariEntradaNoDefinida {
        TipusInstruccionsCTA c = null;
        switch(t.tipus){
            case "ET" -> c = TipusInstruccionsCTA.INT;
            case "ST" -> c = TipusInstruccionsCTA.OUTT;
            case "EF" ->c = TipusInstruccionsCTA.INF;
            case "SF" -> c = TipusInstruccionsCTA.OUTF;
        }
        String s = o.C3A();
        p.generador.genera(c, s, null,null);
        
        
        return "";
    }
    
}
