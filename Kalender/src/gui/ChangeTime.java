package gui;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import com.sun.javafx.css.CalculatedValue;

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

public class ChangeTime extends Application{

	private Event event;
	
	public ChangeTime(Event e){
		super();
		this.event = e;
	}

	public void start(final Stage primaryStage){
		primaryStage.setTitle("Kalender -" + event.getName() + "- Endre tidspunkt");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		final Button btn1 = new Button("lagre endring");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn1);
		grid.add(hbBtn, 1, 6);

		final Button btn2 = new Button("Tilbake");
		hbBtn.getChildren().add(btn2);
		Scene scene = new Scene(grid, 300, 275);
		primaryStage.setScene(scene);

		Text scenetitle = new Text("Skriv inn nytt tidspunkt");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		Label date = new Label("DD MM YYYY");
		Label time = new Label ("HH MM");
		Label date2 = new Label("DD MM YYYY");
		Label time2 = new Label ("HH MM");

		GridPane dateField = new GridPane();
		final NumberTextField day = new NumberTextField(2);
		final NumberTextField month= new NumberTextField(2);
		final NumberTextField year = new NumberTextField(4);
		dateField.add(day, 0, 0);
		dateField.add(month, 1, 0);
		dateField.add(year, 2, 0);
		
		GridPane timeField = new GridPane();
		final NumberTextField hours = new NumberTextField(2);
		final NumberTextField minutes= new NumberTextField(2);
		timeField.add(hours, 0, 0);
		timeField.add(minutes, 1, 0);
		
		GridPane dateField2 = new GridPane();
		final NumberTextField day2 = new NumberTextField(2);
		final NumberTextField month2= new NumberTextField(2);
		final NumberTextField year2 = new NumberTextField(4);
		dateField2.add(day2, 0, 0);
		dateField2.add(month2, 1, 0);
		dateField2.add(year2, 2, 0);
		
		GridPane timeField2 = new GridPane();
		final NumberTextField hours2 = new NumberTextField(2);
		final NumberTextField minutes2= new NumberTextField(2);
		timeField2.add(hours2, 0, 0);
		timeField2.add(minutes2, 1, 0);
		
		
		grid.add(date, 0, 4);
		grid.add(dateField, 1, 4);
		grid.add(time, 0, 5);
		grid.add(timeField, 1, 5);
		
		grid.add(date2, 2, 4);
		grid.add(dateField2, 3, 4);
		grid.add(time2, 2, 5);
		grid.add(timeField2, 3, 5);



		primaryStage.show();

		btn1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				Calendar cal = Calendar.getInstance();
				cal.set(Integer.valueOf(year.getText()), Integer.valueOf(month.getText()) - 1, Integer.valueOf(day.getText()), Integer.valueOf(hours.getText()), Integer.valueOf(minutes.getText()));
				
				Date date = cal.getTime();
				
				Timestamp t = new Timestamp(date.getTime());
				cal.set(Integer.valueOf(year2.getText()), Integer.valueOf(month2.getText()) - 1, Integer.valueOf(day2.getText()), Integer.valueOf(hours2.getText()), Integer.valueOf(minutes2.getText()));
				Timestamp t2 = new Timestamp(cal.getTime().getTime());
				
				event.changeTime(Login.conn, Login.me, t , t2);
				RenderEvent r = new RenderEvent(event);
				r.start(primaryStage);
				
			}									
		});

		btn2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
			RenderEvent r = new RenderEvent(event);
			r.start(primaryStage);
			}
		});
	}
	private class NumberTextField extends TextField
	{
		private int max;
		
		private NumberTextField(int max){
			this.max = max;
		}

	    @Override
	    public void replaceText(int start, int end, String text)
	    {
	        if (validate(text))
	        {
	            super.replaceText(start, end, text);
	        }
	    }

	    @Override
	    public void replaceSelection(String text)
	    {
	        if (validate(text))
	        {
	            super.replaceSelection(text);
	            verify();
	        }
	    }

	    private boolean validate(String text)
	    {
	        return ("".equals(text) || text.matches("[0-9]"));
	    }
	    private void verify() {
	        if (getText().length() > max) {
	            setText(getText().substring(0, max));
	        }

	    }
	}
}