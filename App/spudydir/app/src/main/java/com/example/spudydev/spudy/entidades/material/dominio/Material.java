package com.example.spudydev.spudy.entidades.material.dominio;

/**
 * The type Material.
 */
public class Material {

    private String titulo;
    private String conteudo;
    private String idMaterial;

    /**
     * Gets id material.
     *
     * @return the id material
     */
    public String getIdMaterial() {
        return idMaterial;
    }

    /**
     * Sets id material.
     *
     * @param idMaterial the id material
     */
    public void setIdMaterial(String idMaterial) {
        this.idMaterial = idMaterial;
    }

    /**
     * Gets titulo.
     *
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Sets titulo.
     *
     * @param string the string
     */
    public void setTitulo(String string) {
        this.titulo = string;
    }

    /**
     * Gets conteudo.
     *
     * @return the conteudo
     */
    public String getConteudo() {
        return conteudo;
    }

    /**
     * Sets conteudo.
     *
     * @param string the string
     */
    public void setConteudo(String string) {
        this.conteudo = string;
    }
}
