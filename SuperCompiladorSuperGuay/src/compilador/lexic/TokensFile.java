package compilador.lexic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TokensFile {

    BufferedWriter tokens_file;

    public TokensFile (){
        try {
            tokens_file = new BufferedWriter( new FileWriter("tokens.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToken(String token){
        try {
            tokens_file.write(token+"\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close(){
        try {
            tokens_file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}