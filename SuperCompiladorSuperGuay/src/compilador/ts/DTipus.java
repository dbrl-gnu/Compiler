package compilador.ts;

public class DTipus extends Descripcio {
    public TSB tsb;
    // El nom no el guardam perque estara dins bloc

    public DTipus (TSB tsb) {
        this.tsb = tsb;
    }

    @Override
    public String toString() {
        return "DTipus: TSB = " + tsb;
    }

}