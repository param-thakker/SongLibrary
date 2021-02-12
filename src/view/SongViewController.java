package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SongViewController {
	@FXML         
	ListView<String> listView;                

	private ObservableList<String> obsList;              

	public void start(Stage mainStage) throws IOException {                
		// create an ObservableList 
		// from an ArrayList 
		List<String> list=new ArrayList<>();
		
		File file = new File("songs.txt"); 
		Scanner sc = new Scanner(file); 
		    
		    while (sc.hasNextLine()) { 
		    	String nextSong = sc.nextLine().trim();
		    	System.out.println("adding song " + nextSong);
		    	list.add(nextSong);
		    }
		
		Collections.sort(list,new sortSongs());
		
			  
		System.out.println(list);
		
		obsList = FXCollections.observableArrayList(list); 

		listView.setItems(obsList); 

		// select the first item
		listView.getSelectionModel().select(0);

		// set listener for the items
		listView
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				showItemInputDialog(mainStage));

	}
	
	private void songDetail(Stage mainStage) {                
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainStage);
		alert.setTitle("Song Details");
		alert.setHeaderText(
				"Selected list item properties");

		String content = "Index: " + listView.getSelectionModel().getSelectedIndex() + 
				"\nValue: " + listView.getSelectionModel().getSelectedItem();

		alert.setContentText(content);
		alert.showAndWait();
	}
	
	private void showItemInputDialog(Stage mainStage) {                
		String song = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();

		TextInputDialog dialog = new TextInputDialog(song);
		dialog.initOwner(mainStage); dialog.setTitle("List Item");
		dialog.setHeaderText("Selected Item (Index: " + index + ")");
		dialog.setContentText("Enter name: ");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) { obsList.set(index, result.get()); }
	}
	class sortSongs implements Comparator<String> 
	{ 
	    public int compare(String a, String b) 
	    { 
	       String[] aDivide=a.split("|",2);
	       String[] bDivide=b.split("|",2);
	       String firstSong=aDivide[0].toLowerCase();
	       String secondSong=bDivide[0].toLowerCase();
	       int cmp=firstSong.compareTo(secondSong);
	       if (cmp!=0) {
	    	   return cmp;
	       }
	       return aDivide[1].compareTo(bDivide[1]);
	       
	    } 
	} 
	  
}
