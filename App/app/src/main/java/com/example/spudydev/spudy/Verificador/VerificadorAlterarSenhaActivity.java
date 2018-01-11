package com.example.spudydev.spudy.Verificador;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.spudydev.spudy.DAO.AcessoFirebase;
import com.example.spudydev.spudy.Entidades.Usuario;
import com.example.spudydev.spudy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Matheus on 07/01/2018.
 */

public class VerificadorAlterarSenhaActivity {

    private String vef;

    public String Verificar(String email, String senha, final String senhaNova, final String senhaConfirma) {

        if (senha.isEmpty()){
            return "O campo senha antiga está vazio.";
        }

        if (senhaNova.isEmpty()){
            return "O campo senha nova está vazio.";
        }

        if (senhaConfirma.isEmpty()){
            return "O campo confirmar senha está vazio.";
        }
        //caso atualize a senha
        vef = "Senha alterada com Sucesso.";
        AcessoFirebase.getFirebaseAutenticacao().signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (senhaNova.equals(senhaConfirma)) {
                        FirebaseUser user = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser();
                        user.updatePassword(senhaNova);
                    }else{
                        vef = "Digite a mesma senha nos campos: Senha nova e Confirmar senha nova.";
                    }
                }else{
                    vef = "A senha atual é incorreta.";
                }
            }
        });
        return vef;
    }
}