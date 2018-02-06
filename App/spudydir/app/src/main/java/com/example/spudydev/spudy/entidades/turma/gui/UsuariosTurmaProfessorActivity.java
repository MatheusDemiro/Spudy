package com.example.spudydev.spudy.entidades.turma.gui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsuariosTurmaProfessorActivity extends AppCompatActivity {

    //ListView
    private ListView lvUsuariosTurma;

    //Lista com UID's dos usuários da turma
    private ArrayList<String> listaUidUsuarios = new ArrayList<>();

    //ArrayList que será composto pelas strings dos usuários que serão apresentados na tela. Ex: "Fulano\n~ Aluno"
    private ArrayList<String> arrayListUsuariosTurma = new ArrayList<>();

    //Private adapter
    private ArrayAdapter<String> itemsAdapter;


    private AlertDialog alertaRemocao;
    private String codigoTurma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios_turma_professor);

        //instanciando o ListView/Button
        lvUsuariosTurma = findViewById(R.id.lvUsuariosTurmaProfessor);

        Button btnInformacaoSair = findViewById(R.id.btnInformacaoSairProfessor);

        codigoTurma = getIntent().getStringExtra("codigoTurma");

        //carregando usuários da turma
        carregarUsuarios();

        btnInformacaoSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auxiliar.criarToast(UsuariosTurmaProfessorActivity.this, "Caso deseje remover um aluno selecione-o na lista e clique em OK para confirmar.");
            }
        });

        lvUsuariosTurma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                confirmarRemocao(position);
            }
        });
        AcessoFirebase.getFirebase().child("turma").child(codigoTurma).child("usuariosDaTurma").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                carregarUsuarios();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void confirmarRemocao(final int position) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Remover aluno");
        //define a mensagem
        builder.setMessage("Tem certeza que deseja excluir o usuário?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1) {
                //chamar método
                removerUsuario(listaUidUsuarios.get(position));
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alertaRemocao.dismiss();
            }
        });
        //cria o AlertDialog
        alertaRemocao = builder.create();
        //Exibe
        alertaRemocao.show();
    }
    public void removerUsuario(String uidUsuario) {
        if (uidUsuario.equals(AcessoFirebase.getUidUsuario())) {
            Auxiliar.criarToast(getApplicationContext(),"O professor não pode ser removido.");

        } else {
            AcessoFirebase.getFirebase().child("turma").child(codigoTurma).child("usuariosDaTurma").child(uidUsuario).setValue(null);
            AcessoFirebase.getFirebase().child("aluno").child(uidUsuario).child("turmas").child(codigoTurma).setValue(null);

        }
    }
    //carregando usuários cadastrados na turma
    public void carregarUsuarios() {

        arrayListUsuariosTurma.clear();
        listaUidUsuarios.clear();

        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getUidUsuariosTurma(dataSnapshot);
                montandoArrayListView(dataSnapshot);
                setListViewUsuariosTurma();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //Método que recupera um Iterable com os uids dos usuários e os transfere para um ArrayList
    private void getUidUsuariosTurma(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> referencia = dataSnapshot.child("turma").child(codigoTurma).child("usuariosDaTurma").getChildren();

        //For percorrendo e adicionando os UID's dos usuarios a listaUidUsuarios.
        for (DataSnapshot dataSnapshotChild : referencia) {
                listaUidUsuarios.add(dataSnapshotChild.getKey());
        }
    }

    private void montandoArrayListView(DataSnapshot dataSnapshot) {
        for (String i : listaUidUsuarios) {
            if (AcessoFirebase.getUidUsuario().equals(i)) {
                arrayListUsuariosTurma.add("Você" + "\n~ " + "Professor");
            } else {
                String nomeUsuario = dataSnapshot.child("pessoa").child(i).child("nome").getValue(String.class);
                arrayListUsuariosTurma.add(nomeUsuario + "\n~ " + "Aluno");
            }
       }
    }

    public void setListViewUsuariosTurma() {
        //Setando ListView com o auxílio de um ArrayAdapter
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListUsuariosTurma);
        lvUsuariosTurma.setAdapter(itemsAdapter);
        lvUsuariosTurma.setSelection(lvUsuariosTurma.getAdapter().getCount() - 1);

    }
}

