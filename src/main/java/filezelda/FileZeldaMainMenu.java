package filezelda;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.util.EmptyRunnable;
import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;

import static com.almasb.fxgl.dsl.FXGL.*;

public class FileZeldaMainMenu extends FXGLMenu {

    private static final int SIZE = 150;

    private Animation<?> animation;

    public FileZeldaMainMenu() {
        super(MenuType.MAIN_MENU);

        getContentRoot().setTranslateX(FXGL.getAppWidth() / 2.0 - SIZE);
        getContentRoot().setTranslateY(FXGL.getAppHeight() / 2.0 - SIZE);

        getContentRoot().setScaleX(0);
        getContentRoot().setScaleY(0);
        VBox vb = new VBox();
        vb.setSpacing(10);
        //vb.setTranslateX(150);
        vb.setTranslateY(110);
        for (File sysDrive : getDrives()){
            if (sysDrive.length() > 0) {

                Rectangle r = new Rectangle(120, 110, 240, 55);
                r.setStrokeWidth(2.5);
                r.getStyleClass().add("main-menu-item");

                r.setOnMouseClicked(e -> {
                    setDirr(sysDrive.toString());
                    fireNewGame();
                });

                Text d = new Text("DRIVE: " + sysDrive);
                d.getStyleClass().add("main-menu-drive");

                StackPane sp = new StackPane();
                sp.getChildren().addAll(r, d);
                sp.getStyleClass().add("main-menu-button");

                vb.getChildren().add(sp);
            }

            getContentRoot().getChildren().add(vb);

        }

        animation = animationBuilder()
                .duration(Duration.seconds(0.66))
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .scale(getContentRoot())
                .from(new Point2D(0, 0))
                .to(new Point2D(1, 1))
                .build();
    }

    public File[] getDrives() {
        File[] drives = File.listRoots();
        return drives;
    }
    private static String dirr = "DEFAULT";

    public static String getDirr(){
        return dirr;
    }

    public static void setDirr(String dat) {
        dirr = dat;
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
