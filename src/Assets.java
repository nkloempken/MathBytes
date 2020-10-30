import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * The Assets class stores all of the images and other assets that will be used by the game.
 */
abstract class Assets {

    public static final File backgroundMusic, buttonClick;

    public static final BufferedImage exclamationPoint,redExclamationPoint,checkMark,downArrow,upArrow;

    public static final ArrayList<BufferedImage> stars;
    public static final ArrayList<BufferedImage> blueNums;
    public static final ArrayList<BufferedImage> redNums;
    public static final ArrayList<BufferedImage> yellowNums;
    public static final ArrayList<BufferedImage> greenNums;
    public static final ArrayList<BufferedImage> yellowOthers;
    public static final ArrayList<BufferedImage> redX;
    public static final ArrayList<BufferedImage> sparks;
    public static final ArrayList<BufferedImage> burst;

    //Current assets in the game include background music and a sound effect for button clicking
    static{
        backgroundMusic = new File("backgroundMusic.wav");
        buttonClick = new File("buttonClick.wav");
        exclamationPoint = ImageLoader.loadImage("/res/other/EP1.png");
        redExclamationPoint = ImageLoader.loadImage("/res/other/redPoint.png");
        checkMark = ImageLoader.loadImage("/res/other/checkMark2.png");
        downArrow = ImageLoader.loadImage("/res/other/DownArrow.png");
        upArrow = ImageLoader.loadImage("/res/other/UpArrow.png");

        stars = new ArrayList<>();
        for(int i = 1; i < 31;i++){
            stars.add(ImageLoader.loadImage("res/stars/s"+i+".png"));
        }

        redX = new ArrayList<>();
        for(int i = 1; i < 31;i++){
            redX.add(ImageLoader.loadImage("res/redX/x"+i+".png"));
        }

        blueNums = new ArrayList<>();
        for(int i = 0; i < 10;i++){
            blueNums.add(ImageLoader.loadImage("res/blueNums/tran"+i+"3d.png"));
        }

        redNums = new ArrayList<>();
        for(int i = 0; i < 10;i++){
            redNums.add(ImageLoader.loadImage("res/redNums/tranred"+i+"3d.png"));
        }

        yellowNums = new ArrayList<>();
        for(int i = 0; i < 10;i++){
            yellowNums.add(ImageLoader.loadImage("res/yellowNums/tranyellow"+i+"3d.png"));
        }

        greenNums = new ArrayList<>();
        for(int i = 0; i < 10;i++){
            greenNums.add(ImageLoader.loadImage("res/greenNums/trangreen"+i+"3d.png"));
        }
        yellowOthers = new ArrayList<>();
        yellowOthers.add(ImageLoader.loadImage("res/yellowNums/tranyellow'3d.png"));
        yellowOthers.add(ImageLoader.loadImage("res/yellowNums/tranyellow+3d.png"));
        yellowOthers.add(ImageLoader.loadImage("res/yellowNums/tranyellow-3d.png"));
        yellowOthers.add(ImageLoader.loadImage("res/yellowNums/tranyellow,3d.png"));
        yellowOthers.add(ImageLoader.loadImage("res/yellowNums/tranyellow=3d.png"));

        sparks = new ArrayList<>();
        burst = new ArrayList<>();
        for(int i = 5; i<10;i++){
            BufferedImage before = ImageLoader.loadImage("res/burst and spark/burst"+i+".png");
            BufferedImage after = new BufferedImage(before.getWidth(),before.getHeight(),BufferedImage.TYPE_INT_ARGB);
            AffineTransform at = new AffineTransform();
            at.scale(0.2,0.2);
            AffineTransformOp scaleOP = new AffineTransformOp(at,AffineTransformOp.TYPE_BILINEAR);
            after = scaleOP.filter(before,after);
            burst.add(after);
        }
        for(int i = 1; i<11;i++){
            BufferedImage before = ImageLoader.loadImage("res/burst and spark/spark"+i+".png");
            BufferedImage after = new BufferedImage(before.getWidth(),before.getHeight(),BufferedImage.TYPE_INT_ARGB);
            AffineTransform at = new AffineTransform();
            at.scale(0.34,0.3);
            AffineTransformOp scaleOP = new AffineTransformOp(at,AffineTransformOp.TYPE_BILINEAR);
            after = scaleOP.filter(before,after);
            sparks.add(after);
        }
    }

}