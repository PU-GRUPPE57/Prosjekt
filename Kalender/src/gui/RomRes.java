package gui;

import java.sql.Connection;
import java.time.LocalDate;

import event.Event;
import event.Room;
import users.Admin;
import users.User;
import notification.Varsel;
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
import javafx.stage.Stage;
import javafx.util.Callback;

public class RomRes extends Application{

	
	public RomRes(Event e){
		super();
		this.event = e;
	}

	private ObservableList<Room> room = FXCollections.observableArrayList();
	private TableView<Room> table = new TableView();
	
	private Event event;
	private Room selected;
	
	@Override
	public void start(final Stage primaryStage) {
		primaryStage.setTitle("Kalender - Inviter Brukere - " + Login.me.getName());

		
		if (event!=null) room.addAll(Room.getRooms(Login.conn, event.getStart(), event.getEnd()));
		if (room.size() != 0) selected = room.get(0);
		
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));		
		
		Scene scene = new Scene(new Group());
		
		Label label = new Label("Velg hvem du vil invitere:");
		
		table.setEditable(true);
		
		Callback<TableColumn<Room, String>, TableCell<Room, String>> stringCellFactory =
				new Callback<TableColumn<Room, String>, TableCell<Room, String>>() {
			@Override
			public TableCell call(TableColumn p) {
				MyStringTableCell cell = new MyStringTableCell();
				cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new OnClick());
				return cell;
			}
		};
		
		TableColumn<Room, String> col1 = new TableColumn<Room, String>("RomNavn");
		col1.setCellValueFactory(new PropertyValueFactory<Room,String>("name"));
		TableColumn<Room, String> col2 = new TableColumn<Room, String>("Romtype");
		col2.setCellValueFactory(new PropertyValueFactory<Room,String>("type"));
		TableColumn<Room, String> col3 = new TableColumn<Room, String>("Kapasitet");
		col3.setCellValueFactory(new PropertyValueFactory<Room,String>("cap"));
		col1.setCellFactory(stringCellFactory);
		
		table.setItems(room);
		table.getColumns().add(col1);
		table.getColumns().add(col2);
		table.getColumns().add(col3);
		
 
        Button btn1 = new Button("Reserver");
		Button btn2 = new Button("Tilbake");
		Button btn3 = new Button("Endre Rom");
		
		if ((room.size() !=0)) grid.add(btn1, 0, 0);
       	grid.add(btn2, 2, 0);
       	
       	VBox vbox = new VBox();
       	vbox.setSpacing(5);
       	vbox.setPadding(new Insets(10, 0, 0, 10));
       	vbox.getChildren().add(grid);
       	vbox.getChildren().addAll(label, table);
       	
       	((Group) scene.getRoot()).getChildren().addAll(vbox);
       	
    	btn1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				selected.romRes(Login.conn, event);
				RenderEvent re = new RenderEvent(event);
				re.start(primaryStage);
			}
		});

    	btn2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				RenderEvent re = new RenderEvent(event);
				re.start(primaryStage);
			}
		});
    	
    	btn3.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent e){
    			RenderEvent re = new RenderEvent(event);
    			re.start(primaryStage);
    		}
    	});
    	
        
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	private class OnClick implements EventHandler<MouseEvent>{

		@Override
		public void handle(MouseEvent click) {
			TableCell t  = (TableCell) click.getSource();
			selected =  room.get(t.getIndex());
			
			
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
	
	public Room getSelected() {
		return selected;
	}
}