package FragmentShader;

import org.joml.Vector3f;

public class SpotLight {
	PointLight pl;
	Vector3f conedir;
	float cutoff;

	SpotLight(PointLight pl,Vector3f conedir,float cutoff){
		this.pl = pl;
		this.conedir = conedir;
		this.cutoff = cutoff;}

	public PointLight getPl() {
		return pl;
	}

	public void setPl(PointLight pl) {
		this.pl = pl;
	}

	public Vector3f getConedir() {
		return conedir;
	}

	public void setConedir(Vector3f conedir) {
		this.conedir = conedir;
	}

	public float getCutoff() {
		return cutoff;
	}

	public void setCutoff(float cutoff) {
		this.cutoff = cutoff;
	}
}
