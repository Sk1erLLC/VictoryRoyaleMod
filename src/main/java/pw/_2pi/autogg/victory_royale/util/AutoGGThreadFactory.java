package pw._2pi.autogg.victory_royale.util;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class AutoGGThreadFactory implements ThreadFactory
{
    private final AtomicInteger threadNumber;
    
    public AutoGGThreadFactory() {
        this.threadNumber = new AtomicInteger(1);
    }
    
    @Override
    public Thread newThread(final Runnable r) {
        return new Thread(r, "AutoGG" + this.threadNumber.getAndIncrement());
    }
}
