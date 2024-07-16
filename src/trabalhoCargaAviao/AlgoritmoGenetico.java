package trabalhoCargaAviao;

import java.io.*;
import java.util.*;

public class AlgoritmoGenetico {
    private int tamPopulacao;
    private int tamCarga = 0;
    private double capacidadePeso;
    private double larguraMaxima, alturaMaxima, profundidadeMaxima;
    private int probMutacao;
    private int qtdCruzamentos; 
    private int numeroGeracoes;
    private ArrayList<Carga> cargas = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> populacao = new ArrayList<>();
    private ArrayList<Integer> roletaVirtual = new ArrayList<>();

    public AlgoritmoGenetico(int tamanhoPopulacao, double capacidadePeso, double larguraMaxima, double alturaMaxima, double profundidadeMaxima, int probabilidadeMutacao, int qtdCruzamentos, int numeroGeracoes){
        this.tamPopulacao = tamanhoPopulacao;
        this.capacidadePeso = capacidadePeso;
        this.larguraMaxima = larguraMaxima;
        this.alturaMaxima = alturaMaxima;
        this.profundidadeMaxima = profundidadeMaxima;
        this.probMutacao = probabilidadeMutacao;
        this.qtdCruzamentos = qtdCruzamentos;
        this.numeroGeracoes = numeroGeracoes;
    }
    
    public void executar(){
        this.criarPopulacao();
        for(int i = 0; i < this.numeroGeracoes; i++){
            System.out.println("Geracao: " + i);
            mostraPopulacao();
            operadoresGeneticos();
            novoPopulacao();
            ArrayList<Integer> pior = obterPiorCromossomo();
            ArrayList<Integer> melhor = obterMelhor();            
            System.out.println("Pior cromossomo: " + pior);
            System.out.println("Avaliação do pior cromossomo: " + fitness(pior));
            System.out.println("Melhor cromossomo: " + melhor);
            System.out.println("Avaliação do melhor cromossomo: " + fitness(melhor));
            
        }
    }
    
    public void mostraPopulacao(){
        for(int i = 0; i < this.tamPopulacao; i++){
            System.out.println("Cromossomo " + i + ": " + populacao.get(i));
            System.out.println("Avaliacao: " + fitness(populacao.get(i)));
        }
    }
    
    public void carregaArquivo(String fileName){
        String csvFile = fileName;
        String line = "";
        String[] carga = null;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                carga = line.split(",");
                Carga novaCarga = new Carga();
                novaCarga.setDescricao(carga[0]);
                novaCarga.setValor(Double.parseDouble(carga[1]));
                novaCarga.setLargura(Double.parseDouble(carga[2]));
                novaCarga.setAltura(Double.parseDouble(carga[3]));
                novaCarga.setProfundidade(Double.parseDouble(carga[4]));
                cargas.add(novaCarga);
                System.out.println(novaCarga);
                this.tamCarga++;
            }
            System.out.println("Tamanho da carga: " + this.tamCarga);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private ArrayList<Integer> criarCromossomo(){
        ArrayList<Integer> novoCromossomo = new ArrayList<>();
        for(int i = 0; i < this.tamCarga; i++){
            if(Math.random() < 0.6)
                novoCromossomo.add(0);
            else
                novoCromossomo.add(1);
        }
        return novoCromossomo;
    }
    
    private void criarPopulacao(){
        for(int i = 0; i < this.tamPopulacao; i++)
            this.populacao.add(criarCromossomo());
    }
    
