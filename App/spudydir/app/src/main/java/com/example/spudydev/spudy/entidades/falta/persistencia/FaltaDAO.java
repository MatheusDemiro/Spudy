package com.example.spudydev.spudy.entidades.falta.persistencia;

import com.example.spudydev.spudy.entidades.falta.dominio.Falta;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;

public class FaltaDAO {
    public void inserir(Falta falta, String codigoTurma, String uidAluno){
        AcessoFirebase.getFirebase().child("falta").child(codigoTurma).child(uidAluno).push().setValue(falta);
    }
}
