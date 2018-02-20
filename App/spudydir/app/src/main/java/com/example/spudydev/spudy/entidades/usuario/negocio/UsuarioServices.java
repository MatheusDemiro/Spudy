package com.example.spudydev.spudy.entidades.usuario.negocio;

import com.example.spudydev.spudy.entidades.usuario.dominio.Usuario;
import com.example.spudydev.spudy.entidades.usuario.persistencia.UsuarioDAO;

/**
 * The type Usuario services.
 */
public final class UsuarioServices {

    private UsuarioServices(){
        //Construtor vazio
    }

    /**
     * Inserir usuario boolean.
     *
     * @param usuario the usuario
     * @return the boolean
     */
//Método que chama o DAO para inserirUsuarioDAO o usuário
    public static boolean inserirUsuarioServices(Usuario usuario){
        return UsuarioDAO.inserirUsuarioDAO(usuario);
    }

    /**
     * Alterar email boolean.
     *
     * @param novoEmail the novo email
     * @return the boolean
     */
//Método que chama o DAO para alterar nome
    public static boolean alterarEmailServices(String novoEmail){
        return UsuarioDAO.alterarEmailDAO(novoEmail);
    }

    /**
     * Alterar instituicao boolean.
     *
     * @param novaInstituicao the nova instituicao
     * @return the boolean
     */
    public static boolean alterarInstituicaoServices(String novaInstituicao){
        return UsuarioDAO.alterarInstituicaoDAO(novaInstituicao);
    }

    /**
     * Alterar senha boolean.
     *
     * @param novaSenha the nova senha
     * @return the boolean
     */
    public static boolean alterarSenhaServices(String novaSenha){
        return UsuarioDAO.alterarSenhaDAO(novaSenha);
    }
}
