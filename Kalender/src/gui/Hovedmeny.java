package gui;
import java.sql.Connection;
import java.util.Scanner;
import users.Admin;
import users.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Hovedmeny extends Application{

	public void start(final Stage primaryStage){
		primaryStage.setTitle("Kalender - Hovedmeny - " + Login.me.getName());
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Button btn1 = new Button("Varsel");
		grid.add(btn1, 0,1);
		Button btn2 = new Button("Kalender");
		grid.add(btn2, 1, 1);
		Button btn3 = new Button("Grupper");
		grid.add(btn3, 0, 2);
		Button btn4 = new Button("Eventer");
		grid.add(btn4, 1 , 2);
		Button btn5 = new Button("Logg ut");
		grid.add(btn5, 1,3);
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
		Scene scene = new Scene(grid, 300, 275);
		primaryStage.setScene(scene);
		Text scenetitle = new Text("PU er kjempegøy");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);
		
		btn1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				RenderNotifications r = new RenderNotifications();
				r.start(primaryStage);
			}
		});
		
		btn5.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				Login.me = null;
				Login l = new Login();
				l.start(primaryStage);
			}
		});
		
		btn3.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				RenderGroups rg = new RenderGroups();
				rg.start(primaryStage);
			}
		});
		
		
		primaryStage.show();

	}
}