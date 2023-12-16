package Controller;


import Utils.Coordinate;
import View.VueHunter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;




public class ControlHunterPlayer implements ControlHunter {
    public VueHunter view;
    public Coordinate clickedCase;

    public ControlHunterPlayer(VueHunter view) {
        this.view = view;
    }
    /**
     * Gère le mouvement du chasseur dans le jeu en réponse aux clics de souris sur la grille.
     * Lorsque le joueur clique sur le Label de la grille, cette méthode est appelée
     * pour gérer le déplacement du chasseur.
     *
     * @implNote Cette méthode récupère la grille à partir de la vue et configure un gestionnaire
     *           d'événements pour les clics de souris sur la grille. Si le joueur clique sur une étiquette,
     *           le chasseur peut se déplacer en fonction de certaines conditions (notamment, l'état "canMoove").
     *           Si le chasseur réussit à se déplacer vers une nouvelle position, la carte du chasseur est mise
     *           à jour pour refléter le tir et la victoire. De plus, les états "canMoove" du chasseur et du
     *           monstre sont modifiés en conséquence.
     *
     * @implNote Cette méthode est liée à un jeu ou à une simulation où le chasseur peut tirer sur la grille
     *           pour éliminer le monstre. La victoire est déterminée en fonction de la position du chasseur.
     */
    public void hMouvement() {
        refresh();
        view.getGridPane().setOnMouseClicked(this::handleMouseClick);
    }

    private void handleMouseClick(MouseEvent event) {
        Node source = event.getPickResult().getIntersectedNode();
        StackPane clickedStackPane = findClickedStackPane(source);

        if (clickedStackPane != null) {
            int clickedRow = GridPane.getRowIndex(clickedStackPane);
            int clickedCol = GridPane.getColumnIndex(clickedStackPane);

            this.clickedCase = new Coordinate(clickedRow, clickedCol);

            if (view.getHunter().getGameModel().currentPlayer == 2) {
                handleHunterTurn(clickedRow, clickedCol);
            }
        }
    }

    private StackPane findClickedStackPane(Node source) {
        if (source instanceof ImageView) {
            return (StackPane) source.getParent();
        } else if (source instanceof StackPane) {
            return (StackPane) source;
        }
        return null;
    }

    private void handleHunterTurn(int clickedRow, int clickedCol) {
        Model.Hunter hunter = view.getHunter();
        Model.GameModel gameModel = hunter.getGameModel();

        hunter.shoot(clickedRow, clickedCol);
        view.updatePlateau();

        if (hunter.victory(clickedRow, clickedCol)) {
            gameModel.currentPlayer = 3;
            view.showVictoryMessage();
        } else {
            gameModel.changeCurrentPlayer();
        }

        updateHunterPosition(clickedRow, clickedCol);
    }

    private void updateHunterPosition(int row, int col) {
        view.getHunter().getHunted().setCol(col);
        view.getHunter().getHunted().setRow(row);
    }
    
    public Coordinate getClickedCase() {
        return clickedCase;
    }


    public void refresh(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        if (view.getHunter().getGameModel().currentPlayer==1) {
            view.getCurrentLabel().setText("C'est au tour du Monstre");
        }else{
            view.getCurrentLabel().setText("C'est au tour du Chasseur");
        }
        }));
        // Configure la répétition indéfinie de la timeline, ce qui signifie que le rafraîchissement continuera indéfiniment.
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    
}



