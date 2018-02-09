package com.example.spudydev.spudy.entidades.turma.persistência;


import com.example.spudydev.spudy.entidades.turma.dominio.Turma;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.google.firebase.database.DatabaseException;

public class TurmaDAO {
    //Método para o aluno adicionar turma
    public static boolean adicionarTurma(String codigoTurma){
        boolean verificador = true;

        try {
            //Adicionar verificação se aluno já esta na turma
            String uid = AcessoFirebase.getUidUsuario();
            AcessoFirebase.getFirebase().child("aluno").child(uid).child("turmas").child(codigoTurma).setValue(codigoTurma);
            AcessoFirebase.getFirebase().child("turma").child(codigoTurma).child("usuariosDaTurma").child(uid).setValue(uid);

        }catch (DatabaseException e){
            verificador = false;
        }
        return verificador;
    }
    //Método para o professor criar turma
    public static boolean criarTurma(Turma turma){
        boolean verificador;
        try {
            String codigoTurma = gerarCodigo();
            String uid = AcessoFirebase.getUidUsuario();

            //Salvando a turma na árvore turma
            AcessoFirebase.getFirebase().child("turma").child(codigoTurma).setValue(turma.toMapTurma());
            //Salvando a turma na árvore professor
            AcessoFirebase.getFirebase().child("professor").child(uid).child("turmasMinistradas").child(codigoTurma).setValue(codigoTurma);
            //Add professor usuariosDaTurma
            AcessoFirebase.getFirebase().child("turma").child(codigoTurma).child("usuariosDaTurma").child(uid).setValue(uid);

            verificador = true;
        }catch (DatabaseException e){
            verificador = false;
        }
        return verificador;
    }
    //Pegando a chave gerada pelo push()
    private static String gerarCodigo(){
        return AcessoFirebase.getFirebase().child("turma").push().getKey();
    }
}
