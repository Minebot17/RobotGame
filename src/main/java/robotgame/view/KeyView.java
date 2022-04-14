package robotgame.view;

import robotgame.model.cellobject.CellObject;
import robotgame.model.cellobject.Key;

public class KeyView extends CellObjectView {

    public KeyView(CellObject cellObject) {
        super(cellObject);
    }

    @Override
    protected String getLetter() {
        return "K";
    }
}
