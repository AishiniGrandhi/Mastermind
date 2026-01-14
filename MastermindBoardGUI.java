import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;

/**
 * MastermindBoardGUI: Interactive interface using Java Swing.
 * Connects the GraphBFSNavigator (AI) and MastermindEngine (Logic).
 */
public class MastermindBoardGUI extends JPanel {
    // --- UI Scaling Constants ---
    private static final int PEG_SZ = 32;
    private static final double PEG_GAP = 0.10;
    private static final double PEG_HINT = 0.35;
    private static final double BORDER_MUL = 0.5;

    private final int gapSz = (int) (PEG_SZ * PEG_GAP);
    private final int hintSz = (int) (PEG_SZ * PEG_HINT);
    private final int border = (int) (PEG_SZ * BORDER_MUL);
    private final int pegOff = PEG_SZ + gapSz;

    // --- Core Logic and State (Renamed) ---
    private MastermindEngine engine; // Formerly MastermindGame
    private GraphBFSNavigator navigator; // Formerly MastermindSolver
    private int[][] history;
    private int[][] feedbackHistory;
    private int[] currentGuess = new int[MastermindEngine.SLOTS];
    private int nextGo = 0;
    private boolean solved = false;
    private boolean revealSolution = false;
    private boolean isHumanTurn = true;

    private int dragColor = -1;

    public MastermindBoardGUI() {
        initGameSystem();
        setBackground(new Color(224, 224, 224));
        setPreferredSize(calculateBoardSize());
        initInputListeners();
    }

    private void initGameSystem() {
        this.engine = new MastermindEngine();
        this.navigator = new GraphBFSNavigator(engine);
        this.history = new int[10][MastermindEngine.SLOTS];
        this.feedbackHistory = new int[10][2];

        for (int[] row : history)
            Arrays.fill(row, -1);
        Arrays.fill(currentGuess, -1);

        this.nextGo = 0;
        this.solved = false;
        this.revealSolution = false;
        this.isHumanTurn = true;
    }

    private Dimension calculateBoardSize() {
        int hintW = (MastermindEngine.SLOTS + 1) / 2;
        int w = (int) (PEG_SZ * (BORDER_MUL * 2 + 2.0 + MastermindEngine.SLOTS + (PEG_GAP * MastermindEngine.SLOTS)
                + (PEG_HINT * hintW + 1)));
        int h = (int) (PEG_SZ * (BORDER_MUL * 2 + (10 + 2) + (PEG_GAP * (10 + 1))));
        return new Dimension(w, h);
    }

