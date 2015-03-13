package gui;
import java.sql.Connection;
import java.util.Scanner;

import users.Admin;
import users.Group;
import users.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class RenderGroup extends Application{
	
	private users.Group g = new Group("Invalid", null);
	private ObservableList<User> users = FXCollections.observableArrayList();
	private TableView<User> table = new TableView();

	public void start(final Stage primaryStage){
		primaryStage.setTitle("Kalender - Gruppe - " + g.getName() + " - " + Login.me.getName());
		
		users.addAll(g.getUsers(Login.conn));
		
		Scene scene = new Scene(new javafx.scene.Group());
		
		Label label = new Label("Brukere i gruppen");
		
		table.setEditable(true);
		
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Button btn2 = new Button("Inviter Brukere");
		grid.add(btn2, 1, 1);
		Button btn3 = new Button("Opprett event");
		grid.add(btn3, 0, 2);
		Button btn4 = new Button("Til hovedmeny");
		grid.add(btn4, 1 , 2);
		
		TableColumn<User, String> col1 = new TableColumn<User, String>("Brukernavn");
		col1.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
		
		table.setItems(users);
		table.getColumns().add(col1);
		
		VBox vbox = new VBox();
       	vbox.setSpacing(5);
       	vbox.setPadding(new Insets(10, 0, 0, 10));
       	vbox.getChildren().add(grid);
       	vbox.getChildren().addAll(label, table);
       	
       	((javafx.scene.Group) scene.getRoot()).getChildren().addAll(vbox);
		
		
		
//		Button btn6 = new Button("");
//		hbBtn.getChildren().add(btn6);
//		Button btn7 = new Button("");
//		hbBtn.getChildren().add(btn7);
//		Button btn8 = new Button("");
//		hbBtn.getChildren().add(btn8);
//		Button btn9 = new Button("");
//		hbBtn.getChildren().add(btn9);
//		Button btn10 = new Button("");
//		hbBtn.getChildren().add(btn10);
		primaryStage.setScene(scene);
		Text scenetitle = new Text("Gruppe " + g.getName());
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);
		
		btn2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				InviteUsers i = new InviteUsers();
				i.init(g);
				i.start(primaryStage);
			}
		});
		
		
		btn3.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				//TODO;
			}
		});
		btn4.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				Hovedmeny hm = new Hovedmeny();
				hm.start(primaryStage);
			}
		});
		
		
		
		
		primaryStage.show();

	}
	
	public void init(users.Group g) {
		this.g = g;
	}
}