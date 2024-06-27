package FragmentShader;

import org.joml.Vector3f;

public class Fog {
	int activeFog;
	Vector3f colour;
	float density;

	Fog(int activeFog, Vector3f colour, float density) {
		this.activeFog = activeFog;
		this.colour = colour;
		this.density = density;

	}

	public int getActiveFog() {
		return activeFog;
	}

	public void setActiveFog(int activeFog) {
		this.activeFog = activeFog;
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
