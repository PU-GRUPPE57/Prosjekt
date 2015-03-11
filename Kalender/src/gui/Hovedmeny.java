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
	public static void main(String[] args) {
		launch(args);
	}
	public void start(final Stage primaryStage){
		primaryStage.setTitle("Kalender - Hovedmeny");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		grid.add(hbBtn, 1, 4);	
		Button btn1 = new Button("Varsel");
		hbBtn.getChildren().add(btn1);
		Button btn2 = new Button("Kalender");
		hbBtn.getChildren().add(btn2);
		Button btn3 = new Button("Grupper");
		hbBtn.getChildren().add(btn3);
		Button btn4 = new Button("Eventer");
		hbBtn.getChildren().add(btn4);
		Button btn5 = new Button("Logg ut");
		hbBtn.getChildren().add(btn5);
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
		primaryStage.show();
		//		btnLogin.setOnAction(new EventHandler<ActionEvent>() {
		//			public void handle(ActionEvent e){
		//				Connection conn = Admin.getConnection("root", "root");
		//		    	boolean log = User.login(conn, userTextField.getText(), pwBox.getText());
		//		    	System.out.println(log);
		//			}
		//		});
		//		
		//		btnNewUser.setOnAction(new EventHandler<ActionEvent>() {
		//		    public void handle(ActionEvent e) {
		//		    	
		//		    }
		//		});
	}
}