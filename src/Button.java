import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * The Button class is used to create buttons which are able to be interacted with by the user.  Highlight when hovered,
 * and an action taken while clicked (Although the clicking check is expected to come from outside this class).
 */
class Button{

    // Class variables
    private static Button selectedButton = null; // Button last hovered by the mouse.

    // Class method

    /**
     * This method draws two oscillating arrows next to a button if it is currently being hovered and is clickable.
     * @param g - Game
     */
    public static void drawButtonShine(Graphics g, Game game){
        //Draws the arrows on both sides of the button.
        if(selectedButton != null && selectedButton.getClickable()){
            g.setColor(Colors.shineColor);
            g.fillRoundRect(selectedButton.x+(selectedButton.width/2)-(game.width/200)+(int)(Math.sin(game.mediumConstantCounter)*((selectedButton.width/2.05)-(game.width/200))),selectedButton.y-selectedButton.height, game.width/100,selectedButton.height,4,10);
            Button.selectedButton = null;
            game.setCursor(false);
        }else{
            game.mediumConstantCounter = -1;
        }
    }

    /**
     * The drawButtons function will draw all buttons in a given gameState.  All the State has to do is call this class
     * function and all buttons will be drawn in their proper locations.
     * @param g - Graphics object
     * @param game - Game object
     * @param buttons - An arrayList of all buttons that need to be drawn.
     */
    public static void drawButtons(Graphics g,Game game, ArrayList<Button> buttons,int offsetX, int offsetY){
        for (Button button : buttons) {
            button.drawButton(g, game,offsetX,offsetY);
        }
    }


    // Fields
    private final int y, roundedness;
    public final String label; // Name of the button
    private final Color baseBorderColor, highlightedBorderColor, mainColor, highlightedMainColor, textBaseColor, textHighlightedColor;
    private final int type;
    private int x;
    private int width;
    private int height;
    private int stringLocX;
    private int stringLocY;
    private boolean clickable;
    private Font textFont;
    private final BufferedImage image;

    /**
     * The full constructor takes a lot of parameters to allow for a variety of specific types/looks of Button objects.
     * @param game - Game
     * @param g - Graphics 2D
     * @param label - Name of the button
     * @param x - x location
     * @param y - y location
     * @param width - width
     * @param height - height
     * @param textBaseColor - Unhovered text color - usually black
     * @param textHighlightedColor - Hovered text color - usually white
     * @param mainColor - main, unhovered rectangle color
     * @param highlightedMainColor - main, hovered color
     * @param baseBorderColor - unhovered border color
     * @param highlightedBorderColor - hovered border color
     * @param fontSize - font size of text
     * @param rectRoundedness - roundedness of the button
     * @param centered - whether or not it should be centered horizontally(Overrides x value input).
     * @param clickable - Whether or not the button is currently clickable
     */
    public Button(Game game,Graphics g,int type,BufferedImage image, String label, int x, int y, int width, int height, Color textBaseColor,
                  Color textHighlightedColor, Color mainColor,Color highlightedMainColor, Color baseBorderColor,
                  Color highlightedBorderColor, int fontSize, int rectRoundedness, boolean centered, boolean clickable){

        this.x = x;
        if(centered){
            this.x = (game.width/2)-(this.width/2);
        }
        this.y = y;
        this.image = image;

        this.width = width;
        this.height = height;
        this.label = label;
        this.changeFont(g, new Font("SansSerif", Font.BOLD, fontSize));
        this.roundedness = rectRoundedness;
        this.type = type;

        // Makes sure that the given font and text fit inside of the given width and height of the button.
        updateTextSize(g, width, height);
        // Set all colors.
        this.textBaseColor = textBaseColor;
        this.textHighlightedColor = textHighlightedColor;
        this.mainColor = mainColor;
        this.baseBorderColor = baseBorderColor;
        this.highlightedMainColor = highlightedMainColor;
        this.highlightedBorderColor = highlightedBorderColor;
        this.clickable = clickable;
    }

    /**
     * Secondary constructor, used when a text only button.
     * @param game - Game
     * @param g - Graphics 2D
     * @param label - Name of the button
     * @param x - x location
     * @param y - y location
     * @param textBaseColor - Unhovered text color - usually black
     * @param textHighlightedColor - Hovered text color - usually white
     * @param fontSize - font size of text
     * @param centered - whether or not it should be centered horizontally(Overrides x value input).
     * @param clickable - Whether or not the button is currently clickable
     */
    public Button(Game game, Graphics g, String label, int x, int y, Color textBaseColor, Color textHighlightedColor,
                  int fontSize, boolean centered, boolean clickable){
        this(game,g,0,null,label,x,y,0,0,textBaseColor,textHighlightedColor,null,null,
                null,null, fontSize,0,centered, clickable);
    }

    /**
     * Alternate Button Constructor
     * @param game - game
     * @param g - g
     * @param image - image
     * @param x - x
     * @param y - y
     * @param clickable - clickable
     */
    public Button(Game game, Graphics g, BufferedImage image, int x, int y,int width, int height, boolean clickable){
        this(game,g,1,image,"",x,y,width,height,null,null,null,
                null,null,null,0,0,false,clickable);
    }

