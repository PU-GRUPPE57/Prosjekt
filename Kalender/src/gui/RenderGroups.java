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

public class RenderGroups extends Application{
	public static void main(String[] args) {
		launch(args);
	}

	private ObservableList<users.Group> groups = FXCollections.observableArrayList();
	private TableView<users.Group> table = new TableView();
	private Connection conn = Admin.getConnection("root", "");
	private User u = User.getUser(conn, 3);
	public RenderGroups() {
	}
	
	@Override
	public void start(Stage primaryStage) {
		groups.addAll(u.getGroups(conn));
		
		Scene scene = new Scene(new Group());
		
		Label label = new Label("Grupper");
		
		table.setEditable(true);
		
		TableColumn<users.Group, String> col1 = new TableColumn<users.Group, String>("Gruppenavn");
		col1.setCellValueFactory(new PropertyValueFactory<users.Group,String>("name"));
		
		table.setItems(groups);
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