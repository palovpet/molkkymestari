package molkkymestari.ui;

import java.io.IOException;
import java.util.Random;
import molkkymestari.logic.MolkkyService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import com.vdurmont.emoji.EmojiParser; 

/**
 * Class provides methods for the GUI of Mölkkymestari, which allows the end user
 * to edit and view game settings, players, and document the throws. Class has
 * five Scenes that each provide different functionalities. 
 */
public class MolkkyApplication extends Application{
    private MolkkyService service;
    private Scene startScene;
    private Scene addPlayersScene;
    private Scene gameSettingsScene;
    private Scene gameScene;
    private Scene winnerScene;  
    
    public static void main(String[] args) {  
        launch(MolkkyApplication.class);
    }
    
    /**
     * Method makes the needed preparations for the launching of the GUI
     * @throws Exception 
     */
    @Override
    public void init() throws Exception {
        service = new MolkkyService();       
    }
    
    /**
     * Method creates the visual GUI of the application
     * @param window
     * @throws IOException 
     */
    @Override
    public void start(Stage window) throws IOException { 
        String buttonColourLight = "#DDAADD; ";
        String buttonColourDark = "#D76FB0; ";
        String textFieldColour = "#ECB8F2; ";
        
        // ELEMENTIT JA ASETTELU
        // 1: AddPlayers-näkymän toimintoelementit    
        Label addedPlayers = new Label();
        addedPlayers.setText(service.getPlayersToPrint());
        addedPlayers.setMinSize(200, 200);
        
        TextField addPlayersField = new TextField();
        addPlayersField.setStyle("-fx-background-color: "+ textFieldColour);
        addPlayersField.setMinSize(150, 50);
        addPlayersField.setMaxSize(300, 100);
        
        Button savePlayer = createButtonWithColour("Tallenna pelaaja", buttonColourLight); 
        
        Button toSettingsButton = createButtonWithColour("Peliasetuksiin", buttonColourDark);
  
        // 1: AddPlayers-näkymän asettelu        
        VBox addPlayersLayout = createAddPlayersLayout(window, addedPlayers, 
                addPlayersField, savePlayer, toSettingsButton);
        addPlayersScene= new Scene(addPlayersLayout);
        
        // 2: GameSettings-näkymän toimintoelementit         
        Button pointLimitButton = 
                createButtonWithColour("Pisteraja: " + service.getPointLimit(), buttonColourLight); 
        
        Button pointLimitBrokenButton = 
                createButtonWithColour("Pisteet puolitetaan pisterajan ylittyessä", buttonColourLight);
        
        Button startGameButton = 
                createButtonWithColour("Aloita peli", buttonColourDark);
        
        Button addMorePlayersButton = 
                createButtonWithColour("Palaa lisäämään pelaajia", buttonColourDark);
        
        // 2: GameSettings-näkymän asettelu     
        VBox gameSettingsLayout = createGameSettingsLayout(window, pointLimitButton, 
                pointLimitBrokenButton, startGameButton, addMorePlayersButton);       
        gameSettingsScene = new Scene(gameSettingsLayout);   
        
        // 3: Game-näkymän toimintoelementit     
        Label whosTurn = new Label(" ");
        
        Label whosTurnPoints = new Label("0");
        
        Label whosTurnNext = new Label(" ");
        
        TextField pointsToDocument = new TextField();
        pointsToDocument.setStyle("-fx-background-color: " + textFieldColour);
        
        Button documentButton = createButtonWithColour("Kirjaa", buttonColourLight);
        
        Label commentLabel = new Label();
        
        Button closeAndStartNewGame = createButtonWithColour("Lopeta peli ja palaa alkuun", buttonColourDark); 
        
        // 3: Game-näkymän asettelu
        VBox gameLayout = createGameLayout(window, whosTurn, whosTurnPoints, 
                whosTurnNext, pointsToDocument, documentButton, commentLabel, closeAndStartNewGame);
        gameScene = new Scene(gameLayout);    
        
        //ELEMENTTIEN TOIMINNOT        
        // 1: AddPlayers-näkymän painikkeiden toiminnot
        toSettingsButton.setOnAction((event) -> {
            addPlayersField.clear();
            window.setScene(gameSettingsScene);
        });       
        
        savePlayer.setOnAction((event) -> {
            if (service.getHowManyPlayers() >= 10 ) {
                addPlayersField.setText("Pelissä on jo 10 pelaajaa.");
                return;
            }
              
            String name = addPlayersField.getText();
            if(!(name.matches("[a-öA-Ö0-9]{3,20}"))) {
                addPlayersField.setText("Anna kunnollinen nimi!");
                return;
            }
                  
            if (service.checkIfNameIsAlreadyAdded(name).equals(true)) {
                addPlayersField.setText("Nimimerkki on jo käytetty");
                return;
            } 
                
            service.addNewPlayer(name);
            addPlayersField.clear();
            addedPlayers.setText(service.getPlayersToPrint());                    
        });
        
        //2: GameSettings-näkymän painikkeiden toiminnot        
        pointLimitButton.setOnAction((event ->{
            if(service.getPointLimit() == 50){
                service.setPointLimit(30);
                pointLimitButton.setText("Pisteraja: 30");
                
            } else if (service.getPointLimit() == 30){
                service.setPointLimit(50);
                pointLimitButton.setText("Pisteraja: 50");
            }            
        }));
        
        pointLimitBrokenButton.setOnAction((event ->{
            if(service.getPointsToZeroWhenPointLimitPassed() == false){
                service.setPointsToZeroWhenPointLimitPassed(true);
                pointLimitBrokenButton.setText("Pisteet nollataan "
                        + "pisterajan ylittyessä");
                
            } else if(service.getPointsToZeroWhenPointLimitPassed() == true){
                service.setPointsToZeroWhenPointLimitPassed(false);
                pointLimitBrokenButton.setText("Pisteet puolitetaan "
                        + "pisterajan ylittyessä");
            }            
        }));
        
        startGameButton.setOnAction((event) -> {
            window.setScene(gameScene);
            whosTurn.setText(service.getWhosTurnName());
            whosTurnNext.setText(service.getWhosNextName());           
        });
        
        addMorePlayersButton.setOnAction((event) -> {
            window.setScene(addPlayersScene);
        });
        
        //3: Game-näkymän painikkeiden toiminnot       
        documentButton.setOnAction((event) -> { 
            if (service.getHowManyPlayers() == 0) {
                pointsToDocument.setText("Pelaajat puuttuu! Palaa alkuun.");
                return;
            }
            String pointsString = pointsToDocument.getText();
            
            if (service.checkIfValidNumber(pointsString).equals(false)) {
                pointsToDocument.setText("Virheellinen syöte");
 
                return;
            }
            
            commentLabel.setText(generateComment(pointsString));
            commentLabel.setFont(new Font("comic sans", 20));
            commentLabel.setTextFill(Color.web("c69fff", 0.8));
            
            service.documentThrow(pointsString);
           
            if(service.getWinnerFound() == true) {               
                Button closeAndRestart = createButtonWithColour("Lopeta peli ja palaa alkuun", buttonColourDark);                   
                VBox winnerLayout = createWinnerLayout(window, closeAndRestart);
                winnerScene = new Scene(winnerLayout);
                window.setScene(winnerScene);              
            }                
                
            pointsToDocument.clear();
            
            if(service.getHowManyPlayers()== 0) {
                whosTurn.setText("Ei pelaajia");
                whosTurnPoints.setText("0");
                whosTurnNext.setText("Ei pelaajia"); 
                    
               return;
            }
            
                whosTurnPoints.setText(service.getPlayersPointsWithIndex(service.getWhosTurnIndex()));
                whosTurn.setText(service.getWhosTurnName());
                whosTurnNext.setText(service.getWhosNextName());                        
        }); 
        
        Image bigLogoImage = new Image("/images/logo.jpg");
        ImageView bigLogoImageView = new ImageView(bigLogoImage);        
        
        VBox selectionsLayout= createStartScene(window, bigLogoImageView, addedPlayers, pointLimitButton, 
                pointLimitBrokenButton, whosTurn, whosTurnPoints, whosTurnNext, pointsToDocument, commentLabel);
        
        startScene = new Scene(selectionsLayout);
        window.setTitle("Mölkkymestari");
        window.setScene(startScene);
        window.show();
        
    }    
    
