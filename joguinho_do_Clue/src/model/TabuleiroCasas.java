package model;

import java.util.*;

public class TabuleiroCasas {

    public static final int COLS = 28;
    public static final int ROWS = 27;

    // IDs dos comodos
    public static final int COZINHA        = 1000;
    public static final int SALA_MUSICA    = 1001;
    public static final int JARDIM_INVERNO = 1002;
    public static final int SALAO_JOGOS    = 1003;
    public static final int SALA_JANTAR    = 1004;
    public static final int DETETIVE       = 1005;
    public static final int BIBLIOTECA     = 1006;
    public static final int ENTRADA        = 1007;
    public static final int SALA_ESTAR     = 1008;
    public static final int ESCRITORIO     = 1009;

    // Posicoes iniciais
    static final int INICIO_CORONEL_MUSTARD = 17 * 28 + 2;
    static final int INICIO_SRA_WHITE       = 0  * 28 + 11;
    static final int INICIO_REV_GREEN       = 0  * 28 + 16;
    static final int INICIO_SRA_PEACOCK     = 6  * 28 + 25;
    static final int INICIO_SRTA_SCARLET    = 24 * 28 + 9;
    static final int INICIO_PROF_PLUM       = 19 * 28 + 25;

    // Passagens secretas
    public static final int[][] PASSAGENS_SECRETAS = {
        {COZINHA, ESCRITORIO},
        {JARDIM_INVERNO, SALA_ESTAR}
    };

    // Configuracoes da grade para a View
    public static final int GRID_X0   = 0;
    public static final int GRID_Y0   = 50;
    public static final int CELL_SIZE = 25;

    private static final Set<Integer> bloqueadas = new HashSet<>();
    // porta (cellId) -> comodoId: usada apenas para construir o grafo
    private static final Map<Integer, Integer> portasParaComodo = new HashMap<>();

    static {
        inicializarBloqueadas();
        inicializarPortas();
    }

    static int cellId(int row, int col) {
        return row * COLS + col;
    }

    public static int[] cellPos(int id) {
        return new int[]{id / COLS, id % COLS};
    }

