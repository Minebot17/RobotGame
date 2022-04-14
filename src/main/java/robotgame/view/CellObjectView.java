package robotgame.view;

import robotgame.model.cellobject.CellObject;

import java.awt.*;

public abstract class CellObjectView {

    public CellObject cellObject;

    public CellObjectView(CellObject cellObject) {
        this.cellObject = cellObject;
    }

    public boolean paint(Graphics2D gr2d){
        if (cellObject == null){
            return false;
        }

        gr2d.setColor(Color.black);
        gr2d.setFont(new Font("Microsoft JhengHei Light", Font.BOLD, 20));

        String letter = getLetter();
        FontMetrics fm = gr2d.getFontMetrics();
        int msgWidth = fm.stringWidth(letter);
        int msgHeight = fm.getHeight();

        gr2d.drawString(letter, (CellView.CELL_SIZE - msgWidth)/2, CellView.CELL_SIZE / 2 + msgHeight/4);
        return true;
    }

    protected abstract String getLetter();
}
