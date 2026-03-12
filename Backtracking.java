import java.util.*;

public class Backtracking {

    private MastermindEngine engine;

    private List<int[]> pastGuesses = new ArrayList<>();
    private List<int[]> pastFeedback = new ArrayList<>();

    public Backtracking(MastermindEngine engine) {
        this.engine = engine;
    }

    public void addConstraint(int[] guess, int[] feedback) {
        pastGuesses.add(guess.clone());
        pastFeedback.add(feedback.clone());
    }

    public List<int[]> generateCandidates() {

        List<int[]> results = new ArrayList<>();

        int[] candidate = new int[MastermindEngine.SLOTS];

        backtrack(0, candidate, results);

        return results;
    }

    private void backtrack(int pos, int[] candidate, List<int[]> results) {

        if (!prefixConsistent(candidate, pos))
            return;

        if (pos == MastermindEngine.SLOTS) {

            for (int i = 0; i < pastGuesses.size(); i++) {

                int[] guess = pastGuesses.get(i);
                int[] expected = pastFeedback.get(i);

                int[] fb = engine.evaluateGuess(guess, candidate);

                if (fb[0] != expected[0] || fb[1] != expected[1])
                    return;
            }

            results.add(candidate.clone());
            return;
        }

        for (int color = 0; color < MastermindEngine.COLORS; color++) {

            candidate[pos] = color;

            backtrack(pos + 1, candidate, results);
        }
    }

    private boolean prefixConsistent(int[] candidate, int filled) {

        for (int i = 0; i < pastGuesses.size(); i++) {

            int[] guess = pastGuesses.get(i);
            int[] expected = pastFeedback.get(i);

            int black = 0;

            for (int j = 0; j < filled; j++) {
                if (candidate[j] == guess[j])
                    black++;
            }

            if (black > expected[0])
                return false;
        }

        return true;
    }
}
