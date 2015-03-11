package gui;

import java.sql.Connection;

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

public class RenderGroups extends Application{


	private ObservableList<users.Group> groups = FXCollections.observableArrayList();
	private TableView<users.Group> table = new TableView();
	
	private users.Group selected;
	
	public RenderGroups() {
	}
	
	@Override
	public void start(final Stage primaryStage) {
		groups.addAll(Login.me.getGroups(Login.conn));
		if (groups.size() != 0) selected = groups.get(0);
		
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));		
		
		Scene scene = new Scene(new Group());
		
		Label label = new Label("Grupper du er medlem i:");
		
		table.setEditable(true);
		
		Callback<TableColumn<users.Group, String>, TableCell<users.Group, String>> stringCellFactory =
				new Callback<TableColumn<users.Group, String>, TableCell<users.Group, String>>() {
			@Override
			public TableCell call(TableColumn p) {
				MyStringTableCell cell = new MyStringTableCell();
				cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new OnClick());
				return cell;
			}
		};
		
		TableColumn<users.Group, String> col1 = new TableColumn<users.Group, String>("Gruppenavn");
		col1.setCellValueFactory(new PropertyValueFactory<users.Group,String>("name"));
		col1.setCellFactory(stringCellFactory);
		
		table.setItems(groups);
		table.getColumns().add(col1);
		
 
        Button btn1 = new Button("Tilbake");
		Button btn2 = new Button("Se detaljer");
       	Button btn3 = new Button("Opprett gruppe");
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
				Hovedmeny hm = new Hovedmeny();
				hm.start(primaryStage);
			}
		});

    	btn2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				RenderGroup rg = new RenderGroup();
				rg.init(selected);
				rg.start(primaryStage);
			}
		});
    	
    	btn3.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent e){
    			CreateGroup cg = new CreateGroup();
    			cg.start(primaryStage);
    			
    		}
    	});
        
    	
    	
//    	users.Group person = (users.Group)table.getSelectionModel().getSelectedItem();
//    	System.out.println(person);
        
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	private class OnClick implements EventHandler<MouseEvent>{

		@Override
		public void handle(MouseEvent click) {
			TableCell t  = (TableCell) click.getSource();
			selected =  groups.get(t.getIndex());
			
			
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
	
	public users.Group getSelected() {
		return selected;
	}
}