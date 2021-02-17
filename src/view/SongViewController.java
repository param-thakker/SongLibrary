package view;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;


public class SongViewController {
	@FXML         
	ListView<String> listView;             
	@FXML
	Button add;
	@FXML
	Button del;
	@FXML
	Button edit;
	@FXML
	Button save;
	@FXML
	Button saveAdd;
	@FXML
	Button cancelAdd;
	@FXML 
	Button cancelEdit;
	@FXML
	TextField nameDet;
	@FXML
	TextField artistDet;
	@FXML
	TextField albumDet;
	@FXML
	TextField yearDet;
	@FXML
	TextField rTitle;
	
	private ObservableList<String> obsList;  
	Map<String,List<String>> map=new HashMap<>();
	List<String> list=new ArrayList<>();
	

	public void start(Stage mainStage) throws IOException {                 
		
		
		File file = new File("songs.txt"); 
		Scanner sc = new Scanner(file); 
		    
		    while (sc.hasNextLine()) { 
		    	String item=sc.nextLine().trim();
		    	String[] content=item.split("\\|",4);
		        list.add(content[0].trim()+ "|" + content[1].trim());
		        map.put(content[0].trim()+ "|" + content[1].trim(), new ArrayList<>(Arrays.asList(content[2].trim(),content[3].trim())));
		    }
		
		Collections.sort(list,new sortSongs());
		
		obsList = FXCollections.observableArrayList(list); 

		listView.setItems(obsList); 

		// select the first item
		listView.getSelectionModel().select(0);
		songDetailV2(); //displays song deets without user having to interact

		// set listener for the items
		listView
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				songDetail(mainStage));

	}
	
	private void songDetail(Stage mainStage) { 
		if (!listView.getSelectionModel().isEmpty()) {
			String[] song=listView.getSelectionModel().getSelectedItem().split("\\|",3);
			List<String> songDets=map.get(listView.getSelectionModel().getSelectedItem());
			
			String album=songDets.get(0);
			String year=songDets.get(1);
			//display current song info in details pane
			nameDet.setText(song[0]);
			artistDet.setText(song[1]);
			albumDet.setText(album);
			yearDet.setText(year);
			
		}
	}
	private void songDetailV2() { 
		
		String[] song=listView.getSelectionModel().getSelectedItem().split("\\|",3);
		List<String> songDets=map.get(listView.getSelectionModel().getSelectedItem());
		
		String album=songDets.get(0);
		String year=songDets.get(1);
		//display current song info in details pane
		nameDet.setText(song[0]);
		artistDet.setText(song[1]);
		albumDet.setText(album);
		yearDet.setText(year);
	
	
}
	public void addSong(ActionEvent e) {
		rTitle.setText("Add Song:");
		//show relevant buttons
		nameDet.setEditable(true);
		artistDet.setEditable(true);
		albumDet.setEditable(true);
		yearDet.setEditable(true);
		nameDet.clear();
		artistDet.clear();
		albumDet.clear();
		yearDet.clear();
		saveAdd.setVisible(true);
		listView.setMouseTransparent(true);
		listView.setFocusTraversable(false);
		del.setDisable(true);
		edit.setDisable(true);
		cancelAdd.setVisible(true);
	}
	public void saveAdd(ActionEvent e) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "Save changes?\n Name"
															+ nameDet.getText() +
															"\nArtist: " + artistDet.getText() +
															"\nAlbum: " + albumDet.getText() +
															"\nYear: " + yearDet.getText(), 
															ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();
		//show the relevant buttons
		nameDet.setEditable(false);
		artistDet.setEditable(false);
		albumDet.setEditable(false);
		yearDet.setEditable(false);
		listView.setMouseTransparent(false);
		listView.setFocusTraversable(true);
		del.setDisable(false);
		edit.setDisable(false);
		
		
		//add new song using same implementation as beginning
		if (alert.getResult() == ButtonType.YES) {
			
			String errMsg = "";
			if (nameDet.getText().equals("") || artistDet.getText() == "") {
				if (nameDet.getText().equals("")) { errMsg+= "NAME cannot be empty!"; }
				if (artistDet.getText().equals("")) {errMsg+= "\nARTIST cannot be empty!";}
				Alert badAdd = new Alert(AlertType.ERROR);
				badAdd.setHeaderText(errMsg);
				badAdd.showAndWait();
				
			}else {
				String item = nameDet.getText() + " | " + artistDet.getText() + " | " + albumDet.getText() + " | " + yearDet.getText();
		    	String[] content=item.split("\\|",4);
		        list.add(content[0].trim()+ "|" + content[1].trim());
		        map.put(content[0].trim()+ "|" + content[1].trim(), new ArrayList<>(Arrays.asList(content[2].trim(),content[3].trim())));
		        
		        Collections.sort(list, new sortSongs());
		        obsList = FXCollections.observableArrayList(list); 
				listView.setItems(obsList); 
			}
		}
		cancelAdd.setVisible(false);
		saveAdd.setVisible(false);
		rTitle.setText("Song Details");
	}
	public void deleteSong(ActionEvent e) {
		if (listView.getSelectionModel().getSelectedItem() != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + listView.getSelectionModel().getSelectedItem() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
			alert.showAndWait();
			
			int currIndex = listView.getSelectionModel().getSelectedIndex();
			if (alert.getResult() == ButtonType.YES) {
				listView.getItems().remove(currIndex);
				list.remove(currIndex);
				//removing map remove atm because it causes weird bugs in songDetails for whatever reason but app seems to work fine without it
				//map.remove(listView.getSelectionModel().getSelectedItem()); 
				
			}
			
			if (!listView.getSelectionModel().isEmpty()) { //if not empty make a selection
				if (listView.getSelectionModel().getSelectedItem() != null) {
					listView.getSelectionModel().select(currIndex);
				}else {
					listView.getSelectionModel().select(currIndex-1);
				}
				songDetailV2(); //displays details of the next song without the user having to interact
			}else {
				nameDet.clear();
				artistDet.clear();
				albumDet.clear();
				yearDet.clear();
			}
			
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("No selected item to delete!");
			alert.showAndWait();

		}
	}
	public void editSong(ActionEvent e) {
		//show relevant buttons
		nameDet.setEditable(true);
		artistDet.setEditable(true);
		albumDet.setEditable(true);
		yearDet.setEditable(true);
		save.setVisible(true);
		listView.setMouseTransparent(true);
		listView.setFocusTraversable(false);
		del.setDisable(true);
		add.setDisable(true);
		cancelEdit.setVisible(true);
		System.out.println("Current Song details are: \n Name"
							+ nameDet.getText() +
							"\nArtist: " + artistDet.getText() +
							"\nAlbum: " + albumDet.getText() +
							"\nYear: " + yearDet.getText());
	}
	public void saveSong(ActionEvent e) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "Save changes?\nSong Name: "
															+ nameDet.getText() +
															"\nArtist: " + artistDet.getText() +
															"\nAlbum: " + albumDet.getText() +
															"\nYear: " + yearDet.getText(), 
															ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();
		//show relevant buttons
		nameDet.setEditable(false);
		artistDet.setEditable(false);
		albumDet.setEditable(false);
		yearDet.setEditable(false);
		listView.setMouseTransparent(false);
		listView.setFocusTraversable(true);
		del.setDisable(false);
		add.setDisable(false);
		if (alert.getResult() == ButtonType.YES) {
			
			String errMsg = "";
			if (nameDet.getText().equals("") || artistDet.getText() == "") {
				if (nameDet.getText().equals("")) { errMsg+= "NAME cannot be empty!"; }
				if (artistDet.getText().equals("")) {errMsg+= "\nARTIST cannot be empty!";}
				Alert badAdd = new Alert(AlertType.ERROR);
				badAdd.setHeaderText(errMsg);
				badAdd.showAndWait();
				
			}else {
				String[] song=listView.getSelectionModel().getSelectedItem().split("\\|",3);
				List<String> songDets=map.get(listView.getSelectionModel().getSelectedItem());
				int index = listView.getSelectionModel().getSelectedIndex();
				
				
				//there's an NullPointerException when you try to change the song name or artist, but not when you change the album or year
				//i think it has to do with the fact that its physically changing the keys 
				song[0] = nameDet.getText();
				System.out.println("new song name is " + song[0]);
				song[1] = artistDet.getText();
				System.out.println("new song artist is " + song[1]);
				songDets.set(0, albumDet.getText());
				System.out.println("new song album is " + songDets.get(0));
				songDets.set(1, yearDet.getText());
				System.out.println("new song year is " + songDets.get(1));
				
				obsList.set(index, song[0] + "|" + song[1]);
			}
			save.setVisible(false);
			cancelEdit.setVisible(false);
		}
		
	}
	public void cancel(ActionEvent e) {
		nameDet.setEditable(false);
		artistDet.setEditable(false);
		albumDet.setEditable(false);
		yearDet.setEditable(false);
		listView.setMouseTransparent(false);
		listView.setFocusTraversable(true);
		del.setDisable(false);
		add.setDisable(false);
		edit.setDisable(false);
		cancelAdd.setVisible(false);
		cancelEdit.setVisible(false);
		saveAdd.setVisible(false);
		save.setVisible(false);
		songDetailV2();
	}
	
	
	class sortSongs implements Comparator<String> 
	{ 
	    public int compare(String a, String b) 
	    { 

	       String[] aDivide=a.split("|",2);
	       String[] bDivide=b.split("|",2);

	       String firstSong=aDivide[0].trim();
	       String secondSong=bDivide[0].trim();
	       int cmp=firstSong.compareToIgnoreCase(secondSong);
	       if (cmp!=0) {
	    	   return cmp;
	       }
	       return aDivide[1].trim().compareToIgnoreCase(bDivide[1].trim());
	     
	       
	    } 
	} 
	  
}
