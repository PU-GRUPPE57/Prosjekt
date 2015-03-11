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
public class Login extends Application{
	public static void main(String[] args) {
		launch(args);
	}
	public void start(final Stage primaryStage){
		primaryStage.setTitle("Kalender - Log inn");
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		Button btnLogin = new Button("Sign in");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btnLogin);
		grid.add(hbBtn, 1, 4);
		
		Button btnNewUser = new Button("New user");
		hbBtn.getChildren().add(btnNewUser);
		Scene scene = new Scene(grid, 300, 275);
		primaryStage.setScene(scene);
		
		Text scenetitle = new Text("Welcome");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);
		Label userName = new Label("User Name:");
		grid.add(userName, 0, 1);
		final TextField userTextField = new TextField();
		grid.add(userTextField, 1, 1);
		Label pw = new Label("Password:");
		grid.add(pw, 0, 2);
		final PasswordField pwBox = new PasswordField();
		grid.add(pwBox, 1, 2);
		
		primaryStage.show();
		
		btnLogin.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
//				Connection conn = Admin.getConnection("root", "root");
//		    	boolean log = User.login(conn, userTextField.getText(), pwBox.getText());
		    	Hovedmeny hm = new Hovedmeny();
		    	hm.start(primaryStage);
			}
		});
		
		btnNewUser.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		    	
		    }
		});
	}
	
}