package Codi3A;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import compilador.sintactic.Parser;
import compilador.ts.*;

public class Ensamblador {

    ArrayList<String> codi68k;

    ArrayList<Instruccio> c3a;
    ArrayList<String> vars = new ArrayList<>();
    int indxVars = 1;

    BufferedWriter bw ;
    Generador generador;
    TaulaSimbols ts;
    Parser p;
    String procediment_actual = "";
    
    boolean primerFunc = false;



    
    public Ensamblador(ArrayList<Instruccio> codi3a, Parser p) throws TaulaSimbols.ErrorUsuariEntradaNoDefinida {
        this.c3a = codi3a;
        this.codi68k = new ArrayList<>();
        this.p = p;
        this.ts = p.taulaSimbols;
        this.generador = p.generador;
        
        comprovaMain();
        
        try{
            bw= new BufferedWriter(new FileWriter("codiEnsamblador.X68"));
        }catch (IOException e){ 
            System.err.println(e.getLocalizedMessage());
            System.exit(1);
        }
    }
   /* private void etiquetar(Instruccio i){
        if (instruccionsEtiquetablesTots.contains(i.getTipus())){
            i.setOp1(i.getOp1()+"_"+procediment_actual);
            i.setOp2(i.getOp2()+"_"+procediment_actual);
            i.setDesti(i.getDesti()+"_"+procediment_actual);
        }else if (instruccionsEtiquetablesDesti.contains(i.getTipus())){
            i.setDesti(i.getDesti()+"_"+procediment_actual);
        }
    }*/
    
    private void comprovaMain() throws TaulaSimbols.ErrorUsuariEntradaNoDefinida {
        boolean conteMain = false;
                
        for(Procediment proc: p.generador.taulaDeProcediments) {
            if(proc.nomOG.equals("main")) {
                
                Descripcio dMain = ts.consultarID("main");
                
                if(((DProcediment) dMain).retorn == TSB.NULL) {
                    conteMain = true;
                    break;   
                }
            }   
        }
        
        if(!conteMain) {
            try {
                throw new MainNoDefinit();
            } catch (MainNoDefinit e) {
                System.err.println(e.getLocalizedMessage());
                System.exit(1);
            }
        }        
    }
    
    public class MainNoDefinit extends Exception {
        public MainNoDefinit() {
            super("ERROR: Procediment main no definit correctament.");
        }
    }

    public void generaCodi() throws TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariTipusIncompatible {
        // Inicializacion del codigo ensamblador.
        codi68k.clear();
        codi68k.add("\tORG $1000\n");
        codi68k.add("; Constants.");
        codi68k.add("STR_INT DC.W 'Introdueix un int > ',0");
        codi68k.add("STR_OUT DC.W 'Int mostrat > ',0");
        codi68k.add("STR_ERR DC.W 'ERROR en temps d execucio.',0");
        codi68k.add("\nSTART:");
        
        
        // Traduccion de las instrucciones junto con un comentario de esta antes de traducir.
        for (int i = 0; i < c3a.size(); i++) {
            codi68k.add(";   " + c3a.get(i).toString());

            if(!primerFunc && c3a.get(i).cta ==  TipusInstruccionsCTA.SKIP) {
                codi68k.add("; Botam al bloc de codi que representa el nostre main.");
                codi68k.add("\tJSR main");
                codi68k.add("\tJMP end");
                primerFunc = true;
            }
            
          //  etiquetar(c3a.get(i));
            this.identificarOperacio(c3a.get(i));
        }
        
        afegirSubrutines();
        codi68k.add("error");
        codi68k.add("\tJSR PRINT_ERR");
        codi68k.add("end:");

        codi68k.add("\tSIMHALT");
        codi68k.add("\tEND START");
        
        try {
            for (int i = 0; i < codi68k.size(); i++) {
                bw.write(codi68k.get(i)+"\n");

            }
            bw.close();
        } catch (IOException e) {System.err.println("ERROR D'ESCRITURA: "+e.getLocalizedMessage());}
    }

