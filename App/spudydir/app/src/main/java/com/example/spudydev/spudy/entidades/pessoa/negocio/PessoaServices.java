package com.example.spudydev.spudy.entidades.pessoa.negocio;

import com.example.spudydev.spudy.entidades.pessoa.dominio.Pessoa;
import com.example.spudydev.spudy.entidades.pessoa.persistencia.PessoaDAO;

/**
 * The type Pessoa services.
 */
public final class PessoaServices {

    private PessoaServices(){
        //Construtor vazio
    }

    /**
     * Inserir pessoa boolean.
     *
     * @param pessoa the pessoa
     * @return the boolean
     */
//Método que chama a camada DAO para inserirUsuarioDAO pessoa
    public static boolean inserirPessoaServices(Pessoa pessoa){
        return PessoaDAO.inserirPessoaDAO(pessoa);
    }

    /**
     * Alterar data nascimento boolean.
     *
     * @param novaDataNascimento the nova data nascimento
     * @return the boolean
     */
//Método que chama a camdad DAO para alterar o endereço
    public static boolean alterarDataNascimentoServices(String novaDataNascimento){
        return PessoaDAO.alterarDataNascimentoDAO(novaDataNascimento);
    }

    /**
     * Alterar nome boolean.
     *
     * @param novoNome the novo nome
     * @return the boolean
     */
//Método que chama a camada DAO para alterar o nome
    public static boolean alterarNomeServices(String novoNome){
        return PessoaDAO.alterarNomeDAO(novoNome);
    }
}