    /**
     * Cooperative logic using the Graph-based AI.
     */
    private void submitGuess() {
        if (nextGo >= 10 || solved)
            return;

        // 1. Evaluate current move using Engine
        int[] fb = engine.evaluateGuess(currentGuess, engine.getSecret());
        feedbackHistory[nextGo] = fb;
        history[nextGo] = currentGuess.clone();

        // 2. PRUNE the Graph and SORT remaining nodes
        navigator.traverseToNextState(currentGuess, fb);

        if (fb[0] == MastermindEngine.SLOTS) {
            solved = true;
            revealSolution = true;
            String winner = isHumanTurn ? "Human" : "Bot";
            repaint();
            JOptionPane.showMessageDialog(this, "Goal State reached by " + winner + "!");
        } else {
            nextGo++;
            isHumanTurn = !isHumanTurn;

            // 3. Trigger Bot using GREEDY selection from BFS Navigator
            if (!isHumanTurn) {
                Timer botTimer = new Timer(800, e -> {
                    int[] botMove = navigator.getGreedyMove();
                    if (botMove != null) {
                        currentGuess = botMove.clone();
                        submitGuess();
                    }
                });
                botTimer.setRepeats(false);
                botTimer.start();
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int guessX = border + PEG_SZ * 2;
        int guessY = border;

        // Draw Palette (Vertices Colors)
        for (int i = 0; i < MastermindEngine.COLORS; i++) {
            renderPeg(g2, border, guessY + (i * pegOff), i, PEG_SZ);
        }

        // Draw Search History (Graph Path)
        for (int r = 0; r < 10; r++) {
            int rowY = guessY + (r * pegOff);

            if (r == nextGo && !solved) {
                g2.setColor(new Color(255, 128, 128));
                g2.fillRect(guessX - 5, rowY, 2, PEG_SZ);
            }

            for (int p = 0; p < MastermindEngine.SLOTS; p++) {
                int col = (r == nextGo) ? currentGuess[p] : history[r][p];
                renderPeg(g2, guessX + (p * pegOff), rowY, col, PEG_SZ);
            }
            renderHints(g2, guessX + (MastermindEngine.SLOTS * pegOff) + gapSz, rowY, r);
        }

        // Show Solution Node at the bottom
        if (revealSolution) {
            int solnY = guessY + (10 + 1) * pegOff;
            g2.setColor(Color.BLACK);
            g2.drawLine(guessX, solnY - 5, guessX + (MastermindEngine.SLOTS * pegOff), solnY - 5);

            int[] secret = engine.getSecret();
            for (int p = 0; p < secret.length; p++) {
                renderPeg(g2, guessX + (p * pegOff), solnY, secret[p], PEG_SZ);
            }
        }
    }

    private void renderPeg(Graphics2D g2, int x, int y, int colorIdx, int size) {
        if (colorIdx == -1) {
            g2.setColor(new Color(149, 149, 149));
        } else {
            Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN, new Color(51, 77, 255), Color.ORANGE,
                    new Color(128, 0, 179) };
            g2.setColor(colors[colorIdx % colors.length]);
        }
        g2.fill(new Ellipse2D.Double(x, y, size, size));
        g2.setColor(Color.BLACK);
        g2.draw(new Ellipse2D.Double(x, y, size, size));
    }

    private void renderHints(Graphics2D g2, int x, int y, int r) {
        int black = feedbackHistory[r][0];
        int white = feedbackHistory[r][1];
        int hOff = hintSz + gapSz;

        for (int i = 0; i < MastermindEngine.SLOTS; i++) {
            int hX = x + (i % 2) * hOff;
            int hY = y + (i / 2) * hOff;
            if (i < black)
                g2.setColor(Color.BLACK);
            else if (i < black + white)
                g2.setColor(Color.WHITE);
            else
                g2.setColor(new Color(149, 149, 149));

            g2.fill(new Ellipse2D.Double(hX, hY, hintSz, hintSz));
            g2.setColor(Color.BLACK);
            g2.draw(new Ellipse2D.Double(hX, hY, hintSz, hintSz));
        }
    }

    private void initInputListeners() {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (solved || !isHumanTurn)
                    return;
                // Check if clicking the palette (colors on the left)
                if (e.getX() >= border && e.getX() <= border + PEG_SZ) {
                    int idx = (e.getY() - border) / pegOff;
                    if (idx >= 0 && idx < MastermindEngine.COLORS) {
                        dragColor = idx;
                        // CHANGE 1: Change cursor to "Hand" when grabbing
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // CHANGE 2: Reset cursor back to default when color is dropped
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                if (dragColor != -1) {
                    int p = (e.getX() - (border + PEG_SZ * 2)) / pegOff;
                    if ((e.getY() - border) / pegOff == nextGo && p >= 0 && p < MastermindEngine.SLOTS) {
                        currentGuess[p] = dragColor;
                    }
                    dragColor = -1;
                    repaint();
                }

                // Check for Submit click
                if (e.getX() > border + PEG_SZ * 2 + (MastermindEngine.SLOTS * pegOff) && isHumanTurn) {
                    submitGuess();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // CHANGE 3: Feedback when hovering over the palette colors
                if (e.getX() >= border && e.getX() <= border + PEG_SZ) {
                    int idx = (e.getY() - border) / pegOff;
                    if (idx >= 0 && idx < MastermindEngine.COLORS) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                } else {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        };

        // Must add both listeners for the cursor to update while moving
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Mastermind: BFS & Greedy Solver");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MastermindBoardGUI board = new MastermindBoardGUI();

        JPanel controls = new JPanel();
        JButton restartBtn = new JButton("Restart Game");
        restartBtn.addActionListener(e -> {
            board.initGameSystem();
            board.repaint();
        });
        controls.add(restartBtn);

        f.setLayout(new BorderLayout());
        f.add(board, BorderLayout.CENTER);
        f.add(controls, BorderLayout.SOUTH);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}