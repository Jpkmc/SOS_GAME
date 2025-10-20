package controller;

import model.sos_Model;
import view.sos_View;


public class sos_Controller {
    private sos_Model model;
    private sos_View view;

    public sos_Controller(sos_Model model, sos_View view){
        this.view = view;
        this.model = model;
        
    }
    
}
