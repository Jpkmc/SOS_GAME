import javax.swing.*;
import java.awt.*;

public class sos_GUI extends JFrame {
   private JCheckBox checkBox;
   private JRadioButton rbOption1, rbOption2;
   private ButtonGroup radioGroup;
   public sos_GUI() {
       setTitle("SOS Board Game");
       setSize(1000, 1000);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setLayout(new BorderLayout());
       // Panel with a line border
       JPanel mainPanel = new JPanel(new GridLayout(4, 1));
       mainPanel.setBorder(BorderFactory.createTitledBorder("SOS Game"));
       // Text (label)
       mainPanel.add(new JLabel("Choose your options:"));
       // Checkbox
       checkBox = new JCheckBox("Enable feature");
       mainPanel.add(checkBox);
       // Radio buttons
       rbOption1 = new JRadioButton("Option 1");
       rbOption2 = new JRadioButton("Option 2");
       radioGroup = new ButtonGroup();
       radioGroup.add(rbOption1);
       radioGroup.add(rbOption2);
       mainPanel.add(rbOption1);
       mainPanel.add(rbOption2);
       add(mainPanel, BorderLayout.CENTER);
   }
   public static void main(String[] args) {
       SwingUtilities.invokeLater(() -> {
       	sos_GUI gui = new sos_GUI();
           gui.setVisible(true);
       });
   }
}


