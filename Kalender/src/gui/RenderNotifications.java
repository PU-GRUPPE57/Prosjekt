package gui;

import java.time.LocalDate;

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

public class RenderNotifications extends Application{


	private ObservableList<Varsel> notifications = FXCollections.observableArrayList();
	private TableView<Varsel> table = new TableView();
	
	private Varsel selected;
	
	private Button btn1 = new Button("Tilbake");
   	private Button btn2 = new Button("Til gruppe");
   	private Button btn3 = new Button("Til avtale");
   	private GridPane grid = new GridPane();
	
	public RenderNotifications() {
	}
	
	@Override
	public void start(final Stage primaryStage) {
		notifications.addAll(Login.me.getNotifications(Login.conn));
		if (notifications.size()!=0) selected = notifications.get(0);
		primaryStage.setTitle("Kalender - Varsel - " + Login.me.getName());

		
		Scene scene = new Scene(new Group());

		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		Label label = new Label("Notifications");
		
		table.setEditable(true);
		
		Callback<TableColumn<Varsel, String>, TableCell<Varsel, String>> stringCellFactory =
				new Callback<TableColumn<Varsel, String>, TableCell<Varsel, String>>() {
			@Override
			public TableCell call(TableColumn p) {
				MyStringTableCell cell = new MyStringTableCell();
				cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new OnClick());
				return cell;
			}
		};
		
		TableColumn<Varsel, String> col1 = new TableColumn<Varsel, String>("Varseltext");
		col1.setCellValueFactory(new PropertyValueFactory<Varsel,String>("text"));
		col1.setCellFactory(stringCellFactory);
		
		
		table.setItems(notifications);
		table.getColumns().add(col1);
		
		VBox vbox = new VBox(20);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
        
		
       	grid.add(btn1, 0, 0);
       	
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        
        vbox.getChildren().add(grid);
 
        primaryStage.setScene(scene);
        primaryStage.show();
        
		btn1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				Hovedmeny hm = new Hovedmeny(LocalDate.now(),Hovedmeny.VISIBLE);
				hm.start(primaryStage);
			}
		});
		btn2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				Login.me.removeVarsel(Login.conn, selected);
				RenderGroup rg = new RenderGroup(selected.getGroup());
				rg.start(primaryStage);
				}
		});
		btn3.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){

			Login.me.removeVarsel(Login.conn, selected);
			RenderEvent re = new RenderEvent(selected.getEvent());
			re.start(primaryStage);
			}
		});
	}
	private class OnClick implements EventHandler<MouseEvent>{

		@Override
		public void handle(MouseEvent click) {
			TableCell t  = (TableCell) click.getSource();
			selected =  notifications.get(t.getIndex());
			grid.getChildren().clear();
			//EVENTVARSEL
	       	if (selected.getType()==1){
	       		grid.add(btn3, 2, 0);
	       	}
	       	//GRUPPEVARSEL
	       	else if (selected.getType() == 2){
	       		if (!selected.toString().contains("fjernet")){
	       			grid.add(btn2, 2, 0);       		       			
	       		}
	       	}
	       	//SVAR PA INVITASJONVARSEL
	       	else if (selected.getType() == 3){
	       		grid.add(btn3, 2, 0);
	       	}
	       	grid.add(btn1, 0, 0);
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
	
	public Varsel getSelected() {
		return selected;
	}
}