package view;

import javax.swing.*;
import java.awt.*;
import model.sos_Model;


public class sos_View extends JFrame {
   private JButton newGameButton;
   private JRadioButton rbSimple, rbGeneral;
   private ButtonGroup modeSelection;

   private JRadioButton rbPlayer1S, rbPlayer1O;
   private JRadioButton rbPlayer2S, rbPlayer2O;
   private JRadioButton rbPlayer1, rbPlayer2;

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

       // Radio buttons
       rbSimple = new JRadioButton("Simple", true);
       rbGeneral = new JRadioButton("General");
       modeSelection = new ButtonGroup();
       modeSelection.add(rbSimple);
       modeSelection.add(rbGeneral);
       topPanel.add(rbSimple);
       topPanel.add(rbGeneral);
       add(topPanel, BorderLayout.CENTER);
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
 

}