package com.example.spudydev.spudy.entidades.usuario.persistencia;

import com.example.spudydev.spudy.entidades.pessoa.dominio.Pessoa;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;


public class PessoaDAO {

    //Cadastrar pessoa no banco
    public void inserirPessoa(Pessoa pessoa) {
        //Obtendo uid do usuário atual
        String uidUsuario = AcessoFirebase.getUidUsuario();
        //Salvando pessoa
        AcessoFirebase.getFirebase().child("pessoa").child(uidUsuario).setValue(pessoa);
    }

}
