package com.example.spudydev.spudy.entidades.endereco.negocio;

import com.example.spudydev.spudy.entidades.endereco.dominio.Endereco;
import com.example.spudydev.spudy.entidades.endereco.persistencia.EnderecoDAO;

/**
 * The type Endereco services.
 */
public final class EnderecoServices {

    private EnderecoServices(){
        //Construtor vazio
    }

    /**
     * Inserir endereco boolean.
     *
     * @param endereco the endereco
     * @return the boolean
     */
//Chamando método de inserirUsuarioDAO endereço da camada DAO
    public static boolean inserirEndereco(Endereco endereco){
        return EnderecoDAO.inserir(endereco);
    }

    /**
     * Alterar endereco boolean.
     *
     * @param endereco the endereco
     * @return the boolean
     */
//Chamando método de alterar endereço da camada DAO
    public static boolean alterarEndereco(Endereco endereco){
        return EnderecoDAO.alterarEndereco(endereco);
    }
}
