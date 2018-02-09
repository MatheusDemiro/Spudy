package com.example.spudydev.spudy.entidades.chat.negocio;

import android.content.Context;

import com.example.spudydev.spudy.entidades.chat.persistencia.MensagemDAO;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;

/**
 * Created by User on 05/02/2018.
 */

public class MensagemServices {
    //Verificando campos
    public void verificarMensagem(Context context, String texto, String codigoDaTurma){
        if (texto.isEmpty()){
            Auxiliar.criarToast(context, "Campo de mensagem vazio.");
        }if (texto.length() > 100){
            Auxiliar.criarToast(context, "A mensagem só pode conter no máximo 100 caracteres.");
        }else {
            MensagemDAO mensagemDAO = new MensagemDAO();
            mensagemDAO.enviarMensagem(texto, codigoDaTurma);
        }
    }
}
