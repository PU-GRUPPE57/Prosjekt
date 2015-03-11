package gui;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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


public class Main extends Application{
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Connect to database:");
		System.out.println("Enter Username");
		String user = buf.readLine();
		System.out.println("Enter Password");
		String pass = buf.readLine();
		
		System.out.println(user);
		System.out.println(pass.equals(""));
		
		Connection conn = Admin.getConnection(user, pass);
		buf.close();
		
		Login lg = new Login();
		lg.start(primaryStage);
		
	}
}