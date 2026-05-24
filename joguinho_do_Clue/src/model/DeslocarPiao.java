package model;

import java.util.Set;

public class DeslocarPiao {

	public boolean deslocarPiao(Jogador jogador, int casaEscolhida, Set<Integer> casasAlcancaveis) {
        if (!casasAlcancaveis.contains(casaEscolhida)) {
            return false;
        }
        jogador.setPosicaoAtual(casaEscolhida);
        return true;
    }
}