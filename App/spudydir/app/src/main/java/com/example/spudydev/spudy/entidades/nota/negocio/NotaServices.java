package com.example.spudydev.spudy.entidades.nota.negocio;

import android.widget.ListView;

import com.example.spudydev.spudy.entidades.nota.dominio.Nota;
import com.example.spudydev.spudy.entidades.nota.persistencia.NotaDAO;

public class NotaServices {

    //Inserindo notas no banco
    public void inserirNota(Nota nota, String uidUsuario, String codigoTurma) {
        NotaDAO notaDAO = new NotaDAO();
        notaDAO.inserir(nota, uidUsuario, codigoTurma);
    }
}
