package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import model.SOSLine;
import model.sos_Model;

public class sos_View extends JFrame {
   private JButton newGameButton;
   private JRadioButton rbSimple, rbGeneral;
   private ButtonGroup modeSelection;
   private JTextField txtBoardsize;

   private JRadioButton rbPlayer1S, rbPlayer1O;
   private JRadioButton rbPlayer2S, rbPlayer2O;
   private JCheckBox cbPlayer1Computer, cbPlayer2Computer;

   private JButton[][] boardButton;
   private JPanel boardPanel;
   private JLayeredPane gameBoardPane;
   private JPanel buttonPanel;
   private LinePanel linePanel;
   private List<SOSLine> sosLines = new ArrayList<>();
   private int windowSize = 500; // Default window size for the game board

   private JPanel topPanel, player1Panel, player2Panel;
   private ButtonGroup player1Group, player2Group;


   
   public sos_View() {
       setTitle("SOS Board Game");
       setSize(800, 700);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setLayout(new BorderLayout(10,10));

       buildTopPanel();
        buildCenterPanel();
       //buildBottomPanel();

       add(topPanel, BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
      // add(bottomPanel, BorderLayout.SOUTH);

   }

   private void buildTopPanel(){
      topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15,10));
       topPanel.setBorder(BorderFactory.createTitledBorder("SOS Game"));
        newGameButton = new JButton("New Game");
        topPanel.add(newGameButton);
       rbSimple = new JRadioButton("Simple", true);
       rbGeneral = new JRadioButton("General");
       modeSelection = new ButtonGroup();
       modeSelection.add(rbSimple);
       modeSelection.add(rbGeneral);
       topPanel.add(rbSimple);
       topPanel.add(rbGeneral);
       add(topPanel, BorderLayout.CENTER);


