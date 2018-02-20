package com.example.spudydev.spudy.entidades.nota.negocio;

import com.example.spudydev.spudy.entidades.nota.dominio.Nota;
import com.example.spudydev.spudy.entidades.nota.persistencia.NotaDAO;

/**
 * The type Nota services.
 */
public final class NotaServices {

    private NotaServices(){
        //Construtor vazio
    }

    /**
     * Inserir nota.
     *
     * @param nota        the nota
     * @param uidUsuario  the uid usuario
     * @param codigoTurma the codigo turma
     * @return the boolean
     */
//Inserindo notas no banco
    public static boolean inserirNotaServices(Nota nota, String uidUsuario, String codigoTurma) {
        return NotaDAO.inserirNotaDAO(nota, uidUsuario, codigoTurma);
    }
}
