package Menu;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Model.GameModel;
import Model.Hunter;
import Model.Monster;
import View.VueHunter;
import View.VueMonster;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MenuControlleur implements Initializable {
    Stage stage;
    Parent root;
    Scene scene;

    @FXML
    TextField taille;

    @FXML
    TextField pourcentage;

    @FXML
    ComboBox<String> comboBox;

    @FXML
    ComboBox<String> comboBox2;

    String[] players = {"bot","joueur"};

    public static String stringTaillePlateau;
    public static String stringProbaWall;
    


    public void option(ActionEvent e) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("option.fxml"));
        stage=(Stage)((Node) e.getSource()).getScene().getWindow();
        scene= new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void retour(ActionEvent e) throws IOException {
        stringTaillePlateau = taille.getText();
        stringProbaWall= pourcentage.getText();   
        Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    

    public void quitter(ActionEvent e) throws IOException{
        stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }

    public void jouer(ActionEvent e) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("PreGame.fxml"));
        stage=(Stage)((Node) e.getSource()).getScene().getWindow();
        scene= new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void play(ActionEvent e) throws IOException {
        stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        stage.close();
        
        int size=10;
       
        if(stringTaillePlateau!=null){
            size=Integer.parseInt(stringTaillePlateau);
        }
        int probaWall=20;

        if(stringProbaWall!=null){
            probaWall=Integer.parseInt(stringProbaWall);
        }
        //Création des objets
        GameModel gameModel = new GameModel(null, null, size,probaWall);
        // Ensuite, utilisez ce GameModel pour créer un Monster
        Monster monster = new Monster("STYLESHEET_CASPIAN", gameModel);

        // Puis, utilisez le GameModel et le Monster pour créer un Hunter
        Hunter hunter = new Hunter("STYLESHEET_CASPIAN", gameModel);

        // Enfin, utilisez le Monster et le Hunter pour mettre à jour le GameModel si nécessaire
        gameModel.setMonster(monster);
        gameModel.setHunter(hunter);

        boolean controlHunter = comboBox2.getValue().equals("bot");
        boolean controlMonster = comboBox.getValue().equals("bot");
        


        VueHunter hunterView = new VueHunter(hunter,controlHunter);
        VueMonster monsterView = new VueMonster(monster,hunter,controlMonster);

        //Création des deux stages joueurs
        Stage hunterStage= hunterView.creerStage();
        Stage monsterStage = monsterView.creerStage();

       

        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        
        // Divisez l'écran en deux en ajustant les dimensions et les positions des stages
            //Screen 1
        hunterStage.setX(0);
        hunterStage.setY(0);
        hunterStage.setWidth(screenWidth / 2);
        hunterStage.setHeight(screenHeight);
        //Screen 2
        monsterStage.setX(screenWidth / 2);
        monsterStage.setY(0);
        monsterStage.setWidth(screenWidth / 2);
        monsterStage.setHeight(screenHeight);
        // Déplacez le deuxième stage à droite du premier
        monsterStage.setX(hunterStage.getX() + hunterStage.getWidth());
        monsterStage.setY(hunterStage.getY());
        hunterStage.sizeToScene();
        monsterStage.sizeToScene();
        hunterStage.show();
        monsterStage.show();
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (comboBox != null) {
            comboBox.getItems().addAll(players);
        } else {
            System.out.println("La ComboBox est null");
        }
        if (comboBox2 != null) {
            comboBox2.getItems().addAll(players);
        } else {
            System.out.println("La ComboBox est null");
        }
    }
}