package com.example.spudydev.spudy.infraestrutura.persistencia;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * The type Acesso firebase.
 */
//Classe de acesso ao Firebase.
public final class AcessoFirebase {

    private AcessoFirebase(){
        //Construtor vazio
    }

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth autenticacao;

    /**
     * Get firebase database reference.
     *
     * @return the database reference
     */
    public static DatabaseReference getFirebase(){
        if (referenciaFirebase == null){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    /**
     * Get firebase autenticacao firebase auth.
     *
     * @return the firebase auth
     */
    public static FirebaseAuth getFirebaseAutenticacao(){
        if (autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

    /**
     * Get uid usuario string.
     *
     * @return the string
     */
    public static String getUidUsuario(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}