package com.example.spudydev.spudy.entidades.pessoa.dominio;

/**
 * The type Pessoa.
 */
public class Pessoa {

    private String nome;
    private String dataNascimento;

    /**
     * Gets data nascimento.
     *
     * @return the data nascimento
     */
    public String getDataNascimento() { return dataNascimento; }

    /**
     * Sets data nascimento.
     *
     * @param string the string
     */
    public void setDataNascimento(String string) {
        this.dataNascimento = string;
    }

    /**
     * Gets nome.
     *
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets nome.
     *
     * @param string the string
     */
    public void setNome(String string) {
        this.nome = string;
    }
}
