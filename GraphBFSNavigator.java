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
    private Queue<SearchState> bfsQueue;
    private SearchState currentState;

    public GraphBFSNavigator(MastermindEngine engine) {
        this.engine = engine;
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
        for (int[] vertex : currentState.reachableVertices) {
            int[] testFb = engine.evaluateGuess(lastGuess, vertex);
            if (testFb[0] == feedback[0] && testFb[1] == feedback[1]) {
                filteredVertices.add(vertex);
            }
        }

        // 2. SORTING: Order the reachable vertices lexicographically
        // This ensures the greedy choice is deterministic and prioritized
        filteredVertices.sort((a, b) -> {
            for (int i = 0; i < a.length; i++) {
                if (a[i] != b[i])
                    return Integer.compare(a[i], b[i]);
            }
            return 0;
        });

        // 3. BFS QUEUE UPDATE: Move to the next level of the search
        this.currentState = new SearchState(filteredVertices, currentState.depth + 1);
        bfsQueue.clear(); // Clear old level
        bfsQueue.add(currentState); // Add new level
    }

    // GREEDY SELECTION: Pick the first vertex from the sorted list
    public int[] getGreedyMove() {
        if (currentState.reachableVertices.isEmpty())
            return null;
        return currentState.reachableVertices.get(0);
    }
}