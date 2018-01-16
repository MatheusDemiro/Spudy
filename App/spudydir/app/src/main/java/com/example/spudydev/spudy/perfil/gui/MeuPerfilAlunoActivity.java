package com.example.spudydev.spudy.perfil.gui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.gui.LoginActivity;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.perfil.negocio.DadosMenuLateral;
import com.example.spudydev.spudy.pessoa.dominio.Pessoa;
import com.example.spudydev.spudy.pessoa.gui.AlterarDataNascimentoActivity;
import com.example.spudydev.spudy.pessoa.gui.AlterarInstituicaoActivity;
import com.example.spudydev.spudy.pessoa.gui.AlterarNomeActivity;
import com.example.spudydev.spudy.pessoa.gui.AlterarSenhaActivity;
import com.example.spudydev.spudy.usuario.dominio.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MeuPerfilAlunoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView emailConsulta;
    private TextView dataNascimentoConsulta;
    private TextView instituicaoConsulta;
    private TextView nomeConsulta;
    private TextView senhaConsulta;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DadosMenuLateral dadosMenuLateral = new DadosMenuLateral();
    private AlertDialog alertaSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_perfil_aluno);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //instanciando os TextViews/Button
        nomeConsulta =  (TextView) findViewById(R.id.txtContaAlunoNomeAlterar);
        emailConsulta = (TextView) findViewById(R.id.txtContaAlunoEmailAlterar);
        dataNascimentoConsulta = (TextView) findViewById(R.id.txtContaAlunoDataNascimentoAlterar);
        instituicaoConsulta = (TextView) findViewById(R.id.txtContaAlunoInstituicaoAlterar);
        senhaConsulta = (TextView) findViewById(R.id.txtContaAlunoSenhaAlterar);
        Button btnInformacao = (Button) findViewById(R.id.btnInformacaoPerfilAluno);
        //fim instancias

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.sp_navigation_drawer_open, R.string.sp_navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setando email e nome do header
        dadosMenuLateral.resgatarUsuario(navigationView, user);

        //setando os TXT'S do meu perfil aluno
        informacoesPerfilAluno();

        //Informações sobre como trocar dados perfil
        btnInformacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MeuPerfilAlunoActivity.this, R.string.sp_click_informacao, Toast.LENGTH_SHORT).show();
            }
        });

        //Alterar Nome
        nomeConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAlteraNomeActivity();
            }
        });

        //Alterar Email
        emailConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MeuPerfilAlunoActivity.this, "Em construção...", Toast.LENGTH_SHORT).show();
            }
        });

        //Alterar datanascimento
        dataNascimentoConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAlteraDataNascimentoActivity();
            }
        });

        //Alterar Instituição
        instituicaoConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaAlteraInstituicaoActivity();
            }
        });

        //Alterar Senha
        senhaConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaAlterarSenhaActivity();
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_meu_perfil:
                        return true;
                    case R.id.nav_turmas:
                        //Activity de turmas
                        Toast.makeText(MeuPerfilAlunoActivity.this, "Em construção", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav_sair:
                        //sair
                        sair();
                        return true;
                    default:
                        return false;
                }
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
        getMenuInflater().inflate(R.menu.meu_perfil_aluno, menu);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_turmas) {
            // Handle the camera action
        } else if (id == R.id.nav_meu_perfil) {

        } else if (id == R.id.nav_sair) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void informacoesPerfilAluno() {
        DatabaseReference referencia = AcessoFirebase.getFirebase();

        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Pessoa pessoa = dataSnapshot.child("pessoa").child(user.getUid()).getValue(Pessoa.class);
                Usuario usuario = dataSnapshot.child("usuario").child(user.getUid()).getValue(Usuario.class);
                if (usuario != null && pessoa != null) {
                    nomeConsulta.setText(pessoa.getNome());
                    emailConsulta.setText(usuario.getEmail());
                    instituicaoConsulta.setText(usuario.getInstituicao());
                    dataNascimentoConsulta.setText(pessoa.getDataNascimento());
                    senhaConsulta.setText("******");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Janela de diálogo sair
    private void sair() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Sair");
        //define a mensagem
        builder.setMessage("Tem certeza que deseja sair?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1) {
                FirebaseAuth.getInstance().signOut();
                abrirTelaLoginActivity();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alertaSair.dismiss();
            }
        });
        //cria o AlertDialog
        alertaSair = builder.create();
        //Exibe
        alertaSair.show();
    }

    //Intent Alterar Nome
    public void abrirTelaAlteraNomeActivity(){
        Intent intentAbrirTelaAlteraNomeActivity = new Intent(MeuPerfilAlunoActivity.this, AlterarNomeActivity.class);
        startActivity(intentAbrirTelaAlteraNomeActivity);
    }

    //Intent Alterar Data
    public void abrirTelaAlteraDataNascimentoActivity(){
        Intent intentAbrirTelaAlteraDataNascimentoActivity = new Intent(MeuPerfilAlunoActivity.this, AlterarDataNascimentoActivity.class);
        startActivity(intentAbrirTelaAlteraDataNascimentoActivity);
    }
    //Intent Alterar Instituicao
    public void abrirTelaAlteraInstituicaoActivity(){
        Intent intentAbrirTelaAlteraInstituicaoActivity = new Intent(MeuPerfilAlunoActivity.this, AlterarInstituicaoActivity.class);
        startActivity(intentAbrirTelaAlteraInstituicaoActivity);
    }
    //Intent Alterar Senha
    public void abrirTelaAlterarSenhaActivity(){
        Intent intentAbrirTelaAlteraSenhaActivity = new Intent(MeuPerfilAlunoActivity.this, AlterarSenhaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email", user.getEmail());
        intentAbrirTelaAlteraSenhaActivity.putExtras(bundle);
        startActivity(intentAbrirTelaAlteraSenhaActivity);
    }

    //Intent Tela Login
    public void abrirTelaLoginActivity(){
        Intent intentAbrirTelaLoginAcitivty = new Intent(MeuPerfilAlunoActivity.this, LoginActivity.class);
        startActivity(intentAbrirTelaLoginAcitivty);
        finish();
    }
}
