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

   private JPanel topPanel, player1Panel, player2Panel, bottomPanel;


   
   public sos_View() {
       setTitle("SOS Board Game");
       setSize(600, 400);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setLayout(new BorderLayout(10,10));

       buildTopPanel();
       buildCenterPanel();
       buildBottomPanel();

       add(topPanel, BorderLayout.NORTH)
       add(buildCenterPanel(), BorderLayout.CENTER);
       add(bottomPanel, BorderLayout.SOUTH);

   }

   private void buildTopPanel(){
     JPanel mainPanel = new JPanel(new GridLayout(4, 1));
       mainPanel.setBorder(BorderFactory.createTitledBorder("SOS Game"));
       // Text (label)
       mainPanel.add(new JLabel("Choose your board size: "));

        newGameButton = new JButton("New Game");
        //newGameButton.addActionListener(e -> startANewGame());
        mainPanel.add(newGameButton);

       // Radio buttons
       rbSimple = new JRadioButton("Simple", true);
       rbGeneral = new JRadioButton("General");
       modeSelection = new ButtonGroup();
       modeSelection.add(rbSimple);
       modeSelection.add(rbGeneral);
       mainPanel.add(rbSimple);
       mainPanel.add(rbGeneral);
       add(mainPanel, BorderLayout.CENTER);
   }
}