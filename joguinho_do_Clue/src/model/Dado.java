package model;

import java.util.concurrent.ThreadLocalRandom;

class Dado{
    private final int numFaces;
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

    //APENAS PARA DEBUG
    public static void main(String[] args) {
        Dado d = new Dado(6);
        d.lancar();
        System.out.println("Valor: " + d.getValorAtual());
    }
}