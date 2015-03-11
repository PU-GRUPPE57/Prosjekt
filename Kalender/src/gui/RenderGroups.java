package gui;

import java.sql.Connection;

import users.Admin;
import users.User;
import notification.Varsel;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
	public RenderGroups() {
	}
	
	@Override
	public void start(final Stage primaryStage) {
		groups.addAll(Login.me.getGroups(Login.conn));
		
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
 
        Button btn1 = new Button("Tilbake");
       	vbox.getChildren().add(btn1);
        
    	btn1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				Hovedmeny hm = new Hovedmeny();
				hm.start(primaryStage);
			}
		});
        
        
        primaryStage.setScene(scene);
        primaryStage.show();
	}
}