import java.awt.*;
import java.awt.image.BufferedImage;

class InputNum {


    public int value;
    public final int xLoc;
    public final int yLoc;
    public BufferedImage image;

    public InputNum(int startingValue,int xLoc,int yLoc){
        this.value = startingValue;
        this.xLoc = xLoc;
        this.yLoc = yLoc;
        this.setImage();
    }

    public void drawInputNum(Graphics g){ g.drawImage(image,xLoc-250,yLoc-250,null);
    }

    public void setProblemResult(int result){
        if(result ==0){
            image = Assets.blueNums.get(value%10);
        }else if(result ==-1){
            image = Assets.redNums.get(value%10);
        }else{
            image = Assets.greenNums.get(value%10);
        }
    }

    public void incrementValue(boolean b,boolean subtract){
        if(subtract){
            if (b){
                if(this.value<Integer.MAX_VALUE){
                    value++;
                    this.setImage();
                }
            }else{
                if(this.value>1){
                    value--;
                    this.setImage();
                }
            }
        }else{
            if (b){
                if(this.value<Integer.MAX_VALUE){
                    value++;
                    this.setImage();
                }
            }else{
                if(this.value>0){
                    value--;
                    this.setImage();
                }
            }
        }
    }

    private void setImage(){
        this.image = Assets.blueNums.get(value%10);
    }
}