package Vue;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import Controleur.Controleur;
import Exceptions.NombreSaisiInterdit;
import Modele.ModeleFractale;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Vue extends Stage implements Observer {

	// Taille de nombre fenetre
	private static final int FENETRE_WIDTH = 1650;
	private static final int FENETRE_HEIGHT = 800;

	// Taille du menu a gauche
	private static final int MENU_WIDTH = 250;

	// Taille du rectangle pour la sphere
	private static final int CANVAS_X = MENU_WIDTH + 15;
	private static final int CANVAS_Y = 0;
	private static final int CANVAS_WIDTH = 1135;
	private static final int CANVAS_HEIGHT = FENETRE_HEIGHT;

	// Rayon de la sphere
	private static final double SPHERE_RAYON = CANVAS_HEIGHT / 2.5;

	// ATTRIBUTS

	private ModeleFractale modele;
	private Controleur controleur;
	private Pane root;
	private Scene scene;

	private VBox menuGauche;
	private VBox menuDroite;
	private Button zoomIn;
	private Button zoomOut;
	private Button saveImage;
	private Button restart;
	private Button rotate;

	private ToggleGroup changementFractale;
	private RadioButton mandelbrotSelection;
	private RadioButton juliaSelection;
	private RadioButton troisiemeSelection;
	private ToggleGroup changementVue;
	private RadioButton deuxDSelection;
	private RadioButton troisDSelection;
	private TextField nombreOccurence;
	private TextField entrerZ;
	private TextField entrerZi;
	private Label nombreZooms;
	private Slider sliderZoomSphere;
	
	private Label nombreOccurenceText;
	private Label nombreOccurenceErreur;
	private Label textSphere;
	private Label entrerZText;
	private Label entrerZTextErreur;
	private Label entrerZiText;
	private Label entrerZiTextErreur;
	
	private Separator sep ;
	private Separator sep2 ;

	private Sphere sphere;
	private Rectangle separateurGauche;
	private Rectangle separateurDroite;

	private WritableImage writableImage;
	private ImageView imageDeuxD;
	private PhongMaterial sphereMaterial;

	private double mouseX;
	private double mouseY;
	private double translateX;
	private double translateY;

	public Vue(Stage primaryStage, ModeleFractale modele, Controleur controleur) {
		try {
			this.modele = modele;
			this.controleur = controleur;
			this.start(primaryStage);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void start(Stage primaryStage) throws IOException {

		// On cr�e le menu de gauche
		menuGauche = new VBox(25);	
		menuGauche.setMinWidth(MENU_WIDTH + 10);
		menuGauche.setMinHeight(FENETRE_HEIGHT);
		menuGauche.setLayoutX(0);
		menuGauche.setLayoutY(0);
		menuGauche.setAlignment(Pos.CENTER);
		menuGauche.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
		menuGauche.isResizable();

		// On cr�e le menu de gauche
		menuDroite = new VBox(35);
		menuDroite.setMinWidth(MENU_WIDTH + 10);
		menuDroite.setMinHeight(FENETRE_HEIGHT);
		menuDroite.setLayoutX(1405);
		menuDroite.setLayoutY(0);
		menuDroite.setAlignment(Pos.CENTER);
		menuDroite.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
		menuDroite.isResizable();

		// Conteneur pour la barre � gauche et la maquette a droite
		root = new Pane();

		// Premier bouton
		zoomIn = new Button();
		zoomIn.setText("Zoomer");
		zoomIn.isResizable();
		zoomIn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				controleur.zoomIn();
			}
		});

		// Deuxieme bouton
		zoomOut = new Button();
		zoomOut.setText("Dezoomer");
		zoomOut.isResizable();
		zoomOut.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				controleur.zoomOut();
			}
		});
		
		saveImage = new Button();
		saveImage.setText("Screenshot");
		saveImage.isResizable();
		saveImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				if ( changementFractale.getSelectedToggle().isSelected() != false)
				{
				    File file = new File("src/Image/"+modele.getType().toString() + ".png");		    
				    try {
				        ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
				    } catch (IOException e) {
				        System.out.println("Erreur de sauvegarde ... ");
				    }
				}

			}
		});
		
		// Deuxieme bouton
		restart = new Button();
		restart.setText("Restart");
		restart.isResizable();
		restart.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				controleur.restart();
			}
		});

		// Cases a cocher pour chnager la fractale courante
		
		changementFractale = new ToggleGroup();

		mandelbrotSelection = new RadioButton();
		mandelbrotSelection.setText("Mandelbrot");
		mandelbrotSelection.setToggleGroup(changementFractale);
		mandelbrotSelection.isResizable();
		mandelbrotSelection.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				controleur.setType(ModeleFractale.Type.MANDELBROT);
				entrerZ.setVisible(false);
				entrerZText.setVisible(false);
				entrerZi.setVisible(false);
				entrerZiText.setVisible(false);
				sep.setVisible(false);
			}
		});

		juliaSelection = new RadioButton();
		juliaSelection.setText("Julia");
		juliaSelection.setToggleGroup(changementFractale);
		juliaSelection.isResizable();
		juliaSelection.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				controleur.setType(ModeleFractale.Type.JULIA);
				entrerZ.setVisible(true);
				entrerZText.setVisible(true);
				entrerZi.setVisible(true);
				entrerZiText.setVisible(true);
				sep.setVisible(true);
				
			}
		});

		troisiemeSelection = new RadioButton();
		troisiemeSelection.setText("Newton");
		troisiemeSelection.setToggleGroup(changementFractale);
		troisiemeSelection.isResizable();
		troisiemeSelection.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				controleur.setType(ModeleFractale.Type.NEWTON);
				entrerZ.setVisible(false);
				entrerZText.setVisible(false);
				entrerZi.setVisible(false);
				entrerZiText.setVisible(false);
				sep.setVisible(false);
			}

		});

		// Cases a cocher pour passer du mode 2D ou 3D sur la spère 
		
		changementVue = new ToggleGroup();

		deuxDSelection = new RadioButton();
		deuxDSelection.setText("Vue 2D");
		deuxDSelection.setToggleGroup(changementVue);
		deuxDSelection.isResizable();
		deuxDSelection.setSelected(true);
		deuxDSelection.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				sliderZoomSphere.setVisible(false);
				textSphere.setVisible(false);
				sep2.setVisible(false);
				sphere.setVisible(false);
				imageDeuxD.setVisible(true);
				rotate.setVisible(false);
				refreshImage();
			}
		});

		troisDSelection = new RadioButton();
		troisDSelection.setText("Vue 3D");
		troisDSelection.setToggleGroup(changementVue);
		troisDSelection.isResizable();
		troisDSelection.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				sliderZoomSphere.setVisible(true);
				textSphere.setVisible(true);
				sep2.setVisible(true);
				sphere.setVisible(true);
				imageDeuxD.setVisible(false);
				rotate.setVisible(true);
				refreshImage();
			}
		});


		// Input box pour le nombre d'itérations
		
		nombreOccurenceText = new Label("Nombre d'itération max:");
		nombreOccurenceErreur = new Label("Nombre inferieur à 0 ! !");
		nombreOccurenceErreur.setTextFill(Color.RED);
		nombreOccurenceErreur.setVisible(false);
		nombreOccurence = new TextField(Integer.toString(((ModeleFractale) modele).getIterationMax()));
		nombreOccurence.setMaxWidth(MENU_WIDTH - 20);
		nombreOccurence.isResizable();
		nombreOccurence.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				TextField textField = (TextField) t.getSource();
				String value = textField.getText();
				try {
					modele.setIterationMax(Integer.parseInt(value));
					nombreOccurenceErreur.setVisible(false);
				} catch (NumberFormatException | NombreSaisiInterdit e) {
					nombreOccurence.setText(Integer.toString(((ModeleFractale) modele).getIterationMax()));
					nombreOccurenceErreur.setVisible(true);
				}
			}
		});
		
		
		// Label pour changer le paramètre Z pour l'ensemble de Julia, afin d'avoir différentes fractales de l'ensemble
		
		entrerZText = new Label("Entrer Z:");
		entrerZText.setVisible(false);
		entrerZTextErreur = new Label("Nombre entre -1 et 1 demand�! ");
		entrerZTextErreur.setVisible(false);
		entrerZTextErreur.setTextFill(Color.RED);
		entrerZ = new TextField(Double.toString(((ModeleFractale) modele).getZ()));
		entrerZ.setMaxWidth(MENU_WIDTH - 20);
		entrerZ.isResizable();
		entrerZ.setVisible(false);
		entrerZ.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				TextField textField = (TextField) t.getSource();
				String value = textField.getText();
				try{
					modele.setZ(Double.parseDouble(value));
					entrerZTextErreur.setVisible(false);

				}catch (NumberFormatException | NombreSaisiInterdit e) {
					entrerZ.setText(Double.toString(((ModeleFractale) modele).getZ()));
					entrerZTextErreur.setVisible(true);
				}
			}
		});
		
		
		// Label pour changer le paramètre Zi pour l'ensemble de Julia, afin d'avoir différentes fractales de l'ensemble
		
		entrerZiText = new Label("Entrer Zi:");
		entrerZiText.setVisible(false);
		entrerZiTextErreur = new Label("Nombre entre -1 et 1 demand�! ");
		entrerZiTextErreur.setVisible(false);
		entrerZiTextErreur.setTextFill(Color.RED);
		entrerZi = new TextField(Double.toString(((ModeleFractale) modele).getZi()));
		entrerZi.setMaxWidth(MENU_WIDTH - 20);
		entrerZi.isResizable();
		entrerZi.setVisible(false);
		entrerZi.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				TextField textField = (TextField) t.getSource();
				String value = textField.getText();
				try {
					modele.setZi(Double.parseDouble(value));
					entrerZiTextErreur.setVisible(false);
				}
				catch (NumberFormatException | NombreSaisiInterdit e) {
					entrerZi.setText(Double.toString(((ModeleFractale) modele).getZi()));
					entrerZiTextErreur.setVisible(true);
				}

			}
		});
		
		
		//Slider pour changer la taille de la spère à l'écran
		textSphere = new Label("Zoom sur la sphere:");
		textSphere.setVisible(false);
		sliderZoomSphere = new Slider(-1000, 1000, 1);
		sliderZoomSphere.setShowTickLabels(true);
		sliderZoomSphere.setMajorTickUnit(1000);
		sliderZoomSphere.setMaxWidth(MENU_WIDTH - 35);
		sliderZoomSphere.setVisible(false);
		sliderZoomSphere.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				sphere.setTranslateZ(-sliderZoomSphere.getValue());
			}
		});
		
		
		
		// Bouton pour lancer la rotation de la spère, ou bien l'arrêter si elle toune déjà
		rotate = new Button();
		rotate.setText("Rotation");
		rotate.isResizable();
		rotate.setVisible(false);
		rotate.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
					
				rotateAroundYAxis(sphere).stop();
				if( rotate.getText() == "Rotation"){
					rotateAroundYAxis(sphere).play();
					rotate.setText("Stop rotation");
				} else{
					stopRotateAroundYAxis(sphere).play();
					rotate.setText("Rotation");
				}
			}
		});
		
		nombreZooms = new Label("Nombre de zooms: " + modele.getNombreZooms());
		nombreZooms.isResizable();

		sep = new Separator();
		sep.setVisible(false);
		
		// On ajoute les objets a notre menu de gauche
		menuGauche.getChildren().addAll(zoomIn, zoomOut, new Separator(), mandelbrotSelection, juliaSelection,
				troisiemeSelection, new Separator(), nombreOccurenceText, nombreOccurence,nombreOccurenceErreur,nombreZooms, new Separator(),sep,entrerZText,entrerZ,entrerZTextErreur,entrerZiText,entrerZi,entrerZiTextErreur);
		menuGauche.isResizable();
		
		
		sep2 = new Separator();
		sep2.setVisible(false);
		
		// On ajoute les objets a notre menu de droite
		menuDroite.getChildren().addAll(restart,new Separator(),saveImage,new Separator(),deuxDSelection, troisDSelection,sep, textSphere, sliderZoomSphere,rotate);
		menuDroite.isResizable();

		writableImage = new WritableImage(modele.getImageWidth(), modele.getImageHeight());
		imageDeuxD = new ImageView(writableImage);
		imageDeuxD.setVisible(true);
		imageDeuxD.setLayoutX(CANVAS_X);
		imageDeuxD.setLayoutY(CANVAS_Y);

		// On cree la sphere
		sphere = new Sphere(SPHERE_RAYON);
		sphere.setLayoutX(( CANVAS_WIDTH/2)- CANVAS_X -40 + CANVAS_WIDTH / 2);
		sphere.setLayoutY(CANVAS_HEIGHT / 2);
		sphere.setVisible(false);
		sphereMaterial = new PhongMaterial();
		addCanvasEvents(imageDeuxD);
		
		addCanvasEvents(sphere);

		// Ligne de séparation des deux scenes
		separateurGauche = new Rectangle(MENU_WIDTH + 10, CANVAS_Y, 5, FENETRE_HEIGHT);
		separateurGauche.setFill(Color.GREY);
		separateurGauche.isResizable();

		// Ligne de séparation des deux scenes
		separateurDroite = new Rectangle(1400, CANVAS_Y, 5, FENETRE_HEIGHT);
		separateurDroite.setFill(Color.GREY);
		separateurDroite.isResizable();

		// On ajoute le menu de gauche + separateur + maquette
		root.getChildren().addAll(sphere,imageDeuxD, menuGauche, separateurGauche, menuDroite, separateurDroite);
		root.isResizable();

		// On ajoute notre box a la scene
		scene = new Scene(root, FENETRE_WIDTH, FENETRE_HEIGHT, Color.WHITE);
		PerspectiveCamera camera = new PerspectiveCamera();

		scene.setCamera(camera);

		primaryStage.setTitle("Fractale -- Mirande &  -- S3C");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	// fonction pour ajouter tous les évenements à notre fractale
	public void addCanvasEvents(Node node) {
        
		
		// Les deux fontions suivantes servent à capture les coordonnées de la souris afin de pouvoir déplacer l'image en fonction de la distance parcourue
		node.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				mouseX = me.getSceneX();
				mouseY = me.getSceneY();
				translateX = (((Node) me.getSource())).getTranslateX();
				translateY = ((Node) (me.getSource())).getTranslateY();

			}
		});

		node.setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {

				double offsetX = me.getSceneX() - mouseX;
				double offsetY = me.getSceneY() - mouseY;
				double newTranslateX = translateX + offsetX;
				double newTranslateY = translateY + offsetY;

				//controleur.decalerCanvas(controleur.reelToImaginaireX(newTranslateX),
						//controleur.reelToImaginaireY(newTranslateY));
				controleur.decalerCanvas(newTranslateX, newTranslateY);


			}
		});

		// Cette fonction permet de zoomer à l'aide de la roulette de la souris
		node.setOnScroll((ScrollEvent event) -> {
			node.getCursor();
			double deltaY = event.getDeltaY();

			if (deltaY < 0) {
				controleur.zoomOut();
			} else {
				controleur.zoomIn();
			}
		});

	}

	// fonction pour lancer la rotation de la sphère 
	private RotateTransition rotateAroundYAxis(Node node) {
		RotateTransition rotate = new RotateTransition(Duration.seconds(30), node);
		rotate.setAxis(Rotate.Y_AXIS);
		rotate.setFromAngle(node.getRotate()+360);
		rotate.setToAngle(node.getRotate());
		rotate.setInterpolator(Interpolator.LINEAR);
		rotate.setCycleCount(RotateTransition.INDEFINITE);

		return rotate;
	}
	
	//fonction pour stopper la rotation de la sphère
	private RotateTransition stopRotateAroundYAxis(Node node) {
		RotateTransition rotate = new RotateTransition(Duration.seconds(30), node);
		rotate.setAxis(Rotate.Y_AXIS);
		rotate.setFromAngle(node.getRotate());
		rotate.setToAngle(node.getRotate());
		rotate.setInterpolator(Interpolator.LINEAR);
		rotate.setCycleCount(RotateTransition.INDEFINITE);

		return rotate;
	}

	// Fonction pour rafraichir l'image à l'écran
	public void refreshImage() {
		writableImage = controleur.paintImage(modele.getImageWidth(), modele.getImageHeight());

		if (deuxDSelection.isSelected()){
			imageDeuxD.setImage(writableImage);
		}
		else {
			
			//sphereMaterial.setSelfIlluminationMap(writableImage);
			sphereMaterial.setDiffuseMap(writableImage);
		//	sphereMaterial.diffuseColorProperty();
			//sphereMaterial.setBumpMap(writableIm1age);
			sphere.setMaterial(sphereMaterial);	
		}

	}

	@Override
	public void update(Observable modele, Object o) {

		refreshImage();
		nombreZooms.setText("Nombre de zooms: " + ((ModeleFractale) modele).getNombreZooms());
		nombreOccurence.setText(Integer.toString(((ModeleFractale) modele).getIterationMax()));
		entrerZ.setText(Double.toString(((ModeleFractale) modele).getZ()));
		entrerZi.setText(Double.toString(((ModeleFractale) modele).getZi()));

	}

}
