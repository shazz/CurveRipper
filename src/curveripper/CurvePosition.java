/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package curveripper;

import java.util.Comparator;

/**
 *
 * @author admin
 */
public class CurvePosition implements Comparator
{

    private int number;
    private int posX;
    private int posY;

    public CurvePosition(int number, int posX, int posY)
    {
        this.posX = posX;
        this.posY = posY;
        this.number = number;
    }

    public CurvePosition()
    {
    }

    public int getPosX()
    {
        return posX;
    }

    public void setPosX(int posX)
    {
        this.posX = posX;
    }

    public int getPosY()
    {
        return posY;
    }

    public void setPosY(int posY)
    {
        this.posY = posY;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public int compare(Object o1, Object o2)
    {
        CurvePosition cv1 = (CurvePosition) o1;
        CurvePosition cv2 = (CurvePosition) o2;

        if (cv1.getNumber() < cv2.getNumber())
        {
            return -1;
        }
        else if (cv1.getNumber() > cv2.getNumber())
        {
            return +1;
        }
        else
        {
            return 0;
        }
    }

}
