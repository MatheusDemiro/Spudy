package com.example.spudydev.spudy.Verificador;

import android.support.annotation.NonNull;

import com.example.spudydev.spudy.DAO.AcessoFirebase;
import com.example.spudydev.spudy.Entidades.Usuario;
import com.example.spudydev.spudy.Helper.MD5;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Matheus on 07/01/2018.
 */
// A classe re-cadastra o usuário e apaga o anterior! Besteira na estrutura :D
public class VerificadorAlterarEmailActivity {

    //String para pegar retorno no void caso coloque senha errada
    private String vef;

    public String Verificar(final String email, String senha) {

        if (email.isEmpty()) {
            return "O campo novo email está vazio.";
        }
        if (senha.isEmpty()) {
            return "O campo senha está vazio.";
        }
        return " :D  ";
    }
}

        //final FirebaseUser user = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser();

        //Abaixo pegamos o email do usuário atual e a senha que ele digitou, se for a senha correta ele altera seu email.

        //AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),senha);

       // user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>()