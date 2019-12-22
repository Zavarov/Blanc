package vartas.discord.bot.internal;

import vartas.discord.bot.EntityAdapter;
import vartas.discord.bot.entities.Cluster;
import vartas.discord.bot.entities.Rank;

import javax.annotation.Nonnull;

public class UpdateRank extends Cluster.VisitorDelegator{
    private final Rank rank;
    public UpdateRank(Rank rank){
        setClusterVisitor(new Cluster.ClusterVisitor());
        this.rank = rank;
    }

    @Override
    public void visit(@Nonnull EntityAdapter adapter){
        adapter.store(rank);
    }
}