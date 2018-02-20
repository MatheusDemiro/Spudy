package com.example.spudydev.spudy.entidades.chat.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.avaliacao.gui.AvaliacaoActivity;
import com.example.spudydev.spudy.entidades.chat.MensagemException;
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
import java.util.HashMap;
import java.util.List;

/**
 * The type Chat activity.
 */
public class ChatActivity extends AppCompatActivity {

    //Views utilizadas
    private TextView tvNomeTurma;
    private EditText edtChatBarraMensagem;
    private ListView lvMensagens;

    private String tipoConta;
    private String codigoTurma;

    //Objeto para verificar conexão com a internet
    private VerificaConexao verificaConexao;

    //ArrayList que irá comportar os dicionários com os nomes e mensagens do usuários
    private List<HashMap<String, String>> listaMensagem = new ArrayList<>();

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();

        //Instanciando Button's|EditText|TextView|ListView
        tvNomeTurma = findViewById(R.id.tvChatNomeTurma);
        Button btnChatUsuario = findViewById(R.id.btnChatUsuario);
        Button btnChatFaltas = findViewById(R.id.btnChatFaltas);
        Button btnChatNotas = findViewById(R.id.btnChatNotas);
        Button btnChatMateriais = findViewById(R.id.btnChatMateriais);
        Button btnAvaliacao = findViewById(R.id.btnAvaliarTurma);
        Button btnChatEnviarMensagem = findViewById(R.id.btnChatEnviarMensagem);
        edtChatBarraMensagem = findViewById(R.id.edtChatBarraMensagem);
        lvMensagens = findViewById(R.id.lvMensagens);

        //Instanciando objeto para verificar conexão
        verificaConexao = new VerificaConexao(this);

        //Resgatando código de turma da intent
        codigoTurma = getIntent().getStringExtra("codigoTurma");

        //Resgatando o tipo de conta do usuário para direcioná-lo a respectiva tela
        resgatandoTipoConta();

        //Carregando nome da turma no TextView
        tvNomeTurma.setText(getIntent().getStringExtra("nomeTurma"));

        //Carregando as mensagens enviadas pelos usuários
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
                    verificarMensagem();
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

        btnAvaliacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaAvaliacao();
            }
        });
    }
    //Verificando a view e o Exception
    private void verificarMensagem() {
        try{
            if (verificaCampo()) {
                String texto = edtChatBarraMensagem.getText().toString();
                Mensagem mensagem = criarMensagem(texto);

                MensagemServices.enviarMensagem(codigoTurma, mensagem);

                edtChatBarraMensagem.setText("");
            }
        }catch (MensagemException e){
            Auxiliar.criarToast(getApplicationContext(), e.getMessage());
        }
    }
    //Verificando a barra de mensagem do chat
    private boolean verificaCampo(){
        boolean verificador = true;

        if (edtChatBarraMensagem.getText().toString().isEmpty()){
            verificador = false;
        }
        if (edtChatBarraMensagem.getText().toString().trim().length() == 0){
            verificador = false;
        }
        return verificador;
    }
    //Criando objeto mensagem
    private Mensagem criarMensagem(String texto){
        Mensagem mensagem = new Mensagem();
        mensagem.setAutor(AcessoFirebase.getUidUsuario());
        mensagem.setTexto(texto);
        return mensagem;
    }
    //Recuperando tipo de conta do usuário
    private void resgatandoTipoConta(){
        AcessoFirebase.getFirebase().child("usuario").child(AcessoFirebase.getUidUsuario()).child("tipoConta").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tipoConta = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //Carregando mensagens salvas no banco
    private void carregarMensagens(){
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                montandoArrayListMensagem(dataSnapshot);
                setListViewMensagens();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Montando o RetornoLista com as mensagens da turma
    private void montandoArrayListMensagem(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> referencia = dataSnapshot.child("chat").child(codigoTurma).getChildren();

        for (DataSnapshot dataSnapshotChild: referencia){
            if (!dataSnapshotChild.getKey().equals("SENTINELA")) {
                HashMap<String, String> dicionarioMensagem = new HashMap<>();

                Mensagem mensagem = dataSnapshotChild.getValue(Mensagem.class);
                if (mensagem != null) {
                    String nome = dataSnapshot.child("pessoa").child(mensagem.getAutor()).child("nome").getValue(String.class);
                    String texto = mensagem.getTexto();

                    dicionarioMensagem.put("nome", nome);
                    dicionarioMensagem.put("mensagem", texto);
                    listaMensagem.add(dicionarioMensagem);
                }
            }
        }
    }
    //Setando o ListView com as mesnagens do chat
    private void setListViewMensagens(){
        SimpleAdapter adapter = new SimpleAdapter(this, listaMensagem,R.layout.modelo_list_view_chat,
                new String[]{"nome","mensagem"},
                new int[]{R.id.txtNomeUsuario,
                        R.id.txtMensagem});
        lvMensagens.setAdapter(adapter);
        lvMensagens.setSelection(lvMensagens.getAdapter().getCount()-1);
    }
    //Direcionando o usuário para a tela de notas de acordo com seu tipo de conta
    private void abrirTelaNotas(){
        if (tipoConta.equals("Aluno")){
            abrirTelaNotasAluno();
        }else {
            abrirTelaNotasProfessor();
        }
    }
    //Direcionando o usuário pelo seu tipo de conta
    private void abrirTelaMaterial(){
        if (tipoConta.equals("Aluno")){
            abrirTelaMainMaterialAluno();
        } else {
            abrirTelaMainMaterialProfessor();
        }
    }
    //Intent para a tela de faltas
    private void abrirTelaFaltas(){
        if (tipoConta.equals("Aluno")){
            abrirTelaFaltasAluno();
        } else {
            abrirTelaFaltasProfessor();
        }
    }
    //Intent para a tela usuarios
    private void abrirTelaUsuarios(){
        if (tipoConta.equals("Aluno")){
            abrirTelaUsuariosTurmaAluno();
        } else {
            abrirTelaUsuariosTurmaProfessor();
        }
    }
    //Intent para a tela de avaliação
    private void abrirTelaAvaliacao(){
        if (tipoConta.equals("Aluno")){
            abrirTelaAvaliacaoAluno();
        }else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_professor_avaliar_turma));
        }
    }
    //Intent para a tela de usuários em relação ao professor
    private void abrirTelaUsuariosTurmaProfessor() {
        Intent intent = new Intent(getApplicationContext(), UsuariosTurmaProfessorActivity.class);
        intent.putExtra("codigoTurma",codigoTurma);
        startActivity(intent);
    }
    //Intent para a tela de usuários em relação ao aluno
    private void abrirTelaUsuariosTurmaAluno() {
        Intent intent = new Intent(getApplicationContext(), UsuariosTurmaAlunoActivity.class);
        intent.putExtra("codigoTurma",codigoTurma);
        startActivity(intent);
    }
    //Intent para a tela Aluno
    private void abrirTelaMainMaterialProfessor() {
        Intent intent = new Intent(getApplicationContext(), MainMaterialProfessorActivity.class);
        intent.putExtra("codigoTurma",codigoTurma);
        startActivity(intent);
    }
    //Intent para a tela professor
    private void abrirTelaMainMaterialAluno() {
        Intent intent = new Intent(getApplicationContext(), MainMaterialAlunoActivity.class);
        intent.putExtra("codigoTurma",codigoTurma);
        startActivity(intent);
    }
    //Intent para a tela de notas do aluno
    private void abrirTelaNotasAluno() {
        Intent intent = new Intent(getApplicationContext(), MainNotasAlunoActivity.class);
        intent.putExtra("codigoTurma", codigoTurma);
        startActivity(intent);
    }
    //Intent para a tela de notas do professor
    private void abrirTelaNotasProfessor() {
        Intent intent = new Intent(getApplicationContext(), MainNotasProfessorActivity.class);
        intent.putExtra("codigoTurma", codigoTurma);
        startActivity(intent);
    }
    //Intent para a tela de faltas do professor
    private void abrirTelaFaltasProfessor() {
        Intent intent = new Intent(getApplicationContext(), MainFaltaProfessorActivity.class);
        intent.putExtra("codigoTurma", codigoTurma);
        startActivity(intent);
    }
    //Intent para a tela de faltas do aluno
    private void abrirTelaFaltasAluno() {
        Intent intent = new Intent(getApplicationContext(), MainFaltaAlunoActivity.class);
        intent.putExtra("codigoTurma", codigoTurma);
        intent.putExtra("nomeTurma", tvNomeTurma.getText().toString());
        startActivity(intent);
    }
    //Intent para a tela de avaliação
    private void abrirTelaAvaliacaoAluno(){
        Intent intent = new Intent(getApplicationContext(), AvaliacaoActivity.class);
        intent.putExtra("codigoTurma", codigoTurma);
        startActivity(intent);
    }
}