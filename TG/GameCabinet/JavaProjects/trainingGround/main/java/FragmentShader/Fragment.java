package FragmentShader;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Fragment {
	
	private static int MAX_POINT_LIGHTS = 5;
	private static int MAX_SPOT_LIGHTS = 5;
	Texture texture_sampler;
	Vector3f ambientLight;
	float specularPower;
	Material material;
	PointLight pointLights[];
	SpotLight spotLights[];
	DirectionalLight directionalLight;
	Fog fog;


	Vector4f ambientC;
	Vector4f diffuseC;
	Vector4f speculrC;
	
	
	void setupColours(Material material, Vector2f textCoord)
	{
	    if (material.hasTexture == 1)
	    {
	    	ambientC = texture(texture_sampler, textCoord);
	        diffuseC = ambientC;
	        speculrC = ambientC;
	    }
	    else
	    {
	        ambientC = material.ambient;
	        diffuseC = material.diffuse;
	        speculrC = material.specular;
	    }
	}

	private Vector4f texture(Texture texture_sampler2, Vector2f textCoord) {
		
		return texture_sampler2.getColorAt((int)textCoord.x, (int)textCoord.y);
	}

	Vector4f calcLightColour(Vector3f light_colour, float light_intensity, Vector3f position, Vector3f to_light_dir, Vector3f normal)
	{
		Vector4f diffuseColour = new Vector4f(0, 0, 0, 0);
	    Vector4f specColour = new Vector4f(0, 0, 0, 0);

	    // Diffuse Light
	    float diffuseFactor = normal.dot(to_light_dir);
	    if(diffuseFactor<0.0f)
	    	diffuseFactor=0.0f;
	    Vector4f vec = new Vector4f(light_colour.x,light_colour.y,light_colour.z, 1.0f);
	    diffuseColour = vec.mul(diffuseC).mul(light_intensity).mul(diffuseFactor);

	    // Specular Light
	    Vector3f camera_direction = position.mul(-1.0f).normalize();
	    Vector3f from_light_dir = to_light_dir.mul(-1.0f);
	    Vector3f reflected_light = normal.reflect(from_light_dir).normalize();
	    float specularFactor = camera_direction.dot(reflected_light);
	    if(specularFactor<0.0)
	    	specularFactor = 0.0f;
	    specularFactor = (float) Math.pow(specularFactor, specularPower);
	    specColour = speculrC.mul(light_intensity).mul(specularFactor).mul( material.reflectance).mul( new Vector4f(light_colour.x,light_colour.y,light_colour.z, 1.0f));

	    return diffuseColour.add(specColour);
	}

	Vector4f calcPointLight(PointLight light, Vector3f position, Vector3f normal)
	{
	    Vector3f light_direction = light.position.sub(position);
	    Vector3f to_light_dir  = light_direction.normalize();
	    Vector4f light_colour = calcLightColour(light.colour, light.intensity, position, to_light_dir, normal);

	    // Apply Attenuation
	    float distance = light_direction.length();
	    float attenuationInv = light.att.constant + light.att.linear* distance +
	        light.att.exponent * distance * distance;
	    return light_colour.div( attenuationInv);
	}

	Vector4f calcSpotLight(SpotLight light, Vector3f position, Vector3f normal)
	{
	    Vector3f light_direction = light.pl.position.sub(position);
	    Vector3f to_light_dir  = light_direction.normalize();
	    Vector3f from_light_dir  = to_light_dir.mul(1.0f);
	    float spot_alfa = from_light_dir.dot( light.conedir.normalize());
	    
	    Vector4f colour = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
	    
	    if ( spot_alfa > light.cutoff ) 
	    {
	        colour = calcPointLight(light.pl, position, normal);
	        colour = colour.mul((float) (1.0 - (1.0 - spot_alfa)/(1.0 - light.cutoff)));
	    }
	    return colour;    
	}
	    
	public Vector4f oneIteration(Vector2f outTexCoord,Vector3f mvVertexNormal,Vector3f mvVertexPos)
	{
	    setupColours(material, outTexCoord);

	    Vector4f diffuseSpecularComp = calcDirectionalLight(directionalLight, mvVertexPos, mvVertexNormal);

	    for (int i=0; i<MAX_POINT_LIGHTS; i++)
	    {
	        if ( pointLights[i].intensity > 0 )
	        {
	            diffuseSpecularComp = diffuseSpecularComp.add(calcPointLight(pointLights[i], mvVertexPos, mvVertexNormal)); 
	        }
	    }

	    for (int i=0; i<MAX_SPOT_LIGHTS; i++)
	    {
	        if ( spotLights[i].pl.intensity > 0 )
	        {
	            diffuseSpecularComp = diffuseSpecularComp.add(calcSpotLight(spotLights[i], mvVertexPos, mvVertexNormal));
	        }
	    }
	    Vector4f vector4f = new Vector4f(ambientLight, 1);
	    Vector4f fragColor = vector4f.mul(ambientC).add(diffuseSpecularComp);

		if(fog.activeFog == 1)
		{
			fragColor = calcFog(mvVertexPos,fragColor,fog,ambientLight,directionalLight);
		}
		return fragColor;
}


	Vector4f calcDirectionalLight(DirectionalLight light, Vector3f position, Vector3f normal)
	{
	    return calcLightColour(light.color, light.intensity, position, light.direction.normalize(), normal);
	}

	Vector4f calcFog(Vector3f pos, Vector4f colour, Fog fog,Vector3f ambientLight, DirectionalLight dirLight)
	{

		Vector3f fogColor = fog.colour.mul(dirLight.color.mul(dirLight.intensity).add(ambientLight));
		float distance = pos.length();
		float fogFactor = (float) (1.0 / Math.exp((distance * fog.density)*(distance*fog.density)));
		fogFactor = clamp(fogFactor,0.0f,1.0f);
	
		
		
		
		
		
		Vector3f resultColour = mix(fogColor,new Vector3f(colour.x,colour.y,colour.z),fogFactor);
		return new Vector4f(resultColour.x,resultColour.y,resultColour.z,1.0f);
	}
	private Vector3f mix(Vector3f fogColor, Vector3f vector3f, float fogFactor) {
		Vector3f mul = fogColor.mul(1-fogFactor);
		Vector3f mul2 = vector3f.mul(fogFactor);
		return mul.add(mul2);
	}

	float clamp(float x, float a, float b)
	{
		float winner =b;
		if(winner>x)
			winner = x;
		if(winner<a)
			winner = a;
		
		
		
	  return winner;
	}
	
	

//	void main()
//	{
//	    setupColours(material, outTexCoord);
//
//	    Vector4f diffuseSpecularComp = calcDirectionalLight(directionalLight, mvVertexPos, mvVertexNormal);
//
//	    for (int i=0; i<MAX_POINT_LIGHTS; i++)
//	    {
//	        if ( pointLights[i].intensity > 0 )
//	        {
//	            diffuseSpecularComp += calcPointLight(pointLights[i], mvVertexPos, mvVertexNormal); 
//	        }
//	    }
//
//	    for (int i=0; i<MAX_SPOT_LIGHTS; i++)
//	    {
//	        if ( spotLights[i].pl.intensity > 0 )
//	        {
//	            diffuseSpecularComp += calcSpotLight(spotLights[i], mvVertexPos, mvVertexNormal);
//	        }
//	    }
//	    
//	    fragColor = ambientC * Vector4f(ambientLight, 1) + diffuseSpecularComp;
//
//		if(fog.activeFog == 1)
//		{
//			fragColor = calcFog(mvVertexPos,fragColor,fog,ambientLight,directionalLight);
//		}
//}
}
