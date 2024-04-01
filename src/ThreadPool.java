import java.util.concurrent.LinkedBlockingDeque;

public class ThreadPool {

    private WorkerThread[] workerThreads;
    private LinkedBlockingDeque<Runnable> taskQueue;

    public ThreadPool(int numThreads) {
        this.taskQueue = new LinkedBlockingDeque<>(numThreads);
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
