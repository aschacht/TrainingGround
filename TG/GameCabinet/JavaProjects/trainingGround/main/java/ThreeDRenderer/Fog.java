package ThreeDRenderer;

import org.joml.Vector3f;

public class Fog {
private boolean active;
private Vector3f colour;
private float density;
public static Fog NOFOG = new Fog();

public Fog(){
	setActive(false);
	setColour(new Vector3f(0,0,0));
	setDensity(0);
}

public Fog(boolean act,Vector3f color,float dens){
	setColour(color);
	setDensity(dens);
	setActive(act);
}

public boolean isActive() {
	return active;
}

public void setActive(boolean active) {
	this.active = active;
}

public Vector3f getColour() {
	return colour;
}

public void setColour(Vector3f colour) {
	this.colour = colour;
}

public float getDensity() {
	return density;
}

public void setDensity(float density) {
	this.density = density;
}
	
	
	
}