    /**
     * Method for closing the whole application.
     */
    public void close() {
        Platform.exit();
    }
    
    /**
     * Method creates the first Scene that is shown to end user.Shows a logo
     * and with the click of a button the player can start a new game.
     * @param window
     * @param bigLogoImageView
     * @param addedPlayers
     * @param pointLimitButton
     * @param pointLimitBrokenButton
     * @param whosTurn
     * @param whosTurnPoints
     * @param whosTurnNext
     * @param pointsToDocument
     * @param commentLabel
     * @return VBox layout for the startScene-Scene
     * @throws IOException 
     */
    public VBox createStartScene(Stage window, ImageView bigLogoImageView, Label addedPlayers, Button pointLimitButton, 
            Button pointLimitBrokenButton, Label whosTurn, Label whosTurnPoints, 
            Label whosTurnNext, TextField pointsToDocument, Label commentLabel) throws IOException  {
        
        VBox startSceneLayout = new VBox();
        vBoxLayoutSettings(startSceneLayout);       
        Button newGame = createButtonWithColour("Uusi peli", "#D76FB0; ");
        
        startSceneLayout.getChildren().addAll(bigLogoImageView, newGame);
        
        newGame.setOnAction((event)-> {
            service = new MolkkyService();
            resetButtonsForNewGame(addedPlayers, pointLimitButton, pointLimitBrokenButton, 
                    whosTurn, whosTurnPoints, whosTurnNext, pointsToDocument, commentLabel);          
            
            window.setScene(addPlayersScene);
        });

        return startSceneLayout;
    }
    
