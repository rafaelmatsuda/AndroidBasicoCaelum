package br.com.caelum.support;

import org.json.JSONException;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import br.com.caelum.converter.AlunoConverter;
import br.com.caelum.model.Aluno;

public class Webclient {

    public String post(List<Aluno> alunos){
        try {

            URL url = new URL("https://www.caelum.com.br/mobile");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept","application/json");

            con.setDoInput(true);
            con.setDoOutput(true);

            PrintStream saida = new PrintStream(con.getOutputStream());
            saida.println(new AlunoConverter().toJSON(alunos));

            con.connect();

            Scanner retornoApi = new Scanner(con.getInputStream());
            String texto = retornoApi.next();

            return texto;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }
}
