package com.crispytwig.naturalist.world.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import com.crispytwig.naturalist.world.entity.SurfaceClimbing;
import com.crispytwig.naturalist.world.entity.SurfaceCrawler;
import com.crispytwig.naturalist.world.entity.ai.control.SurfaceCrawlerMoveControl;

public class CrawlerNavigation extends GroundPathNavigation {
    private final SurfaceCrawler crawler;
    private BlockPos crawlTarget;

    public <T extends Mob & SurfaceCrawler> CrawlerNavigation(T mob, Level level) {
        super(mob, level);
        this.crawler = mob;
    }

    @Override
    protected boolean canUpdatePath() {
        return this.mob.onGround() || this.mob.isInLiquid() || this.crawler.getClimbing().isAttached();
    }

    @Override
    public boolean moveTo(double x, double y, double z, double speed) {
        return super.moveTo(x, y, z, speed) || this.startCrawlTo(BlockPos.containing(x, y, z), speed);
    }

    @Override
    public boolean moveTo(Entity entity, double speed) {
        return super.moveTo(entity, speed) || this.startCrawlTo(entity.blockPosition(), speed);
    }

    private boolean startCrawlTo(BlockPos target, double speed) {
        if (!this.crawler.getClimbing().isAttached()) {
            return false;
        }
        this.crawlTarget = target;
        this.speedModifier = speed;
        return true;
    }

    public boolean crawlTo(BlockPos target, double speed) {
        this.stop();
        return this.startCrawlTo(target, speed);
    }

    @Override
    public void tick() {
        SurfaceClimbing climbing = this.crawler.getClimbing();
        if (climbing.isOnSide() && this.path != null) {
            BlockPos pathTarget = this.path.getTarget();
            this.stop();
            this.crawlTarget = pathTarget;
        }
        if (this.crawlTarget == null) {
            super.tick();
            return;
        }
        if (!climbing.isAttached()) {
            return;
        }
        Vec3 target = Vec3.atBottomCenterOf(this.crawlTarget);
        if (this.mob.position().closerThan(target, Math.max(this.mob.getBbWidth(), 1.0D))) {
            this.crawlTarget = null;
            if (this.mob.getMoveControl() instanceof SurfaceCrawlerMoveControl control) {
                control.giveUp();
            }
        } else {
            this.mob.getMoveControl().setWantedPosition(target.x, target.y, target.z, Math.max(this.speedModifier, 0.7D));
        }
    }

    @Override
    public boolean isDone() {
        return super.isDone() && this.crawlTarget == null;
    }

    @Override
    public void stop() {
        super.stop();
        this.crawlTarget = null;
    }
}
