
/** The main purpose of the application is to simulate a duel between two players.
  * The 'PlayerStatus' class updates the status of a player within the game. */

public class PlayerStatus {
    /** FIELDS */
    // The internal state of 'PlayerStatus' objects will contain:
    private String nickname; // the player's name
    private int score; // the player's score
    private int lives; // the player's number of lives
    private int health; // percentage of life left (0 --> 100)
    private String weaponInHand; // the player's weapon
    private double positionX; // OX coordinate of the player's position
    private double positionY; // OY coordinate of the player's position
    private static String gameName; // the name of the game
    private static final int WEAPON_PRICE_KNIFE       = 1_000;
    private static final int WEAPON_PRICE_SNIPER      = 10_000;
    private static final int WEAPON_PRICE_KALASHNIKOV = 20_000;

    /** CONSTRUCTORS */
    PlayerStatus (String nickname, int lives, int score, int health,
                         String weaponInHand, double positionX, double positionY) {
        this.nickname = nickname;
        this.lives = lives;
        this.score = score;
        this.health = health;
        this.weaponInHand = weaponInHand;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    PlayerStatus (String nickname, int lives, int score) {
        this(nickname, lives, score, 100, null, 0, 0);
    }

    PlayerStatus (String nickname, int lives) {
        this(nickname, lives, 10_000, 100, null, 0, 0);
    }

    PlayerStatus (String nickname) {
        this(nickname, 3, 10_000, 100, null, 0, 0);
    }

    /** METHODS */

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getScore() {
        return (this.score > 0) ? this.score : 0;
    }

    public int getLives() {
        return this.lives;
    }

    private void setLives(int lives) {
        this.lives = lives;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getWeaponInHand() {
        return this.weaponInHand;
    }

    public boolean setWeaponInHand(String weaponInHand) {
        // Check if the player has chosen an available weapon
        if (!(weaponInHand.equalsIgnoreCase("knife")
                || weaponInHand.equalsIgnoreCase("sniper")
                || weaponInHand.equalsIgnoreCase("kalashnikov"))) {
            System.out.println("The weapon is not available!!!");
            return false;
        }

        // Check if the current player has money for the weapon
        if (weaponInHand.equalsIgnoreCase("knife")
                && this.score >= WEAPON_PRICE_KNIFE) {
            this.weaponInHand = weaponInHand;
            this.score -= WEAPON_PRICE_KNIFE;
            return true;
        } else if (weaponInHand.equalsIgnoreCase("sniper")
                && this.score >= WEAPON_PRICE_SNIPER) {
            this.weaponInHand = weaponInHand;
            this.score -= WEAPON_PRICE_SNIPER;
            return true;
        } else if (weaponInHand.equalsIgnoreCase("kalashnikov")
                && this.score >= WEAPON_PRICE_KALASHNIKOV) {
            this.weaponInHand = weaponInHand;
            this.score -= WEAPON_PRICE_KALASHNIKOV;
            return true;
        } else {
            System.out.println("You don't have enough money to buy a weapon!");
        }
        return false;
    }

    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public static String getGameName() {
        return PlayerStatus.gameName;
    }

    public static void setGameName(String gameName) {
        PlayerStatus.gameName = gameName;
    }

    // Update the player's state based on the found artifact
    public void findArtifact(int artifactCode) {
        if (isPerfect(artifactCode)) {
            System.out.println("Excellent! You found a perfect artifact.");
            this.score += 5_000;
            this.lives++;
            setHealth(100);
        } else if (isPrime(artifactCode)) {
            System.out.println("Excellent! You found a prim artifact.");
            this.score += 1_000;
            this.lives += 2;
            this.health += 25;
            if (this.health > 100) {
                setHealth(100);
            }
        } else if (isTrap(artifactCode)) {
            System.out.println("Oops! You found a trap artifact.");
            this.score -= 3000;
            this.health -= 25;
            if (this.health <= 0) {
                this.lives--;
                setHealth(100);
            }
        } else {
            System.out.println("You found a regular artifact.");
            this.score += artifactCode;
        }
    }

    // Update the player's position by updating the player's 'positionX' and 'positionY'
    public void movePlayerTo(double positionX, double positionY) {
        setPositionX(positionX);
        setPositionY(positionY);
        System.out.println("New positions of the " + this.nickname + " is:"
                + "\nX: " + this.positionX
                + "\tY: " + this.positionY);
    }

    // Simulate a battle between the current player and an opponent
    // to advise the current player if he should attack
    // The method returns 'true' if the current player would win the battle and 'false' otherwise
    public boolean shouldAttackOpponent(PlayerStatus opponent) {
        // Check if the players are alive
        if (this.health <= 0) {
            System.out.println("You are a dead player, you can no longer fight!");
        } else if (opponent.getHealth() <= 0) {
            System.out.println("Your opponent is a dead player, you can't fight with him!");
        }

        if (this.weaponInHand != null && opponent.getWeaponInHand() != null) {
            if (this.weaponInHand.equalsIgnoreCase(opponent.getWeaponInHand())) {
                int probPlayer = (3 * this.health + this.score / 1_000) / 4;
                int probOpponent = (3 * opponent.getHealth() + opponent.getScore() / 1_000) / 4;
                return probPlayer >= probOpponent;
            } else {
                double distanceBetweenPlayers = playersDistance(opponent);

                if (distanceBetweenPlayers > 1_000) {
                    if (this.weaponInHand.equalsIgnoreCase("sniper")) {
                        return true;
                    }
                    return this.weaponInHand.equalsIgnoreCase("kalashnikov")
                            && opponent.getWeaponInHand().equalsIgnoreCase("knife");

                } else if (distanceBetweenPlayers <= 1_000) {
                    if (this.weaponInHand.equalsIgnoreCase("kalashnikov ")) {
                        return true;
                    }
                    return this.weaponInHand.equalsIgnoreCase("sniper ")
                            && opponent.getWeaponInHand().equalsIgnoreCase("knife");
                }
            }
        }
        return false;
    }

    // Simulate a battle between two players
    public void attackOpponent(PlayerStatus opponent) {
        // Check if the players are alive
        if (this.health <= 0) {
            System.out.println("You are a dead player, you can no longer fight!");
        } else if (opponent.getHealth() <= 0) {
            System.out.println("Your opponent is a dead player, you can't fight with him!");
        }

        // Check if both players have weapon in hands
        if (this.weaponInHand != null && opponent.getWeaponInHand() != null) {
            // Check if both weapons are the same
            if (this.weaponInHand.equalsIgnoreCase(opponent.getWeaponInHand())) {
                int probPlayer = (3 * this.health + this.score / 1_000) / 4;
                int probOpponent = (3 * opponent.getHealth() + opponent.getScore() / 1_000) / 4;

                if (probPlayer > probOpponent) {
                    System.out.println("You won the battle with the same weapon!");
                    System.out.println("You have won 10.000 points from your opponent.");
                    this.score += 10_000;
                    opponent.score -= 10_000;
                    opponent.setHealth(opponent.getHealth() - 100);
                } else if (probPlayer < probOpponent) {
                    System.out.println("You lost the battle fighting with the same weapon!");
                    System.out.println("You have lost 10.000 points to your opponent.");
                    this.score -= 10_000;
                    this.health -= 100;
                    opponent.score += 10_000;
                } else {
                    System.out.println("The battle ended in a split draw.");
                    System.out.println("You both lost 5.000 points during the battle.");
                    this.score -= 5_000;
                    this.health -= 50;
                    opponent.score -= 5_000;
                    opponent.setHealth(opponent.getHealth() - 50);
                }
            } else { // otherwise weapons are different
                double distanceBetweenPlayers = playersDistance(opponent);

                if (distanceBetweenPlayers > 1_000) {
                    if (this.weaponInHand.equalsIgnoreCase("sniper")
                            || this.weaponInHand.equalsIgnoreCase("kalashnikov")
                            && opponent.getWeaponInHand().equalsIgnoreCase("knife")) {
                        System.out.println("You won the battle fighting with different weapons!");
                        System.out.println("You have won 10.000 points from your opponent.");
                        this.score += 10_000;
                        opponent.score -= 10_000;
                        opponent.setHealth(opponent.getHealth() - 100);
                    } else {
                        System.out.println("You lost the battle fighting with different weapons!");
                        System.out.println("You have lost 10.000 points to your opponent.");
                        this.score -= 10_000;
                        this.health -= 100;
                        opponent.score += 10_000;
                    }
                } else if (distanceBetweenPlayers <= 1_000) {
                    if (this.weaponInHand.equalsIgnoreCase("kalashnikov ")
                            || this.weaponInHand.equalsIgnoreCase("sniper ")
                            && opponent.getWeaponInHand().equalsIgnoreCase("knife")) {
                        System.out.println("You won the battle fighting with different weapons!");
                        System.out.println("You have 10.000 points from your opponent.");
                        this.score += 10_000;
                        opponent.score -= 10_000;
                        opponent.setHealth(opponent.getHealth() - 100);
                    } else {
                        System.out.println("You lost the battle fighting with different weapons!");
                        System.out.println("You have lost 10.000 points to your opponent.");
                        this.score -= 10_000;
                        this.health -= 100;
                        opponent.score += 10_000;
                    }
                }
            }
        } else if (this.weaponInHand != null || opponent.getWeaponInHand() == null) {
           System.out.println("You won the battle because your opponent doesn't have a weapon!");
           System.out.println("You have won 2.000 points from your opponent.");
           this.score += 2_000;
           opponent.score -= 2_000;
           opponent.setHealth(opponent.getHealth() - 100);
        } else if (this.weaponInHand == null || opponent.getWeaponInHand() != null) {
           System.out.println("You lost the battle because you doesn't have a weapon!");
           System.out.println("You have lost 2.000 points to your opponent.");
           this.score -= 2_000;
           this.health -= 100;
           opponent.score += 2_000;
        } else {
           System.out.println("You are both losers!!!");
           System.out.println("You both have no weapons!!!");
        }

        // Update lives and health if necessary
        if (this.health <= 0) {
            if (this.lives > 0) {
                this.lives--;
                this.health = 100;
            }
        } else if (opponent.getHealth() <= 0) {
            if(opponent.getLives() > 0) {
                opponent.setLives(opponent.getLives() - 1);
                opponent.setHealth(100);
            }
        }
    }

    // Check if 'artifactCode' is a perfect number
    private static boolean isPerfect (int artifactCode) {
        int sumDiv = 1;
        for (int div = 2; div <= artifactCode / 2; div++) {
            if (artifactCode % div == 0) {
                sumDiv += div;
            }
        }
        return sumDiv == artifactCode;
    }

    // Check if 'artifactCode' is a prime number
    private static boolean isPrime (int artifactCode) {
        for (int i = 2; i <= artifactCode / 2; i++)
            if (artifactCode % i == 0) {
                return false;
            }
        return true;
    }

    // Check if 'artifactCode' is a trap number
    private boolean isTrap (int artifactCode) {
        int sumDigits = 0;
        while (artifactCode > 0) {
            sumDigits += artifactCode % 10;
            artifactCode /= 10;
        }
        return artifactCode % 2 == 0 && sumDigits % 3 == 0;
    }

    // Determine the distance between 2 players
    public double playersDistance(PlayerStatus opponent) {
        double x1 = this.positionX;
        double x2 = opponent.positionX;
        double y1 = this.positionY;
        double y2 = opponent .positionY;
        return Math.sqrt((y1 - y2) * (y1 - y2) + (x1 - x2) * (x1 - x2));
    }

    // Display the player's status
    public void displayPlayerStatus () {
        System.out.println("Nickname: " + getNickname()
                + "\tScore: " + getScore()
                + "\tLives: " + getLives()
                + "\tHealth: " + getHealth()
                + "\tWeapon in hand: " + getWeaponInHand()
                + "\tPosition X: " + getPositionX()
                + "\tPosition Y: " + getPositionY());
    }
}