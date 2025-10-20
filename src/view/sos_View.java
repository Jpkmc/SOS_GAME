package view;

import javax.swing.*;
import java.awt.*;
//import model.sos_Model;


public class sos_View extends JFrame {
   private JButton newGameButton;
   private JRadioButton rbSimple, rbGeneral;
   private ButtonGroup modeSelection;
   private JTextField txtBoardsize;

   private JRadioButton rbPlayer1S, rbPlayer1O;
   private JRadioButton rbPlayer2S, rbPlayer2O;

   private JButton[][] boardButton;
   private JPanel boardPanel;

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


       topPanel.add(new JLabel("Borad Size: "));
       txtBoardsize = new JTextField("Enter an Board Size: ");
       topPanel.add(txtBoardsize);


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

   private void buildPlayer1Panel(){
    player1Panel = new JPanel();
    player1Panel.setLayout((new BoxLayout(player1Panel, BoxLayout.Y_AXIS)));
    player1Panel.setBorder(BorderFactory.createTitledBorder("Player 1"));
    player1Panel.setPreferredSize(new Dimension(120,0));

    rbPlayer1S = new JRadioButton("S", true);
    rbPlayer1O = new JRadioButton("0");
    player1Group = new ButtonGroup();
    player1Group.add(rbPlayer1S);
    player1Group.add(rbPlayer1O);

    player1Panel.add(Box.createVerticalGlue());
    player1Panel.add(rbPlayer1S);
    player1Panel.add(Box.createVerticalGlue());
    player1Panel.add(rbPlayer1O);
    player1Panel.add(Box.createRigidArea(new Dimension(0, 10)));
    


   }

   private void buildPlayer2Panel(){
    player2Panel = new JPanel();
    player2Panel.setLayout((new BoxLayout(player2Panel, BoxLayout.Y_AXIS)));
    player2Panel.setBorder(BorderFactory.createTitledBorder("Player 2"));
    player2Panel.setPreferredSize(new Dimension(120,0));

    rbPlayer2S = new JRadioButton("S", true);
    rbPlayer2O = new JRadioButton("0");
    player2Group = new ButtonGroup();
    player2Group.add(rbPlayer2S);
    player2Group.add(rbPlayer2O);


    player2Panel.add(Box.createVerticalGlue());
    player2Panel.add(rbPlayer2S);
    player2Panel.add(Box.createVerticalGlue());
    player2Panel.add(rbPlayer2O);
    player2Panel.add(Box.createRigidArea(new Dimension(0, 10)));

   }

   private void buildBoardPanel(){
    boardPanel = new JPanel();
   }

   private void createBoard(int size){
    boardPanel.removeAll();
    boardPanel.setLayout(new GridLayout(size,size));
    boardButton = new JButton[size][size];

    for (int i = 0; i < size; i++){
        for (int j = 0; j < size; j++){
            JButton button = new JButton("");
            boardButton[i][j] = button;
            boardPanel.add(button);
        }
    }
   }

   public int getBoardsize(){
    try{
        String text = txtBoardsize.getText().trim();
        return Integer.parseInt(text);
    }catch(NumberFormatException e){
        return 3;
    }
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
}