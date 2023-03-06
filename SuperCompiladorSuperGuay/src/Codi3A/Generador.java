package Codi3A;

import compilador.ts.TSB;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Generador {

    ArrayList<Instruccio> llistaDeInstruccions = new ArrayList<>();// Emmagatzema totes les descripcions

    ArrayList<Variable> taulaDeVariables = new ArrayList<>();// Emmagatzema totes les descripcions
    ArrayList<Procediment> taulaDeProcediments = new ArrayList<>();// Emmagatzema totes les dades de subprogrames
    ArrayList<Etiqueta> taulaDeEtiquetes = new ArrayList<>();

    private static int n_temporals = 0;
    private static  int num_procediments = 0;

    private int n_variables_globals = 0;
    int procedimentActual = 0;
    private BufferedWriter fitxerTVariables;
    private BufferedWriter fitxerTProcediments;
    private BufferedWriter fitxerCodiIntermedi;

    public Generador()  {
        try{
            fitxerTVariables = new BufferedWriter(new FileWriter("TaulaDeVariables.txt"));
            fitxerTProcediments = new BufferedWriter(new FileWriter("TaulaDeProcediments.txt"));
            fitxerCodiIntermedi = new BufferedWriter(new FileWriter("CodiIntermedi.txt"));
        }catch (IOException e){
            System.err.println("ERROR: No s'han pogut generar els fitxers de C3A.");
            System.exit(1);
        }

    }

    public Variable novaVar(TSB tipusVar,boolean esCte)  {

        Variable var = new Variable("t"+n_temporals,tipusVar,procedimentActual,esCte, 0 );
        taulaDeVariables.add(var);
        n_temporals++;

        try {
            fitxerTVariables.write(var.registre()+"\n");
        } catch (IOException e) {
            System.err.println("ERROR: No s'han pogut generar els fitxers de C3A.");
            System.exit(1);
        }
        return var;

    }

    public Variable novaVar(TSB tipusVar, String nom, boolean esCte, int dim, String [] vdim)  {
        Variable var = new Variable(nom,tipusVar,procedimentActual,esCte,dim);
        taulaDeVariables.add(var);
        try {
            fitxerTVariables.write(var.registre()+"\n");
        } catch (IOException e) {
            System.err.println("ERROR: No s'han pogut generar els fitxers de C3A.");
            System.exit(1);
        }
        return var;
    }

    public void genera(TipusInstruccionsCTA tipusIns, String desti, String opPrimer, String opSegon ){
        Instruccio instruccio = new Instruccio(tipusIns,opPrimer,opSegon,desti,procedimentActual);

        if (procedimentActual == 0){
            llistaDeInstruccions.add(n_variables_globals++,instruccio);
        }else {
            llistaDeInstruccions.add(instruccio);
        }

        try {
            fitxerCodiIntermedi.write(instruccio+"\n");
        } catch (IOException e) {
            System.err.println("ERROR: No s'han pogut generar els fitxers de C3A.");
            System.exit(1);
        }

      
    }

    public ArrayList<Instruccio> getLlistaDeInstruccions(){
        return llistaDeInstruccions;
    }
    
    public Parametre nouPar(TSB tipusPar, String nom, int dim){
        Parametre par = new Parametre(nom,tipusPar, dim);
        return par;
    }

    public Procediment nouProcediment(String nom,int caller, TSB retorna, ArrayList<Parametre> parametres) {
        num_procediments++;
        procedimentActual = num_procediments;

        Procediment procediment = new Procediment(nom,procedimentActual,caller,retorna, parametres);
        taulaDeProcediments.add(procediment);
        try {
            fitxerTProcediments.write(procediment+"\n");
        } catch (IOException e) {
            System.err.println("ERROR: No s'han pogut generar els fitxers de C3A.");
            System.exit(1);
        }
        return procediment;
    }

    // Cierre de un procedimiento.
    public void closeProcedimiento(int caller) {
        procedimentActual = caller;
    }

    public Etiqueta novaEtiqueta(){
        Etiqueta e = new Etiqueta();
        this.taulaDeEtiquetes.add(e);
        return e;
    }

   

    
    public boolean conteVariable(String SVar) {
        for(var x: taulaDeVariables) {
            if(x.nom.equals(SVar) ) {
                return true;
            }
        }
        return false;
    }
    public boolean procConteVariable(String SVar) {

        for(var x: taulaDeVariables) {
            if(x.nom.equals(SVar) && x.proc == procedimentActual) {
                return true;
            }
        }
        return false;
    }


    public void mostrarTVariables() {
        for(var x: taulaDeVariables) {
            System.out.println(x);
        }
    }

    public int getProcedimentActual(){
        return procedimentActual;
    }
    
    public Procediment getProcediment(String nomProcediment){
        for (Procediment taulaDeProcediment : taulaDeProcediments) {
            if (taulaDeProcediment.nomOG.equals(nomProcediment)) {
                return taulaDeProcediment;
            }
        }
        return null;
    }
    
    public Variable getVariable(String nomVariable){
        for (Variable taulaDeVariable : taulaDeVariables) {
            if (taulaDeVariable.nom.equals(nomVariable)) {
                return taulaDeVariable;
            }
        }
        return null;
    }
    
    public void ocupacio(){
        for (int i = 0; i < taulaDeProcediments.size(); i++) {
            taulaDeProcediments.get(i).ocupVL=0;
            taulaDeProcediments.get(i).despVar=-8;
        }
        for (int i = 0; i < taulaDeVariables.size(); i++) { 
            Variable variable = taulaDeVariables.get(i);
            Procediment proc = getProcediment(taulaDeProcediments.get(variable.proc).nom);
            if (variable.esCte) { //Les constants no ocupen memoria
                System.out.println("Variable " +variable.nom +" ,Tsb"+variable.tsb);
                continue; 
            }
            if(!variable.esParam && variable.proc != procedimentActual){
                proc.ocupVL+=bytesVar(variable.tsb);
                //proc.ocupVL+=variable.ocupacioString; //Guardam el bloc
                proc.despVar-=bytesVar(variable.tsb);
                //proc.despVar-=variable.ocupacioString;
                variable.desp=proc.despVar;
                System.out.println("Variable "+ variable.nom + " del subprograma " + proc.nom + " té mida "+ bytesVar(variable.tsb) 
                        +" de X offset: "+ variable.desp);
            }else if (variable.esParam){
                System.out.println("Parametre "+ variable.nom + " del subprograma " + proc.nom + " té mida "+ bytesVar(variable.tsb) 
                        +" de X offset: "+ variable.desp);
            } 
        }
    }
    
    public static int bytesVar(TSB t) {
        switch (t) {
            case INTEGER:
            case BOOLEAN:
                return 1;
            default:
                return 0;
        }
    }
    public void closeFitxers()  {
        try {
            fitxerTVariables.close();
            fitxerTProcediments.close();
            fitxerCodiIntermedi.close();
        }catch (IOException e){
            System.err.println("ERROR: No s'han pogut generar els fitxers de C3A.");
            System.exit(1);
        }
    }


}