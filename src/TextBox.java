import java.awt.*;

class TextBox {

    private final int width;
    private final int height;
    private final int maxChar;
    private final int locX;
    private final int locY;
    private boolean isActive;
    private String inputString;
    private final Font textBoxFont;

    /**
     * Initializes the text box.
     * @param locX location of x
     * @param locY location of y
     * @param width width
     * @param height height
     * @param maxChar Maximum number of characters
     * @param size size
     */
    public TextBox(int locX, int locY, int width, int height, int maxChar,int size){
        this.locX = locX;
        this.locY = locY;
        this.width = width;
        this.height = height;
        this.maxChar = maxChar;
        this.isActive = false;
        this.inputString = "";
        this.textBoxFont = new Font("times-roman",Font.BOLD,size);
    }

    /**
     * Adds a character if there is space left
     * @param c Character being added
     */
    private void addChar(char c){
        if(inputString.length()<maxChar){
            inputString += c;
        }
    }

    /**
     * Removes a character
     */
    private void removeChar(){
        inputString = inputString.substring(0,inputString.length()-1);
    }

    /**
     * Draws the text box to the screen
     * @param game The game that is currently active
     * @param g The graphics being drawn to the screen
     */
    public void drawTextBox(Game game, Graphics g){
        g.setColor(Colors.ColorAlpha(Colors.whites,200));
        g.fillRoundRect(locX,locY,width,height,4,4);
        g.drawRoundRect(locX,locY,width,height,4,4);

        g.setColor(Colors.ColorAlpha(Colors.darkBlues,255));
        g.setFont(textBoxFont);
        g.drawString(inputString,locX,locY+(int)(height*0.8));
        g.setColor(Colors.ColorAlpha(Colors.lightBlues,255));
        g.drawString(inputString.length()+" / "+maxChar,locX+width+15,locY+(int)(height/1.5));

        if(this.isActive&&Math.sin(game.veryFastConstantCounter)>-0){
            TextBox.drawBar(game,g,this);
        }

        if(this.isActive){
            if(KeyboardManager.keyPressed){
                if(((KeyboardManager.keyCode == 46)||(KeyboardManager.keyCode == 32)||KeyboardManager.keyCode > 64 &&
                        KeyboardManager.keyCode< 91)||(KeyboardManager.keyCode > 96 && KeyboardManager.keyCode< 123)||
                        ((KeyboardManager.keyCode > 47 && KeyboardManager.keyCode < 58))){
                    this.addChar(KeyboardManager.keyCode);
                }
                if(KeyboardManager.keyCode == 8 && this.inputString.length() > 0){
                    this.removeChar();
                }
                KeyboardManager.keyPressed = false;
            }
        }

    }

    /**
     * Sets the text box to active or not active, depending on where the user has clicked and is typing.
     * @param active Boolean to check active or not active
     */
    public void setActive(boolean active){
        this.isActive = active;
    }

    /**
     * Draws the currently active text box.
     * @param game Game object
     * @param g Graphics being drawn to the screen
     * @param activeBox Currently active text box
     */
    private static void drawBar(Game game,Graphics g,TextBox activeBox){
        g.setColor(Colors.ColorAlpha(Colors.blacks,255));
        g.fillRoundRect(2+(((game.width-(Display.centerString(activeBox.inputString,g,activeBox.textBoxFont,game)))-
                (game.width/2))*2)+(activeBox.locX),activeBox.locY+3,5,activeBox.height-6,2,4);

    }

    /**
     * Determines whether or not the text box is currently hovered over.
     * @param x Mouse x coordinate
     * @param y Mouse y coordinate
     * @return If it's within the box's boundaries (as determined by x and y), return true. Else, return false.
     */
    public boolean isHovered(int x, int y){
        return(x>=locX&&y>locY&& x < (locX+width+5)&& y <(locY + height+5));
    }

    /**
     * Gets the string input.
     * @return The string input
     */
    public String getInputString(){
        return this.inputString;
    }

}
