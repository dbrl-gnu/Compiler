package compilador.ts;

import Codi3A.Variable;

public class DVariable extends Descripcio {
    private static int incr = 0;
    
    public String tipus; 
    int numVariable;
    public Variable var;
    public boolean cte;
    
    // No guardam el valor ja que això es sabra durant el temps d'execució
    public DVariable (String tipus, boolean cte) {
        this.tipus = tipus;
        this.numVariable = incr++;
        this.cte = cte;
    }

    public TSB getTipus(){
       switch (tipus){
           case "INTEGER":
               return TSB.INTEGER;
           case "BOOLEAN":
               return TSB.BOOLEAN;

       }
        return null;
    }

    @Override
    public String toString() {
        return "- Variable: tipus = " + tipus + " | nVar = " + numVariable;
    }

}