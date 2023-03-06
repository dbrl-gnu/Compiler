package compilador.Symbols;

import Codi3A.Generador;
import Codi3A.TipusInstruccionsCTA;
import compilador.ts.DVariable;
import compilador.ts.Descripcio;
import compilador.ts.TSB;
import compilador.sintactic.Parser;
import compilador.ts.TaulaSimbols;


/**
 *
 * @author 34601
 */
public class SymbolAssig extends ArbreSymbols{
    String id;
    TSB tsb;
    public Parser p;
    public SymbolExpResp e;
    public SymbolArray a;

    public SymbolAssig(Parser p, SymbolArray a,SymbolExpResp e,int linea) throws ErrorTipusVar, TaulaSimbols.ErrorUsuariEntradaNoDefinida, ErrorConst {

        super(p, "SymbolAssig",linea);

        this.a = a;
        this.id = a.id;
        this.tsb = e.tsb;
        this.p = p;
        this.e = e;
        
        Descripcio descripcio = p.taulaSimbols.consultarID(a.id);

        if (descripcio instanceof DVariable){
            DVariable dVariable = (DVariable) descripcio;

            if (dVariable.getTipus() != this.tsb){

                    throw new ErrorTipusVar();

            }
            if(dVariable.cte){
                throw new ErrorConst();
            }
        }
    }

    public String C3A() throws ErrorNDimArr, ErrorTipusVar, TaulaSimbols.ErrorUsuariEntradaNoDefinida {

        String s = e.C3A();
        String s3;

        if (p.generador.procConteVariable(id) ){
            s3 = id+"_"+p.generador.getProcedimentActual();
        }else {
            s3 = id+"_0";
        }
        if(a.e == null){

            p.generador.genera(TipusInstruccionsCTA.COPY, s3, s,null);
        }else{
            String s2[] = a.C3A(1, 2, true);

            p.generador.genera(TipusInstruccionsCTA.IND_ASS,  s3, s, s2[1]);
        }

        return "";
    }
}