    private void identificarOperacio(Instruccio instruccio) throws TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariTipusIncompatible {

        switch (instruccio.getTipus()) {
            case COPY:
                definirVariableEnsamblador(instruccio);
                copy(instruccio);
                break;
            case ADD:
                definirVariableEnsamblador(instruccio);
                suma(instruccio);
                break;
            case SUB:
                definirVariableEnsamblador(instruccio);
                resta(instruccio);
                break;
            case PROD:
                definirVariableEnsamblador(instruccio);
                mult(instruccio);
                break;
            case DIV:
                definirVariableEnsamblador(instruccio);
                div(instruccio);
                break;

            case NEG:
                definirVariableEnsamblador(instruccio);
                neg(instruccio);
                break;
            case AND:
                definirVariableEnsamblador(instruccio);
                and(instruccio);
                break;
            case OR:
                definirVariableEnsamblador(instruccio);
                or(instruccio);
                break;
            case NOT:
                definirVariableEnsamblador(instruccio);
                not(instruccio);
                break;
            /* COSES D'ARRAY */
            case IND_VAL:
                ind_val(instruccio);
                break;
            case IND_ASS:
                ind_ass(instruccio);
                break;
            case SKIP:
                skip(instruccio);
                break;
            case GOTO:
                goTo(instruccio);
                break;
            case IFEQ:
                ifEq(instruccio);
                break;
            case IFNEQ:
                ifNotEq(instruccio);
                break;
            case IFGT:
                ifGreaterThan(instruccio);
                break;
            case IFLT:
                ifLessThan(instruccio);
                break;
            case IFGTE:
                ifGreaterThanEq(instruccio);
                break;
            case IFLTE:
                ifLessThanEq(instruccio);
                break;
            case CALL:
                call(instruccio);
                break;
            case PMB:
               pmb(instruccio);
                break;
            case RTN:
                rtn(instruccio);
                break;
            case OUTT:
                mostrarPantalla(instruccio);
                break;
            case INT:
                entrarTeclat(instruccio);
                break;
            case PARAM_S:
                param(instruccio);
                break;
            default:
                // handle invalid instruction
                break;
        }
    }

    private void copy(Instruccio i){

        codi68k.add("\tMOVE.W "+traduir(i.getOp1())+","+i.getDesti());
    }
    
    private void suma(Instruccio i) {       
        codi68k.add("\tMOVE.W " + traduir(i.getOp1())+", D0");
        codi68k.add("\tMOVE.W " + traduir(i.getOp2())+", D1");
        codi68k.add("\tADD.W D0, D1");
        codi68k.add("\tMOVE.W D1, "+i.getDesti());
    }
    
    private void resta(Instruccio i) {
        codi68k.add("\tMOVE.W " + traduir(i.getOp1())+", D0");
        codi68k.add("\tMOVE.W " + traduir(i.getOp2())+", D1");
        codi68k.add("\tSUB.W D0, D1");
        codi68k.add("\tMOVE.W D1, "+i.getDesti());
    }
    
    private void div(Instruccio i) {
        codi68k.add("\tMOVE.W " + traduir(i.getOp1())+", D0");
        codi68k.add("\tMOVE.W " + traduir(i.getOp2())+", D1");
        codi68k.add("\tDIV.W D0, D1");
        codi68k.add("\tMOVE.W D1, "+i.getDesti());
    }
    
    private void mult(Instruccio i) {
        codi68k.add("\tMOVE.W " + traduir(i.getOp1())+", D0");
        codi68k.add("\tMOVE.W " + traduir(i.getOp2())+", D1");
        codi68k.add("\tMULS.W D0, D1");
        codi68k.add("\tMOVE.W D1,"+i.getDesti());
    }

    public void goTo(Instruccio i){
        codi68k.add("\tJMP "+i.getDesti());
    }

