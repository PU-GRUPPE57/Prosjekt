package gui;
import java.time.LocalDate;

import event.Event;
import users.User;
import users.UserEventModel;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;


public class RenderEvent extends Application{
	private Event event;
	private ObservableList<UserEventModel> users = FXCollections.observableArrayList();
	private TableView<UserEventModel> table = new TableView<UserEventModel>();
	private User selected;
	private UserEventModel relation;

	public RenderEvent(Event e){
		super();
		this.event = e;
		relation  = UserEventModel.getUserEventModel(Login.conn, Login.me, event);
	}

	public void start(final Stage primaryStage){
		primaryStage.setTitle("Kalender - Event - " + event.getName()  + " - " + Login.me.getName());

		users.addAll(UserEventModel.getUserEventModels(Login.conn, event));
		if (users.size()!=0) selected = users.get(0).getUser();
		Scene scene = new Scene(new javafx.scene.Group());
		
		Label label = new Label("Brukere status");

		table.setEditable(true);


		GridPane maingrid = new GridPane();
		maingrid.setAlignment(Pos.CENTER);
		maingrid.setHgap(10);
		maingrid.setVgap(10);
		maingrid.setPadding(new Insets(25, 25, 25, 25));
		
		GridPane buttongrid = new GridPane();
		buttongrid.setAlignment(Pos.CENTER);
		buttongrid.setHgap(10);
		buttongrid.setVgap(10);
		buttongrid.setPadding(new Insets(25, 25, 25, 25));
		
		
		Button btn1 = new Button("Inviter Brukere");
		Button btn2 = new Button("Til hovedmeny");
		Button btn3 = new Button("Reserver Rom");
		Button btn4 = new Button("Jeg deltar");
		Button btn5 = new Button("Jeg deltar ikke");
		Button btn6 = new Button("Slett avtale");
		Button btn8 = new Button("Endre tidspunkt");
		String btn7txt = (relation.getVisibility() == UserEventModel.VISIBLE) ? "Skjul avtale" : "Fjern avtale fra skjulte";
		Button btn7 = new Button(btn7txt);
		Button btn9 = new Button("Endre Rom");

		//Sjekk for at kun admin kan legge til bruker og opprette event:
		if (Login.me.getId() == event.getOwner().getId()){
			buttongrid.add(btn1, 1, 2);
			buttongrid.add(btn6,1,4);
			buttongrid.add(btn8, 4,4);
			if (event.getRoom(Login.conn) == null) buttongrid.add(btn3, 2,2);
			else buttongrid.add(btn9, 2, 2);

		}
		buttongrid.add(btn2, 0 , 2);
		buttongrid.add(btn4, 0 , 4);
		buttongrid.add(btn5, 2 , 4);
		buttongrid.add(btn7,3,4);

		maingrid.add(buttongrid, 0, 1);
		
		GridPane infogrid = new GridPane();
		infogrid.setAlignment(Pos.CENTER);
		infogrid.setHgap(10);
		infogrid.setVgap(10);
		infogrid.setPadding(new Insets(25, 25, 25, 25));
		
		Label owner = new Label("Avtale administrator: ");
		Text own = new Text(event.getOwner().getUsername());
		Label pri = new Label("Prioritet: ");
		Text p = new Text(String.valueOf(event.getPriority()));
		Label start = new Label("Starttid: ");
		Text stid = new Text(event.getStart().toString());		
		Label slutt = new Label("Sluttid: ");
		Text sltid = new Text(event.getEnd().toString());
		Label l = new Label("Sted: ");
		String stedtxt = "Sted ikke angitt";
		if (event.getRoom(Login.conn) != null) stedtxt = event.getRoom(Login.conn).toString();
		Text sted = new Text(stedtxt);
		
		infogrid.add(owner, 0, 0);
		infogrid.add(own, 1, 0);
		infogrid.add(pri, 0, 1);
		infogrid.add(p, 1, 1);
		infogrid.add(start, 0, 2);
		infogrid.add(stid, 1, 2);
		infogrid.add(slutt, 0, 3);
		infogrid.add(sltid, 1, 3);		
		infogrid.add(l, 0, 4);
		infogrid.add(sted, 1, 4);
		
		
		maingrid.add(infogrid, 1, 0);
		
		Callback<TableColumn<UserEventModel, String>, TableCell<UserEventModel, String>> stringCellFactory =
				new Callback<TableColumn<UserEventModel, String>, TableCell<UserEventModel, String>>() {
			@Override
			public TableCell call(TableColumn p) {
				MyStringTableCell cell = new MyStringTableCell();
				cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new OnClick());
				return cell;
			}
		};

		TableColumn<UserEventModel, String> col1 = new TableColumn<UserEventModel, String>("Brukernavn");
		col1.setCellValueFactory(new PropertyValueFactory<UserEventModel,String>("user"));
		TableColumn<UserEventModel, String> col2 = new TableColumn<UserEventModel, String>("Status");
		col2.setCellValueFactory(new PropertyValueFactory<UserEventModel,String>("status"));
		col1.setCellFactory(stringCellFactory);

		table.setItems(users);
		table.getColumns().add(col1);
		table.getColumns().add(col2);

		VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.getChildren().add(maingrid);
		vbox.getChildren().addAll(label, table);

		((javafx.scene.Group) scene.getRoot()).getChildren().addAll(vbox);


		primaryStage.setScene(scene);
		Text scenetitle = new Text("Event " + event.getName());
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		maingrid.add(scenetitle, 0, 0, 2, 1);

		btn1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				InviteUsers i = new InviteUsers(event);
				i.start(primaryStage);
			}
		});


		btn3.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				RomRes rr = new RomRes(event);
				rr.start(primaryStage);
			}
		});
		btn2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				Hovedmeny hm = new Hovedmeny(LocalDate.now(),Hovedmeny.VISIBLE, Login.me);
				hm.start(primaryStage);
			}
		});
		btn4.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){

				event.replyToInvitation(Login.conn, Login.me, 1);
				RenderEvent re = new RenderEvent(event);
				re.start(primaryStage);
			}
		});
		btn5.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				event.replyToInvitation(Login.conn, Login.me, 2);
				RenderEvent re = new RenderEvent(event);
				re.start(primaryStage);
			}
		});
		btn6.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				event.deleteEvent(Login.conn);
				Hovedmeny hm = new Hovedmeny(LocalDate.now(),Hovedmeny.VISIBLE, Login.me);
				hm.start(primaryStage);
			}
		});
		btn7.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				if (relation.getVisibility() == UserEventModel.VISIBLE){
					Login.me.hideEvent(Login.conn, event);
				}
				else {
					Login.me.unhideEvent(Login.conn, event);
				}
				RenderEvent re = new RenderEvent(event);
				re.start(primaryStage);
			}
		});
		primaryStage.show();
		btn8.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
			ChangeTime c = new ChangeTime(event);
			c.start(primaryStage);
			}
		});
		primaryStage.show();
		btn9.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				RomRes rr = new RomRes(event);
				rr.start(primaryStage);
			}
		});
		primaryStage.show();
	}

	private class OnClick implements EventHandler<MouseEvent>{

		@Override
		public void handle(MouseEvent click) {
			TableCell t  = (TableCell) click.getSource();
			selected =  users.get(t.getIndex()).getUser();


		}
	}
	class MyStringTableCell extends TableCell<Group, String> {

		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			setText(empty ? null : getString());
			setGraphic(null);
		}

		private String getString() {
			return getItem() == null ? "" : getItem().toString();
		}
	}
}