package model;

import java.util.*;

public class TabuleiroCasas {

    // Dimensoes da grade
	public static final int COLS = 28;
	public static final int ROWS = 27;

    // IDs dos comodos
    static final int COZINHA        = 1000;
    static final int SALA_MUSICA    = 1001;
    static final int JARDIM_INVERNO = 1002;
    static final int SALAO_JOGOS    = 1003;
    static final int SALA_JANTAR    = 1004;
    static final int DETETIVE       = 1005;
    static final int BIBLIOTECA     = 1006;
    static final int ENTRADA        = 1007;
    static final int SALA_ESTAR     = 1008;
    static final int ESCRITORIO     = 1009;

    // Posicoes iniciais dos personagens (linha * COLS + coluna)
    static final int INICIO_CORONEL_MUSTARD = 17 * 28 + 2;  // 478
    static final int INICIO_SRA_WHITE       = 0  * 28 + 11; // 11
    static final int INICIO_REV_GREEN       = 0  * 28 + 16; // 16
    static final int INICIO_SRA_PEACOCK     = 6  * 28 + 25; // 193
    static final int INICIO_SRTA_SCARLET    = 24 * 28 + 9;  // 681
    static final int INICIO_PROF_PLUM       = 19 * 28 + 25; // 557

    // Configuracoes da grade para a View
    public static final int GRID_X0   = 0;
    public static final int GRID_Y0   = 50;
    public static final int CELL_SIZE = 25;

    private static Set<Integer> bloqueadas;
    private static Map<Integer, Integer> portasParaComodo;

    static {
        bloqueadas = new HashSet<>();
        portasParaComodo = new HashMap<>();
        inicializarBloqueadas();
        inicializarPortas();
    }

    static int cellId(int row, int col) {
        return row * COLS + col;
    }

    public static int[] cellPos(int id) {
        return new int[]{id / COLS, id % COLS};
    }

