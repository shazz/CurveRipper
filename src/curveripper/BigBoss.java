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
import joptsimple.*;
import static java.util.Arrays.*;

/**
 *
 * @author admin
 */
public class BigBoss
{
    private int aliveMf = 0;
    private SortedSet<CurvePosition> positionSet;

    public synchronized void giveOrders(String motifUrl, String filePattern, String fileExtension, String srcFolder, int nbSrcFiles, int nbFinders)
    {
        BufferedImage motif = null;
        RequestQueue queue = new RequestQueue();

        positionSet = Collections.synchronizedSortedSet(new TreeSet<CurvePosition>(new CurvePosition()));

        try
        {
            // read motif
            System.out.println("Reading motif file : " + (srcFolder + motifUrl));
            motif = ImageIO.read(new File(srcFolder + motifUrl));

            // fill the process queue
            System.out.println("Fill the request queue");
            for (int i = 0; i < nbSrcFiles; i++)
            {
                Request request = new Request(srcFolder + filePattern + intToString(i + 1, 3) + "." + fileExtension, i + 1);
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
        String motifUrl = "motif.bmp";
        String pattern = "CUDDLY_P_";
        String fileExt = "bmp";
        String srcFolder = "D:\\Apps\\STEem\\screenshots\\";
        int nbFiles = 557;
        int nbFinders = 16;

        try
        {
            OptionSet options = null;
            OptionParser parser = new OptionParser()
            {
                {
                    acceptsAll(asList("m", "motif"), "image motif").withRequiredArg().ofType(String.class).describedAs("Motif image file");
                    acceptsAll(asList("p", "pattern"), "source image naming pattern").withRequiredArg().ofType(String.class).describedAs("ex CUDDLY_P_ ");
                    acceptsAll(asList("e", "extension"), "source images extension").withRequiredArg().ofType(String.class);
                    acceptsAll(asList("s", "sourcefolder"), "source images folder").withRequiredArg().ofType(String.class);
                    acceptsAll(asList("n", "number"), "source image number").withRequiredArg().ofType(Integer.class);
                    acceptsAll(asList("t", "threadsFinder"), "number of fidner threads").withOptionalArg().ofType(Integer.class).defaultsTo(16);
                    acceptsAll(asList("h", "?"), "(opt) show help");
                }
            };
            try
            {
                options = parser.parse(args);
                if (options.has("?"))
                {
                    parser.printHelpOn(System.out);
                    System.exit(1);
                }
                if (options.has("motif"))           motifUrl = (String) options.valueOf("motif");
                else throw new Exception("motif argument is missing");
                if (options.has("pattern"))         pattern = (String) options.valueOf("pattern");
                else throw new Exception("pattern argument is missing");
                if (options.has("extension"))       fileExt = (String) options.valueOf("extension");
                else throw new Exception("extension argument is missing");
                if (options.has("sourcefolder"))    srcFolder = (String) options.valueOf("sourcefolder");
                else throw new Exception("sourcefolder argument is missing");
                if (options.has("number"))          nbFiles = (Integer) options.valueOf("number");
                else throw new Exception("number argument is missing");
                if (options.has("threadsFinder"))   nbFinders = (Integer) options.valueOf("threadsFinder");
            }
            catch (Exception ex)
            {
                System.err.println(ex.getMessage());
                parser.printHelpOn(System.out);
                System.exit(1);
            }
        }
        catch (IOException ex)
        {
            System.err.println(ex.getMessage());
        }
        catch(OptionException ex2)
        {
            ex2.printStackTrace();
        }

        BigBoss boss = new BigBoss();
        boss.giveOrders(motifUrl, pattern, fileExt, srcFolder, nbFiles, nbFinders);
    }

    public static void manageArgs(String[] args)
    {

        

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
