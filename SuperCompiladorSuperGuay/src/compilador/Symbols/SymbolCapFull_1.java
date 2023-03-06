package compilador.Symbols;

import Codi3A.Generador;
import Codi3A.Parametre;
import compilador.sintactic.Parser;
import compilador.ts.*;

import java.util.ArrayList;

public class SymbolCapFull_1 extends ArbreSymbols{
    Parser p;
    SymbolTVar tVar;
    SymbolCapFull_2 cf2;
    ArrayList<Parametre> parametres = new ArrayList<>();
    
    public SymbolCapFull_1(Parser p, SymbolTVar tVar, SymbolCapFull_2 cf2, int linea) throws TaulaSimbols.ErrorUsuariRedefinicioEntrada, TaulaSimbols.ErrorUsuariParametreJaExistent, TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariTipusIncompatible {
        super(p, "CapFull_1", linea);
        
        p.taulaSimbols.posarID(cf2.id, new DProcediment(tVar.tsb));
        
        SymbolCapFull_2 cfAux = new SymbolCapFull_2(cf2.p,cf2.arg,cf2.id,cf2.linea);
        
        p.taulaSimbols.entrarBloc();
        
        //Agafam els parametres de la funcio
        
        while(cfAux.arg != null) {
            parametres.add(cfAux.arg.parametre);
            cfAux.arg = cfAux.arg.args;
        }
        
        for(int i = 0; i < parametres.size(); i++) {
            Parametre pAux = parametres.get(i);

            p.taulaSimbols.posarParam(cf2.id, pAux.nom, new DVariable(pAux.tsb.toString(), false));
        }


        for(int i = 0; i < parametres.size(); i++) {
            Parametre pAux = parametres.get(i);

            p.taulaSimbols.posarID(pAux.nom, new DVariable(pAux.tsb.toString(), false));
        }
        
        this.p = p;
        this.tVar = tVar;
        this.cf2 = cf2;
    }
    
    public String C3A() {
         TSB tsb = TSB.NULL;
        switch(tVar.tipus){
            case "int" -> tsb = TSB.INTEGER;
            case "boolean" -> tsb = TSB.BOOLEAN;
        }

        p.generador.nouProcediment(cf2.id,p.generador.getProcedimentActual(), tsb, parametres);
        for (int i = 0; i < parametres.size(); i++) {
            p.generador.novaVar(parametres.get(i).tsb,parametres.get(i).nom,false,0,null);
        }
            cf2.C3A();

       
        return "";
    }
}
