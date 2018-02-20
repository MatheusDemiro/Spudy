package com.example.spudydev.spudy.entidades.endereco.dominio;

/**
 * The type Endereco.
 */
public class Endereco {

    private String rua;
    private String complemento;
    private String cidade;
    private String estado;

    /**
     * Gets rua.
     *
     * @return the rua
     */
    public String getRua() {
        return rua;
    }

    /**
     * Sets rua.
     *
     * @param string the string
     */
    public void setRua(String string) {
        this.rua = string;
    }

    /**
     * Gets complemento.
     *
     * @return the complemento
     */
    public String getComplemento() {
        return complemento;
    }

    /**
     * Sets complemento.
     *
     * @param string the string
     */
    public void setComplemento(String string) {
        this.complemento = string;
    }

    /**
     * Gets cidade.
     *
     * @return the cidade
     */
    public String getCidade() {
        return cidade;
    }

    /**
     * Sets cidade.
     *
     * @param string the string
     */
    public void setCidade(String string) {
        this.cidade = string;
    }

    /**
     * Gets estado.
     *
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Sets estado.
     *
     * @param string the string
     */
    public void setEstado(String string) {
        this.estado = string;
    }
}
