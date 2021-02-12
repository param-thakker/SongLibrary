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
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SongViewController {
	@FXML         
	ListView<String> listView;                

	private ObservableList<String> obsList;  
	Map<String,List<String>> map=new HashMap<>();
	

	public void start(Stage mainStage) throws IOException {                
		// create an ObservableList 
		// from an ArrayList 
		List<String> list=new ArrayList<>();
		
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

		// set listener for the items
		listView
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				songDetail(mainStage));

	}
	
	private void songDetail(Stage mainStage) {                
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainStage);
		alert.setTitle("Song List");
		alert.setHeaderText(
				"Selected song details");
		String[] song=listView.getSelectionModel().getSelectedItem().split("\\|",3);
		List<String> songDets=map.get(listView.getSelectionModel().getSelectedItem());
		String album=songDets.get(0);
		String year=songDets.get(1);
				
		
		String content = "Name: " + song[0] + "\nArtist: " + song[1] + "\nAlbum: " + album + "\nYear: " + year;

		alert.setContentText(content);
		alert.showAndWait();
	}
	
	private void showItemInputDialog(Stage mainStage) {                
		String item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();

		TextInputDialog dialog = new TextInputDialog(item);
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
	       String[] aDivide=a.split("|",4);
	       String[] bDivide=b.split("|",4);
	       String firstSong=aDivide[0].toLowerCase();
	       String secondSong=bDivide[0].toLowerCase();
	       int cmp=firstSong.compareTo(secondSong);
	       if (cmp!=0) {
	    	   return cmp;
	       }
	       return aDivide[1].toLowerCase().compareTo(bDivide[1].toLowerCase());
	       
	    } 
	} 
	  
}
