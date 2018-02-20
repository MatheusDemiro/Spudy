package com.example.spudydev.spudy.entidades.falta.gui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.falta.dominio.Falta;
import com.example.spudydev.spudy.entidades.falta.negocio.FaltaServices;
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
 * The type Main falta professor activity.
 */
public class MainFaltaProfessorActivity extends AppCompatActivity {

    private ListView lvAlunosTurma;
    private EditText edtDataChamada;

    private List<HashMap<String, String>> listaNomeAlunos = new ArrayList<>();
    private List<String> listaUidAlunos = new ArrayList<>();

    private VerificaConexao verificaConexao;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_falta_professor);

        lvAlunosTurma = findViewById(R.id.lvAlunosTurma);
        edtDataChamada = findViewById(R.id.edtDataChamada);

        //Máscara para data
        Auxiliar.criarMascara(edtDataChamada);

        //Instanciando objeto da classe VerificaConexao
        verificaConexao = new VerificaConexao(this);

        //Carregando os alunos cadastrados na turma
        carregarAlunosTurma();

        lvAlunosTurma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (verificaConexao.estaConectado()) {
                    if (verificarCampo()) {
                        confirmarPresenca(position);
                    }
                } else {
                    Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_conexao_falha));
                }
            }
        });
    }
    //Método que exibe janela de diálogo e executa a falta ou presença no banco
    private void confirmarPresenca(final int posicao) {
        final String codigoTurma = getIntent().getStringExtra("codigoTurma");
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Atribuir presença");
        //define a mensagem
        builder.setMessage("Presente ou Faltou?");
        //define um botão como positivo
        builder.setPositiveButton("Presente", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1) {
                    inserirFalta(listaUidAlunos.get(posicao), codigoTurma,true);
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Faltou", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                inserirFalta(listaUidAlunos.get(posicao), codigoTurma,false);
            }
        });
        //cria o AlertDialog
        AlertDialog alertaPresenca = builder.create();
        //Exibe
        alertaPresenca.show();
    }
    //Método que chama a camda de negócio
    private void inserirFalta(String uidAluno, String codigoTurma, boolean presenca){

        if (FaltaServices.inserirFaltaServices(criaFalta(presenca), codigoTurma, uidAluno)){
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_presenca_inserida_sucesso));
        } else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_database));
        }
    }
    //Criando o objeto falta e setando o atributo presenca de acordo com o boolean
    private Falta criaFalta(boolean presenca){

        Falta falta = new Falta();
        falta.setData(edtDataChamada.getText().toString());

        if (presenca) {
            falta.setPresenca(true);
        }else {
            falta.setPresenca(false);
        }
        return falta;
    }
    //Método que verifica o campo data
    private boolean verificarCampo(){
        boolean verificador = true;

        if (edtDataChamada.getText().toString().isEmpty()){
            edtDataChamada.setError(getString(R.string.sp_excecao_campo_vazio));
            verificador = false;
        }
        return verificador;
    }
    //Método que carrega os alunos da turma no ListView
    private void carregarAlunosTurma() {
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
        for (DataSnapshot dataSnapshotChild : referencia) {
            if (!dataSnapshotChild.getKey().equals(AcessoFirebase.getUidUsuario())) {
                listaUidAlunos.add(dataSnapshotChild.getKey());
            }
        }
    }

    //setando o ListView com o auxílio do ArrayAdapter<>
    private void montandoArrayListUsuarios(DataSnapshot dataSnapshot) {
        //RetornoLista que será composto pelos usuários que serão apresentados na tela

        //laço para separar a camada de apresentação ao usuário
        for (String i : listaUidAlunos) {
            if (!AcessoFirebase.getUidUsuario().equals(i)) {
                HashMap<String, String> dicionarioNomeAluno = new HashMap<>();
                String nomeAluno = dataSnapshot.child("pessoa").child(i).child("nome").getValue(String.class);

                dicionarioNomeAluno.put("nome", nomeAluno.toUpperCase());
                listaNomeAlunos.add(dicionarioNomeAluno);
            }
        }
        //Chamando método para setar o ListView de usuários da turma
        setListViewAlunosTurma();
    }
    //setando ListView com o auxílio de um SimpleAdapter
    private void setListViewAlunosTurma() {
        SimpleAdapter adapter = new SimpleAdapter(this, listaNomeAlunos, R.layout.modelo_list_view_chamada,
                new String[]{"nome"}, new int[]{R.id.txtNomeAluno});
        lvAlunosTurma.setAdapter(adapter);
    }
}
