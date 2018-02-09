package com.example.spudydev.spudy.entidades.turma.gui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.aluno.gui.MainAlunoActivity;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsuariosTurmaAlunoActivity extends AppCompatActivity {

    private ListView lvUsuariosTurma;
    private  List<String> listaUidUsuarios = new ArrayList<>();

    private AlertDialog alertaSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios_turma_aluno);
        getSupportActionBar().hide();

        //instanciando o ListView/Button
        lvUsuariosTurma = findViewById(R.id.lvUsuariosTurmaAluno);
        Button btnInfomacaoSair = findViewById(R.id.btnInformacaoSairAluno);
        Button btnSair = findViewById(R.id.btnSairTurma);

        //carregando usuários da turma
        carregarUsuarios();

        btnInfomacaoSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auxiliar.criarToast(getApplicationContext(), "Se você clicar em sair da turma perderá os dados da mesma.");
            }
        });

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sairTurma();
            }
        });
    }
    //carregando usuários cadastrados na turma
    public void carregarUsuarios() {
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getUidUsuariosTurma(dataSnapshot);
                montandoArrayListUsuarios(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //Método que recupera um Iterable com os uids dos usuários e os transfere para um RetornoLista
    private void getUidUsuariosTurma(DataSnapshot dataSnapshot) {
        //O getChildren() retorna um Iterable
        Iterable<DataSnapshot> referencia = dataSnapshot.child("turma").child(getIntent().getStringExtra("codigoTurma")).child("usuariosDaTurma").getChildren();
        //For que percorre o Iterable e adiciona os elementos percorridos ao RetornoLista "listaUidUsuarios"
        for (DataSnapshot dataSnapshotChild : referencia ) {
                listaUidUsuarios.add(dataSnapshotChild.getKey());
        }
    }
    //setando o ListView com o auxílio do ArrayAdapter<>
    public void montandoArrayListUsuarios(DataSnapshot dataSnapshot){
        //RetornoLista que será composto pelos usuários que serão apresentados na tela
        ArrayList<String> listViewUsuariosTurma = new ArrayList<>();

        //laço para separar a camada de apresentação ao usuário
        for (String i: listaUidUsuarios){
            if (AcessoFirebase.getUidUsuario().equals(i)) {
                listViewUsuariosTurma.add("Você" + "\n~ " + "Aluno");
            }else {
                String nomeUsuario = dataSnapshot.child("pessoa").child(i).child("nome").getValue(String.class);
                listViewUsuariosTurma.add( nomeUsuario + "\n~ " + "Professor");
            }
        }
        //Chamando método para setar o ListView de usuários da turma
        setListViewUsuariosTurma(listViewUsuariosTurma);
    }
    //setando ListView com o auxílio de um ArrayAdapter
    private void setListViewUsuariosTurma(ArrayList<String> listViewUsuariosTurma) {
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listViewUsuariosTurma);
        lvUsuariosTurma.setAdapter(itemsAdapter);
        lvUsuariosTurma.setSelection(lvUsuariosTurma.getAdapter().getCount()-1);
    }
    //método que executa a saída do usuário da turma
    public void sairTurma() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Define o titulo
        builder.setTitle("Sair");
        //Define a mensagem
        builder.setMessage("Tem certeza que deseja sair da turma?");
        //Define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                AcessoFirebase.getFirebase().child("turma").child(getIntent().getStringExtra("codigoTurma")).child("usuariosDaTurma").child(AcessoFirebase.getUidUsuario()).setValue(null);
                AcessoFirebase.getFirebase().child("aluno").child(AcessoFirebase.getUidUsuario()).child("turmas").child(getIntent().getStringExtra("codigoTurma")).setValue(null);
                abrirTelaMainAlunoActivity();
            }
        });
        //Define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alertaSair.dismiss();
            }
        });
        //Cria o AlertDialog
        alertaSair = builder.create();

        alertaSair.show();
    }
    //Intent
    public void abrirTelaMainAlunoActivity(){
        Intent intent = new Intent(UsuariosTurmaAlunoActivity.this, MainAlunoActivity.class);
        startActivity(intent);
    }
}
