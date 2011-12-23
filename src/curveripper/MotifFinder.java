/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package curveripper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.SortedSet;
import javax.imageio.ImageIO;

/**
 *
 * @author admin
 */
public class MotifFinder extends Thread
{

    private BufferedImage motif;
    private RequestQueue queue;
    private SortedSet<CurvePosition> posSet;
    private BigBoss boss;

    public MotifFinder(RequestQueue queue, BufferedImage motif, SortedSet<CurvePosition> posSet, BigBoss boss)
    {
        this.queue = queue;
        this.motif = motif;
        this.posSet = posSet;
        this.boss = boss;

         //System.out.println("MotifFinder created");
    }

    private CurvePosition findMotif(Request request) throws MotifException
    {
        try
        {

            //System.out.println("Reading file : " + request.getSourceImg());
            BufferedImage source = ImageIO.read(new File(request.getSourceImg()));

            for (int y = 0; y < (source.getHeight() - motif.getHeight()); y++)
            {
                for (int x = 0; x < (source.getWidth() - motif.getWidth()); x++)
                {
                    if (scanPixel(x, y, source))
                    {
                        return new CurvePosition(request.getNumber(), x, y);
                    }
                }
            }
            throw new MotifException("[" + DateUtils.now() + "] No motif found in image " + request.getNumber());
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            throw new MotifException("IO Error");
        }
    }

    private boolean scanPixel(int x, int y, BufferedImage source)
    {
        boolean isFound = true;
        for (int j = 0; j < motif.getHeight(); j++)
        {
            for (int i = 0; i < motif.getWidth(); i++)
            {
                int pixelMotif = motif.getRGB(i, j);
                int pixelSource = source.getRGB(x + i, y + j);

                if (pixelMotif != pixelSource)
                {
                    isFound = false;
                    break;
                }
            }
        }
        return isFound;
    }

    @Override
    public void run()
    {
        Request request;

        //System.out.println("Starting finder " + this.getName());
        while (!queue.isEmpty())
        {
            request = queue.pullRequest();     // get next available order
            //System.out.println(request.toString() + " processed by " + this.getName());

            try
            {
                CurvePosition pos = findMotif(request);
                System.out.println("[" + DateUtils.now() + "] Motif@" + pos.getPosX() + " x " + pos.getPosY() + " in image " + request.getNumber());
                synchronized (posSet)
                {
                    posSet.add(pos);
                }
            }
            catch (MotifException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        boss.notifyDeath(this.getName());
    }
}
