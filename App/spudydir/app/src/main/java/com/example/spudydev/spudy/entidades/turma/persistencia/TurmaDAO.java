package com.example.spudydev.spudy.entidades.turma.persistencia;

import com.example.spudydev.spudy.entidades.turma.dominio.Turma;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.google.firebase.database.DatabaseException;

/**
 * The type Turma dao.
 */
public final class TurmaDAO {

    private TurmaDAO(){
        //Construtor vazio
    }

    /**
     * Adicionar turma boolean.
     *
     * @param codigoTurma the codigo turma
     * @return the boolean
     */
//Método para o aluno adicionar turma
    public static boolean adicionarTurmaDAO(String codigoTurma){
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

    /**
     * Criar turma boolean.
     *
     * @param turma the turma
     * @return the boolean
     */
//Método para o professor criar turma
    public static boolean criarTurmaDAO(Turma turma){
        boolean verificador;
        try {
            String codigoTurma = gerarCodigoDAO();
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

    /**
     * Sair da turma boolean.
     *
     * @param codigoTurma the codigo turma
     * @return the boolean
     */
//Removendo usuário da turma
    public static boolean sairDaTurmaDAO(String codigoTurma){
        boolean verificador;
        try{
            AcessoFirebase.getFirebase().child("turma").child(codigoTurma).child("usuariosDaTurma").child(AcessoFirebase.getUidUsuario()).setValue(null);
            AcessoFirebase.getFirebase().child("aluno").child(AcessoFirebase.getUidUsuario()).child("turmas").child(codigoTurma).setValue(null);
            verificador = true;
        }catch (DatabaseException e){
            verificador = false;
        }
        return verificador;
    }

    /**
     * Remover aluno turma boolean.
     *
     * @param codigoTurma the codigo turma
     * @param uidUsuario  the uid usuario
     * @return the boolean
     */
    public static boolean removerAlunoTurmaDAO(String codigoTurma, String uidUsuario){
        boolean verificador;
        try {
            AcessoFirebase.getFirebase().child("turma").child(codigoTurma).child("usuariosDaTurma").child(uidUsuario).setValue(null);
            AcessoFirebase.getFirebase().child("aluno").child(uidUsuario).child("turmas").child(codigoTurma).setValue(null);
            verificador = true;
        }catch (DatabaseException e){
            verificador = false;
        }
        return verificador;
    }
    //Pegando a chave gerada pelo push()
    private static String gerarCodigoDAO(){
        return AcessoFirebase.getFirebase().child("turma").push().getKey();
    }
}
