package model;

import java.util.concurrent.ThreadLocalRandom;

class Dado{
    private int numFaces;
    private int valorAtual;

    Dado(int numFaces) {
        this.numFaces = numFaces;
    }

    int lancar(){
        valorAtual = ThreadLocalRandom.current().nextInt(1, numFaces + 1);
        return valorAtual;
    }

    int getValorAtual() {
        return valorAtual;
    }
}