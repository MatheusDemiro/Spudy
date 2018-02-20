package com.example.spudydev.spudy.entidades.aluno.gui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.gui.LoginActivity;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.entidades.turma.gui.AdicionarTurmaActivity;
import com.example.spudydev.spudy.entidades.chat.gui.ChatActivity;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.perfil.negocio.MenuLateralServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The type Main aluno activity.
 */
public class MainAlunoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //ArrayList do ListView
    private List<HashMap<String, String>> listaTurmaAluno = new ArrayList<>();
    //ListView
    private ListView lvTurmaAluno;
    //Janela de diálogo
    private AlertDialog alerta;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_aluno);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.sp_navigation_drawer_open, R.string.sp_navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        //Chamando a classe de negócio para setar os campos de nome e email do nav_header_main_aluno
        MenuLateralServices.setNomeEmailView(navigationView, AcessoFirebase.getFirebaseAutenticacao().getCurrentUser());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_meu_perfil:
                        abrirTelaMeuPerfilAlunoActivity();
                        return true;
                    case R.id.nav_turmas:
                        //Activity de turmas
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_sair:
                        sair();
                        //sair
                        return true;
                    default:
                        return false;
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.btnFlutuanteAdicionarTurma);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainAlunoActivity.this, AdicionarTurmaActivity.class);
                startActivity(intent);
            }
        });

        lvTurmaAluno = findViewById(R.id.lvTurmasAluno);
        //Carregar as turmas
        carregarTurmaAluno();

        lvTurmaAluno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String codigoTurma = ((TextView) view.findViewById(R.id.txtCodigoTurma)).getText().toString();
                String nomeTurma = ((TextView) view.findViewById(R.id.txtNomeTurma)).getText().toString();
               abrirTelaChat(codigoTurma, nomeTurma);
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_aluno, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void sair() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Define o titulo
        builder.setTitle(getString(R.string.sp_dialogo_titulo));
        //Define a mensagem
        builder.setMessage(getString(R.string.sp_dialogo_mensagem_sair_conta));
        //Define um botão como positivo
        builder.setPositiveButton(getString(R.string.sp_dialogo_sim), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1) {
                FirebaseAuth.getInstance().signOut();
                abrirTelaLoginActivity();
            }
        });
        //Define um botão como negativo.
        builder.setNegativeButton(getString(R.string.sp_dialogo_nao), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alerta.dismiss();
            }
        });
        //Cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
    //Recuperando as turmas que o aluno está participando
    private void carregarTurmaAluno(){
        //Lista para salvar as turmas que o professor é responsável
        AcessoFirebase.getFirebase().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getTurmasAluno(dataSnapshot);
                setListViewTurmas();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Método retorna as turmas do aluno
    private void getTurmasAluno(DataSnapshot dataSnapshot) {
        //Limpar a lista anterior
        listaTurmaAluno.clear();
        //Recuperar uma nova
        Iterable<DataSnapshot> turmas = dataSnapshot.child("aluno").child(AcessoFirebase.getUidUsuario()).child("turmas").getChildren();
        //For percorrendo e adicionando os UID's dos usuarios a listaTurmaAluno
        for (DataSnapshot dataSnapshotChild: turmas){
            if (!dataSnapshotChild.getKey().equals("SENTINELA")){
                montandoDicionarioTurmasAluno(dataSnapshot, dataSnapshotChild.getKey());
            }
        }
    }
    //Método que monta o HashMap do SimpleAdapter
    private void montandoDicionarioTurmasAluno(DataSnapshot dataSnapshot, String codigoTurma) {
        //Criar um novo sobrescrevendo o anterior
        HashMap<String, String> dicionarioTurmasAluno = new HashMap<>();
        //Recuperar nome da turma
        String nomeTurma = dataSnapshot.child("turma").child(codigoTurma).child("nomeTurma").getValue(String.class).toUpperCase();
        //Preencher dicionário
        dicionarioTurmasAluno.put("nomeTurma", nomeTurma);
        dicionarioTurmasAluno.put("codigoTurma", codigoTurma);
        //Aqui ele esta com todas as turmas do professor, pegar o cardview e seta o texto
        listaTurmaAluno.add(dicionarioTurmasAluno);
    }

    //Método que seta o ListView com o auxílio de ArrayAdapter
    private void setListViewTurmas(){
        SimpleAdapter adapter = new SimpleAdapter(this, listaTurmaAluno,R.layout.modelo_list_view_turmas,
                new String[]{"nomeTurma","codigoTurma"}, new int[]{R.id.txtNomeTurma, R.id.txtCodigoTurma});
        lvTurmaAluno.setAdapter(adapter);
    }
    private void abrirTelaChat(String codigoTurma, String nomeTurma){
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("codigoTurma", codigoTurma);
        intent.putExtra("nomeTurma", nomeTurma);
        startActivity(intent);
    }
    private void abrirTelaLoginActivity(){
        Intent intentAbrirTelaLoginAcitivty = new Intent(MainAlunoActivity.this, LoginActivity.class);
        startActivity(intentAbrirTelaLoginAcitivty);
        finish();
    }
    private void abrirTelaMeuPerfilAlunoActivity(){
        Intent intentAbrirTelaMeuPerfilAlunoAcitivty = new Intent(MainAlunoActivity.this, MeuPerfilAlunoActivity.class);
        startActivity(intentAbrirTelaMeuPerfilAlunoAcitivty);
    }
}
