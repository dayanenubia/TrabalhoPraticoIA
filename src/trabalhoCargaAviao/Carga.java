
package trabalhoCargaAviao;

public class Carga {
    private String descricao;
    private double valor;
    private double largura;
    private double altura;
    private double profundidade;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getLargura() {
        return largura;
    }

    public void setLargura(double largura) {
        this.largura = largura;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getProfundidade() {
        return profundidade;
    }

    public void setProfundidade(double profundidade) {
        this.profundidade = profundidade;
    }

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "Carga [descricao=" + descricao + ", valor=" + valor + ", largura=" + largura + ", altura=" + altura
				+ ", profundidade=" + profundidade + "]";
	}

    
}
