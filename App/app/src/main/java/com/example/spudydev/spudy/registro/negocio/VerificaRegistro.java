package com.example.spudydev.spudy.registro.negocio;

import com.example.spudydev.spudy.pessoa.dominio.Pessoa;
import com.example.spudydev.spudy.usuario.dominio.Usuario;


public class VerificaRegistro {

    Usuario usuario;
    Pessoa pessoa;

    //Construtor
    public VerificaRegistro(Usuario usuario, Pessoa pessoa) {

        this.usuario = usuario;
        this.pessoa = pessoa;

    }

    public String Verificar() {

        //NOME
        if (this.pessoa.getNome().isEmpty()) {
            return "Preencha o campo NOME.";
        }

        //EMAIL
        if (this.usuario.getEmail().isEmpty()) {
            return "Preencha o campo EMAIL.";
        }

        //DATA NASCIMENTn
        if (this.pessoa.getDataNascimento().isEmpty()) {
            return "Preencha o campo DATA DE NASCIMENTO.";
        }

        //INSTITUICAO
        if (this.usuario.getInstituicao().isEmpty()) {
            return "Preencha o campo INSTITUIÇÃO.";
        }

        //TIPO CONTA
        if (this.usuario.getTipoConta().equals("Selecione o tipo de conta")) {
            return "Selecione um TIPO DE CONTA.";
        }

        //SENHA
        if (this.usuario.getSenha().isEmpty()) {
            return "Preencha o campo SENHA.";
        }
        return "sucesso";
    }
}
