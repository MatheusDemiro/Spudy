package com.example.spudydev.spudy.entidades.avaliacao.persistencia;

import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Slope one.
 */
public class SlopeOne {

    //ArrayList com todas as turmas cadastradas no banco
    private List<String> turmas = new ArrayList<>();
    //ArrayList com todos os usuários cadastrados no banco
    private List<String> alunos = new ArrayList<>();
    //Dicionário com os usuários, turmas e avaaliações
    private HashMap<String, HashMap<String, Double>> dados = new HashMap<String, HashMap<String, Double>>();
    //Dicionário com as frequências
    private HashMap<String, HashMap<String, Integer>> matrizFrequencias = new HashMap<String, HashMap<String, Integer>>();
    //Dicionário com as diferenças
    private HashMap<String, HashMap<String, Double>> matrizDiferencas = new HashMap<String, HashMap<String, Double>>();
    //Dicionários utilizados na predição
    private HashMap<String, Double> predicoes = new HashMap<String, Double>();
    private HashMap<String, Integer> frequencias = new HashMap<String, Integer>();

    /**
     * Criar matriz dados.
     *
     * @param dataSnapshot the data snapshot
     */
//Carregando o dicionário principal
    public void criarMatrizDados(DataSnapshot dataSnapshot){
        for (String uidAluno: alunos) {
            DataSnapshot referencia = dataSnapshot.child("avaliacao").child(uidAluno);
            HashMap<String, Double> dicionarioAuxiliar = new HashMap<>();
            for (String uidTurma: turmas) {
                if (referencia.child(uidTurma).exists()) {
                    dicionarioAuxiliar.put(uidTurma, referencia.child(uidTurma).child("nota").getValue(Double.class));
                    dados.put(uidAluno, dicionarioAuxiliar);
                }
            }
        }
    }

    /**
     * Gets turma.
     *
     * @param dataSnapshot the data snapshot
     */
//Retornando UIDs das turmas e suas respectivas avaliações
    public void getTurma(DataSnapshot dataSnapshot) {
        for (DataSnapshot dataSnapshotChild : dataSnapshot.child("turma").getChildren()) {
            if (!dataSnapshotChild.getKey().equals("SENTINELA")) {
                turmas.add(dataSnapshotChild.getKey());
            }
        }
    }

    /**
     * Gets alunos.
     *
     * @param dataSnapshot the data snapshot
     */
//Retornanod UIDs dos usuários
    public void getAlunos(DataSnapshot dataSnapshot) {
        for (DataSnapshot dataSnapshotChild : dataSnapshot.child("aluno").getChildren()) {
            if (!dataSnapshotChild.getKey().equals("SENTINELA")) {
                alunos.add(dataSnapshotChild.getKey());
            }
        }
    }

    /**
     * Criar matriz diferencas.
     */
//Construindo Matriz de diferenças
    public void criarMatrizDiferencas(){
        for (Map<String, Double> usuario: dados.values()){
            for (Map.Entry<String, Double> turma: usuario.entrySet()){

                String i1 = turma.getKey();
                double r1 = turma.getValue();

                if (!matrizDiferencas.containsKey(i1)){
                    matrizDiferencas.put(i1, new HashMap<String, Double>());
                    matrizFrequencias.put(i1, new HashMap<String, Integer>());
                }
                for (Map.Entry<String, Double> turma2: usuario.entrySet()){

                    String i2 = turma2.getKey();
                    double r2 = turma2.getValue();

                    int valorAntigo = 0;
                    if (matrizFrequencias.get(i1).containsKey(i2)){
                        valorAntigo = matrizFrequencias.get(i1).get(i2);
                    }
                    double diferencaAntiga = 0.0;
                    if (matrizDiferencas.get(i1).containsKey(i2)){
                        diferencaAntiga = matrizDiferencas.get(i1).get(i2);
                    }
                    double novaDiferenca = r1 - r2;
                    matrizFrequencias.get(i1).put(i2, valorAntigo + 1);
                    matrizDiferencas.get(i1).put(i2, diferencaAntiga + novaDiferenca);
                }
            }
        }
        for (String j: matrizDiferencas.keySet()){
            for (String i: matrizDiferencas.get(j).keySet()){

                double valorAntigo = matrizDiferencas.get(j).get(i);
                int contador = matrizFrequencias.get(j).get(i);

                matrizDiferencas.get(j).put(i, valorAntigo / contador);
            }
        }
    }

    /**
     * Recomendar hash map.
     *
     * @return the hash map
     */
    public HashMap<String, Double> recomendar(){
        criarMatrizDiferencas();
        HashMap<String, Double> usuarioAtual = dados.get(AcessoFirebase.getUidUsuario());
        return predicao(usuarioAtual);
    }

    /**
     * Predict hash map.
     *
     * @param usuarioAvaliacao the usuario avaliacao
     * @return the hash map
     */
    public HashMap<String, Double> predicao(HashMap<String, Double> usuarioAvaliacao){
        for (String j: matrizDiferencas.keySet()){
            frequencias.put(j, 0);
            predicoes.put(j, 0.0);
        }
        for (String j: usuarioAvaliacao.keySet()){
            for (String k: matrizDiferencas.keySet()) {
                try {
                    double novoValor = (matrizDiferencas.get(k).get(j) + usuarioAvaliacao.get(j)) * matrizFrequencias.get(k).get(j);
                    predicoes.put(k, predicoes.get(k) + novoValor);
                    frequencias.put(k, frequencias.get(k) + matrizFrequencias.get(k).get(j));
                } catch (NullPointerException e) {
                }
            }
        }
        HashMap<String, Double> limparPredicoes = new HashMap<String, Double>();
        for (String j: predicoes.keySet()){
            if (frequencias.get(j) > 0){
                limparPredicoes.put(j, predicoes.get(j) / frequencias.get(j));
            }
        }
        for (String j: usuarioAvaliacao.keySet()){
            limparPredicoes.put(j, usuarioAvaliacao.get(j));
        }
        return limparPredicoes;
    }
}