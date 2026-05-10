package model;

import java.util.Set;

class DeslocarPiao {

    boolean deslocarPiao(Jogador jogador, int casaEscolhida, Set<Integer> casasAlcancaveis) {
        if (!casasAlcancaveis.contains(casaEscolhida)) {
            return false;
        }
        jogador.setPosicaoAtual(casaEscolhida);
        return true;
    }
}