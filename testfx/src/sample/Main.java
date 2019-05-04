package sample;

import com.sun.prism.paint.Color;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private Circle circle2;
    @Override
    public void start(Stage primaryStage) throws Exception{

        circle2 = new Circle();
        circle2.setCenterX(20);
        circle2.setCenterY(20);
        circle2.setRadius(5);
        circle2.setFill(Paint.valueOf("blue"));

        Group root = new Group();
        root.getChildren().add(circle2);

        /*Button btn1=new Button("Say, Hello World");
        StackPane root=new StackPane();
        root.getChildren().add(btn1);*/
        Scene scene = new Scene(root, 500, 600);

        for(int i = 0; i < 20; i++) {
            addBouncyBall(scene);
        }
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hello World");
        primaryStage.show();
    }

    private void addBouncyBall(final Scene scene) {
        final Ball ball = new Ball(100, 100, 20);

        final Group root = (Group) scene.getRoot();
        root.getChildren().add(ball);

        Timeline tl = new Timeline();
        tl.setCycleCount(Animation.INDEFINITE);
        KeyFrame moveBall = new KeyFrame(Duration.seconds(.0100),
                new EventHandler<ActionEvent>() {

                    public void handle(ActionEvent event) {

                        double xMin = ball.getBoundsInParent().getMinX();
                        double yMin = ball.getBoundsInParent().getMinY();
                        double xMax = ball.getBoundsInParent().getMaxX();
                        double yMax = ball.getBoundsInParent().getMaxY();

                        if (xMin < 0 || xMax > scene.getWidth()) {
                            ball.setDx(ball.getDx() * -1);
                        }
                        if (yMin < 0 || yMax > scene.getHeight()) {
                            ball.setDy(ball.getDy() * -1);
                        }

                        ball.setTranslateX(ball.getTranslateX() + ball.getDx());
                        ball.setTranslateY(ball.getTranslateY() + ball.getDy());

                    }
                });

        tl.getKeyFrames().add(moveBall);
        tl.play();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
