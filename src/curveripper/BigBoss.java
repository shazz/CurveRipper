/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package curveripper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.imageio.ImageIO;

/**
 *
 * @author admin
 */
public class BigBoss
{

    private int aliveMf = 0;
    private SortedSet<CurvePosition> positionSet;

    public synchronized void giveOrders()
    {
        BufferedImage motif = null;
        RequestQueue queue = new RequestQueue();

        String motifUrl = "D:\\Apps\\STEem\\screenshots\\motif.bmp";
        String pattern = "CUDDLY_P_";
        String fileExt = ".bmp";
        String srcFolder = "D:\\Apps\\STEem\\screenshots\\";

        int nbSrcFiles = 500;
        int nbFinders = 10;

        positionSet = Collections.synchronizedSortedSet(new TreeSet<CurvePosition>(new CurvePosition()));

        try
        {
            // read motif
            motif = ImageIO.read(new File(motifUrl));

            // fill the process queue
            System.out.println("Fill the request queue");
            for (int i = 0; i < nbSrcFiles; i++)
            {
                Request request = new Request(srcFolder + pattern + intToString(i + 1, 3) + fileExt, i + 1);
                queue.pushRequest(request);
            }
            System.out.println("Queue has " + queue.getRequestNumber() + " request waiting");

            // run threads
            System.out.println("Starting " + nbFinders + " finders");
            for (int i = 0; i < nbFinders; i++)
            {
                MotifFinder mf = new MotifFinder(queue, motif, positionSet, this);
                aliveMf++;
                mf.start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public synchronized void notifyDeath(String who)
    {
        System.out.println("Thread " + who + " has died");
        aliveMf--;

        if (aliveMf == 0)
        {
            displayResults();
        }
    }

    private void displayResults()
    {
        synchronized (positionSet)
        {
            Iterator<CurvePosition> i = positionSet.iterator(); // Must be in synchronized block
            while (i.hasNext())
            {
                CurvePosition cp = i.next();
                System.out.println("[" + cp.getNumber() + "] " + cp.getPosX() + " x " + cp.getPosY());
            }
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        BigBoss boss = new BigBoss();
        boss.giveOrders();
    }

    static String intToString(int num, int digits)
    {
        assert digits > 0 : "Invalid number of digits";

        // create variable length array of zeros
        char[] zeros = new char[digits];
        Arrays.fill(zeros, '0');
        // format number as String
        DecimalFormat df = new DecimalFormat(String.valueOf(zeros));

        return df.format(num);
    }
}