    public static int pixelParaCasa(int px, int py) {
        int col = (px - GRID_X0) / CELL_SIZE;
        int row = (py - GRID_Y0) / CELL_SIZE;
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) return -1;
        return cellId(row, col);
    }

    public static int[] casaParaPixel(int casaId) {
        if (isComodo(casaId)) return centroComodo(casaId);
        int[] pos = cellPos(casaId);
        int px = GRID_X0 + pos[1] * CELL_SIZE + CELL_SIZE / 2;
        int py = GRID_Y0 + pos[0] * CELL_SIZE + CELL_SIZE / 2;
        return new int[]{px, py};
    }

    public static boolean isComodo(int casaId) {
        return casaId >= 1000;
    }

    // Verifica se um comodo tem passagem secreta
    public static int passagemSecreta(int comodoId) {
        for (int[] par : PASSAGENS_SECRETAS) {
            if (par[0] == comodoId) return par[1];
            if (par[1] == comodoId) return par[0];
        }
        return -1; // sem passagem secreta
    }

    public static String nomeComodo(int comodoId) {
        switch (comodoId) {
            case COZINHA:        return "Cozinha";
            case SALA_MUSICA:    return "Sala de Música";
            case JARDIM_INVERNO: return "Jardim de Inverno";
            case SALAO_JOGOS:    return "Salão de Jogos";
            case SALA_JANTAR:    return "Sala de Jantar";
            case DETETIVE:       return "Detetive";
            case BIBLIOTECA:     return "Biblioteca";
            case ENTRADA:        return "Entrada";
            case SALA_ESTAR:     return "Sala de Estar";
            case ESCRITORIO:     return "Escritório";
            default:             return "Desconhecido";
        }
    }

    static Map<Integer, List<Integer>> construirGrafo() {
        Map<Integer, List<Integer>> adj = new HashMap<>();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int cid = cellId(r, c);

                // Portas e bloqueadas nao sao nos do grafo
                if (bloqueadas.contains(cid) || portasParaComodo.containsKey(cid)) continue;

                List<Integer> vizinhos = new ArrayList<>();
                int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};

                for (int[] d : dirs) {
                    int nr = r + d[0], nc = c + d[1];
                    if (nr < 0 || nr >= ROWS || nc < 0 || nc >= COLS) continue;
                    int nid = cellId(nr, nc);

                    if (portasParaComodo.containsKey(nid)) {
                        // Vizinho e uma porta: aponta direto pro comodo
                        int comodoId = portasParaComodo.get(nid);
                        if (!vizinhos.contains(comodoId)) vizinhos.add(comodoId);
                    } else if (!bloqueadas.contains(nid)) {
                        vizinhos.add(nid);
                    }
                }
                adj.put(cid, vizinhos);
            }
        }

        // Saidas dos comodos: comodo aponta para celulas externas adjacentes a cada porta
        for (Map.Entry<Integer, Integer> e : portasParaComodo.entrySet()) {
            int portaId  = e.getKey();
            int comodoId = e.getValue();
            int[] porta  = cellPos(portaId);

            adj.computeIfAbsent(comodoId, k -> new ArrayList<>());

            int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
            for (int[] d : dirs) {
                int nr = porta[0] + d[0], nc = porta[1] + d[1];
                if (nr < 0 || nr >= ROWS || nc < 0 || nc >= COLS) continue;
                int nid = cellId(nr, nc);
                // Apenas celulas externas caminhaveis (nao porta, nao bloqueada)
                if (!bloqueadas.contains(nid) && !portasParaComodo.containsKey(nid)) {
                    if (!adj.get(comodoId).contains(nid)) adj.get(comodoId).add(nid);
                }
            }
        }

        // Passagens secretas (custo 0 de passos — tratado no controller, nao no grafo)
        // NAO adicionamos aqui pois passagem secreta substitui o lancamento dos dados

        return adj;
    }

    private static void inicializarBloqueadas() {
        for (int r = 0; r < ROWS; r++) {
            bloqueadas.add(cellId(r, 0));
            bloqueadas.add(cellId(r, 1));
            bloqueadas.add(cellId(r, 26));
            bloqueadas.add(cellId(r, 27));
        }
        for (int c = 0; c < COLS; c++) {
            bloqueadas.add(cellId(25, c));
            bloqueadas.add(cellId(26, c));
        }

        adicionarRange(0,  6,  2,  7,  new int[][]{{1,10},{1,11},{1,16},{1,17}});
        adicionarRange(1,  7,  10, 17, new int[][]{{1,10},{1,11},{1,16},{1,17}});
        adicionarRange(1,  5,  20, 25, new int[][]{{5,20}});
        adicionarRange(8,  12, 20, 25, new int[][]{});
        adicionarRange(9,  15, 2,  9,  new int[][]{{9,7},{9,8},{9,9}});
        adicionarRange(10, 16, 12, 16, new int[][]{});
        adicionarRange(14, 18, 19, 25, new int[][]{{14,19},{18,19}});
        adicionarRange(19, 24, 2,  8,  new int[][]{});
        adicionarRange(18, 24, 11, 16, new int[][]{});
        adicionarRange(21, 24, 19, 25, new int[][]{});
    }

    private static void adicionarRange(int r0, int r1, int c0, int c1, int[][] excluir) {
        Set<String> exc = new HashSet<>();
        for (int[] e : excluir) exc.add(e[0] + "," + e[1]);
        for (int r = r0; r <= r1; r++)
            for (int c = c0; c <= c1; c++)
                if (!exc.contains(r + "," + c)) bloqueadas.add(cellId(r, c));
    }

    private static void inicializarPortas() {
        portasParaComodo.put(cellId(5,9),   SALA_MUSICA);
        portasParaComodo.put(cellId(8,11),  SALA_MUSICA);
        portasParaComodo.put(cellId(8,16),  SALA_MUSICA);
        portasParaComodo.put(cellId(5,18),  SALA_MUSICA);
        portasParaComodo.put(cellId(7,6),   COZINHA);
        portasParaComodo.put(cellId(5,20),  JARDIM_INVERNO);
        portasParaComodo.put(cellId(13,24), SALAO_JOGOS);
        portasParaComodo.put(cellId(9,19),  SALAO_JOGOS);
        portasParaComodo.put(cellId(13,22), BIBLIOTECA);
        portasParaComodo.put(cellId(16,18), BIBLIOTECA);
        portasParaComodo.put(cellId(18,8),  SALA_ESTAR);
        portasParaComodo.put(cellId(20,19), ESCRITORIO);
        portasParaComodo.put(cellId(16,8),  SALA_JANTAR);
        portasParaComodo.put(cellId(12,10), SALA_JANTAR);
        portasParaComodo.put(cellId(20,17), ENTRADA);
        portasParaComodo.put(cellId(17,13), ENTRADA);
        portasParaComodo.put(cellId(17,14), ENTRADA);
    }

    private static int[] centroComodo(int comodoId) {
        switch (comodoId) {
            case COZINHA:        return new int[]{GRID_X0 + 4*CELL_SIZE,  GRID_Y0 + 3*CELL_SIZE};
            case SALA_MUSICA:    return new int[]{GRID_X0 + 13*CELL_SIZE, GRID_Y0 + 4*CELL_SIZE};
            case JARDIM_INVERNO: return new int[]{GRID_X0 + 22*CELL_SIZE, GRID_Y0 + 3*CELL_SIZE};
            case SALAO_JOGOS:    return new int[]{GRID_X0 + 22*CELL_SIZE, GRID_Y0 + 10*CELL_SIZE};
            case SALA_JANTAR:    return new int[]{GRID_X0 + 5*CELL_SIZE,  GRID_Y0 + 12*CELL_SIZE};
            case DETETIVE:       return new int[]{GRID_X0 + 14*CELL_SIZE, GRID_Y0 + 13*CELL_SIZE};
            case BIBLIOTECA:     return new int[]{GRID_X0 + 22*CELL_SIZE, GRID_Y0 + 16*CELL_SIZE};
            case SALA_ESTAR:     return new int[]{GRID_X0 + 5*CELL_SIZE,  GRID_Y0 + 21*CELL_SIZE};
            case ENTRADA:        return new int[]{GRID_X0 + 13*CELL_SIZE, GRID_Y0 + 21*CELL_SIZE};
            case ESCRITORIO:     return new int[]{GRID_X0 + 22*CELL_SIZE, GRID_Y0 + 22*CELL_SIZE};
            default:             return new int[]{0, 0};
        }
    }
}