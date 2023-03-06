/*
- Classe per la TAULA DE SIMBOLS.
*/
package compilador.ts;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaulaSimbols {
    // Declaram la variable 'n' que ens inidica a quina profunditat esteim.
    private int nivellActual;
    // Necesitare poder guardar seguente posicio valida.
    private int pValida; // Cada vegada que afegim al hashmap haurem l'haurem d'augmentar.

    // Declaram les taules.
    private Integer[] TaulaAmbits;
    private Bloc[] TaulaDescripcio;
    private Bloc[] TaulaExpansio;

    // HashMap per la obtenir la posicio en la taula de descripcio.
    private HashMap<String, Integer> posicioTD;

    private final int NPMAX = 20;
    private final int NSMAX = 100;
    BufferedWriter bw;

    // Constructor de la taula de simbols.
    public TaulaSimbols()  {
        // El nostre nivell actual sera 0.
        nivellActual = 0;
        // La primera posicio valida es 0.
        pValida = 0;
        try {
            bw = new BufferedWriter(new FileWriter("TaulaDeSimbols.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Inicialitzam les taules.
        TaulaAmbits = new Integer[NPMAX];
        for (int i = 0; i < TaulaAmbits.length; i++) {
            TaulaAmbits[i] = -1;
        }

        TaulaDescripcio = new Bloc[NSMAX];
        for (int i = 0; i < TaulaDescripcio.length; i++) {
            TaulaDescripcio[i] = new Bloc();
        }

        TaulaExpansio = new Bloc[NSMAX];
        for (int i = 0; i < TaulaExpansio.length; i++) {
            TaulaExpansio[i] = new Bloc();
        }

        // Cream el hashmap.
        posicioTD = new HashMap<>();
        
        // Afegim els TSBs a la ts.
        /*
        posarTSB("null", new DTipus(TSB.NULL));
        posarTSB("int", new DTipus(TSB.INTEGER));
        posarTSB("String", new DTipus(TSB.STRING));
        posarTSB("boolean", new DTipus(TSB.BOOLEAN));
        posarTSB("char", new DTipus(TSB.CARACTER));
        */
    }

    // Metode per resetetjar la taula.
    public void buidarTS() {
        // Inicialitzam les taules.
        TaulaAmbits = new Integer[NPMAX];
        for (int i = 0; i < TaulaAmbits.length; i++) {
            TaulaAmbits[i] = 0;
        }

        TaulaDescripcio = new Bloc[NSMAX];
        for (int i = 0; i < TaulaDescripcio.length; i++) {
            TaulaDescripcio[i] = new Bloc();
        }

        TaulaExpansio = new Bloc[NSMAX];
        for (int i = 0; i < TaulaExpansio.length; i++) {
            TaulaExpansio[i] = new Bloc();
        }

        // Borram totes les entrades del HashMap.
        posicioTD.clear();

        // Resetetjam el nivell de profundiatat al default i la posicio valida.
        nivellActual = 0;
        pValida = 0;
    }

    private void posarTSB(String ID, Descripcio desc) {
        posicioTD.put(ID, pValida);
        
        Bloc novaEntrada = new Bloc(ID, desc, -1, -1, -1);
        TaulaDescripcio[pValida] = novaEntrada;
        
        pValida++;
    }
    
    // Metode per insertar un nou identificador a la taula de simbols.
    public void posarID(String ID, Descripcio desc) throws ErrorUsuariRedefinicioEntrada {
        // Obtenim l'index a la taula de descrpicio.
        Integer pTD = posicioTD.get(ID);
        // Si no es null significa que ja ha estat afegida a la TD.
        if (pTD != null) {
            // Si l'identificador ja esta a la taula i te el mateix nivell de profunditat
            // es un error de l'usuari on ha declarat dues variables iguals.
            if (TaulaDescripcio[pTD].np == nivellActual) {

                    throw new ErrorUsuariRedefinicioEntrada(ID);

                // Si existeix hem de moure el valor actual a la TE i actualitzar
                // el valor de la TD.
            } else {
                // Incrementam el marcador de l'ubicació on posar el següent
                // identificador que s’ha d’ocultar
                int idxe = TaulaAmbits[nivellActual];
                idxe++;
                TaulaAmbits[nivellActual] = idxe;

                // Cream una nova entrada (Realment no faria falta, simplement comoditat)
                Bloc trobat = new Bloc();
                // Inicilitzam amb els valors trobats a la TD.
                trobat.id = TaulaDescripcio[pTD].id;
                trobat.d = TaulaDescripcio[pTD].d;
                trobat.np = TaulaDescripcio[pTD].np;
                trobat.next = TaulaDescripcio[pTD].next;
                trobat.nivellDeclarat = TaulaDescripcio[pTD].nivellDeclarat;
                // L'afegfim a la primer posicio de la TE.
                TaulaExpansio[idxe] = trobat;

                // Actualitzam el valor de la TD.
                TaulaDescripcio[pTD].d = desc;
                TaulaDescripcio[pTD].np = nivellActual;
                TaulaDescripcio[pTD].next = -1;
            }
        } else {
            // Sino crearem una nova entrada i l'afegirem.
            posicioTD.put(ID, pValida);
            Bloc novaEntrada = new Bloc(ID, desc, nivellActual, -1, nivellActual);
            TaulaDescripcio[pValida] = novaEntrada;
            pValida++;
        }
    }

    // Metode per retornar la descripcio d'un identificador.
    public Descripcio consultarID(String ID) throws ErrorUsuariEntradaNoDefinida {
        // Obtenim l'index a la taula de descrpicio.
        Integer pTD = posicioTD.get(ID);
        
        // Si la cerca es null significa que no tenim l'identificador.
        if (pTD == null) {
//
                // Llençam l'error, dins trycatch per no arrosegar el throws.
                throw new ErrorUsuariEntradaNoDefinida(ID);

            
        }

        // Retrornam la descripcio del bloc trobat.
        return TaulaDescripcio[pTD].d;
    }

    // Augmentam la profunditat de la taula, de 'nivellActual'.
    public void entrarBloc() {
        nivellActual++;
        TaulaAmbits[nivellActual] = TaulaAmbits[nivellActual - 1];
    }

    //HE LLEVAT LO DES BLOC PERO NO HE POGUT ARREGLAR ES PRINTS
    // SOBRETOT SI SON residus DE posarparam, posarIndex o posarcamp.
     
    // Disminuim la profunditat de la taula, de 'nivellActual'.
    public void sortirBloc() throws ErrorCompiladorNivellProfunditat {
        // Si el nivell de profunditat es 0 tenim un error greu del compilador.
        if (nivellActual == 0) {

                throw new ErrorCompiladorNivellProfunditat();

        }

        // Miram quins indexos estan ocults i han de pasar a visibles.
        Integer lInici = TaulaAmbits[nivellActual];
        nivellActual--;
        Integer lFi = TaulaAmbits[nivellActual];

        // Copiam les entrades de te del nivell anterior a la td del nivell actual.
        for (int i = lInici; i > lFi; i--) {
            // Nomes copiam si el el camp NP no es -1, es a dir, no es un argument. 
            if (TaulaExpansio[i].np != -1) {
                // Obtenim la posicio de la TD del element a actualitzar.
                String ID = TaulaExpansio[i].id;
                Integer pTD = posicioTD.get(ID);            
                // Actualitzam la TD amb els valors de la TE.
                TaulaDescripcio[pTD].np = TaulaExpansio[i].np;
                TaulaDescripcio[pTD].d = TaulaExpansio[i].d;
                TaulaDescripcio[pTD].nivellDeclarat = TaulaExpansio[i].nivellDeclarat;
                TaulaDescripcio[pTD].next = TaulaExpansio[i].next;     
            }
        }
        
        // Eliminam de la taula de descripcio les entrades creades al nivell actual.
        for (Bloc bloc : TaulaDescripcio) {
            // Comparam si es del nivell a borrar.
            if (bloc.nivellDeclarat == nivellActual+1) {
                // Lliberam del hashing TD i lliberam la posicio lliure.
                posicioTD.remove(bloc.id);
            }
        }
    }


    public void mostrarTS() {
        System.out.println("TAULA DE SIMBOLS: ");
        System.out.println("Nivell actual: "+nivellActual);
        System.out.println("Taula de descripció:");
        for (var entry : posicioTD.entrySet()) {
            // Obtenemos el valor asociado a la clave
            Integer value = entry.getValue();
            System.out.println(TaulaDescripcio[value]);
        }
        System.out.println("\nTaula de expansió:");
        for (Bloc blocTE : TaulaExpansio) {
            if (!blocTE.id.equals("") && blocTE.nivellDeclarat <= nivellActual) {
                System.out.println(blocTE.toString());
            }
        }
        System.out.println("=============================================================");
    }
    
    public void guardarTS() throws ErrorGenerarFitxerTS {
        try {

            bw.write("TAULA DE SIMBOLS: \nTaula de descripció:\n");
            for (var entry : posicioTD.entrySet()) {
                // Obtenemos el valor asociado a la clave
                Integer value = entry.getValue();
                bw.write(TaulaDescripcio[value].toString()+"\n");
            }
            bw.write("Taula d'expansió:\n");
            for (Bloc blocTE : TaulaExpansio) {
                if (!blocTE.id.equals("")) {
                    bw.write(blocTE.toString()+"\n");
                }
            }
            bw.close();
        } catch (IOException ex) {
            throw new ErrorGenerarFitxerTS();
        }
    }
    public void posarIndex(String id, Descripcio d) throws ErrorUsuariEntradaNoDefinida, ErrorUsuariTipusIncompatible {
        // Obtenim la posició de la declaracio a la TD.
        Integer pTD = posicioTD.get(id);
        // Si la cerca es null significa que no tenim l'identificador.
        if (pTD == null) {

                throw new ErrorUsuariEntradaNoDefinida();

        }
        // Obtenim la descripcio associada a l'entrada.
        Descripcio da = TaulaDescripcio[pTD].d;
        // Si el tipus no concorda tenim un error d'usuari.
        if (!(d instanceof DArray)) {

                throw new ErrorUsuariTipusIncompatible();

        }
        // Recorrem la llista enllaçada fins arribar al final.
        int idxe = TaulaDescripcio[pTD].next;
        int idxep = -1;
        while (idxe != -1) {
            idxep = idxe;
            // Obtenim el seguent punter de la llista.
            idxe = TaulaExpansio[idxe].next;
        }
        // Augmentam el punter per ocultar la definicio.
        idxe = TaulaAmbits[nivellActual];
        idxe++;
        // Agregam els camps passats per parametre a la taula d'expansio
        TaulaAmbits[nivellActual] = idxe;
        TaulaExpansio[idxe].id = "null";
        TaulaExpansio[idxe].np = -1;
        TaulaExpansio[idxe].d = d;
        // Marcam el nou element com fi de la llista.
        TaulaExpansio[idxe].next = -1;
        TaulaExpansio[idxe].nivellDeclarat = nivellActual;
        // Marcam si es el primer element de l'array o si s'ha enllaçar amb el darrer.
        if (idxep == -1){
            TaulaDescripcio[pTD].next = idxe;
        } else {
            TaulaExpansio[idxep].next = idxe;
        }
    }

    public void posarParam (String idpr, String idparam, Descripcio d) throws ErrorUsuariEntradaNoDefinida, ErrorUsuariTipusIncompatible, ErrorUsuariParametreJaExistent {
        // Obtenim la posició de la declaracio a la TD.
        Integer pTD = posicioTD.get(idpr);
        // Si la cerca es null significa que no tenim l'identificador.
        if (pTD == null) {

                throw new ErrorUsuariEntradaNoDefinida();

        }
        // Obtenim la descripcio associada a l'entrada.
        Descripcio da = TaulaDescripcio[pTD].d;
        // Si el tipus no concorda tenim un error d'usuari.
        if (!(da instanceof DProcediment)) {

                throw new ErrorUsuariTipusIncompatible();

        }

        // Recorrem la llista enllaçada fins arribar al final.
        int idxe = TaulaDescripcio[pTD].next;
        int idxep = -1;
        while (idxe != -1 && !TaulaExpansio[idxe].id.equals(idparam)) {
            idxep = idxe;
            // Obtenim el seguent punter de la llista.
            idxe = TaulaExpansio[idxe].next;
        }
        // Si esta inicialitzat tenim un error a nivell d'usuari.
        if (idxe != -1) {

                throw new ErrorUsuariParametreJaExistent();

        }



        // Agregam els camps passats per parametre a la taula d'expansio
        idxe = pValida;
        pValida++;
        TaulaExpansio[idxe].id = idparam;
        TaulaExpansio[idxe].np = -1;
        TaulaExpansio[idxe].d = d;
        // Marcam el nou element com fi de la llista.
        TaulaExpansio[idxe].next = -1;
        TaulaExpansio[idxe].nivellDeclarat = nivellActual;

        // Marcam si es el primer element del subprograma o si s'ha enllaçar amb el darrer.
        if (idxep == -1){
            TaulaDescripcio[pTD].next = idxe;
        } else {
            TaulaExpansio[idxep].next = idxe;
        }        
    }

    // NOMES ESTA IMPLEMENTAT AMB TAULES PERO PODRIEM FERHO AMB LES ALTRES TAMBE

    // Dóna l’accés a la primera dimensió de una taula, en definitiva, retorna una
    // posició de la taula d’expansió.
    public int first(String id) throws ErrorUsuariTipusIncompatible {
        Integer pTD = posicioTD.get(id);
        DVariable da = (DVariable) TaulaDescripcio[pTD].d;
        if (!da.tipus.equals("taula")) {

                throw new ErrorUsuariTipusIncompatible();

        }
        return TaulaDescripcio[pTD].next;
    }
    // Donada una dimensió d’una taula retorna la següent.
    public int next(int idx) {
        if(TaulaExpansio[idx].next == -1) {
            //Si es el darrer element retornam un el centinella -1.
            return -1;
        }
        return TaulaExpansio[idx].next;
    }
    // Mètode per saber si és la darrera dimensió.
    public boolean last(int idx) {
        return TaulaExpansio[idx].next == -1;
    }
    // Mètode per poder obtenir les dades de la dimensió.
    public Descripcio consulta(int idx) {
        return TaulaExpansio[idx].d;
    }
    
    public ArrayList obtenirTipusArguments(String ID) throws ErrorUsuariEntradaNoDefinida, ErrorUsuariTipusIncompatible {
        ArrayList<TSB> args = new ArrayList<>();
        
        // Obtenim la posició de la declaracio a la TD.
        Integer pTD = posicioTD.get(ID);
        // Si la cerca es null significa que no tenim l'identificador.
        if (pTD == null) {

                throw new ErrorUsuariEntradaNoDefinida();

        }
        
        // Obtenim la descripcio associada a l'entrada.
        Descripcio da = TaulaDescripcio[pTD].d;
        // Si el tipus no concorda tenim un error d'usuari.
        if (!(da instanceof DProcediment)) {

                throw new ErrorUsuariTipusIncompatible();

        }
        
        //Obtenim el primer argument del procediment.
        int idxe = TaulaDescripcio[pTD].next;
                
        //Iteram sobre la llista enllaçada.
        while (idxe != -1) {               
            //Convertim la variable que sera de tipus var.
            DVariable Dtemp = (DVariable) TaulaExpansio[idxe].d;
            //Convertim d'string a TSB i l'afegim a la llista de retorn.
            switch (Dtemp.tipus) {
                case "INTEGER" -> {args.add(0,TSB.INTEGER);}
                case "BOOLEAN" ->{args.add(0,TSB.BOOLEAN);}
            }
            //Obtenim el seguent element de la llista.
            idxe = TaulaExpansio[idxe].next;
        }
        //Retornam el que hem trobat.

        return args;

    }
    public ArrayList obtenirNomArguments(String ID) throws ErrorUsuariEntradaNoDefinida, ErrorUsuariTipusIncompatible {
        ArrayList<String> args = new ArrayList<>();

        // Obtenim la posició de la declaracio a la TD.
        Integer pTD = posicioTD.get(ID);
        // Si la cerca es null significa que no tenim l'identificador.
        if (pTD == null) {

                throw new ErrorUsuariEntradaNoDefinida();

        }

        // Obtenim la descripcio associada a l'entrada.
        Descripcio da = TaulaDescripcio[pTD].d;
        // Si el tipus no concorda tenim un error d'usuari.
        if (!(da instanceof DProcediment)) {

                throw new ErrorUsuariTipusIncompatible();

        }

        //Obtenim el primer argument del procediment.
        int idxe = TaulaDescripcio[pTD].next;

        //Iteram sobre la llista enllaçada.
        while (idxe != -1) {
            //Convertim la variable que sera de tipus var.
            //Convertim d'string a TSB i l'afegim a la llista de retorn.
            args.add(TaulaExpansio[idxe].id);
            //Obtenim el seguent element de la llista.
            idxe = TaulaExpansio[idxe].next;
        }
        //Retornam el que hem trobat.
        return args;
    }
    
    // Clase que representa un bloc de la taula.
    private class Bloc {
        private String id;
        // Atributs de classe, d = descripcio i np = nivellProfunditat.
        private Descripcio d;
        // Ambit/nivell on s’ha declarat
        private int np;
        // Next es la posicio de l'array al seguent valor encadenat.
        private int next;
        // Nivell on s'ha creat, necesari alhora de sortir bloc x eliminar de la TD.
        private int nivellDeclarat;
        
        // Definim els atributs com a valors per defecte.
        public Bloc() {
            id = "";
            d = new Descripcio();
            np = -1;
            next = -1;
            nivellDeclarat = -1;
        }

        public Bloc(String id, Descripcio d, int np, int next, int nd) {
            this.id = id;
            this.d = d;
            this.np = np;
            this.next = next;
            this.nivellDeclarat = nd;
        }

        @Override
        public String toString() {
            return "ID: " + this.id + " / Descripcio: " + this.d + 
                    " / Nivell profunditat declarat: " + this.np +
                    " / Next: " + this.next + " / NPO : " + this.nivellDeclarat;
        }
    }

    // Definim les excepcions que utilitzara el nostre compilador.
    public class ErrorCompiladorNivellProfunditat extends Exception {
        public ErrorCompiladorNivellProfunditat() {
            super("ERROR INTERN COMPILADOR: Revisar nivells de profunditat.");
        }
    }

    public class ErrorUsuariEntradaNoDefinida extends Exception {
        public ErrorUsuariEntradaNoDefinida(String id) {
            super("ERROR D'USUARI: Us d’identificador sense declarar, "+id+".");
        }
        public ErrorUsuariEntradaNoDefinida() {
            super("ERROR D'USUARI: Us d’identificador sense declarar");
        }
    }

    public class ErrorUsuariRedefinicioEntrada extends Exception {
        public ErrorUsuariRedefinicioEntrada(String id) {
            super("ERROR D'USUARI: Redefinició de l’identificador al mateix nivell,  "+id+".");
        }
    }
    
    public class ErrorUsuariTipusIncompatible extends Exception {
        public ErrorUsuariTipusIncompatible() {
            super("ERROR D'USUARI: Tipus de dada incompatible");
        }
    }
        


    public class ErrorUsuariParametreJaExistent extends Exception {
        public ErrorUsuariParametreJaExistent() {
            super("ERROR D'USUARI: Ja existeix un paràmetre amb aquest identificador");
        }
    }
    public class ErrorGenerarFitxerTS extends Exception {
        public ErrorGenerarFitxerTS() {
            super("ERROR: No s'ha pogut generar el fitxer de la taula de simbols");
        }
    }
}