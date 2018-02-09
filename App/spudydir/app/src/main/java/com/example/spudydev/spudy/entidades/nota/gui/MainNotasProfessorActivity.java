package com.example.spudydev.spudy.entidades.nota.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.nota.dominio.Nota;
import com.example.spudydev.spudy.entidades.nota.negocio.NotaServices;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainNotasProfessorActivity extends AppCompatActivity {

    private Spinner spnAlunosTurma;
    private EditText edtTituloNota;
    private EditText edtNotaAluno;
    private ArrayList<String> listaUidUsuarios = new ArrayList<>();
    private ArrayList<String> listaNomesUsuarios = new ArrayList<>();
    private String uidUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_notas_professor);

        //Instanciando Spinner/EditText/Button
        spnAlunosTurma = findViewById(R.id.spnAlunosTurma);
        edtNotaAluno = findViewById(R.id.edtAlunoNota);
        edtTituloNota = findViewById(R.id.edtProfessorTituloNota);
        Button btnSalvarNota = findViewById(R.id.btnSalvarNotaAluno);
        //Fim instâncias

        spnAlunosTurma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                uidUsuario = listaUidUsuarios.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSalvarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificarCampos()){
                    NotaServices notaServices = new NotaServices();
                    notaServices.inserirNota(criarNota(),uidUsuario,getIntent().getStringExtra("codigoTurma"));

                    Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_nota_salva_sucesso));
                    edtNotaAluno.setText("");
                    edtTituloNota.setText("");
                    edtNotaAluno.clearFocus();
                }
            }
        });
        montandoSpinnerAlunosTurma();
    }
    //Criando o objeto nota
    public Nota criarNota(){

        Nota nota = new Nota();
        nota.setTituloNota(edtTituloNota.getText().toString());
        nota.setValorNota(Float.parseFloat(edtNotaAluno.getText().toString()));

        return nota;
    }
    //Verificando os campos de título e nota
    public boolean verificarCampos(){
        boolean verificador = true;

        if (edtTituloNota.getText().toString().trim().isEmpty()){
            edtTituloNota.setError(getString(R.string.sp_excecao_campo_vazio));
            verificador = false;
        }
        if (edtNotaAluno.getText().toString().trim().isEmpty()){
            edtNotaAluno.setError(getString(R.string.sp_excecao_campo_vazio));
            verificador = false;
        }
        if (Double.parseDouble(edtNotaAluno.getText().toString()) > 10.0){
            edtNotaAluno.setError(getString(R.string.sp_excecao_nota_aluno));
            verificador = false;
        }
        return verificador;
    }
    //Montando spinner com os alunos da turma
    public void montandoSpinnerAlunosTurma() {
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
    //Inserindo em um RetornoLista os UIDs dos usuários
    public void getUidUsuariosTurma(DataSnapshot dataSnapshot){
        Iterable<DataSnapshot> referencia = dataSnapshot.child("turma").child(getIntent().getStringExtra("codigoTurma")).child("usuariosDaTurma").getChildren();
        for (DataSnapshot dataSnapshotChild: referencia){
            if (!dataSnapshotChild.getKey().equals(AcessoFirebase.getUidUsuario())) {
                listaUidUsuarios.add(dataSnapshotChild.getKey());
            }
        }
    }
    //Setando o ListView com o auxílio do ArrayAdapter<>
    public void montandoArrayListUsuarios(DataSnapshot dataSnapshot) {
        //Laço para separar a camada de apresentação ao usuário
        for (String i : listaUidUsuarios) {
            if (!i.equals(AcessoFirebase.getUidUsuario())) {
                String nomeUsuario = dataSnapshot.child("pessoa").child(i).child("nome").getValue(String.class);
                listaNomesUsuarios.add(nomeUsuario + " - Aluno");
            }
        }
        setSpnAlunosTurma();
    }
    //Setando o spinner com os nomes dos alunos
    public void setSpnAlunosTurma(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaNomesUsuarios);
        spnAlunosTurma.setAdapter(arrayAdapter);
        spnAlunosTurma.setSelection(0);
    }
}