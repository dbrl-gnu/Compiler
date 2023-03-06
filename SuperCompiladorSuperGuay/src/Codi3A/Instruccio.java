package Codi3A;


public class Instruccio {

    public TipusInstruccionsCTA cta;
    public String op1;

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public void setDesti(String desti) {
        this.desti = desti;
    }

    public String op2;
    public String desti;
    public int nProcediment;
    
    public Instruccio(TipusInstruccionsCTA cta, String op1, String op2, String desti, int nProc ) {
        this.cta = cta;
        this.op1 = op1;
        this.op2 = op2;
        this.desti = desti;
        this.nProcediment = nProc;
    }

    @Override
    public String toString() {
        return "[" + this.cta + ", " + this.op1 +  ", " + this.op2 + ", " + this.desti + "]";
    }

    public TipusInstruccionsCTA getTipus(){
        return cta;
    }

    public String getOp1() {
        return op1;
    }

    public String getOp2() {
        return op2;
    }

    public String getDesti() {
        return desti;
    }
}
