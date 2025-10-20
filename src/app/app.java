package app;

import model.sos_Model;
import view.sos_View;
import controller.sos_Controller;

public class app {
    public app(){

    }

    public static void main(String[] args) {
        sos_Model game = new sos_Model(3, null);
        sos_View view = new sos_View();
       new sos_Controller(game, view);
       view.setVisible(true);
    
    }
}
