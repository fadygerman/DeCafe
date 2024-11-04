package com.example.decafe;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

// Class that is used mainly to control certain assets of the Game like Machines, Upgrades and the Coin Score
// I changed some Variables and Methods here Refactoring/Konuk
public class Game {
    private static final int COFFEE_MACHINE_DURATION = 5;
    private static final int CAKE_MACHINE_DURATION = 5;
    private static final int COFFEE_UPGRADE_COST = 20;
    private static final int CAKE_UPGRADE_COST = 20;
    private static final int PLAYER_UPGRADE_COST = 40;
    private static final int HAPPY_CUSTOMER_COINS = 7;
    private static final int NORMAL_CUSTOMER_COINS = 5;
    private static final int SAD_CUSTOMER_COINS = 3;
    private static final int COFFEE_MACHINE_UPGRADE_DURATION = 2;
    private static final int CAKE_MACHINE_UPGRADE_DURATION = 2;
    private static final int PLAYER_UPGRADE_MOVEMENT = 6;

    private final Machine coffeeMachine; // A Machine Object used to make Coffee
    private final Machine cakeMachine; // A Machine Object used to make Cake
    private final Upgrade coffeeUpgrade; // An Upgrade Object used to upgrade the Coffee Machine
    private final Upgrade cakeUpgrade; // An Upgrade Object used to upgrade the Cake Machine
    private final Upgrade playerUpgrade; // An Upgrade Object used to make the Player faster
    private int coinsEarned; // The amount of Coins earned/used in the Game - 0 at the beginning
    private final String filenameImageThreeCoins; // Image of small amount of money earned
    private final String filenameImageFourCoins; // Image of normal amount of money earned
    private final String filenameImageDollar; // Images of huge amount of money earned

    // Constructor
    Game(ImageView upgradeCoffee, ImageView upgradeCake, ImageView upgradePlayer) {
        this.coffeeMachine = new Machine(COFFEE_MACHINE_DURATION, "coffeeMachineWithCoffee.png", "coffeeMachine.png", "coffee");
        this.cakeMachine = new Machine(CAKE_MACHINE_DURATION, "kitchenAidUsed.png", "kitchenAid.png", "cake");
        this.coffeeUpgrade = new Upgrade(COFFEE_UPGRADE_COST, false, "coffeeUpgrade.png", "coffeeUsed.png", upgradeCoffee);
        this.cakeUpgrade = new Upgrade(CAKE_UPGRADE_COST, false, "cakeUpgrade.png", "cakeUsed.png", upgradeCake);
        this.playerUpgrade = new Upgrade(PLAYER_UPGRADE_COST, false, "upgradeSkates.png", "upgradeSkatesUsed.png", upgradePlayer);
        this.coinsEarned = 0;
        this.filenameImageDollar = "5coins.png";
        this.filenameImageFourCoins = "4coins.png";
        this.filenameImageThreeCoins = "3coins.png";
    }

    // Getter
    public Machine getCakeMachine() {
        return cakeMachine;
    }

    public Machine getCoffeeMachine() {
        return coffeeMachine;
    }

    public Upgrade getCakeUpgrade() {
        return cakeUpgrade;
    }

    public Upgrade getCoffeeUpgrade() {
        return coffeeUpgrade;
    }

    public Upgrade getPlayerUpgrade() {
        return playerUpgrade;
    }

    public String getFilenameImageThreeCoins() {
        return filenameImageThreeCoins;
    }

    public String getFilenameImageFourCoins() {
        return filenameImageFourCoins;
    }

    public String getFilenameImageDollar() {
        return filenameImageDollar;
    }

    public int getCoinsEarned() {
        return coinsEarned;
    }

    // Method used to create an Image Object
    public Image createImage(String filename) throws FileNotFoundException {
        File f = new File(""); // Get filepath of project
        // Get path to certain Image
        String filePath = f.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "com" + File.separator + "example" + File.separator + "decafe" + File.separator + filename;
        InputStream stream = new FileInputStream(filePath); // Convert path into stream
        return new Image(stream); // Convert stream to Image and return it
    }

    // Method to check if the Player can use a certain Upgrade
    public void checkUpgradePossible(Upgrade upgrade) throws FileNotFoundException {
        if (!upgrade.isAlreadyUsedOnce() && this.coinsEarned >= upgrade.getCoinsNeeded()) { // If upgrade was not already used and the Player earned enough coins to buy it
            enableUpgrade(upgrade);
        } else { // If the upgrade was used already or the Player hasn't enough coins to buy it
            disableUpgrade(upgrade);
        }
    }

    private void enableUpgrade(Upgrade upgrade) throws FileNotFoundException {
        // Enable the ImageView
        upgrade.getUpgradeImageView().setDisable(false);
        // Set the Image to the "activated" Upgrade Image
        upgrade.getUpgradeImageView().setImage(createImage(upgrade.getFilenameUpgradeNotUsed()));
    }

    private void disableUpgrade(Upgrade upgrade) throws FileNotFoundException {
        // Disable the Image
        upgrade.getUpgradeImageView().setDisable(true);
        // Set the Image to "deactivated" Upgrade Image
        upgrade.getUpgradeImageView().setImage(createImage(upgrade.getFilenameUpgradeUsed()));
    }

    // Method to do a certain upgrade
    public void doUpgrade(String type, Player CofiBrew) throws FileNotFoundException {
        switch (type) { // Switch the type of upgrade you received
            case "coffee" -> upgradeCoffeeMachine();
            case "cake" -> upgradeCakeMachine();
            case "player" -> upgradePlayer(CofiBrew);
        }
    }

    private void upgradeCoffeeMachine() throws FileNotFoundException {
        // Set the coin score according to what the upgrade cost + change Image and Disable upgrade
        coinsEarned = coffeeUpgrade.doUpgrades(coinsEarned);
        // Increase the speed of the Coffee Machine
        coffeeMachine.setDuration(COFFEE_MACHINE_UPGRADE_DURATION);
    }

    private void upgradeCakeMachine() throws FileNotFoundException {
        // Set the coin score according to what the upgrade cost + change Image and Disable upgrade
        coinsEarned = cakeUpgrade.doUpgrades(coinsEarned);
        // Increase the speed of the Cake Machine
        cakeMachine.setDuration(CAKE_MACHINE_UPGRADE_DURATION);
    }

    private void upgradePlayer(Player CofiBrew) throws FileNotFoundException {
        // Set the coin score according to what the upgrade cost + change Image and Disable upgrade
        coinsEarned = playerUpgrade.doUpgrades(coinsEarned);
        // Increase the movement speed of the Player
        CofiBrew.setMovement(PLAYER_UPGRADE_MOVEMENT);
    }

    // Method to increase coins earned according to how satisfied the customer was
    public void setCoinsEarned(Customer customer) {
        if (customer.isGreen()) { // If customer was happy
            // Increase coin score by 7
            this.coinsEarned += HAPPY_CUSTOMER_COINS;
        } else if (customer.isYellow()) { // If customer left in a "normal" mood
            // Increase coin score by 5
            this.coinsEarned += NORMAL_CUSTOMER_COINS;
        } else if (customer.isRed()) { // If customer left in a bad mood
            // Increase coin score by 3
            this.coinsEarned += SAD_CUSTOMER_COINS;
        }
    }
}