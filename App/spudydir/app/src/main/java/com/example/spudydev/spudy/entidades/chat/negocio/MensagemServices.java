package com.example.spudydev.spudy.entidades.chat.negocio;

import com.example.spudydev.spudy.entidades.chat.MensagemException;
import com.example.spudydev.spudy.entidades.chat.dominio.Mensagem;
import com.example.spudydev.spudy.entidades.chat.persistencia.MensagemDAO;

/**
 * The type Mensagem services.
 */
public final class MensagemServices {

    private MensagemServices(){
        //Construtor vazio
    }

    /**
     * Verificar mensagem.
     *
     * @param codigoDaTurma the codigo da turma
     * @param mensagem      the mensagem
     * @throws MensagemException the mensagem exception
     */
//Verificando campos
    public static void enviarMensagem(String codigoDaTurma, Mensagem mensagem) throws MensagemException{

        final int tamanhoMinimoMensagem = 100;

        if (mensagem.getTexto().length() > tamanhoMinimoMensagem){
            throw new MensagemException("A mensagem só pode conter no máximo 100 caracteres.");
        }else {
            if (!MensagemDAO.enviarMensagem(mensagem.getTexto(), codigoDaTurma)){
                throw new MensagemException("Erro ao enviar mensagem. Verifique sua conexão com a internet ou contate o suporte.");
            }
        }
    }
}
