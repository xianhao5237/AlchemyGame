import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* Program Name: Alchemy Game
 *
 * Purpose: This program allows one user to play the game of Alchemy. Through a 
 * graphical user interface, the player is able to click on the board to place
 * runes which turn the tile beneath it to gold. The goal of the game is to 
 * turn the entire board gold by placing runes which are randomly chosen. Runes
 * can only be placed beside another rune of the same colour and/or shape. When 
 * a row or column is filled with runes then those runes are removed allowing 
 * more runes to be placed on those tiles. The player has to be strategic in 
 * where they place runes because not being able to place runes and discarding 
 * more than 3 times leads to a loss.
 *
 * Author: Xianhao Zhou
 *
 * Start Date: May 2, 2019  
 *
 * End Date: June 3, 2019
 */
//Note: If running on JCreator build file first before running.
public class AlchemyGame_XianhaoZhou implements ActionListener {

    boolean tracing = false;

    JFrame frame = new JFrame("Alchemy");

    JFrame titleFrame = new JFrame("Alchemy");

    JButton continueButton = new JButton("Continue");

    JButton instructionsButton = new JButton("Instructions");

    JButton resetButton = new JButton("Reset");

    JButton aboutButton = new JButton("About");

    JButton discardButton = new JButton("Discard Rune");

    Rune[][] boardButtonArray = new Rune[8][9];

    ImageIcon emptyTileIcon, emptyGoldIcon = null;

    JLabel emptyTileLabel, currentRuneLabel, discardHolderLabel1, discardHolderLabel2, discardHolderLabel3 = null;

    public static void main(String[] args) {

        new AlchemyGame_XianhaoZhou();

    }