    /**
     * Creates the Scene used for adding the players to this game of Mölkky. 
     * With the click of a button end user can access the settings screen.
     * @param window
     * @param addedPlayers
     * @param addPlayersField
     * @param savePlayer
     * @param newGame
     * @return VBox layout for the addPlayers-Scene
     */
    public VBox createAddPlayersLayout(Stage window, Label addedPlayers, TextField addPlayersField, 
            Button savePlayer, Button newGame) {
        
        VBox AddPlayersLayout = new VBox();        
        Label logo = createLogo();       
        HBox buttons = new HBox();
        
        Label infoText = new Label("Lisää Mölkkyyn osallistuvat 1-10 pelaajaa "
                + "yksitellen kenttään, tallenna napsauttamalla "
                + "Tallenna pelaaja -painiketta.\nNimimerkki voi sisältää "
                + "kirjaimia ja numeroita, sen on oltava 3 - 20 merkkiä pitkä.");
        Label headerPlayers = new Label("\nPelaajat:\n");
        subHeaderStyle(headerPlayers);
        
        buttons.getChildren().addAll(savePlayer, newGame);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(30);
                
        addedPlayers.setPadding(new Insets (20, 20, 20, 20));
        addedPlayers.setTextFill(Color.web("ef2684", 0.8));
        
        AddPlayersLayout.getChildren().addAll(logo, infoText, headerPlayers, 
                addedPlayers, addPlayersField, buttons);

        vBoxLayoutSettings(AddPlayersLayout); 
        return AddPlayersLayout;
    }
    
    /**
     * Method for creating the Scene for game settings. Has buttons for changing
     * the point limit of the game and the action that is performed if the
     * point limit is passed with a throw.
     * @param window
     * @param pointLimitButton
     * @param pointLimitBrokenButton
     * @param startGameButton
     * @param addMorePlayersButton
     * @return VBox layout for the gameSetting -Scene
     */
    public VBox createGameSettingsLayout(Stage window, Button pointLimitButton, 
            Button pointLimitBrokenButton, Button startGameButton, Button addMorePlayersButton){      
        
        VBox gameSettingsLayout = new VBox();       
        Label logo = createLogo();      
        Label headerSettings = new Label("Peliasetukset");
        subHeaderStyle(headerSettings);
        Label infoSettings = new Label("Napsauta painikkeita muuttaaksesi asetuksia.");
 
        gameSettingsLayout.getChildren().addAll(logo, headerSettings, infoSettings, pointLimitButton, 
                pointLimitBrokenButton, addMorePlayersButton, startGameButton);
        
        vBoxLayoutSettings(gameSettingsLayout);
        
        return gameSettingsLayout;
    }
    