    /**
     * This method draws the button to the screen given a graphics object.  Calls either the hovered or unhovered draw
     * methods depending on which currently needs to be done.
     * @param g - Graphics 2D
     * @param game - Game
     */
    public void drawButton(Graphics g,Game game,int offsetX, int offsetY){
        if(this.type==0){
            if(clickable){
                if(!checkButtonHovered(game)){
                    drawUnhoveredButton(g, offsetX,offsetY);
                }else{
                    drawHoveredButton(g,game,offsetX,offsetY);
                }
            }else{
                drawUnhoveredButton(g, offsetX,offsetY);
            }
        }else{
            if(image.equals(Assets.stars.get(0))){
                g.drawImage(Assets.stars.get((Math.abs(game.counter)%29)+1),x+offsetX,y+offsetY,null);
            }else{
                g.drawImage(image,x+offsetX,y+offsetY,null);
                if(this.checkButtonHovered(game)&& this.getClickable()){
                    game.setCursor(false);
                }
            }
        }
    }

    /**
     * Used by the State classes in order to set whether or not a button is clickable.
     * @param clickable - True if clickable, false if not clickable.
     */
    public void setClickable(boolean clickable){
        this.clickable = clickable;
    }

    /**
     * This method checks to see if the button is currently being hovered given a Game object which has access to
     * the x and y coordinates of the mouse and window.
     * @param game - Game
     * @return - boolean: True if hovered, false if not hovered.
     */
    public boolean checkButtonHovered(Game game){
        try{
            int mouseX = (int)MouseInfo.getPointerInfo().getLocation().getX() - game.getDisplay().getFrameX()-5;
            int mouseY = (int)MouseInfo.getPointerInfo().getLocation().getY() - game.getDisplay().getFrameY();
            if(this.image==null){
                return !(mouseX < x||mouseX > (x+width)||mouseY < this.y-height|| mouseY > this.y);
            }else{
                return !(mouseX < x||mouseX > (x+width)||mouseY > this.y+height|| mouseY < this.y);
            }
        }catch(Exception e){
            return false;
        }

    }

    /**
     * Used by State classes to check if a button is clickable or not.
     * @return - boolean: True if clickable, false if not.
     */
    private boolean getClickable(){
        return clickable;
    }

    /**
     * Updates the font of a given button.  Automatically makes sure that the button size still works with the new font,
     * and if not, expands the button size.
     * @param g - Graphics 2D
     * @param desiredFont - Desired font.
     */
    private void changeFont(Graphics g, Font desiredFont){
        this.textFont = desiredFont;
        updateTextSize(g, this.width, this.height);
    }

    /**
     * This method draws the button in its hovered state.
     * @param g - Graphics 2D
     */
    private void drawHoveredButton(Graphics g,Game game,int offsetX, int offsetY){
        selectedButton = this;
        g.setFont(textFont);
        int buttonx = this.x+3+offsetX;
        int buttony = this.y+3+offsetY-height;
        if(buttonx < -0.2*game.width||buttonx>1.2*game.width||buttony<-0.2*game.height||buttony>1.2*game.height){
            return;
        }

        if(mainColor != null){
            g.setColor(highlightedBorderColor);
            g.fillRoundRect(buttonx-3,buttony-3,width,height,roundedness,roundedness);
            g.setColor(highlightedMainColor);
            g.fillRoundRect(buttonx,buttony,width-6,height-6,roundedness,roundedness);
        }

        g.setColor(textHighlightedColor);
        g.drawString(label,stringLocX,stringLocY);
        // Makes the button look faded / un-clickable.
        if(!clickable){
            g.setColor(Colors.greyedOut);
            g.fillRoundRect(buttonx,buttony,width,height,roundedness,roundedness);
        }
    }

    /**
     * This method draws the button in its unhovered state.
     * @param g - Graphics 2D
     */
    private void drawUnhoveredButton(Graphics g, int offsetX, int offsetY){
        g.setFont(textFont);
        int buttonX = this.x+offsetX;
        int buttonY = this.y+offsetY-height;
        // Draws the border and the button
        if(mainColor != null){
            g.setColor(baseBorderColor);
            g.fillRoundRect(buttonX,buttonY,width,height,roundedness,roundedness);
            g.setColor(mainColor);
            g.fillRoundRect(buttonX+3,buttonY+3,width-6,height-6,roundedness,roundedness);
        }

        g.setColor(textBaseColor);
        g.drawString(label,stringLocX,stringLocY);

        // Makes the button look faded / un-clickable.
        if(!clickable){
            g.setColor(Colors.greyedOut);
            g.fillRoundRect(buttonX,buttonY,width,height,roundedness,roundedness);
        }

    }

    /**
     * Updates the width and height of the button in order to ensure that the text does not go outside of the box.
     * @param g - Graphics 2D
     * @param width - width of button
     * @param height - height of button
     */
    private void updateTextSize(Graphics g, int width, int height){
        int textSizeX = calcTextWidth(g);
        int textSizeY = calcTextHeight(g);
        if(textSizeX > width){
            this.width = textSizeX;
        }
        if(textSizeY > height){
            this.height = textSizeY;
        }
        stringLocX = x+(this.width/2)-(g.getFontMetrics(textFont).stringWidth(label)/2);
        stringLocY = (y+g.getFontMetrics(textFont).getHeight()/4)-(this.height/2);
    }

    /**
     * Used to calculate how wide text will be given the button text and a font.
     * @param g - Graphics 2D
     * @return - The width of the text on screen.
     */
    private int calcTextWidth(Graphics g){
        return g.getFontMetrics(textFont).stringWidth(label);
    }

    /**
     * Used to calculate how tall text will be given the button text and a font.
     * @param g - Graphics 2D
     * @return - The height of the text on screen.
     */
    private int calcTextHeight(Graphics g){
        return g.getFontMetrics(textFont).getHeight();
    }
}