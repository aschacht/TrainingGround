package ThreeDRenderer;

import org.joml.Vector3f;

public class DirectionalLight {
    
    private Vector3f color;

    private Vector3f direction;

    private float intensity;

    public DirectionalLight(Vector3f c, Vector3f d, float i) {
        color = c;
        direction = d;
        intensity = i;
    }

    public DirectionalLight(DirectionalLight light) {
        this(light.getColor(), light.getDirection(), light.getIntensity());
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}