package com.example.spudydev.spudy.perfil.negocio;

import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.TextView;

import com.example.spudydev.spudy.banco.AcessoFirebase;
import com.example.spudydev.spudy.pessoa.dominio.Pessoa;
import com.example.spudydev.spudy.usuario.dominio.Usuario;
import com.example.spudydev.spudy.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DadosMenuLateral {
    public void resgatarUsuario(final NavigationView navigationView, final FirebaseUser user){

        //Consultando banco de dados para resgatar o nome e email
        final DatabaseReference referencia = AcessoFirebase.getFirebase();

        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Pessoa pessoa = dataSnapshot.child("pessoa").child(user.getUid()).getValue(Pessoa.class);
                Usuario usuario = dataSnapshot.child("usuario").child(user.getUid()).getValue(Usuario.class);
                setUserName(navigationView , pessoa.getNome());
                setUserEmail(navigationView, usuario.getEmail());
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
