package compilador.Symbols;


import compilador.sintactic.Parser;

public class SymbolOAri extends ArbreSymbols {

    String tipus; //+, -, *, /, %
    public SymbolOAri(Parser p,String s, int linea) {
        super( p,"OAri",linea);
        this.tipus = s;
    }
}
