package com.example.spudydev.spudy.entidades.usuario.persistencia;

import com.example.spudydev.spudy.entidades.endereco.dominio.Endereco;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;

public class EnderecoDAO {
    //Cadastrar endereço
    public void inserirEndereco(Endereco endereco){
        //Obtendo uid do usuário atual
        String uidUsuario = AcessoFirebase.getUidUsuario();
        //Salvando endereço no banco
        AcessoFirebase.getFirebase().child("endereco").child(uidUsuario).setValue(endereco);
    }
}
