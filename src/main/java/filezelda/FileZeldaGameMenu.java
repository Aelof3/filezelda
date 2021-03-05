package filezelda;


import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.util.EmptyRunnable;
import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class FileZeldaGameMenu extends FXGLMenu {

    private static final int SIZE = 150;

    private Animation<?> animation;

    public FileZeldaGameMenu() {
        super(MenuType.GAME_MENU);

        getContentRoot().getStyleClass().add("rootie-tootie-game-menu");

        getContentRoot().setTranslateX(FXGL.getAppWidth() / 2.0 - SIZE);
        getContentRoot().setTranslateY(FXGL.getAppHeight() / 2.0 - SIZE);

        var resumeShape = new Rectangle(SIZE*2, SIZE / 2);

        var exitShape = new Rectangle( SIZE*2, SIZE / 2);

        var backShape = new Rectangle(SIZE*2, SIZE / 2);

        resumeShape.getStyleClass().add("game-menu-button-box");
        exitShape.getStyleClass().add("game-menu-button-box");
        backShape.getStyleClass().add("game-menu-button-box");

        resumeShape.setOnMouseClicked(e -> fireResume());
        exitShape.setOnMouseClicked(e -> getGameController().exit());
        backShape.setOnMouseClicked(e -> getGameController().gotoMainMenu());

        Text textResume = new Text("RESUME");
        textResume.getStyleClass().add("game-menu-button-text");
        textResume.setTextAlignment(TextAlignment.CENTER);
        textResume.setMouseTransparent(true);

        Text textExit = new Text("EXIT");
        textExit.getStyleClass().add("game-menu-button-text");
        textExit.setTextAlignment(TextAlignment.CENTER);
        textExit.setMouseTransparent(true);

        Text textBack = new Text("BACK TO MAIN");
        textBack.getStyleClass().add("game-menu-button-text");
        textBack.setTextAlignment(TextAlignment.CENTER);
        textBack.setMouseTransparent(true);

        StackPane resumeButton = new StackPane();
        resumeButton.getChildren().addAll(resumeShape, textResume);
        resumeButton.getStyleClass().add("game-menu-button");

        StackPane exitButton = new StackPane();
        exitButton.getChildren().addAll(exitShape, textExit);
        exitButton.getStyleClass().add("game-menu-button");

        StackPane backButton = new StackPane();
        backButton.getChildren().addAll(backShape, textBack);
        backButton.getStyleClass().add("game-menu-button");

        VBox vb = new VBox();
        vb.setSpacing(20);
        vb.getChildren().addAll(resumeButton,backButton,exitButton);

        getContentRoot().getChildren().add(vb);

        getContentRoot().setScaleX(0);
        getContentRoot().setScaleY(0);

        animation = animationBuilder()
                .duration(Duration.seconds(0.66))
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .scale(getContentRoot())
                .from(new Point2D(0, 0))
                .to(new Point2D(1, 1))
                .build();
    }

    @Override
    public void onCreate() {
        animation.setOnFinished(EmptyRunnable.INSTANCE);
        animation.stop();
        animation.start();
    }

    @Override
    protected void onUpdate(double tpf) {
        animation.onUpdate(tpf);
    }
}