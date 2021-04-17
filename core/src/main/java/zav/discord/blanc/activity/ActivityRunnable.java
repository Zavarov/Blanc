package zav.discord.blanc.activity;

import zav.discord.blanc.Shard;

public class ActivityRunnable implements Runnable {
    private final Shard shard;
    private final ActivityVisitor visitor = new ActivityVisitor();

    public ActivityRunnable (Shard shard) {
        this.shard = shard;
    }

    @Override
    public void run() {
        shard.accept(visitor);
    }
}
