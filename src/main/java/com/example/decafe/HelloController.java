package com.example.decafe;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;

import java.io.*;
import java.net.URL;
import java.util.*;

// I changed some Variables and Methods here Refactoring/Konuk
public class HelloController implements Initializable {

    // Assets of the Start Screen
    public ImageView startButton;
    public ImageView startQuitButton;

    // Assets of the Game Screen
    public ImageView waiterImageView;
    public Label coinsEarnedLabel;
    private BooleanProperty wPressed = new SimpleBooleanProperty();
    private BooleanProperty aPressed = new SimpleBooleanProperty();
    private BooleanProperty sPressed = new SimpleBooleanProperty();
    private BooleanProperty dPressed = new SimpleBooleanProperty();
    private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);

    public ImageView coffeeMachineImageView;
    public ImageView cakeMachineImageView;
    public ImageView trashcanImageView;
    public ProgressBar progressBarCoffee;
    public ProgressBar progressBarCake;
    public ImageView upgradeCoffeeImageView;
    public ImageView upgradeCakeImageView;
    public ImageView upgradePlayerImageView;

    public Label table1;
    public Label table2;
    public Label table3;
    public Label table4;
    public Label plantsAbove;
    public Label countRight;
    public Label countBelow;
    public Label customerTop1;
    public Label customerTop2;
    public Label customerTop3;
    public Label customerTop4;
    public Label customerBot1;
    public Label customerBot2;
    public Label customerBot3;
    public Label plant;
    public Label edgeBot;
    public Label edgeTop;
    public Label edgeLeft;
    public Label edgeRight;

    public ImageView smileyFirst;
    public ImageView smileySecond;
    public ImageView smileyThird;
    public ImageView smileyFourth;
    public ImageView smileyFifth;
    public ImageView smileySixth;
    public ImageView smileySeventh;

    public ImageView coinFirst;
    public ImageView coinSecond;
    public ImageView coinThird;
    public ImageView coinFourth;
    public ImageView coinFifth;
    public ImageView coinSixth;
    public ImageView coinSeventh;

    public ImageView orderlabel1 = new ImageView();
    public ImageView orderlabel2 = new ImageView();
    public ImageView orderlabel3 = new ImageView();
    public ImageView orderlabel4 = new ImageView();
    public ImageView orderlabel5 = new ImageView();
    public ImageView orderlabel6 = new ImageView();
    public ImageView orderlabel7 = new ImageView();

    public ImageView first;
    public ImageView second;
    public ImageView third;
    public ImageView fourth;
    public ImageView fifth;
    public ImageView sixth;
    public ImageView seventh;

    public ImageView gameStartButton;
    public ImageView cofiBrewImage;
    public ImageView playAgainImage;
    public ImageView backToStartImage;
    public Label labelCredits;
    public ImageView endScreenBackground;
    public ImageView quitEndScreenImage;

    public Player player = new Player("CofiBrewUp.png", "CofiBrewCakeLeft.png", "CofiBrewCoffeeLeft.png", 4);
    public Game Play;
    private Label[] collisions;
    public Timer controllerTimer = new Timer();

    public File f = new File("");
    public String musicFile = f.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "com" + File.separator + "example" + File.separator + "decafe" + File.separator + "backgroundmusic.mp3";
    public AudioClip backgroundMusic = new AudioClip(new File(musicFile).toURI().toString());

    private static final int COFFEE_MACHINE_UPGRADE_DURATION = 2;
    private static final int CAKE_MACHINE_UPGRADE_DURATION = 2;
    private static final int PLAYER_UPGRADE_MOVEMENT = 6;

    // Method used to load a certain scene according to the name of the fxml file
    public void loadScene(String sceneName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(sceneName));
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.stage.setScene(scene);
        HelloApplication.stage.show();
    }

    // jump to end screen
    public void switchToEndScreen() throws IOException {
        backgroundMusic.stop();
        loadScene("endScreen.fxml");
    }

    // jump to start screen
    public void switchToStartScreen() throws IOException {
        loadScene("startScreen.fxml");
    }

    // jump from start screen to game screen
    public void switchToGameScreen() throws IOException {
        loadScene("gameScreen.fxml");
        if (Customer.customerImages[0] != null) {
            Customer customer = new Customer();
            customer.startTimerSpawn(1, Customer.getControllerTimer());
            customer.startTimerSpawn(5, Customer.getControllerTimer());
            customer.startTimerSpawn(10, Customer.getControllerTimer());
            Customer.allCustomers.add(customer);
        }
        backgroundMusic.setCycleCount(AudioClip.INDEFINITE);
        backgroundMusic.play();
    }

    // jump to instructions
    public void switchToInstructions() throws IOException {
        loadScene("Instructions.fxml");
    }

    // key events if wasd-keys are pressed
    @FXML
    public void keyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case W -> wPressed.set(true);
            case A -> aPressed.set(true);
            case S -> sPressed.set(true);
            case D -> dPressed.set(true);
        }
    }

    // key events if wasd-keys are released
    @FXML
    public void keyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case W -> wPressed.set(false);
            case A -> aPressed.set(false);
            case S -> sPressed.set(false);
            case D -> dPressed.set(false);
        }
    }

    // for smoother motion
    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long timestamp) {
            handleMovement();
        }
    };

    private void handleMovement() {
        int movementVariable = player.getMovement();
        double move = movementVariable;
        String movement = "none";

        if (wPressed.get() && aPressed.get() || wPressed.get() && dPressed.get() ||
                sPressed.get() && aPressed.get() || sPressed.get() && dPressed.get()) {
            move -= movementVariable - Math.sqrt(Math.pow(movementVariable, 2) / 2);
        }

        double xMove = 0;
        double yMove = 0;

        if (wPressed.get()) {
            yMove = -move;
            movement = "up";
        } else if (sPressed.get()) {
            yMove = move;
            movement = "down";
        } else if (aPressed.get()) {
            xMove = -move;
            movement = "left";
        } else if (dPressed.get()) {
            xMove = move;
            movement = "right";
        }

        waiterImageView.setLayoutX(waiterImageView.getLayoutX() + xMove);
        waiterImageView.setLayoutY(waiterImageView.getLayoutY() + yMove);

        if (checkForCollision(waiterImageView)) {
            waiterImageView.setLayoutX(waiterImageView.getLayoutX() - xMove);
            waiterImageView.setLayoutY(waiterImageView.getLayoutY() - yMove);
            movement = "none";
        } else {
            updateWaiterImage(movement);
        }
    }

    private void updateWaiterImage(String movement) {
        try {
            String productInHand = player.getProductInHand();
            switch (movement) {
                case "up" -> waiterImageView.setImage(createImage(getImageFilename("Up", productInHand)));
                case "down" -> waiterImageView.setImage(createImage(getImageFilename("Down", productInHand)));
                case "left" -> waiterImageView.setImage(createImage(getImageFilename("Left", productInHand)));
                case "right" -> waiterImageView.setImage(createImage(getImageFilename("Right", productInHand)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getImageFilename(String direction, String productInHand) {
        if (productInHand.equals("none")) {
            return "CofiBrew" + direction + ".png";
        } else {
            return "CofiBrew" + productInHand + direction + ".png";
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        keyPressed.addListener((observableValue, aBoolean, t1) -> {
            if (!aBoolean) {
                timer.start();
            } else {
                timer.stop();
            }
        });

        collisions = new Label[]{plant, plantsAbove, customerBot1, customerBot2, customerBot3, customerTop1, customerTop2, customerTop3, customerTop4, table1, table2, table3, table4, edgeBot, edgeLeft, edgeRight, edgeTop, countRight, countBelow};

        Customer.customerImages = new ImageView[]{first, second, third, fourth, fifth, sixth, seventh};
        Customer.smileyImages = new ImageView[]{smileyFirst, smileySecond, smileyThird, smileyFourth, smileyFifth, smileySixth, smileySeventh};
        Customer.orderLabels = new ImageView[]{orderlabel1, orderlabel2, orderlabel3, orderlabel4, orderlabel5, orderlabel6, orderlabel7};
        Customer.coinImages = new ImageView[]{coinFirst, coinSecond, coinThird, coinFourth, coinFifth, coinSixth, coinSeventh};
        Customer.freeChairs = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6));
        Customer.setControllerTimer(controllerTimer);
        Play = new Game(upgradeCoffeeImageView, upgradeCakeImageView, upgradePlayerImageView);
    }

    public Image createImage(String filename) throws FileNotFoundException {
        File f = new File("");
        String filePath = f.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "com" + File.separator + "example" + File.separator + "decafe" + File.separator + filename;
        InputStream stream = new FileInputStream(filePath);
        return new Image(stream);
    }

    public void changeStartCoffeeImage() throws FileNotFoundException {
        startButton.setImage(createImage("startCoffeeHot.png"));
    }

    public void changeStartCoffeeImageBack() throws FileNotFoundException {
        startButton.setImage(createImage("startCoffee.png"));
    }

    public void changeQuitStartScreen() throws FileNotFoundException {
        startQuitButton.setImage(createImage("quitEndScreenBrighter.png"));
    }

    public void changeQuitStartScreenBack() throws FileNotFoundException {
        startQuitButton.setImage(createImage("quitEndScreen.png"));
    }

    public void changeStartImage() throws FileNotFoundException {
        gameStartButton.setImage(createImage("instructionsGotIt.png"));
    }

    public void changeStartImageBack() throws FileNotFoundException {
        gameStartButton.setImage(createImage("instructionsGotItBrighter.png"));
    }

    public void changePlayAgain() throws FileNotFoundException {
        playAgainImage.setImage(createImage("playAgainBrighter.png"));
    }

    public void changePlayAgainBack() throws FileNotFoundException {
        playAgainImage.setImage(createImage("playAgain.png"));
    }

    public void changeBackToStartMenu() throws FileNotFoundException {
        backToStartImage.setImage(createImage("backToStartMenuBrighter.png"));
    }

    public void changeBackToStartMenuBack() throws FileNotFoundException {
        backToStartImage.setImage(createImage("backToStartMenu.png"));
    }

    public void changeQuitEndScreen() throws FileNotFoundException {
        quitEndScreenImage.setImage(createImage("quitEndScreenBrighter.png"));
    }

    public void changeQuitEndScreenBack() throws FileNotFoundException {
        quitEndScreenImage.setImage(createImage("quitEndScreen.png"));
    }

    public void showCoffee() throws FileNotFoundException {
        if (waiterImageView.getBoundsInParent().intersects(coffeeMachineImageView.getBoundsInParent())) {
            Play.getCoffeeMachine().displayProduct(waiterImageView, coffeeMachineImageView, player, progressBarCoffee);
            playSound("test_sound.wav");
        }
    }

    public void showCake() throws FileNotFoundException {
        if (waiterImageView.getBoundsInParent().intersects(cakeMachineImageView.getBoundsInParent())) {
            Play.getCakeMachine().displayProduct(waiterImageView, cakeMachineImageView, player, progressBarCake);
            playSound("test_sound.wav");
        }
    }

    public void noProduct() throws FileNotFoundException {
        if (player.getProductInHand().equals("coffee") || player.getProductInHand().equals("cake")) {
            playSound("trashSound.mp3");
            waiterImageView.setImage(createImage(player.getFilenameImageWithoutProduct()));
            player.setProductInHand("none");
        }
    }

    private void playSound(String soundFile) {
        File f = new File("");
        String musicFile = f.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "com" + File.separator + "example" + File.separator + "decafe" + File.separator + soundFile;
        AudioClip sound = new AudioClip(new File(musicFile).toURI().toString());
        sound.play();
    }

    public Customer findCustomer(List<Customer> customerList, ImageView customerImageView) {
        for (Customer customer : customerList) {
            if (customer.getImage().equals(customerImageView)) {
                return customer;
            }
        }
        return null;
    }

    public void displayPerson(MouseEvent event) throws IOException {
        ImageView customerImageView = (ImageView) event.getSource();
        Customer customer = findCustomer(Customer.customersInCoffeeShop, customerImageView);

        if (!customer.isAlreadyOrdered()) {
            customer.displayOrder(customer.getLabel());
        } else {
            handleCustomerOrder(customer, customerImageView);
        }
    }

    private void handleCustomerOrder(Customer customer, ImageView customerImageView) throws IOException {
        if (customerImageView.getBoundsInParent().intersects(waiterImageView.getBoundsInParent())) {
            try {
                customer.startTimerSpawn(5, Customer.getControllerTimer());
            } catch (NullPointerException e) {
                switchToEndScreen();
            }
            if (customer.checkOrder(player, customer, waiterImageView)) {
                String moneyImage = getMoneyImage(customer);
                customer.getCoinImage().setImage(createImage(moneyImage));
                customer.getCoinImage().setOnMouseClicked(event1 -> {
                    try {
                        getMoney(event1, customer);
                    } catch (IOException e) {
                        try {
                            switchToEndScreen();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private String getMoneyImage(Customer customer) {
        if (customer.isGreen()) {
            return Play.getFilenameImageDollar();
        } else if (customer.isYellow()) {
            return Play.getFilenameImageFourCoins();
        } else {
            return Play.getFilenameImageThreeCoins();
        }
    }

    public void checkUpgradePossible(Upgrade upgrade) throws FileNotFoundException {
        Play.checkUpgradePossible(upgrade);
    }

    public void doUpgrade(MouseEvent e) throws FileNotFoundException {
        Play.doUpgrade(((ImageView) e.getSource()).getId(), player);
        coinsEarnedLabel.setText(String.valueOf(Play.getCoinsEarned()));
        playSound("upgradeSound.wav");
        checkUpgradePossible(Play.getCoffeeUpgrade());
        checkUpgradePossible(Play.getCakeUpgrade());
        checkUpgradePossible(Play.getPlayerUpgrade());
    }

    public boolean checkForCollision(ImageView waiter) {
        for (Label collision : collisions) {
            if (waiter.getBoundsInParent().intersects(collision.getBoundsInParent())) {
                return true;
            }
        }
        return false;
    }

    public void getMoney(MouseEvent e, Customer customer) throws IOException {
        playSound("coinsSound.wav");
        Customer.addFreeSeat(customer.getChair());
        Play.setCoinsEarned(customer);
        ((ImageView) e.getSource()).setVisible(false);
        ((ImageView) e.getSource()).setDisable(true);

        if (Play.getCoinsEarned() < 80) {
            checkUpgradePossible(Play.getCoffeeUpgrade());
            checkUpgradePossible(Play.getCakeUpgrade());
            checkUpgradePossible(Play.getPlayerUpgrade());
            coinsEarnedLabel.setText(String.valueOf(Play.getCoinsEarned()));
            try {
                customer.startTimerSpawn(5, Customer.getControllerTimer());
            } catch (NullPointerException y) {
                switchToEndScreen();
            }
        } else {
            stopTimers();
            switchToEndScreen();
        }
    }

    public void stopTimers() {
        for (Customer customer : Customer.allCustomers) {
            if (customer.getSixtySecondsTimer() != null) {
                customer.getSixtySecondsTimer().cancel();
            }
        }
        Customer.allCustomers.clear();
        Customer.customersInCoffeeShop.clear();
        Customer.freeChairs.clear();
        player.setProductInHand("none");
        controllerTimer.cancel();
        Customer.getControllerTimer().cancel();
    }

    public void endGameQuick() {
        stopTimers();
        backgroundMusic.stop();
        Platform.exit();
        System.exit(0);
    }

    public void endGame() {
        Platform.exit();
        backgroundMusic.stop();
        System.exit(0);
    }
}