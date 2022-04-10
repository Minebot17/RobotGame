package robotgame.view;

import robotgame.model.cell.Cell;
import robotgame.model.cellobject.CellObject;
import robotgame.model.cellobject.Key;
import robotgame.model.cellobject.Robot;

import java.awt.*;

public class CellObjectView {

    private static final String ROBOT_LETTER = "R";
    private static final String KEY_LETTER = "K";

    public Cell cell;

    public CellObjectView(Cell cell) {
        this.cell = cell;
    }

    public boolean paint(Graphics2D gr2d){
        CellObject cellObject = cell.getContainedObject();
        if (cellObject == null){
            return false;
        }

        gr2d.setColor(Color.black);
        gr2d.setFont(new Font("Microsoft JhengHei Light", Font.BOLD, 20));

        String letter = cellObject instanceof Robot ? ROBOT_LETTER : cellObject instanceof Key ? KEY_LETTER : "";
        FontMetrics fm = gr2d.getFontMetrics();
        int msgWidth = fm.stringWidth(letter);
        int msgHeight = fm.getHeight();

        gr2d.drawString(letter, (CellView.CELL_SIZE - msgWidth)/2, CellView.CELL_SIZE / 2 + msgHeight/4);
        return true;
    }
}
