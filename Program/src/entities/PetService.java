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
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.Normalizer;
import java.nio.file.Files;

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
    Scanner sc = new Scanner(System.in);
    Pet pet = new Pet();

    public Pet cadastrarNovoPet() throws Exception {

        try {
            String conteudo = arquivo.ler(caminhoArquivo);
            String[] linhas = conteudo.split("\n");

            for (String linha : linhas) {
                if (linha.trim().isEmpty()) {
                    continue;
                }
                System.out.println(linha);


                if (linha.toLowerCase().contains("nome")) {
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
                            System.out.println("Nome inválido! Digite novamente.");
                        }
                    }


                } else if (linha.toLowerCase().contains("tipo")) {
                    while (true) {
                        String tipoPet = sc.nextLine();
                        try {
                            TipoPet tipo = TipoPet.valueOf(tipoPet.trim().toUpperCase());
                            pet.setTipoPet(tipo);
                            break;

                        } catch (IllegalArgumentException e) {
                            System.err.println("Tipo de pet inválido digite novamente");
                        }
                    }

                } else if (linha.toLowerCase().contains("sexo")) {
                    while (true) {
                        String tipoSexo = sc.nextLine();
                        try {
                            TipoSexo sexoPet = TipoSexo.valueOf(tipoSexo.toUpperCase());
                            pet.setTipoSexo(sexoPet);
                            break;

                        } catch (IllegalArgumentException e) {
                            System.err.println("Sexo do pet inválido digite novamente");
                        }
                    }

                } else if (linha.toLowerCase().contains("endereço")) {
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
                    while (true) {
                        try {
                            Double idade = sc.nextDouble();
                            if (idade > 0 && idade < 1) {
                            } else if (idade < 12) {
                                System.out.println("Convertendo " + idade.intValue() + " meses para anos...");
                                idade = idade / 12;
                            } else if (idade > IDADE_MAXIMA) {
                                System.err.println("Idade inválida! Digite novamente.");
                                continue;
                            }

                            pet.setIdade(idade);
                            break;

                        } catch (InputMismatchException e) {
                            System.err.println("Entrada inválida! Digite apenas números.");
                            sc.nextLine();
                        }
                    }

                } else if (linha.toLowerCase().contains("peso")) {
                    while (true) {
                        try {
                            Double peso = sc.nextDouble();
                            if (peso > PESO_MAXIMO || peso < PESO_MINIMO) {
                                System.err.println("Peso inválido! Digite novamente.");
                                continue;
                            }
                            pet.setPeso(peso);
                            break;

                        } catch (InputMismatchException e) {
                            System.err.println("Peso inválido! Digite novamente.");
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
                            System.out.println("Raça invalida! Digite novamente.");
                        }

                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO DO ARQUIVO");
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


    // PASSO 4

    public static void salvarPetEmArquivo(Pet pet) throws IOException {
        String nomeArquivo = gerarNomeArquivo(pet);
        String raizProjeto = System.getProperty("user.dir");
        Path path = Paths.get(raizProjeto, "petsCadastrados");
        Files.createDirectories(path);
        Path caminhoArquivo = path.resolve(nomeArquivo);


        StringBuilder sb = new StringBuilder();
        sb.append("1 - ").append(pet.getName()).append("\n");
        sb.append("2 - ").append(pet.getTipoPet()).append("\n");
        sb.append("3 - ").append(pet.getTipoSexo()).append("\n");


        sb.append("4 - ")
                .append(pet.getEndereco().getRua()).append(", ")
                .append(pet.getEndereco().getNumero()).append(", ")
                .append(pet.getEndereco().getCidade())
                .append("\n");

        sb.append("5 - ").append(pet.getIdade()).append("\n");
        sb.append("6 - ").append(pet.getPeso()).append("\n");
        sb.append("7 - ").append(pet.getRaca()).append("\n");


        Files.writeString(caminhoArquivo, sb.toString());


    }

    public static String gerarNomeArquivo(Pet pet) {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm");
        String formatoCompacto = agora.format(formatter);

        String nomePet = stripAccents(
                pet.getName().toUpperCase().replace(" ", "")
        );

        return formatoCompacto + "_" + nomePet + ".txt";
    }

    public static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }


    // PASSO 5

    public void buscarPets() {
        TipoPet tipoEscolhido;
        while (true) {
            String tipoPet = sc.nextLine();
            try {
                tipoEscolhido = TipoPet.valueOf(tipoPet.trim().toUpperCase());
                break;

            } catch (IllegalArgumentException e) {
                System.err.println("Tipo de pet inválido digite novamente");

            }
        }


    }

    public Integer criterioBusca() {

        System.out.println("Escolha um critério de busca:");
        System.out.println("1 - Nome");
        System.out.println("2 - Sexo");
        System.out.println("3 - Idade");
        System.out.println("4 - Peso");
        System.out.println("5 - Raça");
        System.out.println("6 - Endereço");
        System.out.println("7 - Nome + Idade");
        System.out.println("8 - Idade + Peso");
        Integer opcaoAlt = sc.nextInt();
        sc.nextLine();

        switch (opcaoAlt) {
            case 1:
                System.out.println("NOME");
                break;

            case 2:
                System.out.println("SEXO");
                break;

            case 3:
                System.out.println("IDADE");
                break;

            case 4:
                System.out.println("PESO");
                break;

            case 5:
                System.out.println("RAÇA");
                break;

            case 6:
                System.out.println("ENDEREÇO");
                break;

            case 7:
                System.out.println("NOME + IDADE");
                break;

            case 8:
                System.out.println("IDADE + PESO");
                break;
        }

        return opcaoAlt;
    }

    public List<Pet> carregarPetsDoDiretorio() throws IOException {
        String raizProjeto = System.getProperty("user.dir");
        File folder = new File(raizProjeto, "petsCadastrados");
        List<Pet> listaPets = new ArrayList<>();

        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    Path caminho = listOfFile.toPath();
                    Pet p = lerPetDeArquivo(caminho);

                    listaPets.add(p);
                }
            }
        }

        return listaPets;
    }

    private Pet lerPetDeArquivo(Path arquivo) throws IOException {

        List<String> linhas = Files.readAllLines(arquivo);
        Pet pet = new Pet();

        for (String linha : linhas) {

            String[] partes = linha.split("\\s*-\\s*");
            if (partes.length < 2) continue;

            String numero = partes[0].trim();
            String resposta = partes[1].trim();

            switch (numero) {

                case "1":
                    pet.setName(resposta);
                    break;

                case "2":
                    try {
                        TipoPet tipo = TipoPet.valueOf(resposta.trim().toUpperCase());
                        pet.setTipoPet(tipo);
                        break;

                    } catch (IllegalArgumentException e) {
                        System.err.println("Tipo de pet inválido");
                    }
                    break;

                case "3":
                    // sexo
                    break;

                case "4":
                    // endereço (tudo em uma linha!)
                    break;

                case "5":
                    // idade
                    break;

                case "6":
                    // peso
                    break;

                case "7":
                    // raça
                    break;
            }
        }

        return pet;
    }
}

 /* String raizProjeto = System.getProperty("user.dir");
        File folder = new File(raizProjeto,"petsCadastrados");
        try {
            BufferedReader reader = new BufferedReader(new FileReader();
            String line;
            while((line = reader.readLine()) != null) {
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (true) {
                        try {
                            TipoPet tipo = TipoPet.valueOf(resposta.trim().toUpperCase());
                            pet.setTipoPet(tipo);
                            break;

                        } catch (IllegalArgumentException e) {
                            System.err.println("Tipo de pet inválido digite novamente");
                        }
                    }*/
// 2. Extrair cada campo
// 3. Converter tipos (enum, inteiro, double)
// 4. Criar Pet e retornar