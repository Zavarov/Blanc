package zav.discord.blanc.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zav.discord.blanc.Shard;

public class ActivityRunnable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityRunnable.class);
    private final Shard shard;
    private final ActivityVisitor visitor = new ActivityVisitor();

    public ActivityRunnable (Shard shard) {
        this.shard = shard;
    }

    @Override
    public void run() {
        LOGGER.info("Update activity for shard {}.", shard.getId());
        shard.accept(visitor);
    }
}
