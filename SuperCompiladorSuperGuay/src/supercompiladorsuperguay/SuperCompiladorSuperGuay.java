package supercompiladorsuperguay;

import Codi3A.Ensamblador;
import compilador.lexic.Scanner;
import compilador.sintactic.Parser;

import java.io.*;

public class SuperCompiladorSuperGuay {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Reader input;
        BufferedWriter errorFile = null;
        java.util.Scanner sc = new java.util.Scanner(System.in);
        Parser parser = null;
        try {
            errorFile = new BufferedWriter(new FileWriter("errors.txt"));
            System.out.println("Benvingut a nes nostre supercompiladorsuperguay");
            System.out.println("Que vols fer?\n\t1) Escriure aqui el teu programa\n\t2) Executar des d'un fitxer");
            System.out.print("Opció a elegir > ");
             int opcio = 0;

            opcio = sc.nextInt();


            
            if (opcio == 2) {
                System.out.print("Quin és el path absolut de l'arxiu a tractar ? > ");
                String path = sc.next();
                
                if(path.startsWith("\"") && path.endsWith("\"")) {
                    path = path.substring(1, path.length()-1);
                }
                
                input = new BufferedReader(new FileReader(path));
            } else if (opcio ==1){
                System.out.println("Escriu el teu programa:");
                System.out.print(">>> ");
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                input = new CharArrayReader(in.readLine().toCharArray());
            }else {
                System.err.println("Opció incorrecta!");
                System.exit(1);
            }
            
            System.out.println("** COMENÇA EL PROCÉS DE COMPILACIÓ. **");
            Scanner scanner = new Scanner(input);
            parser = new Parser(scanner);
            parser.setScanner(scanner);
            parser.parse();
            parser.taulaSimbols.guardarTS();
            parser.tokensFile.close();
            parser.SB.C3A();
            parser.generador.closeFitxers();
            Ensamblador ensamblador = new Ensamblador(parser.generador.getLlistaDeInstruccions(),parser);
            ensamblador.generaCodi();
        } catch(Exception e) {
            String error = "Linea: "+ (parser.getLinea()-1)+" "+e.getLocalizedMessage();
            errorFile.write(error+"\n");
            System.err.println(error);
            


        }
        errorFile.close();
        System.out.println("** FI DEL PROCÉS DE COMPILACIÓ. **");
    }    
}
