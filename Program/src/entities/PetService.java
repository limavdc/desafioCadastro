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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
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

    public List<Pet> buscarPets() throws IOException {
        TipoPet tipoEscolhido = null;
        while (true) {
            String tipoPet = sc.nextLine();
            try {
                tipoEscolhido = TipoPet.valueOf(tipoPet.trim().toUpperCase());
                break;

            } catch (IllegalArgumentException e) {
                System.err.println("Tipo de pet inválido digite novamente");

            }
        }

        List<Pet> todosPets = carregarPetsDoDiretorio();

        List<Pet> listaFiltrada = new ArrayList<>();
        for(Pet pet : todosPets) {
            if(pet.getTipoPet().equals(tipoEscolhido)) {
                listaFiltrada.add(pet);
            }
        }

        Integer condicao = criterioBusca();
        List<Pet> resultados = null;
        switch(condicao) {
            case 1:
                resultados = buscarPorNome(listaFiltrada);
                break;

            case 2:
                resultados = buscarPorSexo(listaFiltrada);
                break;

            case 3:
                resultados = buscarPorIdade(listaFiltrada);
                break;

            case 4:
                resultados = buscarPorPeso(listaFiltrada);
                break;

            case 5:
                resultados = buscarPorRaca(listaFiltrada);
                break;

            case 6:
                resultados = buscarPorEndereco(listaFiltrada);
                break;

            case 7:
                 resultados = buscarPorNomeEIdade(listaFiltrada);
                break;

            case 8:
                resultados = buscarPorIdadeEPeso(listaFiltrada);
                break;
            case 9:
                resultados = buscarPorNomeEPeso(listaFiltrada);
                break;
            case 10:
                resultados = buscarPorSexoEIdade(listaFiltrada);
                break;
            case 11:
                resultados = buscarPorNomeEEndereco(listaFiltrada);
                break;
            case 12:
                resultados = buscarPorPesoEEndereco(listaFiltrada);
                break;

            default:
                System.out.println("Opção inválida.");
                return new ArrayList<>();


        }

        imprimirResultados(resultados, "nome" );

        return resultados;
    }

    private List<Pet> buscarPorNome(List<Pet> lista) {

        List<Pet> resultados = new ArrayList<>();

        System.out.println("Qual nome do pet?");
        String nomeProcurado = sc.nextLine();
        nomeProcurado = unaccent(nomeProcurado).toLowerCase();


        for(Pet petAtual : lista) {
            String nomePetFormalizado = unaccent(petAtual.getName()).toLowerCase();
            if(nomePetFormalizado.contains(nomeProcurado) ) {
                resultados.add(petAtual);
            }
        }

        return resultados;
    }

    private List<Pet> buscarPorSexo(List<Pet> lista) {

        List<Pet> resultados = new ArrayList<>();

        System.out.println("Qual sexo do pet?");
        String tipoSexoInformado = sc.nextLine();


        TipoSexo sexoConvertido;

        try {
           sexoConvertido = TipoSexo.valueOf(tipoSexoInformado.toUpperCase());

        } catch (IllegalArgumentException e) {
            System.err.println("Sexo do pet inválido digite novamente");
            return resultados;
        }

        for(Pet petAtual : lista) {
            if(petAtual.getTipoSexo().equals(sexoConvertido)) {
                resultados.add(petAtual);
            }
        }

        return resultados;
    }

    private List<Pet> buscarPorIdade(List<Pet> lista) {

        List<Pet> resultados = new ArrayList<>();

        System.out.println("Qual idade do Pet?");

        Double idadeInformada;

        try {
            idadeInformada = Double.parseDouble(sc.nextLine().replace(",", "."));
        } catch (NumberFormatException e) {
            System.err.println("Entrada inválida! Digite apenas números.");
            return resultados;
        }

        if (idadeInformada > 0 && idadeInformada < 1) {
            // idade entre 0 e 1 ano → meses já convertidos
            // nada a fazer
        }
        else if (idadeInformada < 12) {
            System.out.println("Convertendo " + idadeInformada + " meses para anos...");
            idadeInformada = idadeInformada / 12.0;
        }
        else if (idadeInformada > IDADE_MAXIMA) {
            System.err.println("Idade acima do permitido!");
            return resultados;
        }

        for(Pet petAtual : lista) {
            Double idadePet = petAtual.getIdade();
            if (Math.abs(idadePet - idadeInformada) < 0.01){
                resultados.add(petAtual);
            }

        }

        return resultados;
    }


    private List<Pet> buscarPorPeso(List<Pet> lista) {

        List<Pet> resultados = new ArrayList<>();

        System.out.println("Qual peso do Pet?");

        Double pesoSelecionado;

        try {
            pesoSelecionado = Double.parseDouble(sc.nextLine().replace(",", "."));
        } catch (NumberFormatException e) {
            System.err.println("Peso inválido! Digite novamente.");
            return resultados;
        }

        if (pesoSelecionado < PESO_MINIMO || pesoSelecionado > PESO_MAXIMO) {
            System.err.println("Peso fora do intervalo permitido!");
            return resultados;
        }


        for(Pet petAtual : lista) {
            Double pesoPet = petAtual.getPeso();
            if(pesoPet.equals(pesoSelecionado)) {
                resultados.add(petAtual);
            }
        }

        return resultados;
    }

    private List<Pet> buscarPorRaca(List<Pet> lista) {

        List<Pet> resultados = new ArrayList<>();


        System.out.println("Qual raca do pet?");
        String racaProcurado = sc.nextLine();
        racaProcurado = unaccent(racaProcurado).toLowerCase();


        for(Pet petAtual : lista) {
            String racaPetFormalizado = unaccent(petAtual.getRaca()).toLowerCase();
            if(racaPetFormalizado.contains(racaProcurado) ) {
                resultados.add(petAtual);
            }
        }

        return resultados;
    }

    private List<Pet> buscarPorEndereco(List<Pet> lista) {

        List<Pet> resultados = new ArrayList<>();

        System.out.println("Qual campo sera buscado: ");

        System.out.println("1 - Rua ");

        System.out.println("2 - Número ");

        System.out.println("3 - Cidade ");

        System.out.println("4 - Rua + Número + Cidade ");



        Integer opcao = Integer.parseInt(sc.nextLine());

        String ruaInformada = "";
        Integer numeroInformado = null;
        String cidadeInformada = "";


        switch (opcao) {
            case 1:
                System.out.println("Digite a rua:");
                ruaInformada = sc.nextLine();
                break;

            case 2:
                System.out.println("Digite o número:");
                numeroInformado = sc.nextInt();
                sc.nextLine(); // limpar buffer
                break;

            case 3:
                System.out.println("Digite a cidade:");
                cidadeInformada = sc.nextLine();
                break;

            case 4:
                System.out.println("Digite a rua:");
                ruaInformada = sc.nextLine();

                System.out.println("Digite o número:");
                numeroInformado = sc.nextInt();
                sc.nextLine();

                System.out.println("Digite a cidade:");
                cidadeInformada = sc.nextLine();
                break;

            default:
                System.out.println("Opção inválida.");
                return resultados;
        }

        for (Pet petAtual : lista) {

            Endereco end = petAtual.getEndereco();

            if (end == null) continue;

            String rua = unaccent(end.getRua()).toLowerCase();
            Integer numero = end.getNumero();
            String cidade = unaccent(end.getCidade()).toLowerCase();

            switch (opcao) {

                case 1:
                    if (rua.contains(ruaInformada)) {
                        resultados.add(petAtual);
                    }
                    break;

                case 2:
                    if (numero.equals(numeroInformado)) {
                        resultados.add(petAtual);
                    }
                    break;

                case 3:
                    if (cidade.contains(cidadeInformada)) {
                        resultados.add(petAtual);
                    }
                    break;

                case 4:
                    if (rua.contains(ruaInformada)
                            && numero.equals(numeroInformado)
                            && cidade.contains(cidadeInformada)) {
                        resultados.add(petAtual);
                    }
                    break;
            }
        }

        return resultados;
    }

    private List<Pet> buscarPorNomeEIdade(List<Pet> lista) {

        List<Pet> resultados = new ArrayList<>();

        System.out.println("Qual nome do pet?");
        String nomeProcurado = sc.nextLine();
        nomeProcurado = unaccent(nomeProcurado).toLowerCase();
        System.out.println("Qual idade do Pet?");

        Double idadeInformada;

        try {
            idadeInformada = Double.parseDouble(sc.nextLine().replace(",", "."));
        } catch (NumberFormatException e) {
            System.err.println("Entrada inválida! Digite apenas números.");
            return resultados;
        }

        if (idadeInformada > 0 && idadeInformada < 1) {
            // idade entre 0 e 1 ano → meses já convertidos
            // nada a fazer
        }
        else if (idadeInformada < 12) {
            System.out.println("Convertendo " + idadeInformada + " meses para anos...");
            idadeInformada = idadeInformada / 12.0;
        }
        else if (idadeInformada > IDADE_MAXIMA) {
            System.err.println("Idade acima do permitido!");
            return resultados;
        }

        for (Pet petAtual : lista) {

            String nomePetFormalizado = unaccent(petAtual.getName()).toLowerCase();
            Double idadePet = petAtual.getIdade();
            if(nomePetFormalizado.contains(nomeProcurado) && Math.abs(idadePet - idadeInformada) < 0.01) {
                resultados.add(petAtual);
            }

        }

        return resultados;
    }

    private List<Pet> buscarPorIdadeEPeso(List<Pet> lista) {

        List<Pet> resultados = new ArrayList<>();

        // IDADE
        System.out.println("Qual idade do pet?");
        Double idadeInformada;

        try {
            idadeInformada = Double.parseDouble(sc.nextLine().replace(",", "."));
        } catch (NumberFormatException e) {
            System.err.println("Idade inválida.");
            return resultados;
        }

        if (idadeInformada < 12) {
            idadeInformada = idadeInformada / 12.0;
        } else if (idadeInformada > IDADE_MAXIMA) {
            System.err.println("Idade acima do permitido.");
            return resultados;
        }

        // PESO
        System.out.println("Qual peso do pet?");
        Double pesoInformado;
        try {
            pesoInformado = Double.parseDouble(sc.nextLine().replace(",", "."));
        } catch (NumberFormatException e) {
            System.err.println("Peso inválido.");
            return resultados;
        }

        for (Pet petAtual : lista) {

            Double idade = petAtual.getIdade();
            Double peso = petAtual.getPeso();

            boolean idadeOK = Math.abs(idade - idadeInformada) < 0.01;
            boolean pesoOK  = Math.abs(peso - pesoInformado) < 0.01;

            if (idadeOK && pesoOK) {
                resultados.add(petAtual);
            }
        }

        return resultados;
    }

    private List<Pet> buscarPorNomeEPeso(List<Pet> lista) {

        List<Pet> resultados = new ArrayList<>();

        System.out.println("Qual nome do pet?");
        String nomeProcurado = sc.nextLine();
        nomeProcurado = unaccent(nomeProcurado).toLowerCase();

        System.out.println("Qual peso do pet?");
        Double pesoInformado;
        try {
            pesoInformado = Double.parseDouble(sc.nextLine().replace(",", "."));
        } catch (NumberFormatException e) {
            System.err.println("Peso inválido.");
            return resultados;
        }

        for (Pet petAtual : lista) {

            String nomePetFormalizado = unaccent(petAtual.getName()).toLowerCase();
            Double peso = petAtual.getPeso();

            boolean nomeOK = nomePetFormalizado.contains(nomeProcurado);
            boolean pesoOK = Math.abs(peso - pesoInformado) < 0.01;

            if (nomeOK && pesoOK) {
                resultados.add(petAtual);
            }
        }

        return resultados;
    }

    private List<Pet> buscarPorSexoEIdade(List<Pet> lista) {

        List<Pet> resultados = new ArrayList<>();

        System.out.println("Qual sexo do pet?");
        String tipoSexoInformado = sc.nextLine();


        TipoSexo sexoConvertido;

        try {
            sexoConvertido = TipoSexo.valueOf(tipoSexoInformado.toUpperCase());

        } catch (IllegalArgumentException e) {
            System.err.println("Sexo do pet inválido digite novamente");
            return resultados;
        }


        System.out.println("Qual idade do pet?");
        Double idadeInformada;

        try {
            idadeInformada = Double.parseDouble(sc.nextLine().replace(",", "."));
        } catch (NumberFormatException e) {
            System.err.println("Idade inválida.");
            return resultados;
        }

        if (idadeInformada < 12) {
            idadeInformada = idadeInformada / 12.0;
        } else if (idadeInformada > IDADE_MAXIMA) {
            System.err.println("Idade acima do permitido.");
            return resultados;
        }

        for (Pet petAtual : lista) {

            Double idade = petAtual.getIdade();

            boolean idadeOK = Math.abs(idade - idadeInformada) < 0.01;
            boolean sexoOK = petAtual.getTipoSexo().equals(sexoConvertido);

            if(sexoOK && idadeOK) {
                resultados.add(petAtual);
            }
        }

        return resultados;
    }

    private List<Pet> buscarPorNomeEEndereco(List<Pet> lista) {

        List<Pet> resultados = new ArrayList<>();

        System.out.println("Qual nome do pet?");
        String nomeProcurado = sc.nextLine();
        nomeProcurado = unaccent(nomeProcurado).toLowerCase();

        System.out.println("Qual campo será buscado:");
        System.out.println("1 - Rua");
        System.out.println("2 - Número");
        System.out.println("3 - Cidade");
        System.out.println("4 - Rua + Número + Cidade");

        Integer opcao = Integer.parseInt(sc.nextLine());

        String ruaInformada = "";
        Integer numeroInformado = null;
        String cidadeInformada = "";


        // Entrada do usuário
        switch (opcao) {
            case 1:
                System.out.println("Digite a rua:");
                ruaInformada = sc.nextLine();
                ruaInformada = unaccent(ruaInformada).toLowerCase();
                break;

            case 2:
                System.out.println("Digite o número:");
                numeroInformado = sc.nextInt();
                sc.nextLine();
                break;

            case 3:
                System.out.println("Digite a cidade:");
                cidadeInformada = sc.nextLine();
                cidadeInformada = unaccent(cidadeInformada).toLowerCase();
                break;

            case 4:
                System.out.println("Digite a rua:");
                ruaInformada = sc.nextLine();
                ruaInformada = unaccent(ruaInformada).toLowerCase();

                System.out.println("Digite o número:");
                numeroInformado = sc.nextInt();
                sc.nextLine();

                System.out.println("Digite a cidade:");
                cidadeInformada = sc.nextLine();
                cidadeInformada = unaccent(cidadeInformada).toLowerCase();
                break;

            default:
                System.out.println("Opção inválida.");
                return resultados;
        }


        // PROCESSO DE FILTRAGEM
        for (Pet petAtual : lista) {

            String nomePetFormalizado = unaccent(petAtual.getName()).toLowerCase();

            // Nome SEMPRE deve bater
            if (!nomePetFormalizado.contains(nomeProcurado)) {
                continue;
            }

            Endereco end = petAtual.getEndereco();
            if (end == null) continue;

            String rua = unaccent(end.getRua()).toLowerCase();
            Integer numero = end.getNumero();
            String cidade = unaccent(end.getCidade()).toLowerCase();


            switch (opcao) {

                case 1:
                    if (rua.contains(ruaInformada)) {
                        resultados.add(petAtual);
                    }
                    break;

                case 2:
                    if (numero.equals(numeroInformado)) {
                        resultados.add(petAtual);
                    }
                    break;

                case 3:
                    if (cidade.contains(cidadeInformada)) {
                        resultados.add(petAtual);
                    }
                    break;

                case 4:
                    if (rua.contains(ruaInformada)
                            && numero.equals(numeroInformado)
                            && cidade.contains(cidadeInformada)) {
                        resultados.add(petAtual);
                    }
                    break;
            }
        }

        return resultados;
    }


    private List<Pet> buscarPorPesoEEndereco(List<Pet> lista) {

        List<Pet> resultados = new ArrayList<>();

        System.out.println("Qual peso do pet?");
        Double pesoInformado;

        try {
            pesoInformado = Double.parseDouble(sc.nextLine().replace(",", "."));
        } catch (NumberFormatException e) {
            System.err.println("Peso inválido!");
            return resultados;
        }

        System.out.println("Qual campo será buscado:");
        System.out.println("1 - Rua");
        System.out.println("2 - Número");
        System.out.println("3 - Cidade");
        System.out.println("4 - Rua + Número + Cidade");

        Integer opcao = Integer.parseInt(sc.nextLine());

        String ruaInformada = "";
        Integer numeroInformado = null;
        String cidadeInformada = "";


        // Entrada do usuário
        switch (opcao) {

            case 1:
                System.out.println("Digite a rua:");
                ruaInformada = unaccent(sc.nextLine()).toLowerCase();
                break;

            case 2:
                System.out.println("Digite o número:");
                numeroInformado = sc.nextInt();
                sc.nextLine();
                break;

            case 3:
                System.out.println("Digite a cidade:");
                cidadeInformada = unaccent(sc.nextLine()).toLowerCase();
                break;

            case 4:
                System.out.println("Digite a rua:");
                ruaInformada = unaccent(sc.nextLine()).toLowerCase();

                System.out.println("Digite o número:");
                numeroInformado = sc.nextInt();
                sc.nextLine();

                System.out.println("Digite a cidade:");
                cidadeInformada = unaccent(sc.nextLine()).toLowerCase();
                break;

            default:
                System.out.println("Opção inválida.");
                return resultados;
        }


        // PROCESSO DE FILTRAGEM
        for (Pet petAtual : lista) {

            Double pesoPet = petAtual.getPeso();
            boolean pesoOK = Math.abs(pesoPet - pesoInformado) < 0.01;

            if (!pesoOK) continue;

            Endereco end = petAtual.getEndereco();
            if (end == null) continue;

            String rua = unaccent(end.getRua()).toLowerCase();
            Integer numero = end.getNumero();
            String cidade = unaccent(end.getCidade()).toLowerCase();


            switch (opcao) {

                case 1:
                    if (rua.contains(ruaInformada)) {
                        resultados.add(petAtual);
                    }
                    break;

                case 2:
                    if (numeroInformado != null && numero.equals(numeroInformado)) {
                        resultados.add(petAtual);
                    }
                    break;

                case 3:
                    if (cidade.contains(cidadeInformada)) {
                        resultados.add(petAtual);
                    }
                    break;

                case 4:
                    if (rua.contains(ruaInformada)
                            && numeroInformado != null && numero.equals(numeroInformado)
                            && cidade.contains(cidadeInformada)) {
                        resultados.add(petAtual);
                    }
                    break;
            }
        }

        return resultados;
    }


    private void imprimirResultados(List<Pet> resultados, String imprimirResultado) {

        if (resultados == null || resultados.isEmpty()) {
            return;
        }

        for (int i = 0; i < resultados.size(); i++) {

            Pet petAtual = resultados.get(i);

            // Proteção contra null
            String nome   = petAtual.getName() == null ? "" : petAtual.getName();
            TipoSexo sexo = petAtual.getTipoSexo();
            Double idade  = petAtual.getIdade() == null ? 0.0 : petAtual.getIdade();
            Double peso   = petAtual.getPeso() == null ? 0.0 : petAtual.getPeso();
            String raca   = petAtual.getRaca() == null ? "" : petAtual.getRaca();

            Endereco end = petAtual.getEndereco();

            String rua = "";
            Integer numero = 0;
            String cidade = "";

            if (end != null) {
                rua = end.getRua() == null ? "" : end.getRua();
                numero = end.getNumero() == null ? 0 : end.getNumero();
                cidade = end.getCidade() == null ? "" : end.getCidade();
            }

            String linha = String.format(
                    "Nome: %s | Sexo: %s | Idade: %.2f | Peso: %.2f | Raça: %s | Endereço: %s, %d - %s",
                    nome,
                    sexo,
                    idade,
                    peso,
                    raca,
                    rua,
                    numero,
                    cidade
            );

            System.out.println((i + 1) + ". " + linha);
        }
    }

    public static String unaccent(String src) {
        return Normalizer
                .normalize(src, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
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

                    } catch (IllegalArgumentException e) {
                        System.err.println("Tipo de pet inválido");
                    }
                    break;

                case "3":
                    try {
                        TipoSexo sexoPet = TipoSexo.valueOf(resposta.trim().toUpperCase());
                        pet.setTipoSexo(sexoPet);

                    } catch (IllegalArgumentException e) {
                        System.err.println("Sexo do pet inválido");
                    }
                    break;

                case "4":
                    String[] partesEndereco =  resposta.split("\\s*,\\s*"); //isso é uma array de string

                    if(partesEndereco.length != 3) {
                        continue;
                    }

                    Endereco end = new Endereco();

                    end.setRua(partesEndereco[0]);
                    end.setNumero(Integer.parseInt(partesEndereco[1]));
                    end.setCidade(partesEndereco[2]);

                    pet.setEndereco(end);
                    break;

                case "5":
                    pet.setIdade(Double.parseDouble(resposta));
                    break;

                case "6":
                    pet.setPeso(Double.parseDouble(resposta));
                    break;

                case "7":
                    pet.setRaca(resposta);
                    break;
            }
        }

        pet.setNomeArquivo(arquivo.getFileName().toString());

        return pet;
    }

    //PASSO 6

    public void alterarPets() throws IOException {

        List<Pet> listaEncontrada = buscarPets();
        if (listaEncontrada == null || listaEncontrada.isEmpty()) {
            System.out.println("Nenhum pet encontrado.");
            return;
        }

        System.out.println("Qual pet será alterado? Digite o número ");
        Integer opcaoEscolhida = sc.nextInt();
        sc.nextLine();

        //Enquanto o número digitado for menor que zero OU o número digitado for maior ou igual ao tamanho da lista... peça o número novamente.


        while (opcaoEscolhida < 1 || opcaoEscolhida > listaEncontrada.size()) {
            System.out.println("Número inválido. Digite novamente:");
            opcaoEscolhida = sc.nextInt();
            sc.nextLine();
        }

        Pet petSelecionado = listaEncontrada.get(opcaoEscolhida - 1);

        Boolean alterando = true;

        while(alterando) {
            System.out.println("O que deseja alterar?");
            System.out.println("1 - Nome");
            System.out.println("2 - Idade");
            System.out.println("3 - Peso");
            System.out.println("4 - Raça");
            System.out.println("5 - Endereço");
            System.out.println("6 - Finalizar alteração");

            Integer opcaoAlt = sc.nextInt();
            sc.nextLine();

            switch (opcaoAlt) {
                case 1:
                    System.out.println("Qual o novo nome?");
                    String nomeNovo = sc.nextLine();
                    if(nomeNovo == null || nomeNovo.isEmpty()){
                        System.out.println("Nome vazio");
                        return;
                    }
                    petSelecionado.setName(nomeNovo);
                    break;

                case 2:
                    System.out.println("Qual a nova idade?");

                    try {
                        Double idadeNova = sc.nextDouble();
                        if (idadeNova > 0 && idadeNova < 1) {
                        } else if (idadeNova < 12) {
                            System.out.println("Convertendo " + idadeNova.intValue() + " meses para anos...");
                            idadeNova = idadeNova / 12;
                        } else if (idadeNova > IDADE_MAXIMA) {
                            System.err.println("Idade inválida! Digite novamente.");
                            continue;
                        }

                        petSelecionado.setIdade(idadeNova);
                        break;

                    } catch (InputMismatchException e) {
                        System.err.println("Entrada inválida! Digite apenas números.");
                        continue;
                    }


                case 3:
                    System.out.println("Qual o novo peso?");
                    Double pesoNovo;

                    try {
                        pesoNovo = Double.parseDouble(sc.nextLine().replace(",", "."));
                    } catch (NumberFormatException e) {
                        System.err.println("Peso inválido! Digite novamente.");
                        return;
                    }

                    if (pesoNovo < PESO_MINIMO || pesoNovo > PESO_MAXIMO) {
                        System.err.println("Peso fora do intervalo permitido!");
                        continue;
                    }
                    petSelecionado.setPeso(pesoNovo);
                    break;

                case 4:
                    System.out.println("Qual a raça nova?");
                    String racaNova = sc.nextLine();
                    if(racaNova == null || racaNova.isEmpty()){
                        System.out.println("Nome vazio");
                        return;
                    }

                    petSelecionado.setRaca(racaNova);
                    break;

                case 5:
                   alterarEndereco(petSelecionado);
                    break;

                case 6:
                    alterando = false;
                    break;

                default:
                    System.out.println("Opção inválida, tente novamente.");

            }
        }

        salvarPetAlterado(petSelecionado);

    }

    private void alterarEndereco(Pet petSelecionado) {
        System.out.println("Qual campo sera atualizado: ");

        System.out.println("1 - Rua ");

        System.out.println("2 - Número ");

        System.out.println("3 - Cidade ");

        System.out.println("4 - Rua + Número + Cidade ");



        Integer opcao = Integer.parseInt(sc.nextLine());

        String ruaNova = "";
        Integer numeroNovo = null;
        String cidadeNova = "";

        Endereco endNovo = petSelecionado.getEndereco();

        if (endNovo == null) {
            endNovo = new Endereco();
        }

        switch (opcao) {
            case 1:
                System.out.println("Digite a rua:");
                ruaNova = sc.nextLine();
                endNovo.setRua(ruaNova);
                break;

            case 2:
                System.out.println("Digite o número:");
                numeroNovo = sc.nextInt();
                endNovo.setNumero(numeroNovo);
                sc.nextLine(); // limpar buffer
                break;

            case 3:
                System.out.println("Digite a cidade:");
                cidadeNova = sc.nextLine();
                endNovo.setCidade(cidadeNova);
                break;

            case 4:
                System.out.println("Digite a rua:");
                ruaNova = sc.nextLine();
                endNovo.setRua(ruaNova);

                System.out.println("Digite o número:");
                numeroNovo = sc.nextInt();
                endNovo.setNumero(numeroNovo);
                sc.nextLine();

                System.out.println("Digite a cidade:");
                cidadeNova = sc.nextLine();
                endNovo.setCidade(cidadeNova);
                break;

            default:
                System.out.println("Opção inválida.");
                return;
        }
        petSelecionado.setEndereco(endNovo);

    }


    private void salvarPetAlterado(Pet pet) throws IOException {
        String nomeArquivo = pet.getNomeArquivo();
        if(nomeArquivo == null) {
            System.out.println("Erro: pet sem nome de arquivo associado");
            return;
        }

        Path pasta = Paths.get("petsCadastrados");
        Files.createDirectories(pasta);

        Path caminho = pasta.resolve(nomeArquivo);

        try(BufferedWriter writer = Files.newBufferedWriter(caminho)) {

            writer.write("1 - " + pet.getName());
            writer.newLine();
            writer.write("2 - " + pet.getTipoPet());
            writer.newLine();
            writer.write("3 - " + pet.getTipoSexo());
            writer.newLine();

            Endereco end = pet.getEndereco();

            String rua = (end != null && end.getRua() != null) ? end.getRua() : "";
            Integer numero = (end != null && end.getNumero() != null) ? end.getNumero() : 0;
            String cidade = (end != null && end.getCidade() != null) ? end.getCidade() : "";

            String enderecoFormatado = rua + ", " + numero + ", " + cidade;

            writer.write("4 - " + enderecoFormatado);
            writer.newLine();

            writer.write("5 - " + pet.getIdade());
            writer.newLine();
            writer.write("6 - " + pet.getPeso());
            writer.newLine();
            writer.write("7 - " + pet.getRaca());
            writer.newLine();
        }
        System.out.println("Pet atualizado com sucesso!");
    }





}

