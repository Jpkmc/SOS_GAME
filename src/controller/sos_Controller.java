package controller;

import model.sos_Model;
import view.sos_View;
import javax.swing.*;


public class sos_Controller {
    private sos_Model model;
    private sos_View view;

    public sos_Controller(sos_Model model, sos_View view){
        this.view = view;
        this.model = model;

        initialzeGame();

        
    }

    private void initialzeGame(){
        // the default of the baord to 3
        //model.setSize(3);

        
    }

    
}
