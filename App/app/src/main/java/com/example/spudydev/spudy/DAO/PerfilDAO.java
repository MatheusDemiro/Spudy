package com.example.spudydev.spudy.DAO;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spudydev.spudy.Activity.Alteracao.AlterarNomeActivity;
import com.example.spudydev.spudy.Entidades.Pessoa;
import com.example.spudydev.spudy.Entidades.Usuario;
import com.example.spudydev.spudy.Helper.MD5;
import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.Verificador.VerificaConexao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by User on 08/01/2018.
 */

public class PerfilDAO {
    Context context;
    public PerfilDAO(Context context){

        this.context = context;
    }
    VerificaConexao verificaConexao = new VerificaConexao(context);

    public String alterarNome(String nome, String email){
        if (verificaConexao.estaConectado()){
            DatabaseReference referencia = AcessoFirebase.getFirebase().child("usuario").child(MD5.HASH(email)).child("nome");
            referencia.setValue(nome);
            return "Nome alterado com sucesso.";
        } else {
            return "Sem conexão com a internet.";
        }
    }
    public void setTxtPerfil(final TextView nomeConsulta,final TextView emailConsulta,final TextView dataNascimentoConsulta,final TextView instituicaoConsulta, FirebaseUser user){
        DatabaseReference referencia_usuario = AcessoFirebase.getFirebase().child("usuario").child(user.getUid());
        DatabaseReference referencia_pessoa = AcessoFirebase.getFirebase().child("pessoa").child(user.getUid());

        referencia_pessoa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Pessoa pessoa = dataSnapshot.getValue(Pessoa.class);
                nomeConsulta.setText(pessoa.getNome());
                dataNascimentoConsulta.setText(pessoa.getData_nascimento());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        referencia_usuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                emailConsulta.setText(usuario.getEmail());
                instituicaoConsulta.setText(usuario.getInstituicao());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
