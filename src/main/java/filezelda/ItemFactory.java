package filezelda;

import filezelda.components.AnimationComponent;
import filezelda.components.DirectoryComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static com.almasb.fxgl.dsl.FXGL.*;

import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;
import static filezelda.Main.EntityType.*;

public class ItemFactory implements EntityFactory {

    private static final int SIZE = 65;
    private static final int IMGPOS = 0;
    private static final int TEXTPOS = 50;


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        return entityBuilder(data)
                .type(PLAYER)
                .viewWithBBox(new Circle(24, 24, 20, Color.TRANSPARENT))
                .with(new AnimationComponent())
                .collidable()
                .build();
    }

    @Spawns("back")
    public Entity newBack(SpawnData data) {
        DirectoryComponent dc = new DirectoryComponent("BACK_BUTTON_ENTITY");

        StackPane sp = new StackPane();

        Rectangle r = dc.getTextRect();

        Text newD = new Text("BACK");
        newD.getStyleClass().add("current-directory-content-path");

        sp.getChildren().addAll(r, newD);


        return entityBuilder(data)
                .type(BACK)
                .viewWithBBox(new Rectangle(110, 40, Color.TRANSPARENT))
                .view(sp)
                .with(dc)
                .collidable()
                .zIndex(-1)
                .build();
    }

    @Spawns("currentDirectory")
    public Entity newCurrentDirectory(SpawnData data) {
        Rectangle r = new Rectangle(20, 20, 240, 65);
        r.setStrokeWidth(2.5);
        r.getStyleClass().add("current-directory-wrapper");
        Text d = data.get("name");
        d.setWrappingWidth(220);
        d.setTextAlignment(TextAlignment.CENTER);

        StackPane spn = new StackPane();
        spn.getChildren().addAll(r,d);

        return entityBuilder(data)
                .type(CURRENTDIRECTORY)
                .viewWithBBox(spn)
                .collidable()
                .zIndex(-1)
                .build();
    }

    @Spawns("directory")
    public Entity newDirectory(SpawnData data) {

        Text nm = data.get("path");

        DirectoryComponent dc = new DirectoryComponent(nm.getText());

        var view = texture("stairs.jpg");
        view.setFitWidth(SIZE);
        view.setFitHeight(SIZE);
        view.getStyleClass().add("content-stairs");
        view.setTranslateY(IMGPOS);
        Text d = data.get("name");
        d.setWrappingWidth(95);
        d.setTextAlignment(TextAlignment.CENTER);
        d.setTranslateY(TEXTPOS);

        Rectangle r = dc.getTextRect();
        r.setTranslateY(TEXTPOS);

        StackPane sp = new StackPane();
        sp.getChildren().addAll(r, d, view);

        return entityBuilder(data)
                .type(DIRECTORY)
                .viewWithBBox(new Circle( 50, 0, 50, Color.TRANSPARENT))
                .view(sp)
                .with(dc)
                .collidable()
                .zIndex(-1)
                .build();
    }

    @Spawns("archive")
    public Entity newArchive(SpawnData data) {
        Text nm = data.get("path");

        DirectoryComponent dc = new DirectoryComponent(nm.getText());

        var view = texture("treasure_chest.jpg");
        view.setFitWidth(SIZE);
        view.setFitHeight(SIZE);
        view.getStyleClass().add("content-tchest");
        view.setTranslateY(IMGPOS);
        Text d = data.get("name");
        d.setWrappingWidth(95);
        d.setTextAlignment(TextAlignment.CENTER);
        d.setTranslateY(TEXTPOS);

        Rectangle r = dc.getTextRect();
        r.setTranslateY(TEXTPOS);

        StackPane sp = new StackPane();
        sp.getChildren().addAll(r, d, view);


        return entityBuilder(data)
                .type(ARCHIVE)
                .viewWithBBox(sp)
                .with(dc)
                .collidable()
                .zIndex(-1)
                .build();
    }

    @Spawns("file")
    public Entity newFile(SpawnData data) {
        Text nm = data.get("path");

        DirectoryComponent dc = new DirectoryComponent(nm.getText());

        int n = getRandomNumber(1,6);
        var view = texture("book" + n + ".jpg");
        view.setFitWidth(SIZE);
        view.setFitHeight(SIZE);
        view.getStyleClass().add("content-book");
        view.setTranslateY(IMGPOS);
        Text d = data.get("name");
        d.setWrappingWidth(95);
        d.setTextAlignment(TextAlignment.CENTER);
        d.setTranslateY(TEXTPOS);

        Rectangle r = dc.getTextRect();
        r.setTranslateY(TEXTPOS);

        StackPane sp = new StackPane();
        sp.getChildren().addAll(r, d, view);

        return entityBuilder(data)
                .type(FILE)
                .viewWithBBox(sp)
                .with(dc)
                .collidable()
                .zIndex(-1)
                .build();
    }

    @Spawns("code")
    public Entity newCode(SpawnData data) {
        Text nm = data.get("path");

        DirectoryComponent dc = new DirectoryComponent(nm.getText());

        var view = texture("scroll.jpg");
        view.setFitWidth(SIZE);
        view.setFitHeight(SIZE);
        view.getStyleClass().add("content-scroll");
        view.setTranslateY(IMGPOS);
        Text d = data.get("name");
        d.setWrappingWidth(95);
        d.setTextAlignment(TextAlignment.CENTER);
        d.setTranslateY(TEXTPOS);

        Rectangle r = dc.getTextRect();
        r.setTranslateY(TEXTPOS);

        StackPane sp = new StackPane();
        sp.getChildren().addAll(r, d, view);

        return entityBuilder(data)
                .type(CODE)
                .viewWithBBox(sp)
                .with(dc)
                .collidable()
                .zIndex(-1)
                .build();
    }

    @Spawns("etc")
    public Entity newETC(SpawnData data) {
        Text nm = data.get("path");

        DirectoryComponent dc = new DirectoryComponent(nm.getText());

        int n = getRandomNumber(1,5);
        var view = texture("food" + n + ".jpg");
        view.setFitWidth(SIZE);
        view.setFitHeight(SIZE);
        view.getStyleClass().add("content-food");
        view.setTranslateY(IMGPOS);
        Text d = data.get("name");
        d.setWrappingWidth(95);
        d.setTextAlignment(TextAlignment.CENTER);
        d.setTranslateY(TEXTPOS);

        Rectangle r = dc.getTextRect();
        r.setTranslateY(TEXTPOS);

        StackPane sp = new StackPane();
        sp.getChildren().addAll(r, d, view);

        return entityBuilder(data)
                .type(ETC)
                .viewWithBBox(sp)
                .with(dc)
                .collidable()
                .zIndex(-1)
                .build();
    }
}
