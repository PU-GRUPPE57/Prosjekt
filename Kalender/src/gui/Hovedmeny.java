package gui;
import java.sql.Connection;
import java.util.Calendar;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;
import java.util.GregorianCalendar;


public class Hovedmeny extends Application{

	public void start(final Stage primaryStage){
		primaryStage.setTitle("Kalender - Hovedmeny - " + Login.me.getName());
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 1000, 750);
		primaryStage.setScene(scene);
		initCalender(root);
		initButtons(root, primaryStage);
		primaryStage.show();
		}
		//Bygger designet til kalenderen:
		private void initCalender(BorderPane root) {
		GridPane calendar = new GridPane();
		String[] days = {"Mandag", "Tirsdag", "Onsdag",
		"Torsdag", "Fredag", "Lørdag", "Søndag"};
		for (int c = 0; c < 8; ++c) {
		ColumnConstraints col = new ColumnConstraints();
		col.setPercentWidth(100/8);calendar.getColumnConstraints().add(col);
		}
		for (int r = 0; r < 7; ++r) {
		RowConstraints row = new RowConstraints();
		row.setPercentHeight(100/7);
		calendar.getRowConstraints().add(row);
		}
		Calendar cal = getStartDateOfCalendar(2015, 3);
		for (int r = 0; r < 7; ++r) {
		for (int c = 0; c < 8; ++c) {
		Pane date = new Pane();
		date.setStyle("-fx-border-color: black;");
		//Setter dager:
		if (c > 0 && r > 0) {
		Label day = new
		Label(String.valueOf(cal.get(Calendar.DATE)));
		date.getChildren().add(day);
		cal.add(Calendar.DATE, 1);
		}
		//setter ukenr:
		if (c == 0 && r > 0) {
		Label week = new
		Label(String.valueOf(cal.get(Calendar.WEEK_OF_YEAR)));
		date.getChildren().add(week);
		}
		//Setter navn på dager:
		if (r == 0 && c > 0) {
		Label weekDay = new Label(days[c-1]);
		date.getChildren().add(weekDay);
		weekDay.setStyle("-fx-focolor: red" );
		}
		//Setter
		calendar.add(date, c, r);
		}
		}
		root.setCenter(calendar);
		}
		
		private boolean isLeapYear(int year) {if (year%400 == 0)
		return true;
		if (year%100 == 0)
		return false;
		if (year%4 == 0)
		return true;
		return false;
		}
		/**
		 * Finner startdagen for måneden:
		 * @param curYear Nåværende år
		 * @param curMonth Nåværende månede
		 * @param curDate Nåværende dag(dato)
		 * @return
		 */
		private Calendar getStartDateOfCalendar(int curYear, int
		curMonth) {
		Date date = Date.valueOf(LocalDate.of(curYear, curMonth,
		1));
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int firstDay = c.get(Calendar.DAY_OF_WEEK);
		firstDay += (firstDay == 1) ? 5 : -2;
		c.add(Calendar.DAY_OF_MONTH, -firstDay);
		return c;
		}
		
		private void initButtons(BorderPane root, final Stage primaryStage){
			GridPane grid = new GridPane();
			grid.setAlignment(Pos.CENTER);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(25, 25, 25, 25));

			Button btn1 = new Button("Varsel");
			grid.add(btn1, 0,1);
			Button btn3 = new Button("Grupper");
			grid.add(btn3, 1, 1);
			Button btn4 = new Button("Eventer");
			grid.add(btn4, 0 , 2);
			Button btn5 = new Button("Logg ut");
			grid.add(btn5, 1, 2);
//			Button btn6 = new Button("");
//			hbBtn.getChildren().add(btn6);
//			Button btn7 = new Button("");
//			hbBtn.getChildren().add(btn7);
//			Button btn8 = new Button("");
//			hbBtn.getChildren().add(btn8);
//			Button btn9 = new Button("");
//			hbBtn.getChildren().add(btn9);
//			Button btn10 = new Button("");
//			hbBtn.getChildren().add(btn10);
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
			root.setTop(grid);
		}
}