    /**
     * Method for creating the Scene for the game-screen.Shows which players turn 
 it is at that current time and their points that far.In the field the
  end user documents the points from that players throw as a number between 
 0-12. Below the point documentation field the next players name is shown.
     * @param window
     * @param whosTurn
     * @param whosTurnPoints
     * @param whosTurnNext
     * @param pointsToDocument
     * @param documentButton
     * @param commentLabel
     * @param closeAndStartNewGame
     * @return VBox layout for the gameLayout -Scene
     */
    public VBox createGameLayout(Stage window, Label whosTurn, Label whosTurnPoints, 
            Label whosTurnNext, TextField pointsToDocument, 
            Button documentButton, Label commentLabel, Button closeAndStartNewGame) {
        
        VBox gameLayout = new VBox();         
        Label logo = createLogo();       
        Label whosTurnText = new Label("Pelivuorossa: ");
        Label whosTurnPointsText = new Label(" - pisteet:  ");
        whosTurn.setTextFill(Color.web("ef2684", 0.8));
        whosTurnPoints.setTextFill(Color.web("ef2684", 0.8)); 
        
        HBox whosTurnInfo = new HBox();      
        whosTurnInfo.setAlignment(Pos.CENTER);
        whosTurnInfo.getChildren().addAll(whosTurnText, whosTurn, 
                whosTurnPointsText, whosTurnPoints);        
         
        pointsToDocument.setMinSize(150, 50);
        pointsToDocument.setMaxSize(300, 100);
        
        Label whosNextText = new Label("Seuraavana vuorossa: ");
        HBox whosNextInfo = new HBox();
        whosNextInfo.setAlignment(Pos.CENTER);
        whosTurnNext.setTextFill(Color.web("ef2684", 0.8)); 
        whosNextInfo.getChildren().addAll(whosNextText, whosTurnNext);
         
        closeAndStartNewGame.setOnAction((event)-> {
            window.setScene(startScene);           
        });  
           
        gameLayout.getChildren().addAll(logo, whosTurnInfo, pointsToDocument, 
                documentButton, commentLabel, whosNextInfo, closeAndStartNewGame);
                
        vBoxLayoutSettings(gameLayout);
        return gameLayout;
    }
    
    /**
     * Method for creating the Scene used to show the winner of the game.Has the name of the winner and the points of all the players in that game.
     * @param window
     * @param closeAndRestart
     * @return VBox layout for the winner-Scene
     */
    public VBox createWinnerLayout(Stage window, Button closeAndRestart) {
        VBox winnerLayout = new VBox();
        vBoxLayoutSettings(winnerLayout);     
        Label logo = createLogo();               
        Label winnerFoundText = new Label("VOITTAJA ON: "); 
        subHeaderStyle(winnerFoundText);
        Label winnersName = new Label("");
        Label emoji = new Label(createEmoji("grin") + createEmoji("heart") + createEmoji("two_hearts"));
        
        emoji.setFont(new Font("impact", 35));
        emoji.setTextFill(Color.web("e91bd4", 0.8)); 
        
        winnersName.setText(service.getWinnerName());       
        winnersName.setFont(new Font("impact", 35));
        winnersName.setTextFill(Color.web("e91bd4", 0.8)); 
        
        Label playersPointsHeader = new Label("Pistetaulukko:");
        subHeaderStyle(playersPointsHeader);
        
        Label playerPointTable = createPlayerPointTable();        
            
        Button closeMolkkymestari = new Button("Sulje");
        closeMolkkymestari.setStyle("-fx-background-color: #B475A9; ");
        
        closeMolkkymestari.setOnAction((event)-> {
            close();
        });
              
        closeAndRestart.setOnAction((event)-> {
            window.setScene(startScene);           
        });   
            
        winnerLayout.getChildren().addAll(logo, winnerFoundText, winnersName, emoji,  
                playersPointsHeader, playerPointTable, closeAndRestart, closeMolkkymestari);
                   
        return winnerLayout;          
    }
    
    /**
     * Method that creates the Mölkkymestari logo.
     * @return logo as a Label object
     */
    public Label createLogo() {
        
        Image logoImage = new Image("/images/smallLogo.jpg");
        ImageView logoImageView = new ImageView(logoImage);
        
        Label logoLabel = new Label("");      
        logoLabel.setGraphic(logoImageView);
        
        return logoLabel;      
    }
    
