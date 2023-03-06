package Codi3A;

import compilador.ts.TSB;

public class Variable {
    private static int nVariables = 0; // contador total
    public int id; // valor enter que l’identifica
    public String nom;

    public TSB tsb;
    public int proc; // de quin subprograma és local
    public boolean esCte;
    public int dim; // quina ocupació té
    
    public int desp;
    public boolean esParam;
    

    public Variable(String nom, TSB tsb, int proc, boolean esCte, int dim){
        nVariables++;
        this.id = nVariables;
        this.nom = nom;
        this.tsb = tsb;
        this.proc = proc;
        this.esCte = esCte;
        this.dim = dim;
    }

    @Override
    public String toString() {
        return this.nom;
    }

    public String registre(){
        return "Variable: "+nom+" Tipus: "+tsb.toString()+" Procediment: "+proc;
    }
}
