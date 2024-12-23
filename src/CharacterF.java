public class CharacterF {
    
    private String character;
    private int frequency;

    public CharacterF(String character, int frequency) {

        this.character = character;
        this.frequency = frequency;
    }

    //Setters
    public void setCharacter(String character) {this.character = character;}
    public void setFrequency(int frequency) {this.frequency = frequency;}

    //Getters
    public String getCharacter() {return this.character;}
    public int getFrequency() {return this.frequency;}

    @Override
    public String toString() {

        return "Character: " + this.getCharacter() + "\nFrequency: " + this.getFrequency() + "\n";
    }
}
