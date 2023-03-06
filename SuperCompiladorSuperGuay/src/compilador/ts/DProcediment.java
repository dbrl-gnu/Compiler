package compilador.ts;

import Codi3A.Procediment;

public class DProcediment extends Descripcio{
    private static int incr = 0;
    public TSB retorn ; // Sempre sera null (void)
    int numProcediment;
    public Procediment subprograma;

    public DProcediment (TSB retu) {
        this.numProcediment = incr++;
        retorn = retu;
    }

    @Override
    public String toString() {
        return "- Procediment: nProcediment = " + numProcediment + " | tRet = " + retorn;
    }
}