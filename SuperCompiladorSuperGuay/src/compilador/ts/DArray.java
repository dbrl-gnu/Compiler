package compilador.ts;

import Codi3A.Variable;

public class DArray extends Descripcio {
    private static int incr = 0;
    
    public String tipus; 
    public int numDim;
    public String[] nvalors;
    public Variable var;

    // No guardam el valor ja que això es sabra durant el temps d'execució
    public DArray (String tipus, int dim, String[] nvalors) {
        this.tipus = tipus;
        this.numDim = dim;
        this.nvalors = nvalors;
    }

    @Override
    public String toString() {
        return "- Array: tipus = " + tipus + " | numDim = " + numDim+" | nValors = "+nvalors;
    }
}
