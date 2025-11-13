package entities;

import java.util.regex.Pattern;
import entities.enums.TipoPet;
import entities.enums.TipoSexo;


public class Pet {
    private String name;
    private TipoPet tipoPet;
    private TipoSexo tipoSexo;
    private Double peso;
    private Double idade;
    private String raca;
    public static final String NAO_INFORMADO = "NÃO INFORMADO";


    Endereco endereco = new Endereco(5, "RUA DOBOLO", "SP");


    public Pet () {
    }

    public Pet(String name, TipoPet tipoPet, TipoSexo tipoSexo, Double peso, Double idade, String raca, Endereco endereco) {
        this.name = name;
        this.tipoPet = tipoPet;
        this.tipoSexo = tipoSexo;
        this.peso = peso;
        this.idade = idade;
        this.raca = raca;
        this.endereco = endereco;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TipoPet getTipoPet() {
        return tipoPet;
    }

    public void setTipoPet(TipoPet tipoPet) {
        this.tipoPet = tipoPet;
    }

    public TipoSexo getTipoSexo() {
        return tipoSexo;
    }

    public void setTipoSexo(TipoSexo tipoSexo) {
        this.tipoSexo = tipoSexo;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getIdade() {
        return idade;
    }

    public void setIdade(Double idade) {
        this.idade = idade;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "Nome: '" + name + '\'' +
                ", TipoPet: " + tipoPet +
                ", TipoSexo: " + tipoSexo +
                ", Peso:" + peso +
                ", Idade: " + idade +
                ", Raca: '" + raca + '\'' +
                ", Endereco: " + endereco;
    }
}