package com.example.spudydev.spudy.entidades.material.negocio;

import android.widget.ListView;

import com.example.spudydev.spudy.entidades.material.persistencia.MaterialDAO;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by User on 05/02/2018.
 */

public class MaterialServices {
    public void cadastrarMaterial(String link, String titulo, String codigoDaTurma){
        MaterialDAO.adicionarMaterial(link, titulo, codigoDaTurma);
    }
}
