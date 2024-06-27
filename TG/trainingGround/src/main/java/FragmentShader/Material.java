package FragmentShader;

import org.joml.Vector4f;

public class Material {
	 Vector4f ambient;
	 Vector4f diffuse;
	 Vector4f specular;
	int hasTexture;
	 float reflectance;

	Material(Vector4f ambient, Vector4f diffuse, Vector4f specular, int hasTexture, float reflectance) {
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		this.hasTexture = hasTexture;
		this.reflectance = reflectance;
		
	}

	public Vector4f getAmbient() {
		return ambient;
	}

	public void setAmbient(Vector4f ambient) {
		this.ambient = ambient;
	}

	public Vector4f getDiffuse() {
		return diffuse;
	}

	public void setDiffuse(Vector4f diffuse) {
		this.diffuse = diffuse;
	}

	public Vector4f getSpecular() {
		return specular;
	}

	public void setSpecular(Vector4f specular) {
		this.specular = specular;
	}

	public int getHasTexture() {
		return hasTexture;
	}

	public void setHasTexture(int hasTexture) {
		this.hasTexture = hasTexture;
	}

	public float getReflectance() {
		return reflectance;
	}

	public void setReflectance(float reflectance) {
		this.reflectance = reflectance;
	}
}
