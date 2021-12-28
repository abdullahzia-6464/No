package SineOfMadness;


// Abstract class for the player and the enemies
public abstract class Character extends Entity {
    private float health;
    private float moveSpeed;  // Entity movement speed.

    private boolean isAlive = true;  // Is the entity alive
    private boolean isRight = true;  // Is the character facing right, used for displaying different animations

    // Variables to make sure there is a delay between damage when Bucky collides with an enemy
    protected long timePassedSinceDamage = 200000, timeOfDamage = System.currentTimeMillis();

    // GETTERS AND SETTERS

    // HEALTH
    public void setHealth(float health) {
        this.health = health;
    }

    public float getHealth() {
        return health;
    }

    // MOVE SPEED
    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    // isALIVE
    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isAlive(){
        return isAlive;
    }

    // isRIGHT
    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }
}
