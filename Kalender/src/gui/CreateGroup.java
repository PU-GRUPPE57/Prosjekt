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

public class CreateGroup extends Application{


	public void start(final Stage primaryStage){
		primaryStage.setTitle("Kalender - Ny gruppe");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Button btn1 = new Button("Opprett gruppe");
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
		Label name1 = new Label("Gruppenavn:");
		grid.add(name1, 0, 1);
		final TextField name1Box = new TextField();
		grid.add(name1Box, 1, 1);

		primaryStage.show();

		btn1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				users.Group g = new users.Group(name1Box.getText(), Login.me);
				g.save(Login.conn);
				
				InviteUsers i = new InviteUsers(g);
				i.start(primaryStage);
			}
		});

		btn2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Hovedmeny hm = new Hovedmeny(LocalDate.now(),Hovedmeny.VISIBLE);
				hm.start(primaryStage);
			}
		});
	}

}