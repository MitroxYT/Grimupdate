package ac.grim.grimac.checks.impl.combat;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

@CheckData(name = "KillauraB")
public class KillauraB extends Check implements PacketCheck {
    public KillauraB(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
           //нулл
        }
<<<<<<< Updated upstream
=======

        // If the player set their look, or we know they have a new tick
        final boolean isFlying = WrapperPlayClientPlayerFlying.isFlying(event.getPacketType());
        if (isFlying ||
                event.getPacketType() == PacketType.Play.Client.PONG ||
                event.getPacketType() == PacketType.Play.Client.WINDOW_CONFIRMATION) {
            tickBetterReachCheckWithAngle(isFlying);
        }
    }

    // This method finds the most optimal point at which the user should be aiming at
    // and then measures the distance between the player's eyes and this target point
    //
    // It will not cancel every invalid attack but should cancel 3.05+ or so in real-time
    // Let the post look check measure the distance, as it will always return equal or higher
    // than this method.  If this method flags, the other method WILL flag.
    //
    // Meaning that the other check should be the only one that flags.
    private boolean isKnownInvalid(PacketEntity reachEntity) {
        // If the entity doesn't exist, or if it is exempt, or if it is dead
        if ((blacklisted.contains(reachEntity.type) || !reachEntity.isLivingEntity()) && reachEntity.type != EntityTypes.END_CRYSTAL)
            return false; // exempt

        if (player.gamemode == GameMode.CREATIVE || player.gamemode == GameMode.SPECTATOR) return false;
        if (player.compensatedEntities.getSelf().inVehicle()) return false;

        // Filter out what we assume to be cheats
        if (cancelBuffer != 0) {
            return checkReach(reachEntity, new Vector3d(player.x, player.y, player.z), true) != null; // If they flagged
        } else {
            SimpleCollisionBox targetBox = reachEntity.getPossibleCollisionBoxes();
            if (reachEntity.type == EntityTypes.END_CRYSTAL) {
                targetBox = new SimpleCollisionBox(reachEntity.desyncClientPos.subtract(1, 0, 1), reachEntity.desyncClientPos.add(1, 2, 1));
            }
            return ReachUtils.getMinReachToBox(player, targetBox) > 3;
        }
    }

    private void tickBetterReachCheckWithAngle(boolean isFlying) {
        for (Map.Entry<Integer, Vector3d> attack : playerAttackQueue.entrySet()) {
            PacketEntity reachEntity = player.compensatedEntities.entityMap.get(attack.getKey().intValue());

            if (reachEntity != null) {
                String result = checkReach(reachEntity, attack.getValue(), false);
                if (result != null) {
                    if (reachEntity.type == EntityTypes.PLAYER) {
                        flagAndAlert(result);
                        flagWithSetback();
                        flagWithSetback();
                        flagWithSetback();
                        flagWithSetback();
                        flagWithSetback();
                    } else {
                        flagAndAlert(result + " type=" + reachEntity.type.getName().getKey());
                        flagWithSetback();
                        flagWithSetback();
                        flagWithSetback();
                        flagWithSetback();
                        flagWithSetback();
                    }
                }
            }
        }
        playerAttackQueue.clear();
        // We can't use transactions for this because of this problem:
        // transaction -> block changed applied -> 2nd transaction -> list cleared -> attack packet -> flying -> reach block hit checked, falses
        if (isFlying) blocksChangedThisTick.clear();
    }

    private String checkReach(PacketEntity reachEntity, Vector3d from, boolean isPrediction) {
        SimpleCollisionBox targetBox = reachEntity.getPossibleCollisionBoxes();

        if (reachEntity.type == EntityTypes.END_CRYSTAL) { // Hardcode end crystal box
            targetBox = new SimpleCollisionBox(reachEntity.desyncClientPos.subtract(1, 0, 1), reachEntity.desyncClientPos.add(1, 2, 1));
        }

        // 1.7 and 1.8 players get a bit of extra hitbox (this is why you should use 1.8 on cross version servers)
        // Yes, this is vanilla and not uncertainty.  All reach checks have this or they are wrong.
        if (player.getClientVersion().isOlderThan(ClientVersion.V_1_9)) {
            targetBox.expand(0.1f);
        }

        targetBox.expand(threshold);

        // This is better than adding to the reach, as 0.03 can cause a player to miss their target
        // Adds some more than 0.03 uncertainty in some cases, but a good trade off for simplicity
        //
        // Just give the uncertainty on 1.9+ clients as we have no way of knowing whether they had 0.03 movement
        if (!player.packetStateData.didLastLastMovementIncludePosition || player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9))
            targetBox.expand(player.getMovementThreshold());

        double minDistance = Double.MAX_VALUE;

        // https://bugs.mojang.com/browse/MC-67665
        List<Vector> possibleLookDirs = new ArrayList<>(Collections.singletonList(ReachUtils.getLook(player, player.xRot, player.yRot)));

        // If we are a tick behind, we don't know their next look so don't bother doing this
        if (!isPrediction) {
            possibleLookDirs.add(ReachUtils.getLook(player, player.lastXRot, player.yRot));

            // 1.9+ players could be a tick behind because we don't get skipped ticks
            if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
                possibleLookDirs.add(ReachUtils.getLook(player, player.lastXRot, player.lastYRot));
            }

            // 1.7 players do not have any of these issues! They are always on the latest look vector
            if (player.getClientVersion().isOlderThan(ClientVersion.V_1_8)) {
                possibleLookDirs = Collections.singletonList(ReachUtils.getLook(player, player.xRot, player.yRot));
            }
        }

        for (Vector lookVec : possibleLookDirs) {
            for (double eye : player.getPossibleEyeHeights()) {
                Vector eyePos = new Vector(from.getX(), from.getY() + eye, from.getZ());
                Vector endReachPos = eyePos.clone().add(new Vector(lookVec.getX() * 6, lookVec.getY() * 6, lookVec.getZ() * 6));

                Vector intercept = ReachUtils.calculateIntercept(targetBox, eyePos, endReachPos).getFirst();

                if (ReachUtils.isVecInside(targetBox, eyePos)) {
                    minDistance = 0;
                    break;
                }

                if (intercept != null) {
                    minDistance = Math.min(eyePos.distance(intercept), minDistance);
                }
            }
        }

        final boolean experimentalChecks = true; //GrimAPI.INSTANCE.getConfigManager().isExperimentalChecks(); // TODO fix for undraft
        HitData foundHitData = null;
        // If the entity is within range of the player (we'll flag anyway if not, so no point checking blocks in this case)
        // Ignore when could be hitting through a moving shulker, piston blocks. They are just too glitchy/uncertain to check.
        if (experimentalChecks && minDistance <= 3 && !player.compensatedWorld.isNearHardEntity(player.boundingBox.copy().expand(4))) {
            final @Nullable Pair<Double, HitData> targetBlock = getTargetBlock(player, possibleLookDirs, from, minDistance);
            // And if the target block is closer to the player than the entity box, they should hit the block instead
            // So, this hit is invalid.
            if (targetBlock != null && targetBlock.getFirst() < (minDistance * minDistance)) { // targetBlock is squared
                minDistance = Double.MIN_VALUE;
                foundHitData = targetBlock.getSecond();
            }
        }

        // if the entity is not exempt and the entity is alive
        if ((!blacklisted.contains(reachEntity.type) && reachEntity.isLivingEntity()) || reachEntity.type == EntityTypes.END_CRYSTAL) {
            if (minDistance == Double.MIN_VALUE && foundHitData != null) {
                cancelBuffer = 1;
                return "Interact on block on damage event Block=" + foundHitData.getState().getType().getName();
            } else {
                cancelBuffer = Math.max(0, cancelBuffer - 0.25);
            }
        }

        return null;
    }

    public void handleBlockChange(Vector3i vector3i, WrappedBlockState state) {
        if (blocksChangedThisTick.size() >= 40) return; // Don't let players freeze movement packets to grow this
        // Only do this for nearby blocks
        if (new Vector(vector3i.x, vector3i.y, vector3i.z).distanceSquared(new Vector(player.x, player.y, player.z)) > 6) return;
        // Only do this if the state really had any world impact
        if (state.equals(player.compensatedWorld.getWrappedBlockStateAt(vector3i))) return;
        blocksChangedThisTick.add(vector3i);
    }

    // Returns a pair so we can check the block type in the flag
    @Nullable
    private Pair<Double, HitData> getTargetBlock(GrimPlayer player, List<Vector> possibleLookDirs, Vector3d from, double minDistance) {
        // Check every possible look direction and every possible eye height
        // IF *NONE* of them allow the player to hit the entity, this is an invalid hit
        HitData bestHitData = null;
        double min = Double.MAX_VALUE;
        for (Vector lookVec : possibleLookDirs) {
            for (double eye : player.getPossibleEyeHeights()) {
                Vector eyes = new Vector(from.getX(), from.getY() + eye, from.getZ());
                final HitData hitResult = BlockRayTrace.getNearestReachHitResult(player, eyes, lookVec, minDistance, 3);
                if (hitResult == null) {
                    return null;
                }

                final double distance = eyes.distanceSquared(hitResult.getBlockHitLocation());
                // Block changes are uncertain, can't check this tick
                if (distance < (minDistance * minDistance) && blocksChangedThisTick.contains(hitResult.getPosition())) {
                    return null;
                }

                bestHitData = hitResult;
                min = Math.min(min, distance);
            }
        }

        return bestHitData == null ? null : Pair.of(min, bestHitData);
    }

    @Override
    public void reload() {
        super.reload();
        this.cancelImpossibleHits = getConfig().getBooleanElse("Reach.block-impossible-hits", true);
        this.threshold = getConfig().getDoubleElse("Reach.threshold", 0.0005);
>>>>>>> Stashed changes
    }
}