    private void neg(Instruccio i){
        codi68k.add("\tMOVE.W #" + i.getOp1()+", D0");
        codi68k.add("\tNEG.W D0");
        codi68k.add("\tMOVE.W D0, "+i.getDesti());
    }


    private void not(Instruccio i){
        codi68k.add("\tMOVE.W #" + i.getOp1()+", D0");
        codi68k.add("\tNOT.W D0");
        codi68k.add("\tMOVE.W D0, "+i.getDesti());  
    }
    
    private void ind_val(Instruccio i){
        definirVariableEnsamblador(i);
            codi68k.add("\t LEA "+ i.getOp1()+", A0" );
            codi68k.add("\t ADD.W "+ traduir(i.getOp2())+",A0");
            codi68k.add("\t ADD.W "+ traduir(i.getOp2())+",A0");
            codi68k.add("\t MOVE.W (A0),"+ traduir(i.getDesti()));       
    }
    
    private void ind_ass(Instruccio i){
        if(i.getOp1() == null){
            
                vars.add(i.getDesti());
                codi68k.add(indxVars,i.getDesti()+" DS.W "+ traduir(i.getOp2()));
                indxVars++;
            
        }else{
            codi68k.add("\t LEA "+ i.getDesti()+", A0" );
            codi68k.add("\t ADD.W "+ traduir(i.getOp2())+",A0");
            codi68k.add("\t ADD.W "+ traduir(i.getOp2())+",A0");
            codi68k.add("\t MOVE.W ("+ traduir(i.getOp1())+"),(A0)");
        }
    }    
    
    private void and(Instruccio i){
        codi68k.add("\tMOVE.W " + traduir(i.getOp1())+", D0");
        codi68k.add("\tMOVE.W " + traduir(i.getOp2())+", D1");
        codi68k.add("\tAND.W D0, D1");
        codi68k.add("\tMOVE.W D1, "+i.getDesti());
    }
    
    private void or(Instruccio i){
        codi68k.add("\tMOVE.W " + traduir(i.getOp1())+", D0");
        codi68k.add("\tMOVE.W " + traduir(i.getOp2())+", D1");
        codi68k.add("\tOR.W D0, D1");
        codi68k.add("\tMOVE.W D1, "+i.getDesti());
    }
   
    private void ifEq(Instruccio i) {
        codi68k.add("\tMOVE.W " + traduir(i.getOp1())+", D0");
        codi68k.add("\tMOVE.W " + traduir(i.getOp2())+", D1");
        codi68k.add("\tCMP D1, D0");
        // EL BOT ÉS A UNA ETIQUETA.
        codi68k.add("\tBEQ "+i.getDesti());
    }
    
    private void ifNotEq(Instruccio i) {
        codi68k.add("\tMOVE.W " + traduir(i.getOp1())+", D0");
        codi68k.add("\tMOVE.W " + traduir(i.getOp2())+", D1");
        codi68k.add("\tCMP D1, D0");
        // EL BOT ÉS A UNA ETIQUETA.
        codi68k.add("\tBNE "+i.getDesti());
    }
    
    private void ifGreaterThan(Instruccio i) {
        codi68k.add("\tMOVE.W " + traduir(i.getOp1())+", D0");
        codi68k.add("\tMOVE.W " + traduir(i.getOp2())+", D1");
        codi68k.add("\tCMP D1, D0");
        // EL BOT ÉS A UNA ETIQUETA.
        codi68k.add("\tBGT "+i.getDesti());
    }
    
    private void ifGreaterThanEq(Instruccio i) {
        codi68k.add("\tMOVE.W " + traduir(i.getOp1())+", D0");
        codi68k.add("\tMOVE.W " + traduir(i.getOp2())+", D1");
        codi68k.add("\tCMP D1, D0");
        // EL BOT ÉS A UNA ETIQUETA.
        codi68k.add("\tBGE "+i.getDesti());
    }
    
