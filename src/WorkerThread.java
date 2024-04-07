// WorkerThread
// Worker thread implementation for ThreadPool

import java.util.concurrent.LinkedBlockingDeque;

public class WorkerThread extends Thread {

    private final LinkedBlockingDeque<Runnable> taskQueue;

    public WorkerThread(LinkedBlockingDeque<Runnable> taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Runnable task = taskQueue.take();
                task.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
