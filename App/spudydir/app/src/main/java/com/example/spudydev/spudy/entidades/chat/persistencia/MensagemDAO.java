package com.example.spudydev.spudy.entidades.chat.persistencia;

import com.example.spudydev.spudy.entidades.chat.dominio.Mensagem;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.google.firebase.database.DatabaseException;


/**
 * The type Mensagem dao.
 */
public final class MensagemDAO {

    private MensagemDAO(){
        //Construtor vazio
    }

    /**
     * Enviar mensagem boolean.
     *
     * @param texto         the texto
     * @param codigoDaTurma the codigo da turma
     * @return the boolean
     */
//Enviar mensagem para a turma
    public static boolean enviarMensagem(String texto, String codigoDaTurma){
        boolean verificador;
        try {
            Mensagem mensagem = new Mensagem();

            mensagem.setAutor(AcessoFirebase.getUidUsuario());
            mensagem.setTexto(texto);
            AcessoFirebase.getFirebase().child("chat").child(codigoDaTurma).push().setValue(mensagem);
            verificador = true;

        } catch (DatabaseException e){
            verificador = false;
        }
        return verificador;
    }
}
