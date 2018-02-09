package com.example.spudydev.spudy.entidades.material.persistencia;

import com.example.spudydev.spudy.entidades.material.dominio.Material;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;

public class MaterialDAO {
    //instanciando e definindo os atributos do objeto material
    private static Material criarMaterial(String conteudo, String titulo) {
        Material material = new Material();
        material.setConteudo(conteudo);
        material.setTitulo(titulo);

        return material;
    }
    //adicionar o material em relação a turma
    public static void adicionarMaterial(String conteudo, String titulo, String codigoDaTurma){
        Material material = criarMaterial(conteudo, titulo);
        AcessoFirebase.getFirebase().child("material").child(codigoDaTurma).push().setValue(material);
    }
}
