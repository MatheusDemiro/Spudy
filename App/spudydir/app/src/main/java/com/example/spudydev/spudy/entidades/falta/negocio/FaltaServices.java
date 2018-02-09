package com.example.spudydev.spudy.entidades.falta.negocio;

import com.example.spudydev.spudy.entidades.falta.dominio.Falta;
import com.example.spudydev.spudy.entidades.falta.persistencia.FaltaDAO;

public class FaltaServices {
    //Verificando se o professor está fazendo a chamada de um mesmo aluno duas vezes
    public void inserirFalta(Falta falta, String codigoTurma, String uidAluno) {
        FaltaDAO faltaDAO = new FaltaDAO();
        faltaDAO.inserir(falta, codigoTurma, uidAluno);
    }
}
