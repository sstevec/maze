package MazeGame;

public abstract class Creature {
    private int currentHealth = 100;
    private int maxHealth = 100;
    private int teamNumber;

    public Creature(int currentHealth, int maxHealth, int teamNumber){
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.teamNumber = teamNumber;
    }

    public void takeDamage(int damage){
        currentHealth = currentHealth - damage;
        if(currentHealth <= 0){
            die();
        }
    }

    public abstract void die();

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }
}
