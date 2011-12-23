/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package curveripper;

/**
 *
 * @author admin
 */
class Request
{
    private String sourceImg;
    private int number;

    public Request(String sourceImg, int number)
    {
        this.sourceImg = sourceImg;
        this.number = number;
    }

    public int getNumber()
    {
        return number;
    }

    public String getSourceImg()
    {
        return sourceImg;
    }

    

}
