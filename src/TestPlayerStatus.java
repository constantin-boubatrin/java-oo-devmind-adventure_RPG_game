
import java.util.Random;
import java.util.Scanner;

/** The 'TestPlayerStatus' class is intended to test 'PlayerStatus' class */

public class TestPlayerStatus {
    private static final Scanner in = new Scanner(System.in);
    private static final Random random = new Random();
    private PlayerStatus currentPlayer;
    private PlayerStatus opponentPlayer;

    // Generate instances of 'PlayerStatus' type
    public PlayerStatus generatePlayer(){
        System.out.println("\nEnter the Player's Nickname (ex.: Raiden):");
        String nickname = in.nextLine();
        System.out.println("Enter the number of Player's Lives (ex.: 3):");
        int lives = in.nextInt();
        System.out.println("Enter the Player's Score (ex.: 10.000):");
        int score = in.nextInt();
        in.nextLine();
        System.out.println("Enter the Player's Weapon (available weapon.: sniper, kalashnikov, knife):");
        String weaponInHand = in.nextLine();
        System.out.println("Enter the Player's positionX (ex.: 100.5):");
        double positionX = in.nextDouble();
        System.out.println("Enter the Player's positionY (ex.: 100.5):");
        double positionY = in.nextDouble();
        in.nextLine();
        return new PlayerStatus(nickname, lives, score, 100, weaponInHand, positionX, positionY);
    }

    // Perform one-time initialization tasks
    public void initGame() {
        System.out.println("Welcome!");
        System.out.println("Enter the game name:");
        String gameName = in.nextLine();
        PlayerStatus.setGameName(gameName);

        System.out.println("Let's generate you a Player");
        this.currentPlayer = generatePlayer();

        System.out.println("Let's generate you an Opponent Player");
        this.opponentPlayer = generatePlayer();
    }

    private static void printCommandsList() {
        System.out.println("""
                        1  - Display the list of commands
                        2  - Display your player's status
                        3  - Display your opponent's status
                        4  - Update your Player Nickname
                        5  - Buy another weapon
                        6  - Find artifact
                        7  - Move Your Player
                        8  - Simulate a battle
                        9  - Attack your opponent
                        10 - Distance between your player and the opponent
                        11 - Close the application""");
    }

    public void run() {
        // Perform one-time initialization tasks
        initGame();

        System.out.println("\nWelcome to the game: " + PlayerStatus.getGameName());

        boolean quit = false;
        while (!quit) {
            boolean isCurrAlive = currentPlayer.getLives() <= 0 && currentPlayer.getHealth() <= 0;
            boolean isOppoAlive = opponentPlayer.getLives() <= 0 && opponentPlayer.getHealth() <= 0;
            if (isCurrAlive || isOppoAlive) {
                System.out.println("\nGame Over!!!");
                System.out.printf("The '%s' Player won the game %s.%n",
                        (isCurrAlive ? currentPlayer.getNickname() : opponentPlayer.getNickname()),
                        PlayerStatus.getGameName());
                break;
            }

            System.out.println("Awaiting command: (1 - Display the list of commands)");
            int command = in.nextInt();
            in.nextLine();
            switch (command) {
                case 1:
                    printCommandsList();
                    break;
                case 2:
                    currentPlayer.displayPlayerStatus();
                    break;
                case 3:
                    opponentPlayer.displayPlayerStatus();
                    break;
                case 4:
                    System.out.println("Updating Player's Nickname...");
                    System.out.println("Enter the new Player's Nickname (ex.: Raiden):");
                    String newNickname = in.nextLine();
                    currentPlayer.setNickname(newNickname);
                    break;
                case 5:
                    System.out.println("Buying weapon...");
                    do {
                        System.out.println("""
                                1  - knife       - cost  1.000 points
                                2  - sniper      - cost 10.000 points
                                3  - kalashnikov - cost 20.000 points
                                """);
                        int typeOfWeapon = in.nextInt();
                        if (typeOfWeapon == 1) {
                            currentPlayer.setWeaponInHand("knife");
                            break;
                        } else if (typeOfWeapon == 2) {
                            currentPlayer.setWeaponInHand("sniper");
                            break;
                        } else if (typeOfWeapon == 3) {
                            currentPlayer.setWeaponInHand("kalashnikov");
                            break;
                        } else {
                            System.out.println("Unknown command. Please try again.");
                        }
                    } while (true);
                    break;
                case 6:
                    System.out.println("Finding artifacts...");
                    int artifactCode = random.nextInt(1000);
                    System.out.println("The Player found the artifact no. " + artifactCode);
                    currentPlayer.findArtifact(artifactCode);
                    break;
                case 7:
                    System.out.println("Moving Player...");
                    System.out.printf("Current coordinates of the Player '%s':%nX -> %.3f Y -> %.3f.%n",
                            currentPlayer.getNickname(),
                            currentPlayer.getPositionX(), currentPlayer.getPositionY());

                    System.out.println("Enter the new Player's position X (ex.: 100):");
                    double coordX = in.nextDouble();

                    System.out.println("Enter the new Player's position Y (ex.: 100):");
                    double coordY = in.nextDouble();

                    currentPlayer.movePlayerTo(coordX, coordY);
                    System.out.printf("Updated coordinates of the Player '%s':%nX -> %.3f Y -> %.3f.%n",
                            currentPlayer.getNickname(),
                            currentPlayer.getPositionX(), currentPlayer.getPositionY());
                    break;
                case 8:
                    System.out.println("Battle simulation...");
                    boolean canWin = currentPlayer.shouldAttackOpponent(opponentPlayer);
                    System.out.printf("If you decide to attack your opponent, " +
                            "you could %s.%n", (canWin ? "win": "lose"));
                    break;
                case 9:
                    System.out.println("Attacking the opponent...");
                    currentPlayer.attackOpponent(opponentPlayer);
                    break;
                case 10:
                    System.out.printf("The distance between your player and " +
                            "the opponent is: %.3f.%n", currentPlayer.playersDistance(opponentPlayer));
                    break;
                case 11:
                    System.out.println("Application is closing...");
                    System.out.println("Goodbye!!!");
                    quit = true;
                    break;
                default:
                    System.out.println("Unknown command. Please try again.");
                    break;
            }
        }

        in.close();
    }

    public static void main(String[] args) {
        // create and run an instance (for test purpose)
        new TestPlayerStatus().run();
    }
}