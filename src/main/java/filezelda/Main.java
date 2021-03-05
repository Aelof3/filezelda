package filezelda;

import com.almasb.fxgl.core.collection.Array;
import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.entity.SpawnData;
import filezelda.components.AnimationComponent;
import filezelda.components.DirectoryComponent;
import javafx.scene.Node;
import org.apache.commons.io.*;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Main extends GameApplication {

    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 885;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(WINDOW_WIDTH);
        settings.setHeight(WINDOW_HEIGHT);
        settings.setTitle("FileZelda");
        settings.getCSSList().add("menu.css");
        settings.setVersion("0.1");
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory(){
            @Override
            public FXGLMenu newMainMenu(){
                return new FileZeldaMainMenu();
            }

            @Override
            public FXGLMenu newGameMenu(){
                return new FileZeldaGameMenu();
            }
        });
    }

    String dirr = FileZeldaMainMenu.getDirr();

    public String[] getDirectoryContents(String path){
        File f = new File(path);
        return f.list();
    }

    public File[] getDrives() {
        File[] drives = File.listRoots();
        return drives;
    }

    public enum EntityType {
        PLAYER,
        CURRENTDIRECTORY,
        DIRECTORY,
        FILE,
        ARCHIVE,
        CODE,
        ETC,
        BACK
    }

    @Override
    protected void initInput() {

        FXGL.getInput().addAction(new UserAction("Action") {
            @Override
            protected void onAction() {
                //player.translateX(5); // move right 5 pixels
                if (standingOnDir != "NOT_STANDING_ON_DIR") {
                    changeDirectory(standingOnDir);
                }
            }
        }, KeyCode.SPACE);

        FXGL.getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                //player.translateX(5); // move right 5 pixels
                player.getComponent(AnimationComponent.class).moveRight();
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                //player.translateX(-5); // move left 5 pixels
                player.getComponent(AnimationComponent.class).moveLeft();
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                //player.translateY(-5); // move up 5 pixels
                player.getComponent(AnimationComponent.class).moveUp();

                if (player.getPosition().getY() < 0) {

                    PropertyMap state = FXGL.getWorldProperties();
                    int s = state.getInt("levelpos");

                    if (s > 0) {
                        state.setValue("levelpos", s - 1);
                        System.out.println("levelpos - 1: " + state.getInt("levelpos"));
                    }

                    reRenderDirContents();

                    setRootClass();

                    player.setPosition(player.getX(),WINDOW_HEIGHT - 50.0);
                }
            }
        }, KeyCode.W);

        FXGL.getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                //player.translateY(5); // move down 5 pixels
                player.getComponent(AnimationComponent.class).moveDown();

                if (player.getPosition().getY() > getGameScene().getHeight()) {
                    PropertyMap state = FXGL.getWorldProperties();
                    int s = state.getInt("levelpos");
                    state.setValue("levelpos", s + 1);
                    System.out.println("levelpos + 1: " + state.getInt("levelpos"));
                    reRenderDirContents();

                    getGameScene().getRoot().getStyleClass().clear();

                    setRootClass();

                    player.setPosition(player.getX(),50.0);
                }
            }
        }, KeyCode.S);

    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void setRootClass() {

        PropertyMap state = FXGL.getWorldProperties();
        int s = state.getInt("levelpos");

        getGameScene().getRoot().getStyleClass().clear();

        if (s == 0) {
            getGameScene().getRoot().getStyleClass().add("root-more-top");
        } else {
            getGameScene().getRoot().getStyleClass().add("root-more-mid");
        }
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("levelpos", 0);
    }

    private Entity player;
    private String currPath = "";
    private String standingOnDir = "";
    private String[] currentDirectoryContent;

    public boolean checkFileType(String fileType, String[] extNames) {
        for (String ext : extNames ) {
            if (fileType.contains(ext)) {
                return true;
            }
        }
        return false;
    }

    protected void contentTypeFilter(String content, int i) {

        // be sure to include:
        //  -   image
        //  -   class for image
        //  -   text to go with image
        //  -   rectangle for text
        //System.out.println(content);

        Image img;

        Text newD = new Text(content);
        newD.getStyleClass().add("current-directory-content-path");

        String path = FilenameUtils.concat(currPath, content);

        File fil = new File(path);

        boolean isDir = fil.isDirectory();

        String fileType = FilenameUtils.getExtension(content);

        String[] types1 = {"zip","rar","7z"};
        String[] types2 = {"txt","doc","docx","pdf","csv"};
        String[] types3 = {"php","js","c","py","rb","java","sql","json","xml","html","css","pl","sh","conf","config","ini","xhtml"};

        PropertyMap state = FXGL.getWorldProperties();
        int s = state.getInt("levelpos");
        int y = (int)Math.floor(i / 7.0) * 150 + 180 - (s * WINDOW_HEIGHT);
        int x = i % 7 * 140 + 200;

        Text nm = new Text(content);
        SpawnData spawnData = new SpawnData(x,y);
        spawnData.put("name", newD);
        spawnData.put("path", nm);

        if (isDir) {
            getGameWorld().spawn("directory", spawnData);
        } else if (checkFileType(fileType, types1)) {
            getGameWorld().spawn("archive", spawnData);
        } else if (checkFileType(fileType, types2)) {
            getGameWorld().spawn("file", spawnData);
        } else if (checkFileType(fileType, types3)) {
            getGameWorld().spawn("code", spawnData);
        } else {
            getGameWorld().spawn("etc", spawnData);
        }
    }

    //@Override
    protected void reRenderDirContents() {
        getGameScene().getRoot().getChildren().removeAll();
        getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.CODE));
        getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.FILE));
        getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.ARCHIVE));
        getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.ETC));
        getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.DIRECTORY));

        int l = 0;

        for (String c : currentDirectoryContent) {

            contentTypeFilter(c,l);

            l = l + 1;
        }
    }

    protected void parseDirContents(String[] drr) {

        StackPane rspn = new StackPane();
        getGameScene().getRoot().getChildren().removeAll();
        getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.CODE));
        getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.CURRENTDIRECTORY));
        getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.FILE));
        getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.ARCHIVE));
        getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.ETC));
        getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.DIRECTORY));
        getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.BACK));

        Text d = new Text("DRIVE: " + currPath);
        d.setId("current-directory");

        SpawnData spawnData = new SpawnData(10,10);
        spawnData.put("name", d);
        getGameWorld().spawn("currentDirectory",spawnData);
        getGameWorld().spawn("back", 50, 350);

        int l = 0;

        for (String c : drr) {

            contentTypeFilter(c,l);

            l = l + 1;
        }
    }

    protected void changeDirectory(String newDirectory) {
        PropertyMap state = FXGL.getWorldProperties();
        state.setValue("levelpos",0);

        if (currPath.length() > 0) {
            if (newDirectory == "BACK_BUTTON_ENTITY") {
                currPath = FilenameUtils.getFullPathNoEndSeparator(currPath);
            } else {
                currPath = FilenameUtils.concat(currPath, newDirectory);
            }
        } else {
            currPath = newDirectory;
        }

        setRootClass();

        player.setPosition(60,400);
        currentDirectoryContent = getDirectoryContents(currPath);

        Arrays.sort(currentDirectoryContent);

        parseDirContents(currentDirectoryContent);
    }

    @Override
    protected void initUI() {
        getGameScene().getRoot().getStylesheets().add("assets/ui/css/style.css");

        dirr = FileZeldaMainMenu.getDirr();

        changeDirectory(dirr);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new ItemFactory());

        player = getGameWorld().spawn("player", 60, 400);
    }

    @Override
    protected void initPhysics() {
        // order of types on the right is the same as on the left
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.DIRECTORY) {
            @Override
            protected void onCollisionBegin(Entity player, Entity directory) {
                if (!directory.hasComponent(DirectoryComponent.class)) {
                    standingOnDir = "NOT_STANDING_ON_DIR";
                    return;
                }
                DirectoryComponent dcom = directory.getComponent(DirectoryComponent.class);
                dcom.textRectBorderActive();
                standingOnDir = dcom.getDirPath();
                System.out.println(standingOnDir);
            }
            @Override
            protected void onCollisionEnd(Entity player, Entity directory) {
                if (!directory.hasComponent(DirectoryComponent.class)) {
                    standingOnDir = "NOT_STANDING_ON_DIR";
                    return;
                }
                DirectoryComponent dcom = directory.getComponent(DirectoryComponent.class);
                dcom.textRectBorderInactive();
                standingOnDir = "NOT_STANDING_ON_DIR";
                System.out.println(standingOnDir);
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.BACK) {
            @Override
            protected void onCollisionBegin(Entity player, Entity back) {
                if (!back.hasComponent(DirectoryComponent.class)) {
                    standingOnDir = "NOT_STANDING_ON_DIR";
                    return;
                }
                DirectoryComponent dcom = back.getComponent(DirectoryComponent.class);
                dcom.textRectBorderActive();
                standingOnDir = dcom.getDirPath();
                System.out.println(standingOnDir);
            }
            @Override
            protected void onCollisionEnd(Entity player, Entity back) {
                if (!back.hasComponent(DirectoryComponent.class)) {
                    standingOnDir = "NOT_STANDING_ON_DIR";
                    return;
                }
                DirectoryComponent dcom = back.getComponent(DirectoryComponent.class);
                dcom.textRectBorderInactive();
                standingOnDir = "NOT_STANDING_ON_DIR";
                System.out.println(standingOnDir);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
