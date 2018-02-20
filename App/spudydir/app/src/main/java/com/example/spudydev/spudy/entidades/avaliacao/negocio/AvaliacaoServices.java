package com.example.spudydev.spudy.entidades.avaliacao.negocio;

import com.example.spudydev.spudy.entidades.avaliacao.dominio.Avaliacao;
import com.example.spudydev.spudy.entidades.avaliacao.persistencia.AvaliacaoDAO;

/**
 * The type Avaliacao services.
 */
public final class AvaliacaoServices {

    private AvaliacaoServices(){
        //Construtor vazio
    }

    /**
     * Inserir avaliacao turma boolean.
     *
     * @param avaliacao   the avaliacao
     * @param codigoTurma the codigo turma
     * @return the boolean
     */
//Chamando método da camada DAO para inserirUsuarioDAO avaliação no banco
    public static boolean inserirAvaliacaoTurma(Avaliacao avaliacao, String codigoTurma){
        return AvaliacaoDAO.inserirAvaliacao(avaliacao,codigoTurma);
    }
}
