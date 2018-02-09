package com.example.spudydev.spudy.entidades.usuario.persistencia;

import com.example.spudydev.spudy.entidades.usuario.dominio.Usuario;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;

public class UsuarioDAO {
    //Cadastrar usuario
    public void inserirUsuario(Usuario usuario){
        //Obtendo uid do usuário atual
        String uidUsuario = AcessoFirebase.getUidUsuario();
        //Salvando Pessoa e usuario
        AcessoFirebase.getFirebase().child("usuario").child(uidUsuario).setValue(usuario);

        //Salvando Aluno OU Professor
        if (usuario.getTipoConta().equals("Aluno")){
            AcessoFirebase.getFirebase().child("aluno").child(uidUsuario).child("turmas").child("SENTINELA").setValue("0");
        }else{
            AcessoFirebase.getFirebase().child("professor").child(uidUsuario).child("turmasMinistradas").child("SENTINELA").setValue("0");
        }
    }
}