       topPanel.add(new JLabel("Board Size: "));
       txtBoardsize = new JTextField("3", 3);
       txtBoardsize.setToolTipText("Enter a number 3 or greater");
       topPanel.add(txtBoardsize);
       // Add a spacer
       topPanel.add(Box.createHorizontalStrut(10));


   }
   private JPanel buildCenterPanel(){
    JPanel centerPanel = new JPanel(new BorderLayout(10,10));

    buildPlayer1Panel();
    buildPlayer2Panel();
    buildBoardPanel();

    centerPanel.add(player1Panel, BorderLayout.WEST);
    centerPanel.add(player2Panel, BorderLayout.EAST);
    centerPanel.add(boardPanel, BorderLayout.CENTER);

    return centerPanel;
   }

   private JLabel scoreboardP1;
   private JLabel scoreboardP2;

   private void buildPlayer1Panel(){
    player1Panel = new JPanel();
    player1Panel.setLayout(new BoxLayout(player1Panel, BoxLayout.Y_AXIS));
    player1Panel.setBorder(BorderFactory.createTitledBorder("Blue player"));
    player1Panel.setPreferredSize(new Dimension(120,0));
    player1Panel.setForeground(Color.BLUE);

    scoreboardP1 = new JLabel("Score Blue: 0");
    scoreboardP1.setAlignmentX(Component.CENTER_ALIGNMENT);
    scoreboardP1.setForeground(Color.BLUE);
    player1Panel.add(scoreboardP1);

    rbPlayer1S = new JRadioButton("S", true);
    rbPlayer1O = new JRadioButton("O");
    rbPlayer1S.setAlignmentX(Component.CENTER_ALIGNMENT);
    rbPlayer1O.setAlignmentX(Component.CENTER_ALIGNMENT);
    rbPlayer1S.setForeground(Color.BLUE);
    rbPlayer1O.setForeground(Color.BLUE);
    
    player1Group = new ButtonGroup();
    player1Group.add(rbPlayer1S);
    player1Group.add(rbPlayer1O);

    player1Panel.add(Box.createVerticalGlue());
    player1Panel.add(rbPlayer1S);
    player1Panel.add(Box.createVerticalGlue());
    player1Panel.add(rbPlayer1O);
    player1Panel.add(Box.createRigidArea(new Dimension(0, 10)));
    
    cbPlayer1Computer = new JCheckBox("Computer");
    cbPlayer1Computer.setAlignmentX(Component.CENTER_ALIGNMENT);
    cbPlayer1Computer.setForeground(Color.BLUE);
    player1Panel.add(cbPlayer1Computer);
   }

   private void buildPlayer2Panel(){
    player2Panel = new JPanel();
    player2Panel.setLayout(new BoxLayout(player2Panel, BoxLayout.Y_AXIS));
    player2Panel.setBorder(BorderFactory.createTitledBorder("Red player"));
    player2Panel.setPreferredSize(new Dimension(120,0));
    player2Panel.setForeground(Color.RED);
    
    scoreboardP2 = new JLabel("Score Red: 0");
    scoreboardP2.setAlignmentX(Component.CENTER_ALIGNMENT);
    scoreboardP2.setForeground(Color.RED);
    player2Panel.add(scoreboardP2);
    
    rbPlayer2S = new JRadioButton("S", true);
    rbPlayer2O = new JRadioButton("O");
    rbPlayer2S.setAlignmentX(Component.CENTER_ALIGNMENT);
    rbPlayer2O.setAlignmentX(Component.CENTER_ALIGNMENT);
    rbPlayer2S.setForeground(Color.RED);
    rbPlayer2O.setForeground(Color.RED);
    
    player2Group = new ButtonGroup();
    player2Group.add(rbPlayer2S);
    player2Group.add(rbPlayer2O);


    player2Panel.add(Box.createVerticalGlue());
    player2Panel.add(rbPlayer2S);
    player2Panel.add(Box.createVerticalGlue());
    player2Panel.add(rbPlayer2O);
    player2Panel.add(Box.createRigidArea(new Dimension(0, 10)));
    
    cbPlayer2Computer = new JCheckBox("Computer");
    cbPlayer2Computer.setAlignmentX(Component.CENTER_ALIGNMENT);
    cbPlayer2Computer.setForeground(Color.RED);
    player2Panel.add(cbPlayer2Computer);

   }

   private class LinePanel extends JPanel {
       @Override
       protected void paintComponent(Graphics g) {
           super.paintComponent(g);
           
           if (!sosLines.isEmpty()) {
               int boardSize = boardButton.length;
               int cellW = getWidth() / boardSize;
               int cellH = getHeight() / boardSize;

               Graphics2D g2 = (Graphics2D) g;
               // Enable antialiasing for smoother lines
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               // Set composite mode for semi-transparent lines
               g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
               // Make lines thicker based on board size
               float strokeWidth = Math.max(4, Math.min(8, 16f / boardSize));
               g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

               // Draw each SOS line
               for (SOSLine line : sosLines) {
                   g2.setColor(line.color);
                   // Calculate center points of cells
                   int x1 = line.c1 * cellW + cellW / 2;
                   int y1 = line.r1 * cellH + cellH / 2;
                   int x2 = line.c2 * cellW + cellW / 2;
                   int y2 = line.r2 * cellH + cellH / 2;
                   g2.drawLine(x1, y1, x2, y2);
               }

               // Reset composite to fully opaque for any subsequent drawing
               g2.setComposite(AlphaComposite.SrcOver);
           }
       }

       @Override
       public boolean isOpaque() {
           return false;
       }

       @Override
       protected void addImpl(Component comp, Object constraints, int index) {
           // Prevent adding any child components to this panel
           return;
       }

       @Override
       public void repaint() {
           super.repaint();
           if (getParent() != null) {
               // Force immediate repaint
               getParent().repaint();
           }
       }
   }

   private void buildBoardPanel() {
       boardPanel = new JPanel(new BorderLayout());
       boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
       
       // Create layered pane
       gameBoardPane = new JLayeredPane();
       gameBoardPane.setPreferredSize(new Dimension(windowSize, windowSize));
       
       // Create button panel
       buttonPanel = new JPanel(new GridLayout(3, 3)); // Default 3x3 grid
       buttonPanel.setBounds(0, 0, windowSize, windowSize);
       buttonPanel.setOpaque(false);
       
       // Create line panel
       linePanel = new LinePanel();
       linePanel.setBounds(0, 0, windowSize, windowSize);
       linePanel.setOpaque(false);
       
       // Add panels to layered pane
       gameBoardPane.add(buttonPanel, JLayeredPane.DEFAULT_LAYER);
       gameBoardPane.add(linePanel, JLayeredPane.PALETTE_LAYER);
       
       // Add layered pane to board panel
       boardPanel.add(gameBoardPane, BorderLayout.CENTER);
       
       createBoard(3); // Initialize with default 3x3 board
   }

   public void createBoard(int size) {
       buttonPanel.removeAll();
       buttonPanel.setLayout(new GridLayout(size, size));
       boardButton = new JButton[size][size];

       // Recalculate cell size
       int cellSize = Math.min(windowSize / size, windowSize / size);

       for (int i = 0; i < size; i++) {
           for (int j = 0; j < size; j++) {
               JButton button = new JButton("");
               button.setPreferredSize(new Dimension(cellSize, cellSize));
               button.setFont(new Font("Arial", Font.BOLD, cellSize / 2)); // Make text size proportional to cell
               button.setMargin(new Insets(0, 0, 0, 0)); // Remove internal button margins
               boardButton[i][j] = button;
               buttonPanel.add(button);
           }
       }

       // Ensure proper size and layout
       buttonPanel.setPreferredSize(new Dimension(windowSize, windowSize));
       linePanel.setPreferredSize(new Dimension(windowSize, windowSize));

       // Update the layouts
       buttonPanel.revalidate();
       buttonPanel.repaint();
       linePanel.revalidate();
       linePanel.repaint();
       
       // Make sure the gameBoardPane size is updated
       gameBoardPane.revalidate();
       gameBoardPane.repaint();
   }

   public void updateLines(List<SOSLine> lines) {
       this.sosLines = new ArrayList<>(lines);
       if (linePanel != null) {
           linePanel.repaint();
       }
   }

   public int getBoardsize() throws NumberFormatException {
        String text = txtBoardsize.getText().trim();
        if (text.isEmpty()) {
            throw new NumberFormatException("Board size cannot be empty");
        }
        return Integer.parseInt(text);
   }

   public void setBoardSize(int boardSize){
    txtBoardsize.setText(String.valueOf(boardSize));
   }


   public JButton getNewGameButton() {return newGameButton;}
   public void setNewGameButton(JButton newGameButton) {this.newGameButton = newGameButton;}
   public JRadioButton getRbSimple() {return rbSimple;}
   public void setRbSimple(JRadioButton rbSimple) {this.rbSimple = rbSimple;}
   public JRadioButton getRbGeneral() {return rbGeneral;}
   public void setRbGeneral(JRadioButton rbGeneral) {this.rbGeneral = rbGeneral;}
   public ButtonGroup getModeSelection() { return modeSelection; }
   public void setModeSelection(ButtonGroup modeSelection) { this.modeSelection = modeSelection; }
   public JRadioButton getRbPlayer1S() {return rbPlayer1S;}
   public void setRbPlayer1S(JRadioButton rbPlayer1S) {this.rbPlayer1S = rbPlayer1S;}
   public JRadioButton getRbPlayer1O() { return rbPlayer1O; }
   public void setRbPlayer1O(JRadioButton rbPlayer1O) { this.rbPlayer1O = rbPlayer1O; }
   public JRadioButton getRbPlayer2S() {return rbPlayer2S; }
   public void setRbPlayer2S(JRadioButton rbPlayer2S) { this.rbPlayer2S = rbPlayer2S; }
   public JRadioButton getRbPlayer2O() { return rbPlayer2O;}
   public void setRbPlayer2O(JRadioButton rbPlayer2O) { this.rbPlayer2O = rbPlayer2O; }
   public JButton[][] getBoardButton() { return boardButton;}
   public void setBoardButton(JButton[][] boardButton) { this.boardButton = boardButton; }
   public JPanel getBoardPanel() { return boardPanel;}
   public void setBoardPanel(JPanel boardPanel) { this.boardPanel = boardPanel; }
   public JPanel getTopPanel() {  return topPanel;  }
   public void setTopPanel(JPanel topPanel) {  this.topPanel = topPanel; }
   public JPanel getPlayer1Panel() { return player1Panel;  }
   public void setPlayer1Panel(JPanel player1Panel) { this.player1Panel = player1Panel;  }
   public JPanel getPlayer2Panel() {return player2Panel;}
   public void setPlayer2Panel(JPanel player2Panel) {this.player2Panel = player2Panel;}
   public ButtonGroup getPlayer1Group() {return player1Group;}
   public void setPlayer1Group(ButtonGroup player1Group) { this.player1Group = player1Group;}
   public ButtonGroup getPlayer2Group() {return player2Group; }
   public void setPlayer2Group(ButtonGroup player2Group) { this.player2Group = player2Group;}
   public JCheckBox getCbPlayer1Computer() { return cbPlayer1Computer; }
   public void setCbPlayer1Computer(JCheckBox cbPlayer1Computer) { this.cbPlayer1Computer = cbPlayer1Computer; }
   public JCheckBox getCbPlayer2Computer() { return cbPlayer2Computer; }
   public void setCbPlayer2Computer(JCheckBox cbPlayer2Computer) { this.cbPlayer2Computer = cbPlayer2Computer; }
   public void updateScore(int player, int score) {
       SwingUtilities.invokeLater(() -> {
           if (player == 1 && scoreboardP1 != null) {
               scoreboardP1.setText("Score Blue: " + score);
           } else if (player == 2 && scoreboardP2 != null) {
               scoreboardP2.setText("Score Red: " + score);
           }
       });
   }

}