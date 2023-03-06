package compilador.Symbols;

import Codi3A.Generador;
import Codi3A.Parametre;
import Codi3A.TipusInstruccionsCTA;
import Codi3A.Variable;
import compilador.sintactic.Parser;
import compilador.ts.*;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SymbolCridada extends ArbreSymbols{
    String id;
    Parser p;
    SymbolArgCrid a;
    ArrayList<Parametre> parametres = new ArrayList<>();
    //sa cridada no te arguments
    public SymbolCridada(Parser p, String id, int linea) throws ErrorNArgsCridada, TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariTipusIncompatible {
        super(p, "Cridada", linea);
        this.id = id;
        
        this.p = p;
       
        p.taulaSimbols.consultarID(id);
        Descripcio descripcio =  p.taulaSimbols.consultarID(id);
        if (descripcio instanceof DProcediment){
            //DProcediment dProcediment = (DProcediment) descripcio;
            ArrayList tipusArguments = p.taulaSimbols.obtenirTipusArguments(id);//Arguments funcio declarada
            if (!tipusArguments.isEmpty()){

                    throw new ErrorNArgsCridada();

            }

        }
        
    }
        //sa cridada te arguments
        // ID -> Funcio a cridar A -> Arguments subministrats.
        public SymbolCridada(Parser p, String id, SymbolArgCrid a, int linea) throws ErrorArgsCridada, ErrorNArgsCridada, TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariTipusIncompatible {
        super(p, "Cridada",linea);
        this.id = id;
        
        this.p = p;
        this.a = a;

        Descripcio descripcio =  p.taulaSimbols.consultarID(id);
        if (descripcio instanceof DProcediment) {
            ArrayList tipusArguments = p.taulaSimbols.obtenirTipusArguments(id);

            SymbolArgCrid argAux = new SymbolArgCrid(p,a.a,a.o,a.linea);
            
            //Treim els arguments per controlar a nivell semantic si és correcte
            parametres.add(argAux.parametre);
            while (argAux.a != null) {
                parametres.add(0, argAux.a.parametre);
                argAux = argAux.a;
            }

            if (tipusArguments.size() != parametres.size()) {
                    throw new ErrorNArgsCridada();

            }

            for (int i = 0; i < parametres.size(); i++) {
                if (parametres.get(i).tsb != tipusArguments.get(i)) {

                    throw new ErrorArgsCridada();

                }

            }
        }
    }
        
        public String C3A() throws ErrorTipusVar, ErrorNDimArr{

            if(a != null) {

                for (Parametre parametre : parametres) {
                    if (p.generador.procConteVariable(parametre.nom)){
                        p.generador.genera(TipusInstruccionsCTA.PARAM_S, parametre.nom+ "_" + p.generador.getProcedimentActual(), null, null);
                    }else{
                        p.generador.genera(TipusInstruccionsCTA.PARAM_S, parametre.nom+ "_0", null, null);
                    }

                }
                if(p.generador.getProcediment(id).retorn == TSB.NULL){//el procediment es void
                    p.generador.genera(TipusInstruccionsCTA.CALL,null, id + "__" + p.generador.getProcediment(id).nProcediment, null);
                }else{//el procediment es full
                    Variable varAux = p.generador.novaVar(p.generador.getProcediment(id).retorn,false);
                    p.generador.genera(TipusInstruccionsCTA.CALL,varAux.nom, id + "__" + p.generador.getProcediment(id).nProcediment, null);
                    return varAux.nom;
                }


            }


            return "";
        }
}
