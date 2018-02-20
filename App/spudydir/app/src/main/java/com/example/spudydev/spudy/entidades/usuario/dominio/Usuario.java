package com.example.spudydev.spudy.entidades.usuario.dominio;

/**
 * The type Usuario.
 */
public class Usuario {

    private String email;
    private String tipoConta;
    private String instituicao;

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param string the string
     */
    public void setEmail(String string) { this.email = string; }

    /**
     * Gets tipo conta.
     *
     * @return the tipo conta
     */
    public String getTipoConta() {
        return tipoConta;
    }

    /**
     * Sets tipo conta.
     *
     * @param string the string
     */
    public void setTipoConta(String string) {
        this.tipoConta = string;
    }

    /**
     * Gets instituicao.
     *
     * @return the instituicao
     */
    public String getInstituicao() {
        return instituicao;
    }

    /**
     * Sets instituicao.
     *
     * @param string the string
     */
    public void setInstituicao(String string) {
        this.instituicao = string;
    }

}

