package compilador.Symbols;


import compilador.sintactic.Parser;
import compilador.ts.TSB;

public class SymbolTVar extends ArbreSymbols {

    String tipus; //int, boolean, char, string_literal
    TSB tsb;
    public SymbolTVar(Parser p,String s, int linea) {
        super( p,"TVar",linea);

        this.tipus = s;
            switch (tipus) {
            case "int" -> {tsb = TSB.INTEGER; }
            case "boolean" ->{ tsb = TSB.BOOLEAN; }
        } 
    }
}
