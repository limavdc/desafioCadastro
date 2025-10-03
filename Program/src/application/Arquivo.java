package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Arquivo {



    public String ler(String caminhoArquivo) throws Exception {
        String conteudo = "";


        try (BufferedReader leitor = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha = leitor.readLine();

            while (linha != null) {
                conteudo += linha;
                linha = leitor.readLine();

                if(linha != null) {
                    conteudo += "\n";
                }
            }
        } catch (Exception erro) {
            //System.out.println("Error: " + e.getMessage());
            throw erro;
        }

        return conteudo;
    }
}


