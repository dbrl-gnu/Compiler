package compilador.Symbols;

import compilador.sintactic.Parser;

public class SymbolCapFull_2 extends ArbreSymbols{
    Parser p;
    String id;
    SymbolArgs arg;
    public SymbolCapFull_2(Parser p, String id , int linea) {
        super(p, "CapFull_2", linea);

        this.p = p;
        this.id = id;
    }
    public SymbolCapFull_2(Parser p, SymbolArgs arg , String id, int linea) {
        super(p, "CapFull_2",linea);

        this.p = p;
        this.arg = arg;
        this.id = id;

        
    }
    
    public String C3A(){
        if(arg != null){
            arg.C3A();
        }
        
        
        return "";
    }


}
