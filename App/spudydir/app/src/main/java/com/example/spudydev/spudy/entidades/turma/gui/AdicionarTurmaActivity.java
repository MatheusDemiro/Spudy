package com.example.spudydev.spudy.entidades.turma.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.turma.negocio.TurmaServices;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdicionarTurmaActivity extends AppCompatActivity {

    private EditText edtPesquisa;
    private ListView lvEntidade;
    private Spinner spnPesquisa;
    private ArrayAdapter<String> itemsAdapter;

    //ArrayList com o nome do professor da pesquisa
    private ArrayList<String> arrayNomesProfessores = new ArrayList<>();
    //ArrayList com o(s) UID(s) do professor(es)
    private ArrayList<String> arrayUidsProfessores = new ArrayList<>();

    //ArrayList com os códigos da turma
    private ArrayList<String> arrayIdsTurmas = new ArrayList<>();
    private ArrayList<String> arrayNomesTurmas = new ArrayList<>();

    //Private com a string selecionada do spinner
    private String pesquisa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_adicionar_turma);

        edtPesquisa = findViewById(R.id.edtPesquisa);
        lvEntidade = findViewById(R.id.lvTurmas);
        spnPesquisa = findViewById(R.id.spnPesquisa);

        spnPesquisa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Salvando o método de pesquisa (turma ou professor) em uma variável
                pesquisa = spnPesquisa.getItemAtPosition(position).toString().toLowerCase();
                if (pesquisa.equals("professor")){
                    resgantandoProfessores();
                }if (pesquisa.equals("turma")){
                    resgatandoTurmas();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        lvEntidade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (pesquisa.equals("professor")) {
                    String nomeProfessor = lvEntidade.getItemAtPosition(position).toString();
                    selecionarProfessor(nomeProfessor);
                } else {
                    String nomeTurma = lvEntidade.getItemAtPosition(position).toString();
                    selecionarTurma(nomeTurma);
                }
            }
        });
        //Realizando pesquisa baseado no ArrayAdapter
        edtPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (itemsAdapter != null){
                    itemsAdapter.getFilter().filter(edtPesquisa.getText().toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    //Método que resgata as turmas cadastradas no banco
    public void resgatandoTurmas(){
        limparArrayList();
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resgatandoIdsTurmas(dataSnapshot);
                resgatandoNomeTurmas(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //Resgatando os ids das turmas cadastradas no banco
    public void resgatandoIdsTurmas(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> referencia = dataSnapshot.child("turma").getChildren();
        for (DataSnapshot dataSnapshotChild: referencia){
            if (!dataSnapshotChild.getKey().equals("SENTINELA")){
                //Montando o ArrayList com os ids das turmas
                arrayIdsTurmas.add(dataSnapshotChild.getKey());
            }
        }
    }
    //Resgatando o nome das turmas que estão no ArrayList "arrayIdsTurmas"
    public void resgatandoNomeTurmas(DataSnapshot dataSnapshot){
        //arrayIdsTurmas.removeIf(s -> !s.contains("How"));
        for (String idTurma: arrayIdsTurmas){
            String nomeTurma = dataSnapshot.child("turma").child(idTurma).child("nomeTurma").getValue(String.class);
            arrayNomesTurmas.add(nomeTurma.toUpperCase());
        }
        setListViewTurmas();
    }
    //Método que preenche a lista de uid dos professores cadastrados no sistema
    public void resgantandoProfessores() {

        limparArrayList();

        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resgatandoUidProfessores(dataSnapshot);
                resgatandoNomesProfessores(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void resgatandoUidProfessores(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> referencia = dataSnapshot.child(pesquisa).getChildren();
        for (DataSnapshot dataSnapshotChild: referencia){
            if (!dataSnapshotChild.getKey().equals("SENTINELA")){
                arrayUidsProfessores.add(dataSnapshotChild.getKey());
            }
        }
    }
    //Método que preenche o ArrayList com os nomes do professores
    public void resgatandoNomesProfessores(DataSnapshot dataSnapshot){
        for (String i: arrayUidsProfessores){
            String nomeProfessor = dataSnapshot.child("pessoa").child(i).child("nome").getValue(String.class);
            arrayNomesProfessores.add(nomeProfessor);
        }
        setListViewProfessores();
    }
    //Preenchendo o ListView com os nomes das turmas
    public void setListViewTurmas(){
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayNomesTurmas);
        lvEntidade.setAdapter(itemsAdapter);
        lvEntidade.setSelection(0);
    }
    //Preenchendo o ListView com os nomes dos professores
    public void setListViewProfessores(){
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayNomesProfessores);
        lvEntidade.setAdapter(itemsAdapter);
        lvEntidade.setSelection(0);

        Auxiliar.criarToast(getApplicationContext(), "Selecione o professor para exibir as turmas do mesmo.");
    }
    //Método que captura a turma selecionada pelo aluno e chama a camada de negócio
    public void selecionarTurma(String nomeTurma){
        int count = 0;
        for (String nome: arrayNomesTurmas){
            if (nome.equals(nomeTurma)){
                TurmaServices.criarTurmaAluno(arrayIdsTurmas.get(count),getApplicationContext());
            }
            count++;
        }
    }
    //Método que captura o professor selecionado pelo aluno e direciona para a activity com as turmas do professor
    public void selecionarProfessor(String nomeProfessor){
        int count = 0;
        for (String nome: arrayNomesProfessores){
            if (nome.equals(nomeProfessor)){
                abrirTelaTurmasProfessorActivity(arrayUidsProfessores.get(count));
            }
            count++;
        }
    }
    //Método para limpar as listas e evitar que o ListView fique com nomes duplicados
    public void limparArrayList() {
        arrayIdsTurmas.clear();
        arrayNomesTurmas.clear();
        arrayNomesProfessores.clear();
        arrayUidsProfessores.clear();
    }
    //Intent
    public void abrirTelaTurmasProfessorActivity(String uidProfessor){
        Intent intent = new Intent(AdicionarTurmaActivity.this, TurmasProfessorActivity.class);
        intent.putExtra("uidProfessor", uidProfessor);
        startActivity(intent);
    }
}