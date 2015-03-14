package gui;
import java.sql.Connection;
import java.time.LocalDate;
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

public class Nybruker extends Application{
	

	public void start(final Stage primaryStage){
		primaryStage.setTitle("Kalender - Ny bruker");
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		Button btn1 = new Button("Opprett bruker");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn1);
		grid.add(hbBtn, 1, 5);
		
		Button btn2 = new Button("Tilbake");
		hbBtn.getChildren().add(btn2);
		Scene scene = new Scene(grid, 300, 275);
		primaryStage.setScene(scene);
		
		Text scenetitle = new Text("Skriv inn informasjonen");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);
		Label name1 = new Label("Fornavn:");
		grid.add(name1, 0, 1);
		final TextField name1Box = new TextField();
		grid.add(name1Box, 1, 1);
		Label name2 = new Label("Etternavn:");
		grid.add(name2, 0, 2);
		final TextField name2Box = new TextField();
		grid.add(name2Box, 1, 2);
		Label brukernavn = new Label("Brukernavn:");
		grid.add(brukernavn, 0, 3);
		final TextField brukernavnBox = new TextField();
		grid.add(brukernavnBox, 1, 3);
		Label pw = new Label("Passord:");
		grid.add(pw, 0, 4);
		final PasswordField pwBox = new PasswordField();
		grid.add(pwBox, 1, 4);
		
		
		primaryStage.show();
		
		btn1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				Login.me = new User(name1Box.getText(), name2Box.getText(), brukernavnBox.getText(), pwBox.getText(), false);
				Login.me.save(Login.conn);
				Hovedmeny hm = new Hovedmeny(LocalDate.now(),Hovedmeny.VISIBLE);
				hm.start(primaryStage);
			}
		});
		
		btn2.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		    	Login lg = new Login();
				lg.start(primaryStage);
		    }
		});
	}
	
}