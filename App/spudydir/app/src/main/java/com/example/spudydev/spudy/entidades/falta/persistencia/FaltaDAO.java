package com.example.spudydev.spudy.entidades.falta.persistencia;

import com.example.spudydev.spudy.entidades.falta.dominio.Falta;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.google.firebase.database.DatabaseException;

/**
 * The type Falta dao.
 */
public final class FaltaDAO {

    private FaltaDAO(){
        //Construtor vazio
    }

    /**
     * Inserir.
     *
     * @param falta       the falta
     * @param codigoTurma the codigo turma
     * @param uidAluno    the uid aluno
     * @return the boolean
     */
//Método que chamada a camada de DAO para inserirUsuarioDAO a falta
    public static boolean inserirFaltasDAO(Falta falta, String codigoTurma, String uidAluno){
        boolean verificador;

        try{
            AcessoFirebase.getFirebase().child("falta").child(codigoTurma).child(uidAluno).push().setValue(falta);
            verificador = true;
        } catch (DatabaseException e){
            verificador = false;
        }
        return verificador;
    }
}
