package model;

import java.awt.Color;

public class SOSLine {
    public final int r1, c1, r2, c2;
    public final Color color;

    public SOSLine(int r1, int c1, int r2, int c2, model.sos_Model.Player player) {
        this.r1 = r1;
        this.c1 = c1;
        this.r2 = r2;
        this.c2 = c2;
        this.color = player == model.sos_Model.Player.Player1 ? Color.BLUE : Color.RED;
    }
}