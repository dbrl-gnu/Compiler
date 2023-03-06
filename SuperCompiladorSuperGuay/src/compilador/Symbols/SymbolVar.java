package compilador.Symbols;

import Codi3A.Generador;
import Codi3A.Variable;
import compilador.sintactic.Parser;
import compilador.ts.DArray;
import compilador.ts.DVariable;
import compilador.ts.TSB;
import Codi3A.TipusInstruccionsCTA;
import compilador.ts.TaulaSimbols;

public class SymbolVar extends ArbreSymbols{
    String tipus;
    Variable var;
    Parser p;
    SymbolTVar tVar;
    SymbolArray a;
    SymbolExpResp expResp;
    TSB tsb;
    public SymbolVar(Parser p, SymbolTVar tVar, SymbolArray a, SymbolExpResp expResp, int linea) throws TaulaSimbols.ErrorUsuariRedefinicioEntrada, ErrorTipusVar {
        super(p, "SymbolVar",linea);

        this.p = p;
        this.tVar = tVar;
        this.a = a;
        this.expResp = expResp;

        if (tVar.tsb != expResp.tsb){

                throw new ErrorTipusVar(a.id,linea);


        }
        switch (tVar.tipus) {
            case "int" -> {tsb = TSB.INTEGER; }
            case "boolean" ->{ tsb = TSB.BOOLEAN; }
        }
        DVariable dv = new DVariable(tsb.toString(), false);


        p.taulaSimbols.posarID(a.id, dv);
    }
    public SymbolVar(Parser p, SymbolTVar tVar, SymbolArray a, SymbolExpResp expResp, int linea, int res) throws TaulaSimbols.ErrorUsuariRedefinicioEntrada, ErrorTipusVar, ErrorConstArr {
        super(p, "SymbolVar",linea);

        this.p = p;
        this.tVar = tVar;
        this.a = a;
        this.expResp = expResp;

        if (tVar.tsb != expResp.tsb){

            throw new ErrorTipusVar(a.id,linea);
        }
        if(a.nivell != 0){
            throw new ErrorConstArr();
        }
        switch (tVar.tipus) {
            case "int" -> {tsb = TSB.INTEGER; }

            case "boolean" ->{ tsb = TSB.BOOLEAN; }
        }
        DVariable dv = new DVariable(tsb.toString(), true);

        //DTipus d = (DTipus) p.taulaSimbols.consultarID(tVar.tipus);//El tVar.valor es per comprovar el tsb
        p.taulaSimbols.posarID(a.id, dv);
    }
    
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariRedefinicioEntrada, TaulaSimbols.ErrorUsuariEntradaNoDefinida{
        TSB tsb = null;
        int nbytes = 0;

        switch (tVar.tipus) {
            case "int" -> {tsb = TSB.INTEGER; nbytes = 2;}
            case "boolean" ->{ tsb = TSB.BOOLEAN; nbytes = 2;}
        }

        String s2[] = a.C3A(1, nbytes, false);
        int dim = a.nivell;
        String[] vdim = new String[dim];



        String s = expResp.C3A();
        if(dim > 0){
            p.generador.genera(TipusInstruccionsCTA.IND_ASS, s2[0]+"_"+p.generador.getProcedimentActual(), null, s2[1]);
        }else{
            p.generador.genera(TipusInstruccionsCTA.COPY, s2[0]+"_"+p.generador.getProcedimentActual(), s, null);
        }
        p.generador.novaVar(tsb, s2[0], false, dim, vdim);
        return "";
    }
}
