package compilador.Symbols;

import compilador.sintactic.Parser;

public class SymbolTIo extends ArbreSymbols{

    String tipus;
    public SymbolTIo(Parser p, String tipus, int linea) {
        super(p, "TIo",linea);

        this.tipus = tipus;
    }
    
    
}
