package com.example.spudydev.spudy.perfil.gui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.aluno.gui.MainAlunoActivity;
import com.example.spudydev.spudy.entidades.endereco.dominio.Endereco;
import com.example.spudydev.spudy.entidades.pessoa.dominio.Pessoa;
import com.example.spudydev.spudy.entidades.pessoa.gui.AlterarDataNascimentoActivity;
import com.example.spudydev.spudy.entidades.endereco.gui.AlterarEnderecoActivity;
import com.example.spudydev.spudy.entidades.pessoa.gui.AlterarNomeActivity;
import com.example.spudydev.spudy.entidades.usuario.dominio.Usuario;
import com.example.spudydev.spudy.entidades.usuario.gui.AlterarEmailActivity;
import com.example.spudydev.spudy.entidades.usuario.gui.AlterarInstituicaoActivity;
import com.example.spudydev.spudy.entidades.usuario.gui.AlterarSenhaActivity;
import com.example.spudydev.spudy.infraestrutura.gui.LoginActivity;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.perfil.negocio.MenuLateralServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

/**
 * The type Meu perfil aluno activity.
 */
public class MeuPerfilAlunoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser user = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser();
    private AlertDialog alertaSair;

    //TEXT VIEWS (PRIVATES PARA SEREM CHAMADOS NO ACESSO AO BANCO)

    private TextView tvNomePerfilAluno;
    private TextView tvDataNascimentoPerfilAluno;
    private TextView tvEnderecoPerfilAluno;
    private TextView tvInstituicaoPerfilAluno;
    private TextView tvEmailPerfilAluno;
    private TextView tvSenhaPerfilAluno;

    //Variáveis relacionadas a foto
    private ImageView imgPerfilAluno;
    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 71;

    //Referência Storage
    private StorageReference referenciaStorage;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_perfil_aluno);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        //instanciando os TextViews/Button

        tvNomePerfilAluno =  findViewById(R.id.tvNomePerfilAluno);
        tvDataNascimentoPerfilAluno = findViewById(R.id.tvDataNascimentoPerfilAluno);
        tvEnderecoPerfilAluno = findViewById(R.id.tvEnderecoPerfilAluno);
        tvInstituicaoPerfilAluno = findViewById(R.id.tvInstituicaoPerfilAluno);
        tvEmailPerfilAluno = findViewById(R.id.tvEmailPerfilAluno);
        tvSenhaPerfilAluno = findViewById(R.id.tvSenhaPerfilAluno);
        imgPerfilAluno = findViewById(R.id.imgPerfilAluno);

        Button btnAlterarImagemPerfilAluno = findViewById(R.id.btnAlterarImagemPerfilAluno);
        Button btnAlterarNomePerfilAluno = findViewById(R.id.btnAlterarNomePerfilAluno);
        Button btnAlterarDataNascimentoPerfilAluno = findViewById(R.id.btnAlterarDataNascimentoPerfilAluno);
        Button btnAlterarEnderecoPerfilAluno = findViewById(R.id.btnAlterarEnderecoPerfilAluno);
        Button btnAlterarInstituicaoPerfilAluno = findViewById(R.id.btnAlterarInstituicaoPerfilAluno);
        Button btnAlterarEmailPerfilAluno = findViewById(R.id.btnAlterarEmailPerfilAluno);
        Button btnAlterarSenhaPerfilAluno = findViewById(R.id.btnAlterarSenhaPerfilAluno);

        //fim instancias

        //Referência Storage
        referenciaStorage = FirebaseStorage.getInstance().getReference();

        //setando os TXT'S do meu perfil aluno
        downloadFoto();
        informacoesPerfilAluno();

        //Drawer Menu Lateral
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.sp_navigation_drawer_open, R.string.sp_navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setando email e nome do header
        MenuLateralServices.setNomeEmailView(navigationView, user);

        //Alterar foto
        btnAlterarImagemPerfilAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Galeria!
                escolherFoto();
            }
        });

        //Alterar Nome
        btnAlterarNomePerfilAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAlteraNomeActivity();
            }
        });

        //Alterar datanascimento
        btnAlterarDataNascimentoPerfilAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAlteraDataNascimentoActivity();
            }
        });

        //Alterar Endereco
        btnAlterarEnderecoPerfilAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAlterarEnderecoActivity();
            }
        });

        //Alterar Instituição
        btnAlterarInstituicaoPerfilAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaAlteraInstituicaoActivity();
            }
        });

        //Alterar Email
        btnAlterarEmailPerfilAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAlterarEmailActivity();
            }
        });

        //Alterar Senha
        btnAlterarSenhaPerfilAluno.setOnClickListener(new View.OnClickListener() {
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
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_turmas:
                        abrirTelaMainAlunoActivity();
                        return true;
                    case R.id.nav_sair:
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //Método que abre a galeria para o usuário selecionar um foto
    private void escolherFoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Selecione uma imagem"), PICK_IMAGE_REQUEST);
    }
    //Método que captura o resultado do método anterior
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imgPerfilAluno.setImageBitmap(bitmap);
                uploadFoto();

            }catch(IOException e ){
                Log.d("IOException upload", e.getMessage());
            }
        }
    }
    //Método que realiza o upload da foto
    private void uploadFoto(){

        final double porcentagemUploadFoto = 100.0;

        if (filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = referenciaStorage.child("images/" + user.getUid()+".bmp");
            ref.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    progressDialog.dismiss();
                    Auxiliar.criarToast(MeuPerfilAlunoActivity.this, "Sucesso!");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Auxiliar.criarToast(MeuPerfilAlunoActivity.this, "Falhou!"+e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (porcentagemUploadFoto * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Upload " + (int)progress+"%");
                }
            });

        }
    }
    //Resgatando foto do Storage
    private void downloadFoto(){
        StorageReference ref = referenciaStorage.child("images/" + user.getUid() + ".bmp");

        try {
            final File localFile = File.createTempFile("images", "bmp");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener< FileDownloadTask.TaskSnapshot >() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap minhaFoto = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imgPerfilAluno.setImageBitmap(minhaFoto);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (IOException e) {
            Log.d("IOException downlaod", e.getMessage());
        }
    }
    //Carregando informações do perfil do aluno
    private void informacoesPerfilAluno() {

        AcessoFirebase.getFirebase().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Pessoa pessoa = dataSnapshot.child("pessoa").child(user.getUid()).getValue(Pessoa.class);
                Usuario usuario = dataSnapshot.child("usuario").child(user.getUid()).getValue(Usuario.class);
                Endereco endereco = dataSnapshot.child("endereco").child(user.getUid()).getValue(Endereco.class);

                if (usuario != null && pessoa != null) {
                    setandoInformacoesPerfil(pessoa, usuario, endereco);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setandoInformacoesPerfil(Pessoa pessoa, Usuario usuario, Endereco endereco) {
        tvNomePerfilAluno.setText(pessoa.getNome());
        tvDataNascimentoPerfilAluno.setText(pessoa.getDataNascimento());

        //montando string endereço
        String enderecoPerfilAluno = endereco.getEstado() + " - " + endereco.getCidade() + " - " + endereco.getRua() + " - " + endereco.getComplemento();

        tvEnderecoPerfilAluno.setText(enderecoPerfilAluno);
        tvEmailPerfilAluno.setText(user.getEmail());
        tvInstituicaoPerfilAluno.setText(usuario.getInstituicao());
        tvSenhaPerfilAluno.setText("******");
    }

    //Método que exibe a caixa de diálogo para o aluno confirmar ou não a sua saída da turma
    private void sair() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle(getString(R.string.sp_dialogo_titulo));
        //define a mensagem
        builder.setMessage(getString(R.string.sp_dialogo_mensagem_sair_conta));
        //define um botão como positivo
        builder.setPositiveButton(getString(R.string.sp_dialogo_sim), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1) {
                FirebaseAuth.getInstance().signOut();
                abrirTelaLoginActivity();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton(getString(R.string.sp_dialogo_nao), new DialogInterface.OnClickListener() {
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
    private void abrirTelaAlteraNomeActivity(){
        Intent intentAbrirTelaAlteraNomeActivity = new Intent(MeuPerfilAlunoActivity.this, AlterarNomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tipoConta", "aluno");
        intentAbrirTelaAlteraNomeActivity.putExtras(bundle);
        startActivity(intentAbrirTelaAlteraNomeActivity);
    }
    //Intent Alterar Data Nascimento
    private void abrirTelaAlteraDataNascimentoActivity(){
        Intent intentAbrirTelaAlteraDataNascimentoActivity = new Intent(MeuPerfilAlunoActivity.this, AlterarDataNascimentoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tipoConta", "aluno");
        intentAbrirTelaAlteraDataNascimentoActivity.putExtras(bundle);
        startActivity(intentAbrirTelaAlteraDataNascimentoActivity);
    }

    //Intent Alterar Endereco
    private void abrirTelaAlterarEnderecoActivity(){
        Intent intentAbrirTelaAlteraEnderecoActivity = new Intent(MeuPerfilAlunoActivity.this, AlterarEnderecoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tipoConta", "aluno");
        intentAbrirTelaAlteraEnderecoActivity.putExtras(bundle);
        startActivity(intentAbrirTelaAlteraEnderecoActivity);
        finish();
    }
    //Intent Alterar Instituicao
    private void abrirTelaAlteraInstituicaoActivity(){
        Intent intentAbrirTelaAlteraInstituicaoActivity = new Intent(MeuPerfilAlunoActivity.this, AlterarInstituicaoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tipoConta", "aluno");
        intentAbrirTelaAlteraInstituicaoActivity.putExtras(bundle);
        startActivity(intentAbrirTelaAlteraInstituicaoActivity);
    }
    //Intent alterar email();
    private  void  abrirTelaAlterarEmailActivity(){
        Intent intent = new Intent(MeuPerfilAlunoActivity.this, AlterarEmailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tipoConta", "aluno");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    //Intent Alterar Senha
    private void abrirTelaAlterarSenhaActivity(){
        Intent intentAbrirTelaAlteraSenhaActivity = new Intent(MeuPerfilAlunoActivity.this, AlterarSenhaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tipoConta", "aluno");
        intentAbrirTelaAlteraSenhaActivity.putExtras(bundle);
        startActivity(intentAbrirTelaAlteraSenhaActivity);
    }
    //Intent Alterar MainAluno
    private void abrirTelaMainAlunoActivity(){
        Intent intent= new Intent(MeuPerfilAlunoActivity.this, MainAlunoActivity.class);
        startActivity(intent);
    }
    //Intent Tela LoginServices
    private void abrirTelaLoginActivity(){
        Intent intentAbrirTelaLoginAcitivty = new Intent(MeuPerfilAlunoActivity.this, LoginActivity.class);
        startActivity(intentAbrirTelaLoginAcitivty);
        finish();
    }
}