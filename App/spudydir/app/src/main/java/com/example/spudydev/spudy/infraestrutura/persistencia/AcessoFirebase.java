package com.example.spudydev.spudy.infraestrutura.persistencia;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Classe de acesso ao Firebase.
public class AcessoFirebase {
    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth autenticacao;

    public static DatabaseReference getFirebase(){
        if (referenciaFirebase == null){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }
    public static FirebaseAuth getFirebaseAutenticacao(){
        if (autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
    public static String getUidUsuario(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}