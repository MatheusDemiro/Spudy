package com.example.spudydev.spudy.entidades.turma.gui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.aluno.gui.MainAlunoActivity;
import com.example.spudydev.spudy.entidades.turma.negocio.TurmaServices;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The type Usuarios turma aluno activity.
 */
public class UsuariosTurmaAlunoActivity extends AppCompatActivity {

    //ListView para onde irá o SimpleAdapter
    private ListView lvUsuariosTurma;

    //ArrayList com os UIDs dos usuários da turma
    private List<String> listaUidUsuarios = new ArrayList<>();

    //ArrayList com os dicionários dos usuários para inserirUsuarioDAO no SimpleAdapter
    private List<HashMap<String, String>> listaUsuarios = new ArrayList<>();

    //Janela de diálogo
    private AlertDialog alertaSair;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
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
                Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_sair_turma));
            }
        });

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertaSairTurma();
            }
        });
    }
    //carregando usuários cadastrados na turma
    private void carregarUsuarios() {
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getUidUsuariosTurma(dataSnapshot);
                montandoArrayListUsuarios(dataSnapshot);
                setListViewUsuariosTurma();
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
    private void montandoArrayListUsuarios(DataSnapshot dataSnapshot){
        //laço para separar a camada de apresentação ao usuário
        for (String uidUsuario: listaUidUsuarios){
                HashMap<String,String> dicionarioUsuarios = new HashMap<>();

                if (AcessoFirebase.getUidUsuario().equals(uidUsuario)){
                    dicionarioUsuarios.put("nome", "Você");
                    dicionarioUsuarios.put("tipoConta", "Aluno");
                    listaUsuarios.add(dicionarioUsuarios);
                }else {
                    String nomeUsuario = dataSnapshot.child("pessoa").child(uidUsuario).child("nome").getValue(String.class);
                    String tipoConta = dataSnapshot.child("usuario").child(uidUsuario).child("tipoConta").getValue(String.class);

                    dicionarioUsuarios.put("nome", nomeUsuario);
                    dicionarioUsuarios.put("tipoConta", tipoConta);
                    listaUsuarios.add(dicionarioUsuarios);
                }
        }
    }
    //setando ListView com o auxílio de um SimpleAdapter
    private void setListViewUsuariosTurma() {
        SimpleAdapter adapter = new SimpleAdapter(this, listaUsuarios,R.layout.modelo_list_view_usuarios,
                new String[]{"nome","tipoConta"},
                new int[]{R.id.txtNomeUsuarioTurma,
                        R.id.txtTipoConta});
        lvUsuariosTurma.setAdapter(adapter);
    }
    //método que executa a saída do usuário da turma
    private void alertaSairTurma() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Define o titulo
        builder.setTitle(getString(R.string.sp_dialogo_titulo));
        //Define a mensagem
        builder.setMessage(getString(R.string.sp_dialogo_mensagem_sair_turma));
        //Define um botão como positivo
        builder.setPositiveButton(getString(R.string.sp_dialogo_sim), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                verificandoSairTurma();
            }
        });
        //Define um botão como negativo.
        builder.setNegativeButton(getString(R.string.sp_dialogo_nao), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alertaSair.dismiss();
            }
        });
        //Cria o AlertDialog
        alertaSair = builder.create();

        alertaSair.show();
    }
    //Verificando a conexão antes de chamar a camada de negócio
    private void verificandoSairTurma() {
        VerificaConexao verificaConexao = new VerificaConexao(getApplicationContext());
        if (verificaConexao.estaConectado()) {
            if (TurmaServices.sairDaTurmaServices(getIntent().getStringExtra("codigoTurma"))) {
                abrirTelaMainAlunoActivity();
            }else {
                Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_sair_turma));
            }
        }else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_sair_turma_conexão));
        }
    }
    //Intent
    private void abrirTelaMainAlunoActivity(){
        Intent intent = new Intent(UsuariosTurmaAlunoActivity.this, MainAlunoActivity.class);
        startActivity(intent);
    }
}
