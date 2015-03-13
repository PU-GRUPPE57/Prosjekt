package gui;


import java.sql.Timestamp;
import java.time.LocalDate;

import event.Event;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CreateEvent extends Application{

	users.Group g;

	public CreateEvent(users.Group g){
		super();
		this.g = g;
	}

	public void start(final Stage primaryStage){
		primaryStage.setTitle("Kalender - Ny event");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		final Button btn1 = new Button("Opprett event");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn1);
		grid.add(hbBtn, 1, 5);

		final Button btn2 = new Button("Tilbake");
		hbBtn.getChildren().add(btn2);
		Scene scene = new Scene(grid, 300, 275);
		primaryStage.setScene(scene);

		Text scenetitle = new Text("Skriv inn informasjonen");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);
		Label name = new Label("Navn:");
		grid.add(name, 0, 1);
		final TextField nameBox = new TextField();
		grid.add(nameBox, 1, 1);
		Label pri = new Label("Prioritet:");
		grid.add(pri, 0, 2);
		final TextField priBox = new TextField();
		grid.add(priBox, 1, 2);
		Label beskrivelse = new Label("Beskrivelse:");
		grid.add(beskrivelse, 0, 3);
		final TextField beskBox = new TextField();
		grid.add(beskBox, 1, 3);
		Label date = new Label("DD MM YYYY");
		Label time = new Label ("HH MM");

		GridPane dateField = new GridPane();
		final TextField day = new TextField();
		final TextField month= new TextField();
		final TextField year = new TextField();
		dateField.add(day, 0, 0);
		dateField.add(month, 1, 0);
		dateField.add(year, 2, 0);
		
		GridPane timeField = new GridPane();
		final TextField hours = new TextField();
		final TextField minutes= new TextField();
		timeField.add(hours, 0, 0);
		timeField.add(minutes, 1, 0);
		
		
		grid.add(date, 0, 4);
		grid.add(dateField, 1, 4);
		grid.add(time, 0, 5);
		grid.add(timeField, 1, 5);


		primaryStage.show();

		btn1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				Event ev=new Event(nameBox.getText(), Integer.parseInt(priBox.getText()), null, Login.me, new Timestamp(1), new Timestamp(2), beskBox.getText());					
				ev.save(Login.conn);
				if (g!=null){
					for (User u : g.getUsers(Login.conn)){
						if (u.getId() == ev.getOwner().getId()) continue;
						u.addEvent(Login.conn, ev);
					}					
				}
				ev.replyToInvitation(Login.conn, Login.me, 1);
				RenderEvent r = new RenderEvent(ev);
				r.start(primaryStage);
			}
		});

		btn2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Hovedmeny hm = new Hovedmeny(LocalDate.now());
				hm.start(primaryStage);
			}
		});
	}
}