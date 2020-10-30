import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class is used to listen to the keyboard, allowing the user to type into specific places to advance the program.
 */
class KeyboardManager implements KeyListener {

    public static char keyCode;
    public static boolean keyPressed;

    /**
     * Default state of the keyboard manager, basically a standby state of sorts.
     */
    public KeyboardManager(){
        keyPressed = false;
        keyCode = 0;
    }

    /**
     * This is to detect when a key is typed.
     * @param e Event to detect the typing of a key.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        keyPressed = true;
        keyCode = e.getKeyChar();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
