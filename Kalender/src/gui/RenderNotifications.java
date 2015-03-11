package gui;

import java.sql.Connection;

import users.Admin;
import users.User;
import notification.Varsel;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RenderNotifications extends Application{
	public static void main(String[] args) {
		launch(args);
	}

	private ObservableList<Varsel> notifications = FXCollections.observableArrayList();
	private TableView<Varsel> table = new TableView();
	private Connection conn = Admin.getConnection("root", "");
	private User u = User.getUser(conn, 1);
	public RenderNotifications() {
	}
	
	@Override
	public void start(Stage primaryStage) {
		notifications.addAll(u.getNotifications(conn));
		
		Scene scene = new Scene(new Group());
		
		Label label = new Label("Notifications");
		
		table.setEditable(true);
		
		TableColumn<Varsel, String> col1 = new TableColumn<Varsel, String>("Varseltext");
		col1.setCellValueFactory(new PropertyValueFactory<Varsel,String>("text"));
		
		table.setItems(notifications);
		table.getColumns().add(col1);
		
		VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        primaryStage.setScene(scene);
        primaryStage.show();
	}
}