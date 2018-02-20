package com.example.spudydev.spudy.entidades.endereco.persistencia;

import com.example.spudydev.spudy.entidades.endereco.dominio.Endereco;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.google.firebase.database.DatabaseException;

/**
 * The type Endereco dao.
 */
public final class EnderecoDAO {

    private EnderecoDAO() {
        //Construtor vazio
    }

    /**
     * Inserir boolean.
     *
     * @param endereco the endereco
     * @return the boolean
     */
//Cadastrar endereço
    public static boolean inserir(Endereco endereco) {
        boolean verificador;

        try {
            //Obtendo uid do usuário atual
            String uidUsuario = AcessoFirebase.getUidUsuario();
            //Salvando endereço no banco
            AcessoFirebase.getFirebase().child("endereco").child(uidUsuario).setValue(endereco);

            verificador = true;
        } catch (DatabaseException e) {
            verificador = false;
        }
        return verificador;
    }

    /**
     * Alterar endereco boolean.
     *
     * @param endereco the endereco
     * @return the boolean
     */
//Alterar endereço
    public static boolean alterarEndereco(Endereco endereco) {
        boolean verificador;
        try {
            String uidUsuario = AcessoFirebase.getUidUsuario();
            AcessoFirebase.getFirebase().child("endereco").child(uidUsuario).setValue(endereco);
            verificador = true;
        } catch (DatabaseException e){
            verificador = false;
        }
            return verificador;
        }
    }
