package com.example.spudydev.spudy.entidades.avaliacao.persistencia;

import com.example.spudydev.spudy.entidades.avaliacao.dominio.Avaliacao;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.google.firebase.database.DatabaseException;

/**
 * The type Avaliacao dao.
 */
public final class AvaliacaoDAO {

    private AvaliacaoDAO(){
        //Construtor vazio
    }

    /**
     * Inserir avaliacao boolean.
     *
     * @param avaliacao   the avaliacao
     * @param codigoTurma the codigo turma
     * @return the boolean
     */
//Inserindo avaliação no banco
    public static boolean inserirAvaliacao(Avaliacao avaliacao, String codigoTurma){
        boolean verificador;

        try {
            AcessoFirebase.getFirebase().child("avaliacao").child(AcessoFirebase.getUidUsuario()).child(codigoTurma).setValue(avaliacao);
            verificador = true;
        } catch (DatabaseException e){
            verificador = false;
        }
        return verificador;
    }
}
