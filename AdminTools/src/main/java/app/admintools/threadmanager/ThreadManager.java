/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.admintools.threadmanager;

import java.util.ArrayList;

/**
 *
 * @author lukak
 */
public class ThreadManager {

    private static ArrayList<String> singleThreadPool = new ArrayList<String>();

    /**
     * Managed starting of threads. Beware worker thread management uses names
     *
     * @param thread Thread that is going to be started.
     * @param type Type of thread. If the ThreadType is <b>WORKER</b> the thread
     * will be started only if its the only thread of its type in the
     * application, and if its ThreadType is <b>ASYNCJOB</b> then it starts
     * anyways.
     * @return If the thread has been started
     */
    public static boolean startThread(Thread thread, ThreadType type) {
        if (type == ThreadType.WORKER) {
            if (!singleThreadPool.contains(thread.getName())) {
                singleThreadPool.add(thread.getName());
                thread.start();
                System.out.println("Started thread " + thread.getName());
                return true;
            }
            return false;
        }
        if (type == ThreadType.ASYNCJOB) {
            thread.start();
            return true;
        }
        return false;
    }
}
