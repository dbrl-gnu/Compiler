package Codi3A;

public class Etiqueta {
    private static int nEtiquetes = 0;
    public int id;
    public String nom;

    public Etiqueta(){//Per crear etiqueta amb nom generic
        nEtiquetes++;
        this.id = nEtiquetes;
        this.nom = "e"+nEtiquetes;
    }


    @Override
    public String toString() {
        return this.nom;
    }
}

