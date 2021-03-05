package filezelda.components;

import com.almasb.fxgl.entity.component.Component;
import javafx.scene.shape.Rectangle;

public class DirectoryComponent extends Component {
    private String dirPath = "";
    private Rectangle textRect = new Rectangle( 100, 30);

    public DirectoryComponent(String initPath){
        dirPath = initPath;
        textRect.setStrokeWidth(2.5);
        textRect.getStyleClass().add("current-directory-contents");
    }

    public void textRectBorderActive() {
        textRect.getStyleClass().clear();
        textRect.getStyleClass().add("current-directory-contents-active");
    }

    public void textRectBorderInactive() {
        textRect.getStyleClass().clear();
        textRect.getStyleClass().add("current-directory-contents");
    }

    public Rectangle getTextRect() {
        return textRect;
    }

    public void setDirPath(String p) {
        dirPath = p;
    }

    public String getDirPath() {
        return dirPath;
    }
}
