package ac.grim.grimac.utils;

import org.bukkit.util.Vector;
public class RayCollision {
    private Vector origin;
    private Vector direction;

    public RayCollision(Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction.normalize();
    }

    public Vector getOrigin() {
        return origin;
    }

    public Vector getDirection() {
        return direction;
    }

    public Vector getPointAtDistance(double distance) {
        return origin.clone().add(direction.clone().multiply(distance));
    }
}