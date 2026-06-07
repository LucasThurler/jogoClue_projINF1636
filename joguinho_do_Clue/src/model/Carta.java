package model;

public class Carta {
    private String nome;
    private TipoCarta tipo;

    Carta(String nome, TipoCarta tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    TipoCarta getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return tipo + ": " + nome;
    }
    
//    public List<Carta> getMao() { 
//    	return mao; 
//    	}
}

