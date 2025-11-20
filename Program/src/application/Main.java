package application;


import entities.Pet;
import entities.PetService;


import javax.swing.*;
import java.util.Locale;
import java.util.Scanner;




public class Main {
    public static void main(String[] args) throws Exception {

        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);


        PetService petService = new PetService();



        /*----------------------MENU DE CADASTRO-------------------------------*/

        boolean continuar = true;
        do {
            System.out.println("1. Cadastrar um novo pet");
            System.out.println("2. Alterar os dados do pet cadastrado");
            System.out.println("3. Qual o tipo do pet (Cachorro/Gato)?");
            System.out.println("4. Listar todos os pets cadastrados");
            System.out.println("5. Listar pets por algum critério (idade, nome, raça)");
            System.out.println("6. Sair");
            int opcao = sc.nextInt();
            sc.nextLine();

            switch(opcao) {
                case 1:
                    System.out.println("Cadastro novo pet");
                    try {
                        Pet novoPet = petService.cadastrarNovoPet();
                        System.out.println("\n✅ Pet cadastrado com sucesso:");
                        System.out.println(novoPet);

                        PetService.salvarPetEmArquivo(novoPet);
                        System.out.println("📁 Arquivo salvo com sucesso!");

                    } catch (Exception e) {
                        System.err.println(" Erro ao cadastrar o pet: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Alterar os dados do pet");
                   // Pet alterarPet = petService.buscarPets();
                    break;
                case 3:
                    System.out.println("Qual tipo do pet");
                    break;
                case 4:
                    System.out.println("Listar todos os pets");
                    break;
                case 5:
                    System.out.println("Listar pets por critério");
                    break;
                case 6:
                    System.out.println("Escolheu Sair");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opção invalida.");

            }

        } while (continuar);

        sc.close();
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