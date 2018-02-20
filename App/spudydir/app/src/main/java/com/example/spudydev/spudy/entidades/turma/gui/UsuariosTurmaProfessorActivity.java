package com.example.spudydev.spudy.entidades.turma.gui;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.turma.negocio.TurmaServices;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The type Usuarios turma professor activity.
 */
public class UsuariosTurmaProfessorActivity extends AppCompatActivity {

    //ListView
    private ListView lvUsuariosTurma;

    //Lista com UID's dos usuários da turma
    private List<String> listaUidUsuarios = new ArrayList<>();

    //ArrayList com os dicionários dos usuários para inserirUsuarioDAO no SimpleAdapter
    private List<HashMap<String, String>> arrayListUsuarios = new ArrayList<>();

    private AlertDialog alertaRemocao;
    private String codigoTurma;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios_turma_professor);
        getSupportActionBar().hide();

        //instanciando o ListView/Button
        lvUsuariosTurma = findViewById(R.id.lvUsuariosTurmaProfessor);

        FloatingActionButton btnInformacaoSair = findViewById(R.id.btnInformacaoSairProfessor);

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

    private void confirmarRemocao(final int position) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle(R.string.sp_dialogo_titulo_remover);
        //define a mensagem
        builder.setMessage(R.string.sp_dialogo_excluir_usuario);
        //define um botão como positivo
        builder.setPositiveButton(R.string.sp_dialogo_sim, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //chamar método
                removerUsuario(listaUidUsuarios.get(position));
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton(R.string.sp_dialogo_nao, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alertaRemocao.dismiss();
            }
        });
        //cria o AlertDialog
        alertaRemocao = builder.create();
        //Exibe
        alertaRemocao.show();
    }
    //Chamando camada de negócio
    private void removerUsuario(String uidUsuario) {
        if (uidUsuario.equals(AcessoFirebase.getUidUsuario())) {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_remover_professor));

        } else {
            if (TurmaServices.removerAlunoTurmaServices(codigoTurma, uidUsuario)){
                Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_usuario_removido_sucesso));
            }else {
                Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_database));
            }
        }
    }
    //carregando usuários cadastrados na turma
    private void carregarUsuarios() {

        arrayListUsuarios.clear();
        listaUidUsuarios.clear();

        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getUidUsuariosTurma(dataSnapshot);
                montandoArrayListUidUsuarios(dataSnapshot);
                setListViewUsuariosTurma();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //Método que recupera um Iterable com os uids dos usuários e os transfere para um RetornoLista
    private void getUidUsuariosTurma(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> referencia = dataSnapshot.child("turma").child(codigoTurma).child("usuariosDaTurma").getChildren();

        //For percorrendo e adicionando os UID's dos usuarios a listaUidUsuarios.
        for (DataSnapshot dataSnapshotChild : referencia) {
            listaUidUsuarios.add(dataSnapshotChild.getKey());
        }
    }
    //Montando o dicionario para ser adicionado ao SimpleAdapter
    private void montandoArrayListUidUsuarios(DataSnapshot dataSnapshot) {
        for (String uidUsario : listaUidUsuarios) {
            //Dicionário que irá compor o ListView
            HashMap<String, String> dicionarioUsuarios = new HashMap<>();

            if (AcessoFirebase.getUidUsuario().equals(uidUsario)) {
                dicionarioUsuarios.put("nome", "Você");
                dicionarioUsuarios.put("tipoConta", "Professor");
                arrayListUsuarios.add(dicionarioUsuarios);
            } else {
                String nomeUsuario = dataSnapshot.child("pessoa").child(uidUsario).child("nome").getValue(String.class);
                dicionarioUsuarios.put("nome", nomeUsuario);
                dicionarioUsuarios.put("tipoConta", "Aluno");
                arrayListUsuarios.add(dicionarioUsuarios);
            }
        }
    }
    //Setando o ListView para o adapter
    private void setListViewUsuariosTurma() {
        SimpleAdapter adapter = new SimpleAdapter(this, arrayListUsuarios, R.layout.modelo_list_view_usuarios,
                new String[]{"nome", "tipoConta"},
                new int[]{R.id.txtNomeUsuarioTurma,
                        R.id.txtTipoConta});
        lvUsuariosTurma.setAdapter(adapter);
    }
}

