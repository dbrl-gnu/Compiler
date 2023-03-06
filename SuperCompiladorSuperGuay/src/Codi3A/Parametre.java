package Codi3A;

import compilador.ts.TSB;

public class Parametre {
    public String nom;
    //public Tipus tipus;
    public TSB tsb;
    public int dim;
    public Parametre(String nom, TSB tsb, int dim) {
        this.nom = nom;
        //this.tipus = tipus;
        this.tsb = tsb;
        this.dim = dim;
    }

    @Override
    public String toString() {
        return "Parametre : " + nom + ", dimensio: "+ dim + " i tsb=" + tsb;
    }   
}
