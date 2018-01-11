package com.example.spudydev.spudy.DAO;

import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.TextView;

import com.example.spudydev.spudy.Helper.MD5;
import com.example.spudydev.spudy.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by User on 01/01/2018.
 */

public class DadosMenuDAO {
    public void resgatarUsuario(final NavigationView navigationView, final FirebaseUser user){

        //Consultando banco de dados para resgatar o nome e email
        DatabaseReference databaseReferenceUser = AcessoFirebase.getFirebase().child("usuario");

        databaseReferenceUser.child(MD5.HASH(user.getEmail())).child("nome").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nome = dataSnapshot.getValue(String.class);
                setUserName(navigationView, nome);
                setUserEmail(navigationView , user.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Método que seta o email para o TextView id: txt_nav_UserEmail
    private void setUserEmail(NavigationView navView, String email){
        View headerView = navView.getHeaderView(0);
        TextView userEmail = headerView.findViewById(R.id.txt_nav_header_usuario_email);
        userEmail.setText(email);
    }
    //Método que seta o nome para o TextView id: txt_nav_UserName
    private void setUserName(NavigationView navView, String nome){
        View headerView = navView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.txt_nav_header_usuario_nome);
        userName.setText(nome);
    }
}