    private void ifLessThan(Instruccio i) {
        codi68k.add("\tMOVE.W " + traduir(i.getOp1())+", D0");
        codi68k.add("\tMOVE.W " + traduir(i.getOp2())+", D1");
        codi68k.add("\tCMP D1, D0");
        // EL BOT ÉS A UNA ETIQUETA.
        codi68k.add("\tBLT "+i.getDesti());
    }
    
    private void ifLessThanEq(Instruccio i) {
        codi68k.add("\tMOVE.W " + traduir(i.getOp1())+", D0");
        codi68k.add("\tMOVE.W " + traduir(i.getOp2())+", D1");
        codi68k.add("\tCMP D1, D0");
        // EL BOT ÉS A UNA ETIQUETA.
        codi68k.add("\tBLE "+i.getDesti());
    }
    
    //Cream l'etiqueta on farem els bots.
    public void skip(Instruccio i) {
        codi68k.add(i.desti+":");
    }

    private void call(Instruccio i) throws TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariTipusIncompatible {
        if (i.getDesti()!=null){
            definirVariableEnsamblador(i);
        }

        codi68k.add("\tJSR "+i.getOp1());


        String parametes = i.getOp1().substring(0,i.getOp1().lastIndexOf("_")-1);

        ArrayList <String>nomArguments = p.taulaSimbols.obtenirNomArguments(parametes);

        if(!nomArguments.isEmpty()) {
            codi68k.add("\tADDA.W #"+(nomArguments.size()*2)+",A7");
        }
        if (i.getDesti()!=null){
            codi68k.add("\tMOVE.W D5,"+i.getDesti());
        }




        //treure es return

    }
    public void pmb(Instruccio i) throws TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariTipusIncompatible {
        String etiqueta =i.getDesti().substring(i.getDesti().lastIndexOf("_"));
        String desti = i.getDesti().substring(0,i.getDesti().lastIndexOf("_")-1);

        ArrayList <String>nomArguments = p.taulaSimbols.obtenirNomArguments(desti);

        Procediment proc = p.generador.getProcediment(desti);
        if(!nomArguments.isEmpty()) {
            int stackPointer = 4;

            /* REVISAR SI ÉS CERT AIXÒ.
            //Si te retorn el ho haurem de tenir en compte.
            if(proc.retorn != null) {
                stackPointer = stackPointer + 2;
            }
            */
            //Definim les variables amb el seu nivell únic.
            for(int idx = 0; idx < nomArguments.size(); idx++) {
                //codi68k.add(0,nomArguments.get(idx)+""+proc.nProcediment+" DS.W 1");
                //codi68k.add("\tMOVE.W "+stackPointer+"(A7),"+nomArguments.get(idx)+""+proc.nProcediment);
                vars.add(nomArguments.get(idx)+etiqueta);
                codi68k.add(indxVars,nomArguments.get(idx)+etiqueta+" DS.W 1");
                codi68k.add("\tMOVE.W "+stackPointer+"(A7),"+nomArguments.get(idx)+etiqueta);
                indxVars++;
                stackPointer = stackPointer+2;

            }

        }
    }

    private void param(Instruccio i){
        codi68k.add("\tMOVE.W "+traduir(i.getDesti())+",-(A7)");
    }
    private void rtn(Instruccio i){
        if (i.getDesti() != null){

            codi68k.add("\tMOVE.W "+i.getDesti()+",D5");
        }
        codi68k.add("\tRTS");
    }
    
    private String traduir(String op) {
        if (op.contains("$")){
            return op;
        }
        String etiqueta = "";
        if (op.contains("_")){
             etiqueta=op.substring(op.lastIndexOf("_"));
            op = op.substring(0,op.lastIndexOf("_"));
        }

        if(!generador.conteVariable(op)) {

            return "#"+op;
        } else {
            return op+etiqueta;
        }
    }
    
    private void mostrarPantalla(Instruccio i) {
        codi68k.add("\tMOVE.W " + i.getDesti() + ",-(A7)");
        codi68k.add("\tJSR PRINT_OUT");
        codi68k.add("\tJSR PRINTINT");
        codi68k.add("\tJSR PRINTNL");
        codi68k.add("\tADDA.W #2,A7");
    }

