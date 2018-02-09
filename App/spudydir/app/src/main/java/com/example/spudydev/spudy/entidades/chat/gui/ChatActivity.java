package com.example.spudydev.spudy.entidades.chat.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.chat.dominio.Mensagem;
import com.example.spudydev.spudy.entidades.chat.negocio.MensagemServices;
import com.example.spudydev.spudy.entidades.falta.gui.MainFaltaAlunoActivity;
import com.example.spudydev.spudy.entidades.falta.gui.MainFaltaProfessorActivity;
import com.example.spudydev.spudy.entidades.material.gui.MainMaterialAlunoActivity;
import com.example.spudydev.spudy.entidades.material.gui.MainMaterialProfessorActivity;
import com.example.spudydev.spudy.entidades.nota.gui.MainNotasAlunoActivity;
import com.example.spudydev.spudy.entidades.nota.gui.MainNotasProfessorActivity;
import com.example.spudydev.spudy.entidades.turma.gui.UsuariosTurmaAlunoActivity;
import com.example.spudydev.spudy.entidades.turma.gui.UsuariosTurmaProfessorActivity;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private TextView tvNomeTurma;
    private String codigoTurma;
    private EditText edtChatBarraMensagem;
    private ListView lvMensagens;
    private VerificaConexao verificaConexao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();

        tvNomeTurma = findViewById(R.id.tvChatNomeTurma);
        Button btnChatUsuario = findViewById(R.id.btnChatUsuario);
        Button btnChatFaltas = findViewById(R.id.btnChatFaltas);
        Button btnChatMateriais = findViewById(R.id.btnChatMateriais);
        Button btnChatNotas = findViewById(R.id.btnChatNotas);
        Button btnChatEnviarMensagem = findViewById(R.id.btnChatEnviarMensagem);
        edtChatBarraMensagem = findViewById(R.id.edtChatBarraMensagem);
        lvMensagens = findViewById(R.id.lvMensagens);
        codigoTurma = getIntent().getStringExtra("codigoTurma");

        verificaConexao = new VerificaConexao(this);

        carregaNomeTurma();
        carregarMensagens();

        //Envio de mensagens em Real Time
        AcessoFirebase.getFirebase().child("chat").child(codigoTurma).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                carregarMensagens();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                carregarMensagens();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnChatEnviarMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaConexao.estaConectado()) {

                    MensagemServices mensagemServices = new MensagemServices();
                    String texto = edtChatBarraMensagem.getText().toString();

                    //Método que verifica campos e chama o método de inserir no banco "enviarMensagem()"
                    mensagemServices.verificarMensagem(ChatActivity.this, texto, codigoTurma);
                    edtChatBarraMensagem.setText("");
                }else{
                    Auxiliar.criarToast(getApplicationContext(),getString(R.string.sp_conexao_falha));
                }
            }
        });
        btnChatUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaUsuarios();
            }
        });

        btnChatFaltas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaFaltas();
            }
        });

        btnChatMateriais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaMaterial();
            }
        });

        btnChatNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaNotas();
            }
        });
    }
    public void carregaNomeTurma(){
        AcessoFirebase.getFirebase().child("turma").child(codigoTurma).child("nomeTurma").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvNomeTurma.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Direcionando o usuário para a tela de notas de acordo com seu tipo de conta
    public void abrirTelaNotas(){
        AcessoFirebase.getFirebase().child("usuario").child(AcessoFirebase.getUidUsuario()).child("tipoConta").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals("Aluno")){
                    abrirTelaNotasAluno();
                }else {
                    abrirTelaNotasProfessor();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Direcionando o usuário pelo seu tipo de conta
    public void abrirTelaMaterial(){
        AcessoFirebase.getFirebase().child("usuario").child(AcessoFirebase.getUidUsuario()).child("tipoConta").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals("Aluno")) {
                    abrirTelaMainMaterialAluno();
                } else {
                    abrirTelaMainMaterialProfessor();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    //Intent para a tela de faltas
    public void abrirTelaFaltas(){
        AcessoFirebase.getFirebase().child("usuario").child(AcessoFirebase.getUidUsuario()).child("tipoConta").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //verificando o tipo de conta para direcionar o usuário a sua respectiva tela
                if (dataSnapshot.getValue().toString().equals("Aluno")){
                    abrirTelaFaltasAluno();
                }else{
                    abrirTelaFaltasProfessor();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Intent para a tela usuarios
    public void abrirTelaUsuarios(){
        AcessoFirebase.getFirebase().child("usuario").child(AcessoFirebase.getUidUsuario()).child("tipoConta").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //verificando o tipo de conta para direcionar o usuário a sua respectiva tela
                if (dataSnapshot.getValue().toString().equals("Aluno")){
                    abrirTelaUsuariosTurmaAluno();
                }else{
                    abrirTelaUsuariosTurmaProfessor();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //Carregando mensagens salvas no banco
    public void carregarMensagens(){
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> listMensagem = montandoArrayListMensagem(dataSnapshot);
                setListViewMensagens(listMensagem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Montando o RetornoLista com as mensagens da turma
    private List<String> montandoArrayListMensagem(DataSnapshot dataSnapshot) {
        Iterator<DataSnapshot> dataSnapshots = dataSnapshot.child("chat").child(codigoTurma).getChildren().iterator();
        List<String> listMensagem = new ArrayList<>();
        while (dataSnapshots.hasNext()){
            DataSnapshot dataSnapshotChild = dataSnapshots.next();
            if (!dataSnapshotChild.getKey().equals("SENTINELA")) {
                Mensagem mensagem = dataSnapshotChild.getValue(Mensagem.class);
                listMensagem.add(mensagem.getTexto()+"\n~ "+dataSnapshot.child("pessoa").child(mensagem.getAutor()).child("nome").getValue(String.class));
            }
        }
        return listMensagem;
    }
    //Setando o ListView com as mesnagens do chat
    public void setListViewMensagens(List<String> listaTurmaAluno){
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaTurmaAluno);
        lvMensagens.setAdapter(itemsAdapter);
        lvMensagens.setSelection(lvMensagens.getAdapter().getCount()-1);
    }
    //Intent para a tela de usuários em relação ao professor
    public void abrirTelaUsuariosTurmaProfessor() {
        Intent intent = new Intent(getApplicationContext(), UsuariosTurmaProfessorActivity.class);
        intent.putExtra("codigoTurma",codigoTurma);
        startActivity(intent);
    }
    //Intent para a tela de usuários em relação ao aluno
    public void abrirTelaUsuariosTurmaAluno() {
        Intent intent = new Intent(getApplicationContext(), UsuariosTurmaAlunoActivity.class);
        intent.putExtra("codigoTurma",codigoTurma);
        startActivity(intent);
    }
    //Intent para a tela Aluno
    public void abrirTelaMainMaterialProfessor() {
        Intent intent = new Intent(getApplicationContext(), MainMaterialProfessorActivity.class);
        intent.putExtra("nomeTurma", tvNomeTurma.getText().toString());
        intent.putExtra("codigoTurma",codigoTurma);
        startActivity(intent);
    }
    //Intent para a tela professor
    public void abrirTelaMainMaterialAluno() {
        Intent intent = new Intent(getApplicationContext(), MainMaterialAlunoActivity.class);
        intent.putExtra("nomeTurma", tvNomeTurma.getText().toString());
        intent.putExtra("codigoTurma",codigoTurma);
        startActivity(intent);
    }
    //Intent para a tela de notas do aluno
    public void abrirTelaNotasAluno() {
        Intent intent = new Intent(getApplicationContext(), MainNotasAlunoActivity.class);
        intent.putExtra("codigoTurma", codigoTurma);
        startActivity(intent);
    }
    //Intent para a tela de notas do professor
    public void abrirTelaNotasProfessor() {
        Intent intent = new Intent(getApplicationContext(), MainNotasProfessorActivity.class);
        intent.putExtra("codigoTurma", codigoTurma);
        startActivity(intent);
    }
    //Intent para a tela de faltas do professor
    public void abrirTelaFaltasProfessor() {
        Intent intent = new Intent(getApplicationContext(), MainFaltaProfessorActivity.class);
        intent.putExtra("codigoTurma", codigoTurma);
        startActivity(intent);
    }
    //Intent para a tela de faltas do aluno
    public void abrirTelaFaltasAluno() {
        Intent intent = new Intent(getApplicationContext(), MainFaltaAlunoActivity.class);
        intent.putExtra("codigoTurma", codigoTurma);
        intent.putExtra("nomeTurma", tvNomeTurma.getText().toString());
        startActivity(intent);
    }
}