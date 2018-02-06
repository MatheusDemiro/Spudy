package com.example.spudydev.spudy.entidades.material.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.entidades.material.dominio.Material;

public class AdicionarMaterialActivity extends AppCompatActivity {

    private EditText edtTituloMaterial;
    private EditText edtLinkMaterial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_material);

        edtTituloMaterial = findViewById(R.id.edtTituloMaterial);
        edtLinkMaterial = findViewById(R.id.edtLinkMaterial);
        Button btnAddMaterial = findViewById(R.id.btnAddMaterial);

        btnAddMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(edtTituloMaterial.getText().toString().isEmpty() || edtLinkMaterial.getText().toString().isEmpty())){
                    addMaterial();
                    Auxiliar.criarToast(getApplicationContext(),"Material adicionado com sucesso.");
                    abrirTelaMainProfessorAddMaterial();
                }
            }
        });
    }
    //instanciando e definindo os atributos do objeto material
    private Material criarMaterial() {
        Material material = new Material();
        material.setConteudo(edtLinkMaterial.getText().toString());
        material.setNome(edtTituloMaterial.getText().toString());
        return material;
    }
    //adicionar o material em relação a turma
    public void addMaterial(){
        Material material = criarMaterial();
        AcessoFirebase.getFirebase().child("material").child(getIntent().getStringExtra("codigoTurma")).push().setValue(material);
    }
    public void abrirTelaMainProfessorAddMaterial(){
        Intent intent = new Intent(getApplicationContext(),MainMaterialProfessorActivity.class);
        intent.putExtra("codigoTurma", getIntent().getStringExtra("codigoTurma"));
        startActivity(intent);
        finish();
    }
}
