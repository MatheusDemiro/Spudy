package com.example.spudydev.spudy.entidades.nota.persistencia;

import android.widget.ListView;

import com.example.spudydev.spudy.entidades.nota.dominio.Nota;
import com.example.spudydev.spudy.entidades.usuario.dominio.Usuario;
import com.example.spudydev.spudy.infraestrutura.MpooAppException;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.persistencia.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotaDAO {
    //Inserindo nota no banco de dados
    public void inserir(Nota nota, String uidUsuario, String codigoTurma){
        DatabaseReference ref = FirebaseHelper.instance.getDatabaseReference();
        DatabaseReference notas = ref.child("nota").child(codigoTurma).child(uidUsuario);
        DatabaseReference push = notas.push();
        push.setValue(nota);
    }
}
