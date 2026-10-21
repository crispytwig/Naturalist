package com.crispytwig.naturalist.world.entity.ai.goal;

import com.crispytwig.naturalist.world.entity.animal.fish.Blobfish;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BlobfishStayDeepGoal extends RandomSwimmingGoal {
    private final Blobfish blobfish;

    public BlobfishStayDeepGoal(Blobfish blobfish, double speedModifier, int interval) {
        super(blobfish, speedModifier, interval);
        this.blobfish = blobfish;
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        int deepY = this.blobfish.getDeepY();
        if (this.blobfish.getY() > deepY) {
            return DefaultRandomPos.getPosTowards(this.blobfish, 10, 7, new Vec3(this.blobfish.getX(), deepY - 4, this.blobfish.getZ()), Math.PI / 2.0);
        }
        Vec3 pos = super.getPosition();
        return pos != null && pos.y <= deepY ? pos : null;
    }
}
