
package compilador.Symbols;

import compilador.sintactic.Parser;

/**
 *
 * @author 34601
 */
public class M1 extends ArbreSymbols{

    public M1(Parser p, int linea) {
        super(p, "M",linea);
        p.taulaSimbols.entrarBloc();
    }

}
