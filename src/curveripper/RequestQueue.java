/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package curveripper;

import java.util.LinkedList;

/**
 *
 * @author admin
 */
public class RequestQueue {

        private LinkedList<Request> requestQueue = new LinkedList<Request>();

    public RequestQueue()
    {
        System.out.println(Thread.currentThread().getName() + " is instantiating the order queue");
    }

    public synchronized void pushRequest(Request request)
    {
        requestQueue.addLast(request);
        notifyAll();                              // notify any waiting threads that an order has been added
    }

    public synchronized Request pullRequest()
    {
        while (requestQueue.size() == 0)            // if there are no orders in the queue, wait
        {
            try
            {
                System.out.println(Thread.currentThread().getName() + " is waiting");
                wait();
            }
            catch (InterruptedException e)
            {}
        }
        return requestQueue.removeFirst();
    }

    public synchronized boolean isEmpty()
    {
        return (requestQueue.size() == 0);
    }

    public synchronized int getRequestNumber()
    {
        return requestQueue.size();
    }
}
