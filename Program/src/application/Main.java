package application;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception {

        String caminhoArquivo = "C:\\Users\\augus\\IdeaProjects\\desafioCadastro1\\Program\\src\\application\\formulario.txt";

        //Arquivo arquivo = new Arquivo();

        /*
        * Files.readAlllines -> para ler todas as linhas; quando for um arquivo pequeno
        * BufferedReader -> complexo de escrever; ele é otimo para ler arquviso grande;
        * quando precisa ler linha por linha,
         */

        try {;
            Path caminhos = Paths.get(caminhoArquivo);
            List<String> resultadoLista = Files.readAllLines(caminhos);

            String conteudo = String.join("\n", resultadoLista);
            System.out.println(conteudo);
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null,"ERRO DO ARQUIVO");

        }

    }
}