package FragmentShader;
import org.joml.Vector3f;
public class PointLight {
	Vector3f colour;
	Vector3f position;
	float intensity;
	Attenuation att;

	PointLight(Vector3f colour, Vector3f position,float intensity,Attenuation att){
		this.colour = colour;
		this.position = position;
		this.intensity = intensity;
		this.att = att;
		
		
	}



	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public Attenuation getAtt() {
		return att;
	}

	public void setAtt(Attenuation att) {
		this.att = att;
	}
}
