package filezelda.components;


import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class AnimationComponent extends Component {

    private int speedY = 0;
    private int speedX = 0;

    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalkUp, animWalkDown, animWalkLeft, animWalkRight;

    public AnimationComponent() {
        animIdle = new AnimationChannel(FXGL.image("sprites_link8.png"), 3, 48, 48, Duration.INDEFINITE, 1, 1);
        animWalkDown = new AnimationChannel(FXGL.image("sprites_link8.png"), 3, 48, 48, Duration.INDEFINITE, 0, 2);
        animWalkLeft = new AnimationChannel(FXGL.image("sprites_link8.png"), 3, 48, 48, Duration.INDEFINITE, 3, 5);
        animWalkRight = new AnimationChannel(FXGL.image("sprites_link8.png"), 3, 48, 48, Duration.INDEFINITE, 6, 8);
        animWalkUp = new AnimationChannel(FXGL.image("sprites_link8.png"), 3, 48, 48, Duration.INDEFINITE, 9, 11);

        texture = new AnimatedTexture(animIdle);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {

        // clamp speed values

        if (speedY > 150) {
            speedY = 150;
        }
        if (speedX > 150) {
            speedX = 150;
        }
        if (speedY < -150) {
            speedY = -150;
        }
        if (speedX < -150) {
            speedX = -150;
        }

        entity.translateY(-2 * speedY * tpf);
        entity.translateX(2 * speedX * tpf);

        if (speedY != 0) {
            if (speedY > 0) {
                texture.loopAnimationChannel(animWalkUp);
            } else {
                texture.loopAnimationChannel(animWalkDown);
            }
            speedY = (int) (speedY * 0.5);
            if (FXGLMath.abs(speedY) < 1) {
                speedY = 0;
            }

        }

        if (speedX != 0) {
            if (speedX > 0) {
                texture.loopAnimationChannel(animWalkRight);
            } else {
                texture.loopAnimationChannel(animWalkLeft);
            }

            speedX = (int) (speedX * 0.5);
            if (FXGLMath.abs(speedX) < 1) {
                speedX = 0;
            }
        }

        if (speedX < 1 && speedY < 1 && speedX > -1 && speedY > -1) {
            speedX = 0;
            speedY = 0;
            texture.loopAnimationChannel(animIdle);
        }
    }

    public void moveRight() {
        speedX = 150;
    }

    public void moveLeft() {
        speedX = -150;
    }

    public void moveUp() {
        speedY = 150;
    }

    public void moveDown() {
        speedY = -150;
    }

    public void moveAction() {
        // not sure yet
    }
}