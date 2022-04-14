package robotgame.view;

import robotgame.model.cellobject.CellObject;
import robotgame.model.cellobject.Robot;

public class RobotView extends CellObjectView {

    public RobotView(CellObject cellObject) {
        super(cellObject);
    }

    @Override
    protected String getLetter() {
        return "R";
    }
}
