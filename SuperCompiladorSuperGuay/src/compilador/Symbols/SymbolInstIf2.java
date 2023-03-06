package compilador.Symbols;

import Codi3A.Generador;
import compilador.sintactic.Parser;
import compilador.ts.TaulaSimbols;

/**
 *
 * @author 34601
 */
public class SymbolInstIf2 extends ArbreSymbols{
    public boolean Else;
    public SymbolCos c;
    public Parser p;
    
    //hi ha else
    public SymbolInstIf2(Parser p, SymbolCos c,  int linea){
        super(p,"InstIf2",linea);

        Else = true;
        
        this.c = c;
        this.p = p;
    }
    
    //no hi ha else
    public SymbolInstIf2(Parser p, int linea){
        super(p, "InstIf2",linea);
        Else = false;

    }
    
    //ja gestionam tots els bots a l'instrucció if, aqui nomes cridam a la funció
    //del symol cos
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariRedefinicioEntrada {
        c.C3A();
        return "";
    }
}
