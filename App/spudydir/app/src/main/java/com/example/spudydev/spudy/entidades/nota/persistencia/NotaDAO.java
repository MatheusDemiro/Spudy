package com.example.spudydev.spudy.entidades.nota.persistencia;

import com.example.spudydev.spudy.entidades.nota.dominio.Nota;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.google.firebase.database.DatabaseException;

/**
 * The type Nota dao.
 */
public final class NotaDAO {

    private NotaDAO(){
        //Construtor vazio
    }

    /**
     * Inserir.
     *
     * @param nota        the nota
     * @param uidUsuario  the uid usuario
     * @param codigoTurma the codigo turma
     * @return the boolean
     */
//Inserindo nota no banco de dados
    public static boolean inserirNotaDAO(Nota nota, String uidUsuario, String codigoTurma){
        boolean verificador;

        try{
            AcessoFirebase.getFirebase().child("nota").child(codigoTurma).child(uidUsuario).push().setValue(nota);
            verificador = true;
        } catch (DatabaseException e){
            verificador = false;
        }
        return verificador;
    }
}
