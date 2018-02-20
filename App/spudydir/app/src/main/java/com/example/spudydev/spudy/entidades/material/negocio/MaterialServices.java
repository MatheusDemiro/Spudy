package com.example.spudydev.spudy.entidades.material.negocio;

import com.example.spudydev.spudy.entidades.material.dominio.Material;
import com.example.spudydev.spudy.entidades.material.persistencia.MaterialDAO;

/**
 * The type Material services.
 */
public final class MaterialServices {

    private MaterialServices(){
        //Construtor vazio
    }

    /**
     * Cadastrar material.
     *
     * @param material    the material
     * @param codigoTurma the codigo turma
     * @return the boolean
     */
//Método que chama a camada DAO para cadastrar o material
    public static boolean cadastrarMaterialServices(Material material, String codigoTurma){
        return MaterialDAO.cadastrarMaterialDAO(material, codigoTurma);
    }

    /**
     * Atualizar material boolean.
     *
     * @param material    the material
     * @param codigoTurma the codigo turma
     * @return the boolean
     */
//Método que chama a camada DAO para atualizar o material
    public static boolean atualizarMaterialServices(Material material, String codigoTurma){
        return MaterialDAO.atualizarMaterialDAO(material, codigoTurma);
    }

    /**
     * Excluir material boolean.
     *
     * @param material    the material
     * @param codigoTurma the codigo turma
     * @return the boolean
     */
//Método que chama a camada DAO para excluir o material
    public static boolean excluirMaterialServices(Material material, String codigoTurma){
        return MaterialDAO.excluirMaterialDAO(material, codigoTurma);
    }
}
