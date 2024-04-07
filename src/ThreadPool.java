// ThreadPool
// Implementation of a thread pool to manage worker threads
// Keeps client connections in a queue

import java.util.concurrent.LinkedBlockingDeque;

public class ThreadPool {

    private final WorkerThread[] workerThreads;
    private final LinkedBlockingDeque<Runnable> taskQueue;

    public ThreadPool(int numThreads) {
        this.taskQueue = new LinkedBlockingDeque<>();
        this.workerThreads = new WorkerThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            workerThreads[i] = new WorkerThread(taskQueue);
            workerThreads[i].start();
        }
    }

    public void addTask(Runnable task) {
        taskQueue.add(task);
    }

}
