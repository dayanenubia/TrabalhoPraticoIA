package trabalhoCargaAviao;

public class Main {

    public static void main(String[] args) {
        int populacao = 20;
        double limitePeso = 8000; // Capacidade máxima de peso do avião em kg
        double larguraMaxima = 300; // Largura máxima da área de carga em cm
        double alturaMaxima = 250; // Altura máxima da área de carga em cm
        double profundidadeMaxima = 1000; // Profundidade máxima da área de carga em cm
        int probabilidadeMutacao = 5;
        int qtdCruzamento = 5;
        int numeroGeracoes = 10;

        AlgoritmoGenetico meuAg = new AlgoritmoGenetico(
            populacao, limitePeso, larguraMaxima, alturaMaxima, profundidadeMaxima, probabilidadeMutacao, qtdCruzamento, numeroGeracoes
        );

        meuAg.carregaArquivo("carga_aviao.csv");
        meuAg.executar();
    }
}