    // Pixel -> casaId (para clique do mouse na View)
    public static int pixelParaCasa(int px, int py) {
        int col = (px - GRID_X0) / CELL_SIZE;
        int row = (py - GRID_Y0) / CELL_SIZE;
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) return -1;
        return cellId(row, col);
    }

    // CasaId -> pixel central (para desenhar o peao na View)
    public static int[] casaParaPixel(int casaId) {
        if (casaId >= 1000) {
            return centroComodo(casaId);
        }
        int[] pos = cellPos(casaId);
        int px = GRID_X0 + pos[1] * CELL_SIZE + CELL_SIZE / 2;
        int py = GRID_Y0 + pos[0] * CELL_SIZE + CELL_SIZE / 2;
        return new int[]{px, py};
    }

    public static boolean isBloqueada(int row, int col) {
        return bloqueadas.contains(cellId(row, col));
    }

    public static boolean isComodo(int casaId) {
        return casaId >= 1000;
    }

    // Constroi o grafo completo para o MapeaCasas
    static Map<Integer, List<Integer>> construirGrafo() {
        Map<Integer, List<Integer>> adj = new HashMap<>();

        // Celulas caminhaveis normais
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (bloqueadas.contains(cellId(r, c))) continue;
                int cid = cellId(r, c);
                List<Integer> vizinhos = new ArrayList<>();
                int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
                for (int[] d : dirs) {
                    int nr = r + d[0], nc = c + d[1];
                    if (nr < 0 || nr >= ROWS || nc < 0 || nc >= COLS) continue;
                    int nid = cellId(nr, nc);
                    if (!bloqueadas.contains(nid)) {
                        if (portasParaComodo.containsKey(nid)) {
                            int comodoId = portasParaComodo.get(nid);
                            if (!vizinhos.contains(comodoId)) vizinhos.add(comodoId);
                        } else {
                            vizinhos.add(nid);
                        }
                    }
                }
                adj.put(cid, vizinhos);
            }
        }

        // Saidas dos comodos (porta -> celulas externas adjacentes)
        for (Map.Entry<Integer, Integer> e : portasParaComodo.entrySet()) {
            int[] porta = cellPos(e.getKey());
            int comodoId = e.getValue();
            adj.computeIfAbsent(comodoId, k -> new ArrayList<>());
            int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
            for (int[] d : dirs) {
                int nr = porta[0] + d[0], nc = porta[1] + d[1];
                if (nr < 0 || nr >= ROWS || nc < 0 || nc >= COLS) continue;
                int nid = cellId(nr, nc);
                if (!bloqueadas.contains(nid) && !portasParaComodo.containsKey(nid)) {
                    if (!adj.get(comodoId).contains(nid)) adj.get(comodoId).add(nid);
                }
            }
        }

        // Passagens secretas
        adj.computeIfAbsent(COZINHA,        k -> new ArrayList<>()).add(ESCRITORIO);
        adj.computeIfAbsent(ESCRITORIO,     k -> new ArrayList<>()).add(COZINHA);
        adj.computeIfAbsent(JARDIM_INVERNO, k -> new ArrayList<>()).add(SALA_ESTAR);
        adj.computeIfAbsent(SALA_ESTAR,     k -> new ArrayList<>()).add(JARDIM_INVERNO);

        return adj;
    }

    private static void inicializarBloqueadas() {
        // Bordas do mapa (fora do tabuleiro jogavel)
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

        // Interior dos comodos
        adicionarRange(0,  6,  2,  7,  new int[][]{{1,10},{1,11},{1,16},{1,17}}); // COZINHA (sem irregulares da sala musica)
        adicionarRange(1,  7,  10, 17, new int[][]{{1,10},{1,11},{1,16},{1,17}}); // SALA_MUSICA
        adicionarRange(1,  5,  20, 25, new int[][]{{5,20}});                      // JARDIM_INVERNO
        adicionarRange(8,  12, 20, 25, new int[][]{});                             // SALAO_JOGOS
        adicionarRange(9,  15, 2,  9,  new int[][]{{9,7},{9,8},{9,9}});           // SALA_JANTAR
        adicionarRange(10, 16, 12, 16, new int[][]{});                             // DETETIVE
        adicionarRange(14, 18, 19, 25, new int[][]{{14,19},{18,19}});             // BIBLIOTECA
        adicionarRange(19, 24, 2,  8,  new int[][]{});                             // SALA_ESTAR
        adicionarRange(18, 24, 11, 16, new int[][]{});                             // ENTRADA
        adicionarRange(21, 24, 19, 25, new int[][]{});                             // ESCRITORIO
    }

    private static void adicionarRange(int r0, int r1, int c0, int c1, int[][] excluir) {
        Set<String> exc = new HashSet<>();
        for (int[] e : excluir) exc.add(e[0] + "," + e[1]);
        for (int r = r0; r <= r1; r++) {
            for (int c = c0; c <= c1; c++) {
                if (!exc.contains(r + "," + c)) bloqueadas.add(cellId(r, c));
            }
        }
    }

    private static void inicializarPortas() {
        // Sala de Musica
        portasParaComodo.put(cellId(5,9),  SALA_MUSICA);
        portasParaComodo.put(cellId(8,11), SALA_MUSICA);
        portasParaComodo.put(cellId(8,16), SALA_MUSICA);
        portasParaComodo.put(cellId(5,18), SALA_MUSICA);
        // Cozinha
        portasParaComodo.put(cellId(7,6),  COZINHA);
        // Jardim de Inverno
        portasParaComodo.put(cellId(5,20), JARDIM_INVERNO);
        // Salao de Jogos
        portasParaComodo.put(cellId(13,24),SALAO_JOGOS);
        portasParaComodo.put(cellId(9,19), SALAO_JOGOS);
        // Biblioteca
        portasParaComodo.put(cellId(13,22),BIBLIOTECA);
        portasParaComodo.put(cellId(16,18),BIBLIOTECA);
        // Sala de Estar
        portasParaComodo.put(cellId(18,8), SALA_ESTAR);
        // Escritorio
        portasParaComodo.put(cellId(20,19),ESCRITORIO);
        // Sala de Jantar
        portasParaComodo.put(cellId(16,8), SALA_JANTAR);
        portasParaComodo.put(cellId(12,10),SALA_JANTAR);
        // Entrada
        portasParaComodo.put(cellId(20,17),ENTRADA);
        portasParaComodo.put(cellId(17,13),ENTRADA);
        portasParaComodo.put(cellId(17,14),ENTRADA);

        // Remover portas das bloqueadas
        for (int portaId : portasParaComodo.keySet()) {
            bloqueadas.remove(portaId);
        }
    }

    private static int[] centroComodo(int comodoId) {
        // Pixel central aproximado de cada comodo para posicionar o peao
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