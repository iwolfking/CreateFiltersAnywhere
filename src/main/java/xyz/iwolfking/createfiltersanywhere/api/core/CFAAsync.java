package xyz.iwolfking.createfiltersanywhere.api.core;

import xyz.iwolfking.createfiltersanywhere.CreateFiltersAnywhere;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static xyz.iwolfking.createfiltersanywhere.api.core.CFACache.iterateCache;

public class CFAAsync {
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private static Future<?> currentTask;
    private static boolean isShuttingDown = false;

    public static void asyncIterateCache() {
        if (currentTask != null && !currentTask.isDone()) {
            currentTask.cancel(true);
        }
        currentTask = EXECUTOR.submit(()-> {
            try {
                iterateCache();
            } catch(Exception e) {
                CreateFiltersAnywhere.LOGGER.info("Cache clearing failed");
            }
        });
    }
    public static void shutdownAsync() {
        if (!isShuttingDown) {
            isShuttingDown = true;
            EXECUTOR.shutdownNow();
            EXECUTOR.close();
        }

    }
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(CFAAsync::shutdownAsync));
        CreateFiltersAnywhere.LOGGER.info("shutting down extra thread");
    }
}
