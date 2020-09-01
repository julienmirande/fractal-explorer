package MainPackage;


import Controleur.Controleur;
import Modele.ModeleFractale;
import Vue.Vue;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		ModeleFractale modele = new ModeleFractale();
		Controleur controleur = new Controleur(modele);
		Vue vue = new Vue(primaryStage, modele, controleur);
		modele.addObserver(vue);

	}

	public static void main(String[] args) {
		launch(args);
	}

}
