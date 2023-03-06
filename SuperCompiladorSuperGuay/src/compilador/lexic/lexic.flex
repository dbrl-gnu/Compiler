/**
    començament
**/
package compilador.lexic;
import java.io.*;

import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

import compilador.sintactic.ParserSym;

%%
/** **
%standalone
 ** **/

/**
Utilitzam l'analitzador sintàctic CUP
**/
%cup


%public  
%class Scanner

%char
%line
%column

%eofval{
  return symbol(ParserSym.EOF);
%eofval}



NUM     = 0 | [1-9][0-9]*
ID      = [A-Za-z_][A-Za-z0-9_]*

WS        = [ \t]+
ENDLINE   = [\r\n]
InputCharacter = [^\r\n]

Comments = {LineComment} | {BlockComment}
LineComment = "//" {InputCharacter}* {ENDLINE}?
BlockComment = "/*" [^*] ~"*/" | "/*" "*"+ "/" 


// El següent codi es copiarà també, dins de la classe. És a dir, si es posa res
// ha de ser en el format adient: mètodes, atributs, etc. 
%{
    StringBuffer string = new StringBuffer();


    /**
     Construcció d'un symbol sense atribut associat.
     **/
    private ComplexSymbol symbol(int type) {
        ComplexSymbol s = new ComplexSymbol(ParserSym.terminalNames[type], type);
        s.left = yyline;
        s.right = yycolumn;
        return s;
    }
    
    /**
     Construcció d'un symbol amb un atribut associat.
     **/
    private Symbol symbol(int type, Object value) {
      ComplexSymbol s = new ComplexSymbol(ParserSym.terminalNames[type], type,value);
              s.left = yyline;
              s.right = yycolumn;
              return s;
    }
%}

%state STRING


/****************************************************************************/
%%

// Regles/accions
<YYINITIAL> "uwu"     { return symbol(ParserSym.UWU);                  }
<YYINITIAL> "int"     { return symbol(ParserSym.INT);                  }
<YYINITIAL> "boolean" { return symbol(ParserSym.BOOLEAN);              }
<YYINITIAL> "if"      { return symbol(ParserSym.IF);                   }
<YYINITIAL> "else"    { return symbol(ParserSym.ELSE);                 }
<YYINITIAL> "while"   { return symbol(ParserSym.WHILE);                }
<YYINITIAL> "do"      { return symbol(ParserSym.DO);                   }
<YYINITIAL> "return"  { return symbol(ParserSym.RETURN);               }
<YYINITIAL> "void"    { return symbol(ParserSym.VOID);                 }
<YYINITIAL> "const"   { return symbol(ParserSym.CONST);                }
<YYINITIAL> "("       { return symbol(ParserSym.LPAR);                 }
<YYINITIAL> ")"       { return symbol(ParserSym.RPAR);                 }
<YYINITIAL> "{"       { return symbol(ParserSym.LCOR);                 }
<YYINITIAL> "}"       { return symbol(ParserSym.RCOR);                 }
<YYINITIAL> "["       { return symbol(ParserSym.LBRA);                 }
<YYINITIAL> "]"       { return symbol(ParserSym.RBRA);                 }
<YYINITIAL> ","       { return symbol(ParserSym.COMA);                 }
<YYINITIAL> ";"       { return symbol(ParserSym.PCOMA);                }
<YYINITIAL> "="       { return symbol(ParserSym.IGUAL);                }
<YYINITIAL> "||"      { return symbol(ParserSym.OR);                   }
<YYINITIAL> "&&"      { return symbol(ParserSym.AND);                  }
<YYINITIAL> "<"       { return symbol(ParserSym.MENOR);                }
<YYINITIAL> ">"       { return symbol(ParserSym.MAJOR);                }
<YYINITIAL> "!="      { return symbol(ParserSym.NOTEQ);                }
<YYINITIAL> "=="      { return symbol(ParserSym.IGUALIGUAL);           }
<YYINITIAL> ">="      { return symbol(ParserSym.MAJORIG);              }
<YYINITIAL> "<="      { return symbol(ParserSym.MENORIG);              }
<YYINITIAL> "+"       { return symbol(ParserSym.SUMA);                 }
<YYINITIAL> "-"       { return symbol(ParserSym.RESTA);                }
<YYINITIAL> "*"       { return symbol(ParserSym.PROD);                 }
<YYINITIAL> "/"       { return symbol(ParserSym.DIV);                  }
<YYINITIAL> "ET"      { return symbol(ParserSym.ET);                   }
<YYINITIAL> "ST"      { return symbol(ParserSym.ST);                   }
<YYINITIAL> "true"    { return symbol(ParserSym.TRUE);                 }
<YYINITIAL> "false"   { return symbol(ParserSym.FALSE);                }
<YYINITIAL> {ID}      { return symbol(ParserSym.ID, this.yytext());    }
<YYINITIAL> {NUM}     { return symbol(ParserSym.NUM, Integer.parseInt(this.yytext()));}

<STRING> {
      [^\n\r\"\\]+                   { string.append( yytext() ); }
      \\t                            { string.append('\t'); }
      \\n                            { string.append('\n'); }

      \\r                            { string.append('\r'); }
      \\\"                           { string.append('\"'); }
      \\                             { string.append('\\'); }
}

<YYINITIAL> {
    {WS}                     { /* No fer res amb els espais */  }
    {Comments}               { /* No fer res amb els comentaris */  }
    {ENDLINE}                { /* No fer res amb els bots de linea */  }
}

[^]                      { return symbol(ParserSym.error);  }

/****************************************************************************/