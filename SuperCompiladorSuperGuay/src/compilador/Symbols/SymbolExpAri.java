package compilador.Symbols;


import Codi3A.Generador;
import Codi3A.TipusInstruccionsCTA;
import Codi3A.Variable;
import compilador.ts.TSB;
import compilador.sintactic.Parser;
import compilador.ts.TaulaSimbols;

public class SymbolExpAri extends ArbreSymbols{
    TSB tsb;


    Variable varDesti;
    public Parser p;
    public SymbolExpAri ari;
    public SymbolOAri oAri;
    public SymbolOpernd opernd;
    public boolean esConstant = false;
    
    public SymbolExpAri(Parser p, SymbolExpAri ari, SymbolOAri oAri, SymbolOpernd opernd, int linea) {
        super(p, "ExpAri",linea);
        this.tsb = opernd.tsb;

        if(ari.esConstant && opernd.esConstant){
            this.esConstant = true;
        }
        this.p = p;
        this.ari = ari;
        this.oAri = oAri;
        this.opernd = opernd;
    }
    public SymbolExpAri(Parser p, SymbolOpernd opernd, int linea) {
        super(p, "ExpAri",linea);
        this.opernd = opernd;
        this.esConstant = opernd.esConstant;
        this.tsb = opernd.tsb;
        this.p = p;
    }
    
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariEntradaNoDefinida {
        this.varDesti = p.generador.novaVar(tsb,esConstant);

        //miram que sigui una expresio aritmetica i no nomes un valor
        //si és nomes un valor no tendra una operació
        if(this.oAri != null){
            TipusInstruccionsCTA op = null;

            switch (oAri.tipus) {
                case "+" -> op = TipusInstruccionsCTA.ADD;
                case "-" -> op = TipusInstruccionsCTA.SUB;
                case "*" -> op = TipusInstruccionsCTA.PROD;
                case "/" -> op = TipusInstruccionsCTA.DIV;
            }
            String s = ari.C3A();
            String s2 = opernd.C3A();
            p.generador.genera(op,this.varDesti.toString(), s, s2);
            
         
        }else{
            return opernd.C3A();
        }
        return this.varDesti.toString();   
    }
}