    AlchemyGame_XianhaoZhou() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        titleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //main panel and 3 panels that will go on it
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout()); //south

        JPanel boardPanel = new JPanel(new GridLayout(8, 9));//center

        JPanel sidePanel = new JPanel(new GridLayout(3, 1));//west

        //south
        instructionsButton.addActionListener(this);
        resetButton.addActionListener(this);
        aboutButton.addActionListener(this);

        buttonPanel.add(instructionsButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(aboutButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        //center
        emptyTileIcon = new ImageIcon(this.getClass().getResource("Empty.png"));
        emptyGoldIcon = new ImageIcon(this.getClass().getResource("EmptyGold.png"));

        emptyTileLabel = new JLabel(emptyTileIcon);

        for (int row = 0; row < 8; row++) { //one rune is 90 x 90

            for (int col = 0; col < 9; col++) {

                boardButtonArray[row][col] = new Rune();
                boardButtonArray[row][col].setIcon(emptyTileIcon);
                boardButtonArray[row][col].type = "empty";
                boardButtonArray[row][col].colour = "none";
                boardButtonArray[row][col].addActionListener(this);
                boardPanel.add(boardButtonArray[row][col]);
            }
        }
        boardButtonArray[3][4].setIcon(new ImageIcon(this.getClass().getResource("Tiles/GoldBlock.png"))); //create starting block
        boardButtonArray[3][4].type = "Block";
        boardButtonArray[3][4].colour = "Gold";

        mainPanel.add(boardPanel, BorderLayout.CENTER);

        //west
        JPanel currentPanel = new JPanel(new BorderLayout());

        JTextField currentRuneField = new JTextField("         Current Rune", 14);
        currentRuneField.setFont(currentRuneField.getFont().deriveFont(Font.BOLD, 14f));
        currentRuneField.setEditable(false);
        currentPanel.add(currentRuneField, BorderLayout.NORTH);

        currentRuneLabel = new JLabel(new ImageIcon(this.getClass().getResource("Tiles/" + randomRune() + ".png")));
        currentPanel.add(currentRuneLabel, BorderLayout.CENTER);

        JPanel discardPanel = new JPanel(new BorderLayout());

        discardButton.addActionListener(this);
        discardPanel.add(discardButton, BorderLayout.NORTH);

        discardHolderLabel1 = new JLabel(emptyTileIcon);
        discardHolderLabel2 = new JLabel(emptyTileIcon);
        discardHolderLabel3 = new JLabel(emptyTileIcon);
        discardPanel.add(discardHolderLabel1, BorderLayout.WEST);
        discardPanel.add(discardHolderLabel2, BorderLayout.EAST);
        discardPanel.add(discardHolderLabel3, BorderLayout.SOUTH);

        sidePanel.add(currentPanel);
        sidePanel.add(discardPanel);
        mainPanel.add(sidePanel, BorderLayout.WEST);

        //set up main frame
        frame.setContentPane(mainPanel);

        frame.setSize(1000, 798);

        frame.setResizable(false);

        frame.setLocationRelativeTo(null);

        frame.setVisible(false);

        //set up title frame
        JPanel titlePanel = new JPanel(new BorderLayout());

        ImageIcon titleImage = new ImageIcon(this.getClass().getResource("title.png"));
        JLabel titleLabel = new JLabel(titleImage);

        titlePanel.add(titleLabel, BorderLayout.CENTER);

        continueButton.addActionListener(this);

        titlePanel.add(continueButton, BorderLayout.SOUTH);

        titleFrame.setContentPane(titlePanel);

        titleFrame.setSize(1000, 798);

        titleFrame.setResizable(false);

        titleFrame.setLocationRelativeTo(null);

        titleFrame.setVisible(true);
        if (tracing) { //for this to work, title frame set invisible and frame visible
            System.out.println(boardButtonArray[0][0].getSize().width + "  " + boardButtonArray[0][0].getSize().height);
        }
    }

    boolean placePiece = true;
    int runeCounter = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == continueButton) {
            titleFrame.setVisible(false);
            frame.setVisible(true);
        }
        if (e.getSource() == instructionsButton) {
            String instructions = "";
            try {
                BufferedReader infile = new BufferedReader(new InputStreamReader(AlchemyGame_XianhaoZhou.class.getResourceAsStream("instructions.txt")));

                for (int i = 0; i < 28; i++) {
                    instructions += infile.readLine();
                    instructions += "\n";
                }
                infile.close();
            } catch (IOException ex) {
                Logger.getLogger(AlchemyGame_XianhaoZhou.class.getName()).log(Level.SEVERE, null, ex);
                instructions = "Error. Can't find the file.";
            }

            JOptionPane.showMessageDialog(null, instructions, "Instructions", JOptionPane.INFORMATION_MESSAGE);
        }
        if (e.getSource() == aboutButton) {

            String aboutInfo = "";
            try {
                BufferedReader infile = new BufferedReader(new InputStreamReader(AlchemyGame_XianhaoZhou.class.getResourceAsStream("aboutInfo.txt")));

                for (int i = 0; i < 14; i++) {
                    aboutInfo += infile.readLine();
                    aboutInfo += "\n";
                }
                infile.close();
            } catch (IOException ex) {
                Logger.getLogger(AlchemyGame_XianhaoZhou.class.getName()).log(Level.SEVERE, null, ex);
                aboutInfo = "Error. Can't find the file.";
            }
            JTextArea aboutInfoArea = new JTextArea(aboutInfo);
            aboutInfoArea.setFont(aboutInfoArea.getFont().deriveFont(17f));
            aboutInfoArea.setEditable(false);

            JOptionPane.showMessageDialog(null, aboutInfoArea, "About", JOptionPane.INFORMATION_MESSAGE);
        }
        if (e.getSource() == resetButton) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 9; col++) {
                    boardButtonArray[row][col].setIcon(emptyTileIcon);
                    boardButtonArray[row][col].type = "empty";
                    boardButtonArray[row][col].colour = "none";
                }
            }
            discardHolderLabel1.setIcon(emptyTileIcon);
            discardHolderLabel2.setIcon(emptyTileIcon);
            discardHolderLabel3.setIcon(emptyTileIcon);

            currentRuneLabel.setIcon(new ImageIcon(this.getClass().getResource("Tiles/" + randomRune() + ".png")));

            boardButtonArray[3][4].setIcon(new ImageIcon(this.getClass().getResource("Tiles/GoldBlock.png")));
            boardButtonArray[3][4].type = "Block";
            boardButtonArray[3][4].colour = "Gold";

            placePiece = true;
            runeCounter = 0;
        }
        if (e.getSource() == discardButton) {
            if (placePiece) { //button doesn't do anything after game ends

                if (discardHolderLabel1.getIcon().equals(emptyTileLabel.getIcon())) {
                    discardHolderLabel1.setIcon(currentRuneLabel.getIcon());
                    currentRuneLabel.setIcon(new ImageIcon(this.getClass().getResource("Tiles/" + randomRune() + ".png")));

                } else if (discardHolderLabel2.getIcon().equals(emptyTileLabel.getIcon())) {
                    discardHolderLabel2.setIcon(currentRuneLabel.getIcon());
                    currentRuneLabel.setIcon(new ImageIcon(this.getClass().getResource("Tiles/" + randomRune() + ".png")));

                } else if (discardHolderLabel3.getIcon().equals(emptyTileLabel.getIcon())) {
                    discardHolderLabel3.setIcon(currentRuneLabel.getIcon());
                    currentRuneLabel.setIcon(new ImageIcon(this.getClass().getResource("Tiles/" + randomRune() + ".png")));

                } else {
                    JTextArea loseMessageArea = new JTextArea("\n              Game Over!\n  You have no more discards  \n\n         Reset to try again.  \n");
                    loseMessageArea.setFont(loseMessageArea.getFont().deriveFont(Font.BOLD, 25f));
                    loseMessageArea.setEditable(false);

                    JOptionPane.showMessageDialog(null, loseMessageArea, "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
                    placePiece = false;
                }
            }
            if (tracing) {
                System.out.println(discardHolderLabel1.getIcon().equals(emptyTileLabel.getIcon()));
                System.out.println("first: " + discardHolderLabel1.getIcon() + "\nsecond: " + discardHolderLabel2.getIcon() + "\nthird: " + discardHolderLabel3.getIcon());
            }
        }
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 9; col++) {

                if (e.getSource() == boardButtonArray[row][col]) {
                    if (placePiece) {
                        if (checkPlaceable(row, col)) {

                            boardButtonArray[row][col].setIcon(currentRuneLabel.getIcon());
                            boardButtonArray[row][col].colour = colour;
                            boardButtonArray[row][col].type = shape;
                            currentRuneLabel.setIcon(new ImageIcon(this.getClass().getResource("Tiles/" + randomRune() + ".png")));

                            runeCounter++;
                            
                            if (tracing) {
                                System.out.println("tile: " + boardButtonArray[row][col].getIcon());
                                System.out.println("colour: " + boardButtonArray[row][col].colour);
                                System.out.println("shape: " + boardButtonArray[row][col].type);
                                System.out.println("Rune count: " + runeCounter);
                            }

                            fullRowOrCol();
                            removeDiscard();

                            if (checkWinner(row, col)) {
                                JTextArea winnerMessageArea = new JTextArea("\n           You have won!\n\n          Runes Placed: " + runeCounter + "\n\n Please reset to play again.  \n");
                                winnerMessageArea.setFont(winnerMessageArea.getFont().deriveFont(Font.BOLD, 25f));
                                winnerMessageArea.setEditable(false);

                                JOptionPane.showMessageDialog(null, winnerMessageArea, "WINNER", JOptionPane.INFORMATION_MESSAGE);
                                placePiece = false;
                            } //check winner
                        } //check placeable
                    }
                }

            }
        } // for loop

    }

    private boolean checkWinner(int currentRow, int currentCol) {

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 9; col++) {
                if (boardButtonArray[row][col].getIcon().equals(emptyTileLabel.getIcon())) {
                    return false;
                }
            }
        }
        return true;
    }

    private void fullRowOrCol() {
        int counter;
        for (int row = 0; row < 8; row++) {
            counter = 0;
            for (int col = 0; col < 9; col++) {
                if (!boardButtonArray[row][col].type.equals("empty")) {
                    counter++;
                }
            }
            if (counter == 9) {
                for (int col = 0; col < 9; col++) {
                    boardButtonArray[row][col].setIcon(emptyGoldIcon);
                    boardButtonArray[row][col].type = "empty";
                    boardButtonArray[row][col].colour = "Gold";
                }
            }
        }

        for (int col = 0; col < 9; col++) {
            counter = 0;
            for (int row = 0; row < 8; row++) {
                if (!boardButtonArray[row][col].type.equals("empty")) {
                    counter++;
                }
            }
            if (counter == 8) {
                for (int row = 0; row < 8; row++) {
                    boardButtonArray[row][col].setIcon(emptyGoldIcon);
                    boardButtonArray[row][col].type = "empty";
                    boardButtonArray[row][col].colour = "Gold";
                }
            }
        }
    }

    private void removeDiscard() {

        if (!discardHolderLabel3.getIcon().equals(emptyTileLabel.getIcon())) {
            discardHolderLabel3.setIcon(emptyTileIcon);

        } else if (!discardHolderLabel2.getIcon().equals(emptyTileLabel.getIcon())) {
            discardHolderLabel2.setIcon(emptyTileIcon);

        } else if (!discardHolderLabel1.getIcon().equals(emptyTileLabel.getIcon())) {
            discardHolderLabel1.setIcon(emptyTileIcon);
        }
    }

    private boolean checkPlaceable(int currentRow, int currentCol) {
        boolean below = false;
        boolean above = false;
        boolean right = false;
        boolean left = false;

        if (boardButtonArray[currentRow][currentCol].type.equals("empty")) {
            if (shape.equals("Block")) {
                if (currentRow != 7) {
                    if (!boardButtonArray[currentRow + 1][currentCol].type.equals("empty")) {
                        below = true;
                    }
                }
                if (currentRow != 0) {
                    if (!boardButtonArray[currentRow - 1][currentCol].type.equals("empty")) {
                        above = true;
                    }
                }
                if (currentCol != 8) {
                    if (!boardButtonArray[currentRow][currentCol + 1].type.equals("empty")) {
                        right = true;
                    }
                }
                if (currentCol != 0) {
                    if (!boardButtonArray[currentRow][currentCol - 1].type.equals("empty")) {
                        left = true;
                    }
                }
                if (tracing) {
                    System.out.println("below: " + below + " above: " + above + " right: " + right + " left: " + left);
                }
                return below || above || right || left; // if at least 1 rune or block beside then can place
            } else { //if not a block

                if (currentRow != 7) {
                    if (boardButtonArray[currentRow + 1][currentCol].type.equals(shape) || boardButtonArray[currentRow + 1][currentCol].colour.equals(colour) || boardButtonArray[currentRow + 1][currentCol].type.equals("Block")) {
                        below = true;
                    }
                }
                if (currentRow != 0) {
                    if (boardButtonArray[currentRow - 1][currentCol].type.equals(shape) || boardButtonArray[currentRow - 1][currentCol].colour.equals(colour) || boardButtonArray[currentRow - 1][currentCol].type.equals("Block")) {
                        above = true;
                    }
                }
                if (currentCol != 8) {
                    if (boardButtonArray[currentRow][currentCol + 1].type.equals(shape) || boardButtonArray[currentRow][currentCol + 1].colour.equals(colour) || boardButtonArray[currentRow][currentCol + 1].type.equals("Block")) {
                        right = true;
                    }
                }
                if (currentCol != 0) {
                    if (boardButtonArray[currentRow][currentCol - 1].type.equals(shape) || boardButtonArray[currentRow][currentCol - 1].colour.equals(colour) || boardButtonArray[currentRow][currentCol - 1].type.equals("Block")) {
                        left = true;
                    }
                }
                
                if (tracing) {
                    System.out.println("below: " + below + " above: " + above + " right: " + right + " left: " + left);
                }

                if (below || above || right || left) { // at least one can't be empty, sides that don't fit criteria checked above have to be empty
                    if (!below) {
                        if (currentRow != 7) {
                            if (!boardButtonArray[currentRow + 1][currentCol].type.equals("empty")) {
                                return false;
                            }
                        }
                    }
                    if (!above) {
                        if (currentRow != 0) {
                            if (!boardButtonArray[currentRow - 1][currentCol].type.equals("empty")) {
                                return false;
                            }
                        }
                    }
                    if (!right) {
                        if (currentCol != 8) {
                            if (!boardButtonArray[currentRow][currentCol + 1].type.equals("empty")) {
                                return false;
                            }
                        }
                    }
                    if (!left) {
                        if (currentCol != 0) {
                            if (!boardButtonArray[currentRow][currentCol - 1].type.equals("empty")) {
                                return false;
                            }
                        }
                    }
                } else { // if all above, below, left, right are false then can't place
                    return false;
                }
                return true;
            }
        } else { // if clicked button has a rune on it
            return false;
        }
    }

    public String[] colours = {"Blue", "Green", "Purple", "Red", "Yellow"};
    public String[] shapes = {"Circle", "Lightning", "Square", "Triangle", "Star"};

    String colour;
    String shape;

    private String randomRune() {
        String randomTile = "";
        int randomNum = (int) (Math.random() * 26);

        if(tracing) {
            System.out.println("Block?" + randomNum);
        }
        
        if (randomNum >= 0 && randomNum <= 25) {
            if (randomNum == 5) {
                randomTile = "GoldBlock";
                colour = "Gold";
                shape = "Block";

            } else {
                randomNum = (int) (Math.random() * 5);
                colour = colours[randomNum];

                if (tracing) {
                    System.out.print("random num1: " + randomNum);
                }
                randomNum = (int) (Math.random() * 5);
                shape = shapes[randomNum];
                randomTile = colour + shape;
            }
        }
        if (tracing) {
            System.out.println(" random num2: " + randomNum + " randomTile: " + randomTile);
        }
        return randomTile;
    }

}