    private void entrarTeclat(Instruccio i) {

        codi68k.add("\tJSR PRINT_IN");
        codi68k.add("\tJSR GETINT");
        codi68k.add("\tMOVE.W D7,"+traduir(i.getDesti()));
    }

    public void afegirSubrutines() {
        codi68k.add("PRINTINT:");
        codi68k.add("\tMOVEM.W D0-D1,-(A7)");
        codi68k.add("\tCLR.L D1");
        codi68k.add("\tMOVE.W 8(A7),D1");
        codi68k.add("\tEXT.L D1");
        codi68k.add("\tMOVE #3,D0");
        codi68k.add("\tTRAP #15");
        codi68k.add("\tMOVEM.W (A7)+,D0-D1");
        codi68k.add("\tRTS");
        codi68k.add("");
        codi68k.add("GETINT:");
        codi68k.add("\tMOVEM.W D0-D1,-(A7)");
        codi68k.add("\tMOVE.W #4,D0");
        codi68k.add("\tCLR.L D1");
        codi68k.add("\tTRAP #15");
        codi68k.add("\tMOVE.W D1,D7");
        codi68k.add("\tMOVEM.W (A7)+,D0-D1");
        codi68k.add("\tRTS");
        codi68k.add("");
        codi68k.add("PRINTNL:");
        codi68k.add("\tMOVE.W D0,-(A7)");
        codi68k.add("\tMOVE.W A1,-(A7)");
        codi68k.add("\tMOVE.W #13,D0");
        codi68k.add("\tMOVE.L #11,D0");
        codi68k.add("\tMOVE.W #$00FF,D1");
        codi68k.add("\tTRAP #15");
        codi68k.add("\tADD.W #1,D1");
        codi68k.add("\tAND.W #$00FF,D1");
        codi68k.add("\tTRAP #15");
        codi68k.add("\tMOVE.W (A7)+,A1");
        codi68k.add("\tMOVE.W (A7)+,D0");
        codi68k.add("\tRTS");
        // subrutines per mostrar strings predefinides.
        codi68k.add("PRINT_IN:");
        codi68k.add("\tMOVE.W D0,-(A7)");
        codi68k.add("\tMOVE.W A1,-(A7)");
        codi68k.add("\tLEA STR_INT,A1");
        codi68k.add("\tMOVE.W #14,D0");
        codi68k.add("\tTRAP #15");
        codi68k.add("\tMOVE.W (A7)+,A1");
        codi68k.add("\tMOVE.W (A7)+,D0");
        codi68k.add("\tRTS");
        codi68k.add("PRINT_OUT:");
        codi68k.add("\tMOVE.W D0,-(A7)");
        codi68k.add("\tMOVE.W A1,-(A7)");
        codi68k.add("\tLEA STR_OUT,A1");
        codi68k.add("\tMOVE.W #14,D0");
        codi68k.add("\tTRAP #15");
        codi68k.add("\tMOVE.W (A7)+,A1");
        codi68k.add("\tMOVE.W (A7)+,D0");
        codi68k.add("\tRTS");
        codi68k.add("PRINT_ERR:");
        codi68k.add("\tMOVE.W D0,-(A7)");
        codi68k.add("\tMOVE.W A1,-(A7)");
        codi68k.add("\tLEA STR_ERR,A1");
        codi68k.add("\tMOVE.W #13,D0");
        codi68k.add("\tTRAP #15");
        codi68k.add("\tMOVE.W (A7)+,A1");
        codi68k.add("\tMOVE.W (A7)+,D0");
        codi68k.add("\tRTS");
    }

    private void definirVariableEnsamblador(Instruccio instruccio){
        if (!vars.contains(instruccio.getDesti())){
            vars.add(instruccio.getDesti());
            codi68k.add(indxVars,instruccio.getDesti()+" DS.W 1");
            indxVars++;
        }
    }


}