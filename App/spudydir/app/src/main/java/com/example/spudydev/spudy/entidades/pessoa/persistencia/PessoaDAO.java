package com.example.spudydev.spudy.entidades.pessoa.persistencia;

import com.example.spudydev.spudy.entidades.pessoa.dominio.Pessoa;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.google.firebase.database.DatabaseException;

/**
 * The type Pessoa dao.
 */
public final class PessoaDAO {

    private PessoaDAO(){
        //Construtor vazio
    }

    /**
     * Inserir boolean.
     *
     * @param pessoa the pessoa
     * @return the boolean
     */
//Cadastrar pessoa no banco
    public static boolean inserirPessoaDAO(Pessoa pessoa) {
        boolean verificador;

        try {
            //Obtendo uid do usuário atual
            String uidUsuario = AcessoFirebase.getUidUsuario();
            //Salvando pessoa
            AcessoFirebase.getFirebase().child("pessoa").child(uidUsuario).setValue(pessoa);

            verificador = true;
        } catch (DatabaseException e){
            verificador = false;
        }
        return verificador;
    }

    /**
     * Alterar data nascimento boolean.
     *
     * @param novaData the nova data
     * @return the boolean
     */
//Alterando data de nascimento
    public static boolean alterarDataNascimentoDAO(String novaData){
        boolean verificador;

        try {
            AcessoFirebase.getFirebase().child("pessoa").child(AcessoFirebase.getUidUsuario()).child("dataNascimento").setValue(novaData);
            verificador = true;
        }catch (DatabaseException e){
            verificador = false;
        }
        return verificador;
    }

    /**
     * Alterar nome boolean.
     *
     * @param novoNome the novo nome
     * @return the boolean
     */
//Alterando nome
    public static boolean alterarNomeDAO(String novoNome){
        boolean verificador;

        try {
            AcessoFirebase.getFirebase().child("pessoa").child(AcessoFirebase.getUidUsuario()).child("nome").setValue(novoNome);
            verificador = true;
        }catch (DatabaseException e){
            verificador = false;
        }
        return verificador;
    }
}
