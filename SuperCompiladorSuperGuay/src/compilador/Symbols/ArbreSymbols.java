package compilador.Symbols;

import Codi3A.TipusInstruccionsCTA;
import java.util.ArrayList;
import compilador.sintactic.Parser;

public class ArbreSymbols {

    private static int idIncrement = 0;
    ArrayList<String> fills = new ArrayList<String>();
    private int idNode;
    private static int idTerminals = 0;
    String nomNode;
    Parser p;
    static int linea;

    public ArbreSymbols( Parser p, String NomSimbol, int linea) {
        //assignar i augmentar el node id;
        idNode = idIncrement++;
        this.nomNode = NomSimbol;
        this.p = p;
        this.linea = linea;
    }

    public class TSBsINVALIDEXPR extends Exception{
        public TSBsINVALIDEXPR() {
            super("ERROR: Expressió invàlida, no podem mesclar tipus");
        }
    }

    public class NegacioIncorrecte extends Exception {

        public NegacioIncorrecte() {
            super("ERROR: Estas intentant negar una variable que no es pot negar!");
        }
    }
    public class ErrorTipusVar extends Exception {

        public ErrorTipusVar() {
            super("ERROR: El tipus de la variable és incorrecte");
        }
        public ErrorTipusVar(String id_var, int linea) {
            super("ERROR: El tipus de la variable és incorrecte Var: "+id_var);
        }
    }

    public class ErrorAssigVariable extends Exception {

        public ErrorAssigVariable() {
            super("ERROR: Aquesta assignació és incorrecte");
        }

    }

    public class ErrorRetornIncorrecte extends Exception{
        public ErrorRetornIncorrecte(){
            super("ERROR: El valor de retorn és incorrecte");
        }

    }
    public class ErrorArgsCridada extends Exception{
        public ErrorArgsCridada(){
            super("ERROR: Paràmetres d'entrada incorrectes");
        }

    }
    
    public class ErrorNArgsCridada extends Exception{
        public ErrorNArgsCridada(){
            super("ERROR: Nombre de paràmetres d'entrada incorrectes");
        }

    }
    public class ErrorNDimArr extends Exception{
        public ErrorNDimArr(){
            super("ERROR: Massa dimensions per aquest array");

        }
    }
    public class ErrorConst extends Exception{
        public ErrorConst(){
            super("ERROR: s'està intentat modifica una constant");

        }
    }

    public class ErrorConstArr extends Exception{
        public ErrorConstArr(){
            super("ERROR: els arrays no poden ser constants");

        }
    }

}

