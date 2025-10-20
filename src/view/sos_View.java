package view;

import javax.swing.*;
import java.awt.*;
import model.sos_Model;

public class sos_View extends JFrame {
   private JButton newGameButton;
   private JRadioButton rbSimple, rbGeneral;
   private ButtonGroup radioGroup;
   

   
   public sos_View() {
       setTitle("SOS Board Game");
       setSize(600, 400);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setLayout(new BorderLayout());
       // Panel with a line border
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
       radioGroup = new ButtonGroup();
       radioGroup.add(rbSimple);
       radioGroup.add(rbGeneral);
       mainPanel.add(rbSimple);
       mainPanel.add(rbGeneral);
       add(mainPanel, BorderLayout.CENTER);
   }
}