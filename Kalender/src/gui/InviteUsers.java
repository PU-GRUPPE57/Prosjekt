package gui;

import java.sql.Connection;
import java.time.LocalDate;

import event.Event;
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

public class InviteUsers extends Application{

	
	public InviteUsers(users.Group g){
		super();
		this.g=g;
	}
	public InviteUsers(Event e){
		super();
		this.event = e;
	}

	private ObservableList<User> users = FXCollections.observableArrayList();
	private TableView<User> table = new TableView();
	
	private Event event;
	private users.User selected;
	private users.Group g;
	
	@Override
	public void start(final Stage primaryStage) {
		primaryStage.setTitle("Kalender - Inviter Brukere - " + Login.me.getName());

		
		if (g!= null)users.addAll(User.getInviteList(Login.conn, g));
		else if (event!=null) users.addAll(User.getInviteList(Login.conn, event));
		if (users.size() != 0) selected = users.get(0);
		
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));		
		
		Scene scene = new Scene(new Group());
		
		Label label = new Label("Velg hvem du vil invitere:");
		
		table.setEditable(true);
		
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
		
 
        Button btn1 = new Button("Til hovedmeny");
		Button btn2 = new Button("Inviter bruker");
       	Button btn3 = new Button("Videre" );
       	grid.add(btn1, 0, 0);
       	grid.add(btn2, 2, 0);
       	grid.add(btn3, 4, 0);
       	
       	
       	VBox vbox = new VBox();
       	vbox.setSpacing(5);
       	vbox.setPadding(new Insets(10, 0, 0, 10));
       	vbox.getChildren().add(grid);
       	vbox.getChildren().addAll(label, table);
       	
       	((Group) scene.getRoot()).getChildren().addAll(vbox);
       	
    	btn1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				Hovedmeny hm = new Hovedmeny(LocalDate.now(),Hovedmeny.VISIBLE, Login.me);
				hm.start(primaryStage);
			}
		});

    	btn2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				InviteUsers i;
				if (g == null){
					selected.addEvent(Login.conn, event);
					i = new InviteUsers(event);
				}else{
					selected.addToGroup(Login.conn, g);
					i = new InviteUsers(g);
				}
				i.start(primaryStage);					
			}
		});
    	
    	btn3.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent e){
    			if (g == null){
    				RenderEvent cg = new RenderEvent(event);
        			cg.start(primaryStage);
        				
    			}else{
    				RenderGroup cg = new RenderGroup(g);
    				cg.start(primaryStage);    				
    			}
    			
    		}
    	});
        
    	
        
        primaryStage.setScene(scene);
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
	
	public User getSelected() {
		return selected;
	}
}