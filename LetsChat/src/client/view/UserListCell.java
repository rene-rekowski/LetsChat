package client.view;

import client.model.User;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class UserListCell extends ListCell<User> {
    private final HBox hbox = new HBox(5);
    private final Label label = new Label();
    private final Circle statusCircle = new Circle(5, Color.GREEN);

    public UserListCell() {
        hbox.getChildren().addAll(label, statusCircle);
    }

    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if (empty || user == null) {
            setText(null);
            setGraphic(null);
        } else {
            label.setText(user.getName());
            setGraphic(hbox);
        }
    }
}
