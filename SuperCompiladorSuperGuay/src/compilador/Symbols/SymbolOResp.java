package compilador.Symbols;


import compilador.sintactic.Parser;

public class SymbolOResp extends ArbreSymbols {

    String tipus; //<,>, !=, ==,>=, <=
    public SymbolOResp(Parser p,String s, int linea) {
        super( p,"OResp",linea);
        this.tipus = s;
    }
}
