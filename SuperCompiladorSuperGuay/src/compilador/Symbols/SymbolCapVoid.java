package compilador.Symbols;

import Codi3A.Generador;
import Codi3A.Parametre;
import compilador.sintactic.Parser;
import compilador.ts.DProcediment;
import compilador.ts.DVariable;
import compilador.ts.TSB;
import compilador.ts.TaulaSimbols;

import java.util.ArrayList;

public class SymbolCapVoid extends ArbreSymbols{
    Parser p;
    SymbolCapFull_2 cf2;
    ArrayList<Parametre> parametres = new ArrayList<>();
    
    public SymbolCapVoid(Parser p, SymbolCapFull_2 cf2, int linea) throws TaulaSimbols.ErrorUsuariRedefinicioEntrada, TaulaSimbols.ErrorUsuariParametreJaExistent, TaulaSimbols.ErrorUsuariEntradaNoDefinida, TaulaSimbols.ErrorUsuariTipusIncompatible {
        super(p, "CapVoid", linea);
        
        p.taulaSimbols.posarID(cf2.id, new DProcediment(TSB.NULL));
        
        SymbolCapFull_2 cfAux = new SymbolCapFull_2(cf2.p,cf2.arg,cf2.id,cf2.linea);

        p.taulaSimbols.entrarBloc();
        
        //Agafam els parametres
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
            p.taulaSimbols.posarID( pAux.nom, new DVariable(pAux.tsb.toString(), false));
        }
        
        this.p = p;
        this.cf2 = cf2;
        
    }
    
    public String C3A()  {
         p.generador.nouProcediment(cf2.id,p.generador.getProcedimentActual(), TSB.NULL, parametres);
        for (int i = 0; i < parametres.size(); i++) {
            p.generador.novaVar(parametres.get(i).tsb,parametres.get(i).nom,false,0,null);
        }

       cf2.C3A();
        return "";
    }
}
