package com.example.spudydev.spudy.entidades.chat.persistencia;

import android.content.Context;
import android.widget.Toast;

import com.example.spudydev.spudy.entidades.chat.dominio.Mensagem;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;

/**
 * Created by User on 05/02/2018.
 */

public class MensagemDAO {
    //Enviar mensagem para a turma
    public void enviarMensagem(String texto, String codigoDaTurma){
            Mensagem mensagem = new Mensagem();
            mensagem.setAutor(AcessoFirebase.getUidUsuario());
            mensagem.setIdMensagem("0");
            mensagem.setTexto(texto);
            AcessoFirebase.getFirebase().child("chat").child(codigoDaTurma).push().setValue(mensagem);
    }
}
