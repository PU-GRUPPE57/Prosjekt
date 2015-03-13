package gui;
import java.time.LocalDate;

import users.Group;
import users.User;
import users.UserEventModel;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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


public class RenderGroup extends Application{

	private users.Group g;
	private ObservableList<User> users = FXCollections.observableArrayList();
	private TableView<User> table = new TableView<User>();
	private User selected;
	
	public RenderGroup(users.Group g){
		super();
		this.g = g;
	}
	
	public void start(final Stage primaryStage){
		primaryStage.setTitle("Kalender - Gruppe - " + g.getName() + " - " + Login.me.getName());

		users.addAll(g.getUsers(Login.conn));
		if(users.size() != 0) selected = users.get(0);

		Scene scene = new Scene(new javafx.scene.Group());

		Label label = new Label("Brukere i gruppen");

		table.setEditable(true);


		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Button btn2 = new Button("Inviter Brukere");
		Button btn3 = new Button("Opprett event");
		Button btn4 = new Button("Til hovedmeny");
		Button btn5 = new Button("Slett gruppe");
		Button btn6 = new Button("Slett bruker");
		
		//Sjekk for at kun admin kan legge til bruker og opprette event:
		if (Login.me.getId() == g.getAdmin().getId()){
			grid.add(btn2, 0, 2);
			grid.add(btn3, 1, 2);
			grid.add(btn5, 3 , 2);
			grid.add(btn6, 4,2);
		}
		grid.add(btn4, 2 , 2);

		Callback<TableColumn<User, String>, TableCell<User, String>> stringCellFactory =
				new Callback<TableColumn<User, String>, TableCell<User, String>>() {
			@Override
			public TableCell call(TableColumn p) {
				MyStringTableCell cell = new MyStringTableCell();
				cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new OnClick());
				return cell;
			}
		};
		
		
		TableColumn<User, String> col1 = new TableColumn<User, String>("Brukernavn");
		col1.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
		col1.setCellFactory(stringCellFactory);
		
		
		table.setItems(users);
		table.getColumns().add(col1);

		VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.getChildren().add(grid);
		vbox.getChildren().addAll(label, table);

		((javafx.scene.Group) scene.getRoot()).getChildren().addAll(vbox);
		
		
		primaryStage.setScene(scene);
		Text scenetitle = new Text("Gruppe " + g.getName());
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		btn2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				InviteUsers i = new InviteUsers(g);
				i.start(primaryStage);
			}
		});


		btn3.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				CreateEvent c = new CreateEvent(g);
				c.start(primaryStage);
			}
		});
		btn4.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				Hovedmeny hm = new Hovedmeny(LocalDate.now());
				hm.start(primaryStage);
			}
		});
		btn5.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				g.deleteGroup(Login.conn);
				Hovedmeny hm = new Hovedmeny(LocalDate.now());hm.start(primaryStage);
				hm.start(primaryStage);
			}
		});
		btn6.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				selected.removeGroup(Login.conn, g, Login.me);
				RenderGroup re = new RenderGroup(g);
				re.start(primaryStage);
			}
		});
		primaryStage.show();
	}
	
	private class OnClick implements EventHandler<MouseEvent>{

		@Override
		public void handle(MouseEvent click) {
			TableCell t  = (TableCell) click.getSource();
			selected =  users.get(t.getIndex());
			
			
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