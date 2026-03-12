import java.util.*;

public class MastermindEngine {
    public static final int SLOTS = 4;
    public static final int COLORS = 6;

    private final int[] secretCode;
    private final Random random = new Random();

    //DP TABLE
    private int[][][] feedbackTable;
    private List<int[]> allCodes;

    public MastermindEngine() {
        secretCode = new int[SLOTS];
        for (int i = 0; i < SLOTS; i++)
            secretCode[i] = random.nextInt(COLORS);

        allCodes = generateAllGraphVertices();
        buildFeedbackTable();
    }

    // GRAPH GENERATION: Brute-force generation of all Vertices (V)
    // Replaces recursion with iterative loops
    public List<int[]> generateAllGraphVertices() {
        List<int[]> vertices = new ArrayList<>();
        for (int i = 0; i < COLORS; i++) {
            for (int j = 0; j < COLORS; j++) {
                for (int k = 0; k < COLORS; k++) {
                    for (int l = 0; l < COLORS; l++) {
                        vertices.add(new int[] { i, j, k, l });
                    }
                }
            }
        }
        return vertices;
    }

    private void buildFeedbackTable() {

        int n = allCodes.size(); // 1296
        feedbackTable = new int[n][n][2];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                int[] guess = allCodes.get(i);
                int[] secret = allCodes.get(j);

                int[] fb = evaluateGuess(guess, secret);

                feedbackTable[i][j][0] = fb[0];
                feedbackTable[i][j][1] = fb[1];
            }
        }
    }

    public int[] getFeedbackDP(int[] guess, int[] secret) {

        int guessID = codeToID(guess);
        int secretID = codeToID(secret);

        return feedbackTable[guessID][secretID];
    }

    public int codeToID(int[] code) {
        int id = 0;

        for (int i = 0; i < SLOTS; i++) {
            id = id * COLORS + code[i];
        }

        return id;
    }

    public int[] evaluateGuess(int[] guess, int[] secret) {
        int black = 0, white = 0;
        boolean[] sUsed = new boolean[SLOTS];
        boolean[] gUsed = new boolean[SLOTS];

        for (int i = 0; i < SLOTS; i++) {
            if (guess[i] == secret[i]) {
                black++;
                sUsed[i] = gUsed[i] = true;
            }
        }
        for (int i = 0; i < SLOTS; i++) {
            if (gUsed[i])
                continue;
            for (int j = 0; j < SLOTS; j++) {
                if (!sUsed[j] && guess[i] == secret[j]) {
                    white++;
                    sUsed[j] = true;
                    break;
                }
            }
        }
        return new int[] { black, white };
    }

    public int[] getSecret() {
        return secretCode.clone();
    }
}
