package com.example.spudydev.spudy.entidades.turma.negocio;

import com.example.spudydev.spudy.entidades.turma.dominio.Turma;
import com.example.spudydev.spudy.entidades.turma.persistencia.TurmaDAO;

/**
 * The type Turma services.
 */
public final class TurmaServices {

    private TurmaServices(){
        //Construtor vazio
    }

    /**
     * Sair da turma boolean.
     *
     * @param codigoTurma the codigo turma
     * @return the boolean
     */
//Sair da turma (sem verificações)
    public static boolean sairDaTurmaServices(String codigoTurma){
        return TurmaDAO.sairDaTurmaDAO(codigoTurma);
    }

    /**
     * Criar turma boolean.
     *
     * @param turma the turma
     * @return the boolean
     */
//Método que chama a camada DAO para inserirUsuarioDAO a turma no banco de dados
    public static boolean criarTurmaServices(Turma turma){
        return TurmaDAO.criarTurmaDAO(turma);
    }

    /**
     * Adicionar turma boolean.
     *
     * @param codigoTurma the codigo turma
     * @return the boolean
     */
//Cadastrando o usuário na turma
    public static boolean adicionarTurmaServices(String codigoTurma){
        return TurmaDAO.adicionarTurmaDAO(codigoTurma);
    }

    /**
     * Remover aluno turma boolean.
     *
     * @param codigoTurma the codigo turma
     * @param uidUsuario  the uid usuario
     * @return the boolean
     */
    public static boolean removerAlunoTurmaServices(String codigoTurma, String uidUsuario){
        return TurmaDAO.removerAlunoTurmaDAO(codigoTurma, uidUsuario);
    }
}