    private double fitness(ArrayList<Integer> cromossomo){
        double pesoTotal = 0;
        double volumeTotal = 0;
        double volumeDisponivel = this.larguraMaxima * this.alturaMaxima * this.profundidadeMaxima;

        for(int i = 0; i < this.tamCarga; i++){
            if(cromossomo.get(i) == 1){
                Carga carga = cargas.get(i);
                if(carga.getLargura() > this.larguraMaxima || carga.getAltura() > this.alturaMaxima || carga.getProfundidade() > this.profundidadeMaxima){
                    return 0; // Penaliza se qualquer item excede as dimensões máximas
                }
                volumeTotal += carga.getLargura() * carga.getAltura() * carga.getProfundidade();
            }
        }

        if(pesoTotal > this.capacidadePeso){
            return 0; // Penaliza se o peso total exceder a capacidade
        }

        if(volumeTotal > volumeDisponivel){
            return 0; // Penaliza se o volume total exceder o volume disponível
        }

        return volumeTotal; // Maior volume é melhor
    }

    
    private void gerarRoleta(){
        ArrayList<Double> fitnessIndividuos = new ArrayList<>();
        double totalFitness = 0;
        for(int i = 0; i < this.tamPopulacao; i++){
            double fitnessValue = fitness(this.populacao.get(i));
            fitnessIndividuos.add(fitnessValue);
            totalFitness += fitnessValue;
        }
        System.out.println("Soma total fitness: " + totalFitness);
        System.out.println("Notas: " + fitnessIndividuos);
        for(int i = 0; i < this.tamPopulacao; i++){
            double qtdPosicoes = (fitnessIndividuos.get(i) / totalFitness) * 1000;
            for(int j = 0; j <= qtdPosicoes; j++)
                roletaVirtual.add(i);
        }
        System.out.println("Roleta Virtual: " + roletaVirtual);
    }
    
    private int roleta(){
        Random r = new Random();
        int selecionado = r.nextInt(roletaVirtual.size());
        return roletaVirtual.get(selecionado);
    }
    
    private ArrayList<ArrayList<Integer>> cruzamento(){
        ArrayList<Integer> filho1 = new ArrayList<>();
        ArrayList<Integer> filho2 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> filhos = new ArrayList<>();
        ArrayList<Integer> pai1, pai2;
        int indice_pai1, indice_pai2;
        indice_pai1 = roleta();
        indice_pai2 = roleta();
        pai1 = populacao.get(indice_pai1);
        pai2 = populacao.get(indice_pai2);
        Random r = new Random();
        int pos = r.nextInt(this.tamCarga); // ponto de corte
        for(int i = 0; i <= pos; i++){
            filho1.add(pai1.get(i));
            filho2.add(pai2.get(i));
        }
        for(int i = pos + 1; i < this.tamCarga; i++){
            filho1.add(pai2.get(i));
            filho2.add(pai1.get(i));
        }
        filhos.add(filho1);
        filhos.add(filho2);
        return filhos;
    }
    
    private void mutacao(ArrayList<Integer> filho){
        Random r = new Random();
        int v = r.nextInt(100);
        if(v < this.probMutacao){
            int ponto = r.nextInt(this.tamCarga);
            filho.set(ponto, 1 - filho.get(ponto));
            int ponto2 = r.nextInt(this.tamCarga);
            filho.set(ponto2, 1 - filho.get(ponto2));
            System.out.println("Ocorreu mutação!");
        }
    }
    
    private void operadoresGeneticos(){
        ArrayList<ArrayList<Integer>> filhos;
        gerarRoleta();
        for(int i = 0; i < this.qtdCruzamentos; i++){
            filhos = cruzamento();
            for(ArrayList<Integer> filho : filhos){
                mutacao(filho);
                populacao.add(filho);
            }
        }
    }
    
    protected int obterPior(){
        int indicePior = 0;
        double pior = fitness(populacao.get(0));
        for(int i = 1; i < this.tamPopulacao; i++){
            double nota = fitness(populacao.get(i));
            if(nota < pior){
                pior = nota;
                indicePior = i;
            }
        }
        return indicePior;
    }

    protected ArrayList<Integer> obterPiorCromossomo(){
        return populacao.get(obterPior());
    }

    protected ArrayList<Integer> obterMelhor(){
        int indiceMelhor = 0;
        double melhor = fitness(populacao.get(0));
        for(int i = 1; i < this.tamPopulacao; i++){
            double nota = fitness(populacao.get(i));
            if(nota > melhor){
                melhor = nota;
                indiceMelhor = i;
            }
        }
        return populacao.get(indiceMelhor);
    }
    
    private void novoPopulacao(){
        for(int i = 0; i < this.qtdCruzamentos; i++){
            populacao.remove(obterPior());
            populacao.remove(obterPior());
        }
    }
}