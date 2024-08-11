package renderer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides a service for rendering pixels with parallelization
 */
public class PixelExecutor {
    /**
     * The total amount of pixels in the image
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
     * The executor (only executes pixels...)
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
    /**
     * Lock to ensure thread-safe access to pixel indexes
     */
    private final ReentrantLock lock;

    /**
     * Initializes the executor
     *
     * @param numberOfThreads  the amount of threads for the thread pool
     * @param horizontalPixels the amount of horizontal pixels for the image
     * @param verticalPixels   the amount of vertical pixels for hte image
     */
    public PixelExecutor(int numberOfThreads, int horizontalPixels, int verticalPixels) {
        this.verticalPixels = verticalPixels;
        this.horizontalPixels = horizontalPixels;
        this.totalPixelsCount = verticalPixels * horizontalPixels;

        this.executor = Executors.newFixedThreadPool(numberOfThreads);
        this.completedPixelsCounter = new AtomicInteger(0);
        this.lock = new ReentrantLock();
    }

    /**
     * Submit a new pixel to the executor
     *
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
     * Invoked when a pixel rendering task is completed. It checks if all pixels
     * have been rendered and, if so, shuts down the executor. also responsible for triggering the
     * pixel completion listener.
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
     *
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

    /**
     * Functional-Interface for a callback method to be invoked when a pixel is completed
     * rendering
     */
    public interface PixelCompleteListener {
        /**
         * Called when a pixel has completed rendering
         */
        void onPixelComplete();
    }

    /**
     * Immutable class for object containing allocated pixel (with its row and
     * column numbers)
     *
     * @param col the column index of the pixel - x value
     * @param row the row index of the pixel - y value
     */
    record Pixel(int col, int row) {
    }

    /**
     * Set a method to be called when a pixel is done rendering
     *
     * @param listener object that implements the PixelCompleteListener interface
     */
    public void setPixelCompleteListener(PixelCompleteListener listener) {
        this.listener = listener;
    }

    /**
     * Starts the rendering operation and waits until all pixels are rendered and
     * the executor is shut down.
     */
    public void render() {
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
