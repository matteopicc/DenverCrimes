/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	if(this.model.grafoCreato()== false) {
    		txtResult.appendText("Creare il grafo prima di richiedere questa funzione\n");
    		return;
    	}
    	Adiacenza a = boxArco.getValue();
    	if(a == null) {
    		txtResult.appendText("Selezionare un arco prima di richiedere questo comando\n");
    		return;
    	}
    	txtResult.appendText("Percorso migliore: \n");
    	for(String b : this.model.calcolaPercorso(a.getV1(), a.getV2())) {
    		txtResult.appendText(b+" \n");
    	}

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	int mese;
    	String reato = boxCategoria.getValue();
    	try {
    		mese = boxMese.getValue();
    		if(reato == null) {
    			txtResult.appendText("Mese inserito. Inserire la categoria di reato da anlizzare tramite il menu a tenidna\n");
    			return;
    		}
    		
    		this.model.creaGrafo(reato, mese);
    		txtResult.appendText("GRAFO CREATO!!\n");
    		txtResult.appendText("#VERTICI: "+this.model.nVertici()+"\n");
    		txtResult.appendText("#ARCHI: "+this.model.nArchi()+"\n");
    		boxArco.getItems().addAll(this.model.getArchi());
    		
    	}catch(NullPointerException e) {
    		if(reato == null ) {
    			txtResult.appendText("Inserire entrambi i parametri dei menu a tendina per avere il grafo\n");
    			return;
    		}
    		if(reato != null ) {
    			txtResult.appendText("Reato inserito. Inserire il mese da anlizzare tramite il menu mese\n");
    			return;
    		}
    	}
    		
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxCategoria.getItems().addAll(this.model.getReati());
    	boxMese.getItems().addAll(this.model.getMesi());
    }
}
