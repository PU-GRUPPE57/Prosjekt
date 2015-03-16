package gui;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import event.Event;


public class Hovedmeny extends Application{

	private LocalDate time;
	public static final int VISIBLE = 0;
	public static final int HIDDEN = 1;
	public static final int ALL = 2;
	
	private int choice;

	private HashMap<Timestamp, GridPane> dayList = new HashMap<>();

	public Hovedmeny(LocalDate date, int choice){
		super();
		if (choice<0 || choice >2) throw new IllegalArgumentException("Wrong integer");
		this.time = date;
		this.choice = choice;
	}
	public void start(final Stage primaryStage){
		primaryStage.setTitle("Kalender - Hovedmeny - " + Login.me.getName());
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 1000, 750);
		primaryStage.setScene(scene);
		initCalender(root);
		initButtons(root, primaryStage);
		initEvents(primaryStage);
		primaryStage.show();
	}


	//Bygger designet til kalenderen:
	private void initCalender(BorderPane root) {
		boolean iscorrectmonth = false;
		GridPane calendar = new GridPane();
		String[] days = {"Mandag", "Tirsdag", "Onsdag",
				"Torsdag", "Fredag", "Lørdag", "Søndag"};
		for (int c = 0; c < 8; ++c) {
			ColumnConstraints col = new ColumnConstraints();
			col.setPercentWidth(100/8);
			calendar.getColumnConstraints().add(col);
		}
		
		for (int r = 0; r < 7; ++r) {
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(100/7);
			calendar.getRowConstraints().add(row);
		}
		
		Calendar cal = getStartDateOfCalendar(time.getYear(), time.getMonthValue());
		
		for (int r = 0; r < 7; ++r) {
			for (int c = 0; c < 8; ++c) {
				Pane date = new Pane();
				date.setStyle("-fx-border-color: black;");
				
				//Setter dager:
				if (c > 0 && r > 0) {
					Label day = new
							Label(String.valueOf(cal.get(Calendar.DATE)));
					
					//Lager individuell eventliste i dagens rute:
					GridPane eventlist = new GridPane();
					eventlist.setAlignment(Pos.CENTER);
					date.getChildren().add(eventlist);
					
					//Legger inn i hashmap dayList referanse til ruten:
					if (cal.get(Calendar.DATE) == 1) iscorrectmonth = !iscorrectmonth;
					if (iscorrectmonth) dayList.put(new Timestamp(cal.getTime().getTime()), eventlist);
					
					if (cal.get(Calendar.MONTH) != (time.getMonthValue()-1))
						day.setStyle("-fx-text-fill: #c4c4c4");
					
					date.getChildren().add(day);
					cal.add(Calendar.DATE, 1); //kalender dag++;
				}
				//setter ukenr:
				if (c == 0 && r > 0) {
					Label week = new
					Label(String.valueOf(cal.get(Calendar.WEEK_OF_YEAR)));
					week.setStyle("-fx-text-fill: #c9c9c9;");
					week.setFont(Font.font("Verdana", FontWeight.BOLD, 70));
					date.getChildren().add(week);
				}
				
				//Setter navn på dager:
				if (r == 0 && c > 0) {
					Label weekDay = new Label(days[c-1]);
					date.getChildren().add(weekDay);
					weekDay.setStyle("-fx-text-fill: #3c8efe");
					weekDay.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
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
	 * Finner startdagen for m�neden:
	 * @param curYear N�v�rende �r
	 * @param curMonth N�v�rende m�nede
	 * @param curDate N�v�rende dag(dato)
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


		Label title = new Label(time.getMonth().name());
		Label title2 = new Label(String.valueOf(time.getYear()));
		title.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		title2.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		grid.add(title, 3, 0);
		grid.add(title2, 3, 1);

		Button btn0 = new Button("-->");
		grid.add(btn0, 8, 2);
		Button btn1 = new Button("Varsel");
		grid.add(btn1, 1,2);
		Button btn2 = new Button("Grupper");
		grid.add(btn2, 2, 2);
		Button btn3 = new Button("Opprett Event");
		grid.add(btn3, 3 , 2);
		Button btn4 = new Button("Logg ut");
		grid.add(btn4, 4, 2);
		Button btn5 = new Button("Vis alle event");
		grid.add(btn5, 5 , 2);
		Button btn6 = new Button("Vis kun skjulte");
		grid.add(btn6, 6, 2);
		Button btn7 = new Button("Vis normalt");
		grid.add(btn7, 7, 2);
		Button btn8 = new Button("<--");
		grid.add(btn8, 0, 2);

		btn0.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				Hovedmeny hm = new Hovedmeny(time.plusMonths(1),choice);
				hm.start(primaryStage);
			}
		});
		btn1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				RenderNotifications r = new RenderNotifications();
				r.start(primaryStage);
			}
		});

		btn2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				RenderGroups rg = new RenderGroups();
				rg.start(primaryStage);
			}
		});
		btn3.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				CreateEvent c = new CreateEvent(null);
				c.start(primaryStage);
			}
		});
		btn4.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				Login.me = null;
				Login l = new Login();
				l.start(primaryStage);
			}
		});
		btn5.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				choice = ALL;
				Hovedmeny hm = new Hovedmeny(time,choice);
				hm.start(primaryStage);
			}
		});
		btn6.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				choice = HIDDEN;
				Hovedmeny hm = new Hovedmeny(time,choice);
				hm.start(primaryStage);
			}
		});
		btn7.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				choice = VISIBLE;
				Hovedmeny hm = new Hovedmeny(time,choice);
				hm.start(primaryStage);
			}
		});
		btn8.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				Hovedmeny hm = new Hovedmeny(time.minusMonths(1),choice);
				hm.start(primaryStage);
			}
		});

		root.setTop(grid);
	}
	
	private void addEvent(final Event event, final Stage primaryStage){
		if (!dayList.containsKey(event.getStartReduced())) return;
		GridPane p = dayList.get(event.getStartReduced());
		Button btn = new Button(event.getName());
		p.add(btn, 0 , p.getChildren().size());
		btn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				RenderEvent re = new RenderEvent(event);
				re.start(primaryStage);
			}
		});
	}
	
	private void initEvents(final Stage primaryStage){
		List<Event> events;
		switch (choice){
		case ALL : events = Login.me.getAllEvents(Login.conn);
		break;
		case VISIBLE: events = Login.me.getVisibleEvents(Login.conn);
		break;
		case HIDDEN: events = Login.me.getHiddenEvents(Login.conn);
		break;
		default: throw new IllegalStateException();
		}
		for (Event event : events) {
			addEvent(event, primaryStage);
		}
		
	}
}