package com.example.spudydev.spudy.entidades.falta.negocio;

import com.example.spudydev.spudy.entidades.falta.dominio.Falta;
import com.example.spudydev.spudy.entidades.falta.persistencia.FaltaDAO;

/**
 * The type Falta services.
 */
public final class FaltaServices {

    private FaltaServices(){
        //Construtor vazio
    }

    /**
     * Inserir falta.
     *
     * @param falta       the falta
     * @param codigoTurma the codigo turma
     * @param uidAluno    the uid aluno
     * @return the boolean
     */
//Verificando se o professor está fazendo a chamada de um mesmo aluno duas vezes
    public static boolean inserirFaltaServices(Falta falta, String codigoTurma, String uidAluno) {
        return FaltaDAO.inserirFaltasDAO(falta, codigoTurma, uidAluno);
    }
}
