package com.example.spudydev.spudy.entidades.material.persistencia;

import com.example.spudydev.spudy.entidades.material.dominio.Material;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.google.firebase.database.DatabaseException;

/**
 * The type Material dao.
 */
public final class MaterialDAO {

    private MaterialDAO(){
        //Construtor vazio
    }

    /**
     * Adicionar material.
     *
     * @param material      the material
     * @param codigoDaTurma the codigo da turma
     * @return the boolean
     */
//adicionar o material em relação a turma
    public static boolean cadastrarMaterialDAO(Material material, String codigoDaTurma){
        boolean verificador;

        try {
            AcessoFirebase.getFirebase().child("material").child(codigoDaTurma).push().setValue(material);
            verificador = true;
        } catch (DatabaseException e) {
            verificador = false;
        }
        return verificador;
    }

    /**
     * Atualizar material boolean.
     *
     * @param material    the material
     * @param codigoTurma the codigo turma
     * @return the boolean
     */
//Atualizar material
    public static boolean atualizarMaterialDAO(Material material, String codigoTurma){
        boolean verificador;
        try {
            AcessoFirebase.getFirebase().child("material").child(codigoTurma).child(material.getIdMaterial()).setValue(material);
            verificador = true;
        }catch (DatabaseException e){
            verificador = false;
        }
        return verificador;
    }

    /**
     * Excluir material boolean.
     *
     * @param material    the material
     * @param codigoTurma the codigo turma
     * @return the boolean
     */
    public static boolean excluirMaterialDAO(Material material, String codigoTurma){
        boolean verificador;
        try {
            AcessoFirebase.getFirebase().child("material").child(codigoTurma).child(material.getIdMaterial()).setValue(null);
            verificador = true;
        }catch (DatabaseException e){
            verificador = false;
        }
        return verificador;

    }
}
