import java.util.*;

public class GraphBFSNavigator {
    // A Node in our Graph Traversal
    private static class SearchState {
        List<int[]> reachableVertices;
        int depth;

        SearchState(List<int[]> vertices, int depth) {
            this.reachableVertices = vertices;
            this.depth = depth;
        }
    }

    private final MastermindEngine engine;
    private Backtracking backtracking;
    private Queue<SearchState> bfsQueue;
    private SearchState currentState;

    public GraphBFSNavigator(MastermindEngine engine) {
        this.engine = engine;
        this.backtracking = new Backtracking(engine);
        reset();
    }

    public void reset() {
        List<int[]> allVertices = engine.generateAllGraphVertices();
        this.bfsQueue = new LinkedList<>();
        this.currentState = new SearchState(allVertices, 0);
        bfsQueue.add(currentState);
    }

    // BFS STEP: Moves to the next level in the Graph
    public void traverseToNextState(int[] lastGuess, int[] feedback) {
        List<int[]> filteredVertices = new ArrayList<>();

        // 1. PRUNING: Only keep vertices that satisfy the graph edges (constraints)
        //Before backtracking :
        /*
        for (int[] vertex : currentState.reachableVertices) {
            //int[] testFb = engine.evaluateGuess(lastGuess, vertex); -> before DP
            int[] testFb = engine.getFeedbackDP(lastGuess, vertex);
            if (testFb[0] == feedback[0] && testFb[1] == feedback[1]) {
                filteredVertices.add(vertex);
            }
        }*/
       //BACKTRACKING: 
        backtracking.addConstraint(lastGuess, feedback);
        filteredVertices = backtracking.generateCandidates();

        // 2. SORTING: Order the reachable vertices lexicographically
        // This ensures the greedy choice is deterministic and prioritized
        /*
        filteredVertices.sort((a, b) -> {
            for (int i = 0; i < a.length; i++) {
                if (a[i] != b[i])
                    return Integer.compare(a[i], b[i]);
            }
            return 0;
        });
         */

        filteredVertices = mergeSort(filteredVertices);

        // 3. BFS QUEUE UPDATE: Move to the next level of the search
        this.currentState = new SearchState(filteredVertices, currentState.depth + 1);
        bfsQueue.clear(); // Clear old level
        bfsQueue.add(currentState); // Add new level
    }

    private List<int[]> mergeSort(List<int[]> list) {
        if (list.size() <= 1)
            return list;

        int mid = list.size() / 2;

        List<int[]> left = mergeSort(new ArrayList<>(list.subList(0, mid)));
        List<int[]> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())));

        return merge(left, right);
    }

    private List<int[]> merge(List<int[]> left, List<int[]> right) {
        List<int[]> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (lexCompare(left.get(i), right.get(j)) <= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }

        while (i < left.size())
            result.add(left.get(i++));
        while (j < right.size())
            result.add(right.get(j++));

        return result;
    }

    private int lexCompare(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return Integer.compare(a[i], b[i]);
            }
        }
        return 0;
    }


    // GREEDY SELECTION: Pick the first vertex from the sorted list
    /* 
    public int[] getGreedyMove() {
        if (currentState.reachableVertices.isEmpty())
            return null;
        return currentState.reachableVertices.get(0);
    }
    */

    public int[] getImprovedGreedyMove() {
        List<int[]> candidates = currentState.reachableVertices;

        if (candidates.isEmpty())
            return null;

        int bestScore = Integer.MAX_VALUE;
        int[] bestGuess = null;

        for (int[] guess : candidates) {

            // Map feedback -> count
            Map<String, Integer> buckets = new HashMap<>();

            for (int[] secret : currentState.reachableVertices) {
                //int[] fb = engine.evaluateGuess(guess, secret); -> before DP
                int[] fb = engine.getFeedbackDP(guess, secret);
                String key = fb[0] + "," + fb[1];
                buckets.put(key, buckets.getOrDefault(key, 0) + 1);
            }

            // Worst-case remaining possibilities
            int worstCase = 0;
            for (int size : buckets.values()) {
                worstCase = Math.max(worstCase, size);
            }

            // Minimize worst-case
            if (worstCase < bestScore) {
                bestScore = worstCase;
                bestGuess = guess;
            }
        }

        return bestGuess;
    }

}
