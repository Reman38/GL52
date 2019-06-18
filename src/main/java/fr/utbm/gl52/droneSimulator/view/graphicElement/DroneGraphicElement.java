package fr.utbm.gl52.droneSimulator.view.graphicElement;

import fr.utbm.gl52.droneSimulator.model.SimulationElement;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class DroneGraphicElement extends CenteredAndErgonomicGraphicElement {

    private Timeline timeline = new Timeline();

    /**
     * Default constructor of a drone graphic elements
     * Manage the animation
     *
     * @param se Simulation element
     */
    public DroneGraphicElement(SimulationElement se) {
        super(se);

        setColor("red");
        isFilled(true);

        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame moveDrone = new KeyFrame(Duration.seconds(1f/30f), new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                getShape().setX(getX());
                getShape().setY(getY());
                getGraphicalId().setX(getXId());
                getGraphicalId().setY(getYId());
            }
        });

        timeline.getKeyFrames().add(moveDrone);
        timeline.play();
    }

    /**
     * Stop the animation
     */
    public void stopAnimation(){
        timeline.stop();
    }
}