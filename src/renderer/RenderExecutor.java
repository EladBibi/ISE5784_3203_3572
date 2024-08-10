package renderer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Render executor for rendering an image with parallelization
 */
public class RenderExecutor {
    /**
     * The total amount of pixel in the image
     */
    private final int totalPixelsCount;
    /**
     * The amount of pixel in each column - the y-axis
     */
    private final int verticalPixels;
    /**
     * The amount of pixel in each row - the x-axis
     */
    private final int horizontalPixels;
    /**
     * The executor (executes pixels nothing else...)
     */
    private final ExecutorService executor;
    /**
     * The amount of completed(rendered) pixels
     */
    private final AtomicInteger completedPixelsCounter;
    /**
     * The current horizontal pixel being rendered
     */
    private final AtomicInteger currentHorizontalPixel = new AtomicInteger(0);//x
    /**
     * The current vertical pixel being rendered
     */
    private final AtomicInteger currentVerticalPixel = new AtomicInteger(0);//y
    /**
     * A listener for providing a callback on each pixel completion
     */
    private PixelCompleteListener listener;

    private final ReentrantLock lock;

    /**
     * Initializes the executor
     * @param numberOfThreads  the amount of threads for the thread pool
     * @param horizontalPixels the amount of horizontal pixels for the image
     * @param verticalPixels the amount of vertical pixels for hte image
     */
    public RenderExecutor(int numberOfThreads, int horizontalPixels, int verticalPixels) {
        this.verticalPixels = verticalPixels;
        this.horizontalPixels = horizontalPixels;
        this.totalPixelsCount = verticalPixels * horizontalPixels;

        this.executor = Executors.newFixedThreadPool(numberOfThreads);
        this.completedPixelsCounter = new AtomicInteger(0);
        this.lock = new ReentrantLock();
    }

    /**
     * Submit a new pixel to the executor
     * @param task the task to be added to the executor's thread pool. should be a pixel render call
     */
    public void submit(Runnable task) {
        executor.submit(() -> {
            try {
                task.run();
            } finally {
                onTaskCompleted();
            }
        });
    }

    /**
     * Called on each completion of a pixel. responsible for shutting down the executor
     * when all the pixels are rendered
     */
    private void onTaskCompleted() {
        if (completedPixelsCounter.incrementAndGet() >= totalPixelsCount) {
            if (listener != null) {
                listener.onPixelComplete();
            }
            executor.shutdown();
        }
    }

    /**
     * Gives the next pixel to be rendered
     * @return the next pixel
     */
    public Pixel nextPixel() {
        lock.lock();
        try {
            if (completedPixelsCounter.get() >= totalPixelsCount) {
                return null;
            }

            if (currentHorizontalPixel.incrementAndGet() < horizontalPixels) {
                return new Pixel(currentHorizontalPixel.get(), currentVerticalPixel.get());
            }

            currentHorizontalPixel.set(0);

            if (currentVerticalPixel.incrementAndGet() < verticalPixels) {
                return new Pixel(currentHorizontalPixel.get(), currentVerticalPixel.get());
            }

            return null;
        } finally {
            lock.unlock();
        }
    }

    // Interface for the listener
    public interface PixelCompleteListener {
        void onPixelComplete();
    }

    /**
     * Immutable class for object containing allocated pixel (with its row and
     * column numbers)
     * @param col the column index of the pixel - x value
     * @param row the row index of the pixel - y value
     * */
    record Pixel(int col, int row) {
    }

    /**
     * Set a method to be called when a pixel is done rendering
     * @param listener object that implements the PixelCompleteListener interface
     */
    public void setPixelCompleteListener(PixelCompleteListener listener) {
        this.listener = listener;
    }

    /**
     * Start the operation of the executor the call will exit when
     * the executor is done with all the pixels
     */
    public void start() {
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) {
//        RenderExecutor executor = new RenderExecutor(5, 100);
//
//        // Set the listener to react when a task is completed
//        executor.setTaskCompleteListener((taskId, threadName) ->
//                System.out.println("Task " + taskId + " completed by " + threadName)
//        );
//
//        executor.start();
//    }
}
