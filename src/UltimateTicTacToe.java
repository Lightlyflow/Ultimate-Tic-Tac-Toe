import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UltimateTicTacToe extends JFrame {
    int turn;
    int[] toGo;
    int[][] outerBoardState;
    boolean nextCheckDisabled;
    String winner;
    JPanel[][] panels;
    JButton[][] buttons;
    GridBagConstraints c;
    static Container container;

    public UltimateTicTacToe() {
        super("Ultimate Tic Tac Toe");

        this.getContentPane().setLayout(new GridBagLayout());
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initBoard();
    }
    // Methods
    void initBoard() {
        panels = new JPanel[3][3];
        buttons = new JButton[9][9];
        outerBoardState = new int[3][3];
        c = new GridBagConstraints();
        nextCheckDisabled = true;
        toGo = new int[2];
        turn = 0;
        winner = "";
        container = this.getContentPane();
        this.getContentPane().setBackground(Color.GRAY);

        // Outer board
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                // GridBagConstraints
                this.c.gridx = c;
                this.c.gridy = r;
                this.c.fill = GridBagConstraints.BOTH;
                this.c.insets = new Insets(2, 2, 2, 2);
                this.c.weightx = 1.0;
                this.c.weighty = 1.0;

                panels[r][c] = new JPanel();
                this.getContentPane().add(panels[r][c], this.c);
            }
        }

        // Add inner boards
        addTicTacToeBoard(panels[0][0], 0, 0);
        addTicTacToeBoard(panels[0][1], 0, 1);
        addTicTacToeBoard(panels[0][2], 0, 2);
        addTicTacToeBoard(panels[1][0], 1, 0);
        addTicTacToeBoard(panels[1][1], 1, 1);
        addTicTacToeBoard(panels[1][2], 1, 2);
        addTicTacToeBoard(panels[2][0], 2, 0);
        addTicTacToeBoard(panels[2][1], 2, 1);
        addTicTacToeBoard(panels[2][2], 2, 2);

        this.setVisible(true);
    }
    void addTicTacToeBoard(JPanel parent, int row, int col) {
        new TicTacToeBoard(parent, row, col);
    }

    boolean checkWin() {
        // Check horizontal wins
        for (int[] row : outerBoardState) {
            if(row[0]==0) {
                continue;
            }
            if (row[0] + row[1] + row[2] == 3) {
                winner = "Player 2";
                return true;
            } else if (row[0] + row[1] + row[2] == -3) {
                winner = "Player 1";
                return true;
            }
        }
        // Check vertical wins
        for (int r = 0; r < 3; r++) {
            if (outerBoardState[0][r] + outerBoardState[1][r] +outerBoardState[2][r] == 3) {
                winner = "Player 2";
                return true;
            } else if (outerBoardState[0][r] + outerBoardState[1][r] +outerBoardState[2][r] == -3) {
                winner = "Player 1";
                return true;
            }
        }
        // Check diagonals
        if (outerBoardState[0][2] + outerBoardState[1][1] + outerBoardState[2][0] == 3) {
            winner = "Player 2";
            return true;
        } else if (outerBoardState[0][2] + outerBoardState[1][1] + outerBoardState[2][0] == -3) {
            winner = "Player 1";
            return true;
        } else if (outerBoardState[0][0] + outerBoardState[1][1] + outerBoardState[2][2] == 3) {
            winner = "Player 2";
            return true;
        } else if (outerBoardState[0][0] + outerBoardState[1][1] + outerBoardState[2][2] == -3) {
            winner = "Player 1";
            return true;
        }
        return false;
    }

    // Inner Classes
    static class TicTacToeButton extends JButton {
        private final int ROW;
        private final int COL;
        private final int OROW;
        private final int OCOL;

        public TicTacToeButton(String name, int row, int col, int orow, int ocol) {
            super(name);
            ROW = row;
            COL = col;
            OROW = orow;
            OCOL = ocol;
        }
        public int getRow() {   return ROW; }
        public int getCol() {   return COL; }
        public int getOROW() {  return OROW;}
        public int getOCOL() {  return OCOL;}
    }
    class TicTacToeBoard {
        JPanel parent;
        int[][] boardState;
        private int state;

        // Constructor
        public TicTacToeBoard(JPanel parent, int row, int col) {
            this.parent = parent;
            boardState = new int[3][3];
            state = 0;
            parent.setLayout(new GridBagLayout());

            // Add buttons inside panel
            for (int ir = 0; ir < 3; ir++) {
                for (int ic = 0; ic < 3; ic++) {
                    // GridBagConstraints
                    c.gridx = ic;
                    c.gridy = ir;
                    c.fill = GridBagConstraints.BOTH;
                    c.insets = new Insets(1,1,1,1);
                    c.weightx = 1.0;
                    c.weighty = 1.0;

                    buttons[row*3+ir][col*3+ic] = new TicTacToeButton(null, ir, ic, row, col);

                    buttons[row*3+ir][col*3+ic].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            TicTacToeButton button = (TicTacToeButton) e.getSource();

                            // Check if in wrong row and column
                            if (!nextCheckDisabled) {
                                if (button.getOROW() != toGo[0] || button.getOCOL() != toGo[1]) {
                                    JOptionPane.showMessageDialog(UltimateTicTacToe.container, "Wrong place!");
                                    return;
                                }
                            } else {
                                nextCheckDisabled = false;
                            }

                            toGo[0] = button.getRow();
                            toGo[1] = button.getCol();
                            button.setEnabled(false);

                            // Check if toGo is not possible
                            if (outerBoardState[toGo[0]][toGo[1]] != 0)
                                nextCheckDisabled = true;

                            print1DArr("To Go:", toGo);
                            if (turn++ % 2 == 0) {
                                button.setBackground(Color.PINK);
                                boardState[button.getRow()][button.getCol()] = 1;
                            } else {
                                button.setBackground(Color.MAGENTA);
                                boardState[button.getRow()][button.getCol()] = -1;
                            }
                            print2DArr("Inner board: ", boardState);
                            if (isWin()) {
                                outerBoardState[row][col] = state;
                                print2DArr("Outer Board State:", outerBoardState);
                                for (Component component : parent.getComponents())
                                    component.setEnabled(false);
                                if (checkWin()) {
                                    JOptionPane.showMessageDialog(UltimateTicTacToe.container, String.format("%s won!", winner));
                                }
                            }
                        }
                        boolean isWin() {
                            // Check horizontal wins
                            for (int[] row : boardState) {
                                if(row[0]==0) {
                                    continue;
                                }
                                if (row[0] + row[1] + row[2] == 3) {
                                    winner = "Player 2";
                                    parent.setBackground(Color.PINK);
                                    state = 1;
                                    return true;
                                } else if (row[0] + row[1] + row[2] == -3) {
                                    winner = "Player 1";
                                    parent.setBackground(Color.MAGENTA);
                                    state = -1;
                                    return true;
                                }
                            }
                            // Check vertical wins
                            for (int r = 0; r < 3; r++) {
                                if (boardState[0][0] == 0) {
                                    continue;
                                }
                                if (boardState[0][r] + boardState[1][r] +boardState[2][r] == 3) {
                                    winner = "Player 2";
                                    parent.setBackground(Color.PINK);
                                    state = 1;
                                    return true;
                                } else if (boardState[0][r] + boardState[1][r] +boardState[2][r] == -3) {
                                    winner = "Player 1";
                                    parent.setBackground(Color.MAGENTA);
                                    state = -1;
                                    return true;
                                }
                            }
                            // Check diagonals
                            if (boardState[0][2] + boardState[1][1] + boardState[2][0] == 3) {
                                winner = "Player 2";
                                parent.setBackground(Color.PINK);
                                state = 1;
                                return true;
                            } else if (boardState[0][2] + boardState[1][1] + boardState[2][0] == -3) {
                                winner = "Player 1";
                                parent.setBackground(Color.MAGENTA);
                                state = -1;
                                return true;
                            } else if (boardState[0][0] + boardState[1][1] + boardState[2][2] == 3) {
                                winner = "Player 2";
                                parent.setBackground(Color.PINK);
                                state = 1;
                                return true;
                            } else if (boardState[0][0] + boardState[1][1] + boardState[2][2] == -3) {
                                winner = "Player 1";
                                parent.setBackground(Color.MAGENTA);
                                state = -1;
                                return true;
                            }
                            return false;
                        }
                    });

                    parent.add(buttons[row*3+ir][col*3+ic], c);
                }
            }
        }

        // Methods
        void reset() {
            boardState = new int[3][3];
            state = 0;
            for (Component component : parent.getComponents()) {
                component.setEnabled(true);
                component.setBackground(Color.WHITE);
            }
        }
    }

    // Aux Methods
    void print1DArr(String name, int[] arr) {
        System.out.println(name);
        for (int obj : arr)
            System.out.print(obj + " ");
        System.out.print("\n\n");
    }
    void print2DArr(String name, int[][] arr) {
        System.out.println(name);
        for (int[] row : arr) {
            for (int obj : row)
                System.out.print(obj + " ");
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public static void main(String[] args) {
        new UltimateTicTacToe();
    }
}

// Left off:
/* Need to create where to go indicator (ie. when click which square to play on next)
 *  Make sure to not soft lock when already done.
 *
 *
 * */