/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package entities;

import application.Arquivo;
import entities.enums.TipoPet;
import entities.enums.TipoSexo;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

import static entities.Pet.NAO_INFORMADO;

public class PetService {

    String caminhoArquivo = "C:\\Users\\Augusto\\IdeaProjects\\desafioCadastro1\\Program\\src\\application\\formulario.txt";

    /*
     * Files.readAlllines -> para ler todas as linhas; quando for um arquivo pequeno
     * BufferedReader -> complexo de escrever; ele é otimo para ler arquviso grande;
     * quando precisa ler linha por linha,
     */

    Arquivo arquivo = new Arquivo();
    public static final double PESO_MINIMO = 0.5;
    public static final double PESO_MAXIMO = 60.0;
    public static final int IDADE_MAXIMA = 20;

    public Pet cadastrarNovoPet() throws Exception {
        Scanner sc = new Scanner(System.in);
        Pet pet = new Pet();


        try {
            String conteudo = arquivo.ler(caminhoArquivo);
            String[] linhas = conteudo.split("\n");

            for(String linha : linhas) {
                if (linha.trim().isEmpty()) {
                    continue;
                }
                System.out.println(linha);

                if(linha.toLowerCase().contains("nome")) {
                    while (true) {
                        String nome = sc.nextLine().trim();
                        if (nome.isEmpty()) {
                            pet.setName(NAO_INFORMADO);
                            break;
                        }
                        String[] partes = nome.split("\\s+");
                        if (partes.length >= 2 && validarTexto(nome)) {
                            pet.setName(nome);
                            break;
                        } else {
                            System.out.println("❌ Nome inválido! Digite novamente.");
                        }
                    }

                } else if (linha.toLowerCase().contains("tipo")) {
                    String tipoPet = sc.nextLine();
                    try {
                        TipoPet tipo = TipoPet.valueOf(tipoPet.trim().toUpperCase());
                        pet.setTipoPet(tipo);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Erro: Tipo de pet inválido");
                    }

                } else if (linha.toLowerCase().contains("sexo")) {
                    String tipoSexo = sc.nextLine();
                    try {
                        TipoSexo sexoPet = TipoSexo.valueOf(tipoSexo.toUpperCase());
                        pet.setTipoSexo(sexoPet);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Erro: Tipo de pet inválido");

                    }

                } else if (linha.toLowerCase().contains("endereço")){
                    Endereco endereco = new Endereco();
                    pet.setEndereco(endereco);
                    System.out.println("Qual o número da casa: ");
                    Integer numero = sc.nextInt();
                    sc.nextLine();
                    endereco.setNumero(numero);
                    System.out.println("Qual a cidade: ");
                    String cidade = sc.nextLine();
                    endereco.setCidade(cidade);
                    System.out.println("Qual a Rua: ");
                    String rua = sc.nextLine();
                    endereco.setRua(rua);

                } else if (linha.toLowerCase().contains("idade")) {
                    while(true) {
                        try{
                            Double idade = sc.nextDouble();
                            if(idade > 0 && idade < 1) {
                            } else if (idade < 12) {
                                System.out.println("Convertendo " + idade.intValue() + " meses para anos...");
                                idade = idade / 12;
                            } else if (idade > IDADE_MAXIMA) {
                                System.err.println("⚠️ Idade inválida! Digite novamente.");
                                continue;
                            }

                            pet.setIdade(idade);
                            break;

                        } catch (InputMismatchException e) {
                            System.err.println("⚠️ Entrada inválida! Digite apenas números.");
                            sc.nextLine();
                        }
                    }

                } else if (linha.toLowerCase().contains("peso")) {
                    while (true) {
                        try {
                            Double peso = sc.nextDouble();
                            if (peso > PESO_MAXIMO || peso < PESO_MINIMO) {
                                System.err.println("⚠️ Peso inválido! Digite novamente.");
                                continue;
                            }
                            pet.setPeso(peso);
                            break;

                        } catch (InputMismatchException e) {
                            System.err.println("⚠️ Peso inválido! Digite novamente.");
                            sc.nextLine();
                        }
                    }

                } else if (linha.toLowerCase().contains("raça")) {
                    sc.nextLine();
                    while (true) {
                        String raca = sc.nextLine();
                        if (raca == null || raca.isEmpty()) {
                            pet.setRaca(NAO_INFORMADO);
                        }
                        if (validarTextoRaca(raca)) {
                            pet.setRaca(raca);
                            break;
                        } else {
                            System.out.println("❌ Raça invalida! Digite novamente.");
                        }
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"ERRO DO ARQUIVO");
        }

        return pet;
    }
        public static boolean validarTexto(String nome) {
            if (nome == null) return false;
            nome = nome.trim();
            return nome.matches("^[A-Za-zÀ-ÿ]+(?:[ '-][A-Za-zÀ-ÿ]+)*$");
        }

        public static boolean validarTextoRaca(String raca) {
            if (raca == null) return false;
            raca = raca.trim();
            return raca.matches("^[A-Za-zÀ-ÿ]+(?:[ '-][A-Za-zÀ-ÿ]+)*$");
        }
}