    /**
     * Method creates the visualization of each player who was in the game
     * when it ended. Shows the name and the points they got in the game.
     * @return 
     */
    public Label createPlayerPointTable(){
        String playersAndPoints = service.createPlayerPointTable();      
        Label playerPoints = new Label(playersAndPoints);
        
        return playerPoints;
    }  
    
    /**
     * Generates random comments about players throws. For missed throw a sad comment
     * is generated, for else a happy comment.
     * @param points points from the throw
     * @return String representation of the comment
     */
    public String generateComment(String points) {
        String name = service.getWhosTurnName();
        String comment = "";   
        
        if (points.equals("0")) {
            
            if(service.getMissedThrowsInRowWithIndex(service.getWhosTurnIndex()) == 2) {
                comment = "Hitsin pimpulat " + name + ", kolmas huti eli putoat pelistä " + createEmoji("disappointed");
                return comment;
            }
            
            comment = "Hupsista " + name + ", nyt meni huti " + createEmoji("disappointed");
            return comment;
        } 
                
        Random randomizer = new Random();
        int randomNumber = randomizer.nextInt(5);
        
        if (randomNumber == 0) {
            comment = "Huisi heitto " + name + "!" + createEmoji("grin");
        }
        if (randomNumber == 1) {
            comment = name + " osuu jälleen! " + createEmoji("heart");
        }
        if (randomNumber == 2) {
            comment = "Ja yleisö huutaa: " + name + ", " + name + ", " + name + "!" + createEmoji("two_hearts");
        }
        if (randomNumber == 3) {
            comment = name + " on liekeissä " + createEmoji("fire");
        }
        if (randomNumber == 4) {
            comment = name + " nappaa pisteet!" + createEmoji("blush");
        }
        
        return comment;
    }
    /**
     * Method for creating an emoji from a String command.Only five emojis can
     *  be created at this moment.
     * @param emojiCommand available emoji commands: grin, blush, fire, heart, two_hearts, disappointed
     * @return String object of the emoji
     */
    public String createEmoji(String emojiCommand) {
        String emojiStr = ":" + emojiCommand + ":";
        String emoji = EmojiParser.parseToUnicode(emojiStr);
        
        return emoji;
    }
    
    /**
     * Method used to give correct visual settings for all of the Layout VBoxes 
     * that are used in Scenes.
     * @param VBox 
     */
    public void vBoxLayoutSettings(VBox VBox){
        VBox.setPrefSize(800, 600);
        VBox.setAlignment(Pos.TOP_CENTER);
        VBox.setSpacing(30);
        VBox.setStyle("-fx-background-color: #fbe8ff; ");
    }
    
    /**
     * Creates a button with the hexcolourcode given
     * @param text the text to be shown in the button
     * @param colour hexcolour code as String
     * @return Button-object with specified colour and text
     */
    public Button createButtonWithColour(String text, String colour) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + colour);
        
        return button;
    }
    
    /**
     * Method is used to clear all Button-, Label- and TextField -objects that 
     * contain information about the game that ended, in the event of player 
     * choosing to start a new game after the first one, 
     * without closing the application.
     * @param addedPlayers 
     * @param pointLimitButton
     * @param pointLimitBrokenButton
     * @param whosTurn
     * @param whosTurnPoints
     * @param whosTurnNext
     * @param pointsToDocument 
     * @param commentLabel 
     */
    public void resetButtonsForNewGame(Label addedPlayers, Button pointLimitButton, 
            Button pointLimitBrokenButton, Label whosTurn, Label whosTurnPoints, 
            Label whosTurnNext, TextField pointsToDocument, Label commentLabel) {
        
        addedPlayers.setText("");
        pointLimitButton.setText("Pisteraja: " + service.getPointLimit()); 
        pointLimitBrokenButton.setText("Pisteet puolitetaan pisterajan ylittyessä"); 
        whosTurn.setText("");
        whosTurnPoints.setText("0");
        whosTurnNext.setText("");   
        pointsToDocument.setText("");
        commentLabel.setText("");
    }
    /**
     * Sets font settings for subheaders.
     * @param label 
     */
    public void subHeaderStyle(Label label) {       
        
        label.setTextFill(Color.web("5a2066", 0.8)); 
        label.setFont((new Font("Impact", 18)));
    }
}