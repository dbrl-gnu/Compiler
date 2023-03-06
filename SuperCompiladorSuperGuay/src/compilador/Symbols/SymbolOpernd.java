package compilador.Symbols;
import Codi3A.Generador;
import Codi3A.TipusInstruccionsCTA;
import Codi3A.Variable;
import compilador.sintactic.Parser;
import compilador.ts.*;

import java.sql.SQLOutput;

/**
 *
 * @author 34601
 */
public class SymbolOpernd extends Expressio{
    int numero;
    //String valor;

    TSB tsb;
    int tipus;
    Variable valor;
    boolean esConstant = false;
    
    Parser p;
    SymbolArray a;
    Integer i;
    String s;
    SymbolOpernd o;
    SymbolExpLo e;
    SymbolCridada c;
    
    
    //Cas en el que sigui un ID o un array
    public SymbolOpernd(Parser p, SymbolArray a, int linea) throws TaulaSimbols.ErrorUsuariEntradaNoDefinida, ErrorTipusVar, ErrorAssigVariable {
        super(p,"Opernd",linea);
        //mirar a la taula de symbols quin tipus es aquest array
        Descripcio descripcio = p.taulaSimbols.consultarID(a.id);
        if (descripcio instanceof DVariable){
            DVariable dVariable = (DVariable) descripcio;
            switch (dVariable.tipus) {
                case "INTEGER" -> {this.tsb = TSB.INTEGER; }
                case "BOOLEAN" -> {this.tsb  = TSB.BOOLEAN; }
            }
        }else if (descripcio instanceof DArray){
            DArray dArray = (DArray) descripcio;
            switch (dArray.tipus) {
                case "INTEGER" -> {this.tsb = TSB.INTEGER; }
                case "BOOLEAN" -> {this.tsb  = TSB.BOOLEAN; }
            }
        }else {
            //ERROR
            throw  new ErrorAssigVariable();
        }



        //this.numero = a.numero;
        //this.valor = a.valor;
        this.esConstant = false;

        this.p = p;
        this.a = a;
        this.tipus = 0;
       //FALTEN LES DESCRIPCIONS!!

    }

    //Cas en el que l'operand sigui un numero
    public SymbolOpernd(Parser p, Integer i, int linea){
        super(p,"Opernd",linea);
        this.tsb = TSB.INTEGER;

        this.numero = i;
        this.esConstant = true;
        
        this.p = p;
        this.i = i;
        this.tipus = 1;
    }
    
    //cas en el que l'operand sigui un String, un TRUE o un FALSE
    public SymbolOpernd(Parser p, String s, int i, int linea ){
        super(p,"Opernd",linea);
        esConstant = true;
        this.p = p;
        this.s = s;
        this.tipus = 2;
        
        switch (i) {

            //Es un TRUE
            case 1 -> {
                this.valor = new Variable("-1",TSB.BOOLEAN,0,true,0);
                //afegir codi per aqui
                this.tsb = TSB.BOOLEAN;
            }
            //Es un String
            case 2 -> {
                this.valor = new Variable("0",TSB.BOOLEAN,0,true,0);
                this.tsb = TSB.BOOLEAN;
            }
        }
    }
    
    //en el cas que sigui -Operand
    public SymbolOpernd(Parser p, String s, SymbolOpernd o, int linea) throws NegacioIncorrecte {
        super(p,"Opernd",linea);
        if(o.tsb != TSB.INTEGER){
            throw new NegacioIncorrecte();
        }
        this.numero = -o.numero;
        this.i = -o.numero;
        this.tsb = o.tsb;
        this.esConstant = o.esConstant;
        
        this.p = p;
        this.o = o;
        this.tipus = 3;



    }
    
    //cas en el que sigui (Expresio Logica)
    public SymbolOpernd(Parser p, SymbolExpLo c, int linea){
        super(p,"Opernd",linea);
        this.tsb = c.tsb;
        //this.valor = c.valor;
        if (c.esConstant){
            this.esConstant = true;
        }
        
        this.p = p;
        this.e = c;
        this.tipus = 4;
    }

    //Cridada
    public SymbolOpernd(Parser p, SymbolCridada c, int linea) throws TaulaSimbols.ErrorUsuariEntradaNoDefinida, ErrorAssigVariable {
        super(p, "Opernd",linea);

        this.esConstant = false;
        //falta codi de goto i variable

        this.p = p; 
        this.c = c;
        this.tipus = 5;
        Descripcio descripcio = p.taulaSimbols.consultarID(c.id);
        if (descripcio instanceof DProcediment){
            DProcediment dProcediment = (DProcediment) descripcio;
            this.tsb = dProcediment.retorn;
        }else {
            //error
            throw new ErrorAssigVariable();
        }
    }
    
    public String C3A() throws ErrorTipusVar, ErrorNDimArr, TaulaSimbols.ErrorUsuariEntradaNoDefinida{
        Variable t;
        switch(tipus){
            case 0://id o array
                String s2[] = a.C3A(1, 2, true);
                String s3;
                if (p.generador.procConteVariable(s2[0]) ){
                    s3 = s2[0]+"_"+p.generador.getProcedimentActual();
                }else {
                    s3 = s2[0]+"_0";
                }
                if(a.e == null){//si no es un array
                    return s3;
                }else{//cas en el que sigui un array
                    t = p.generador.novaVar(tsb,false);
                    p.generador.genera(TipusInstruccionsCTA.IND_VAL, t.toString(), s3, s2[1]);
                    return t.toString();
                }
             
            case 1://numero
                t = p.generador.novaVar(tsb,true);
                p.generador.genera(TipusInstruccionsCTA.COPY,t.toString(),Integer.toString(numero), null );
                return t.toString();
            case 2://string true o false
                t = p.generador.novaVar(tsb,true);
                p.generador.genera(TipusInstruccionsCTA.COPY, t.toString(),valor.toString(), null);
                return t.toString();
            case 3://-operands

                t = p.generador.novaVar(tsb,esConstant);
                String s = o.C3A();
                p.generador.genera(TipusInstruccionsCTA.NEG, t.toString(),s, null);
                return t.toString(); 
            case 4://expresio logica
                //nomes hem de retornar la variable on esta el resultat de l'operacio logica
                return e.C3A();
                
            case 5://cridada
                //nomes hem de retornar la variable on esta el resultat de la cridada

                return c.C3A();
                }
            
        
        return "";
    }
}
