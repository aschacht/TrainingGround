package ThreeDRenderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import FragmentShader.Fragment;



import java.util.List;
import java.util.Map;

public class Renderer {

	/**
	 * Field of View in Radians
	 */
	private static final float FOV = (float) Math.toRadians(60.0f);

	private static final float Z_NEAR = 0.01f;

	private static final float Z_FAR = 1000.f;

	private static final int MAX_POINT_LIGHTS = 5;

	private static final int MAX_SPOT_LIGHTS = 5;

	private final Transformation transformation;

	private ShaderProgram sceneShaderProgram;

	private final float specularPower;

	private ShaderProgram hudShaderProgram;

	private ShaderProgram skyBoxShaderProgram;

	public Renderer() {
		transformation = new Transformation();
		specularPower = 10f;
	}

	public void init(Window window) throws Exception {
		// Create shader
		sceneShaderProgram = new ShaderProgram();
		sceneShaderProgram
				.createVertexShader(Utils.loadResource("/home/wes/Wisper Tech 1.0/THEORY/GAMES/LWJGL_III/resources/vertex.vs"));
		sceneShaderProgram.createFragmentShader(
				Utils.loadResource("/home/wes/Wisper Tech 1.0/THEORY/GAMES/LWJGL_III/resources/fragment.fs"));
		sceneShaderProgram.link();

		// Create uniforms for modelView and projection matrices and texture
		sceneShaderProgram.createUniform("projectionMatrix");
		sceneShaderProgram.createUniform("modelViewMatrix");
		sceneShaderProgram.createUniform("texture_sampler");
		// Create uniform for material
		sceneShaderProgram.createMaterialUniform("material");
		// Create lighting related uniforms
		sceneShaderProgram.createUniform("specularPower");
		sceneShaderProgram.createUniform("ambientLight");
		sceneShaderProgram.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);
		sceneShaderProgram.createSpotLightListUniform("spotLights", MAX_SPOT_LIGHTS);
		sceneShaderProgram.createDirectionalLightUniform("directionalLight");

		sceneShaderProgram.createFogUniform("fog");
		
		
		setupSkyBoxShader();

		setupHudShader();

	}

	private void setupSkyBoxShader() throws Exception {
		skyBoxShaderProgram = new ShaderProgram();
		skyBoxShaderProgram.createVertexShader(
				Utils.loadResource("/home/wes/Wisper Tech 1.0/THEORY/GAMES/LWJGL_III/resources/skyBoxVertex.vs"));
		skyBoxShaderProgram.createFragmentShader(
				Utils.loadResource("/home/wes/Wisper Tech 1.0/THEORY/GAMES/LWJGL_III/resources/skyBoxFragment.fs"));
		skyBoxShaderProgram.link();

		skyBoxShaderProgram.createUniform("projectionMatrix");
		skyBoxShaderProgram.createUniform("modelViewMatrix");
		skyBoxShaderProgram.createUniform("texture_sampler");
		skyBoxShaderProgram.createUniform("ambientLight");

	}

	private void setupHudShader() throws Exception {
		hudShaderProgram = new ShaderProgram();
		hudShaderProgram
				.createVertexShader(Utils.loadResource("/home/wes/Wisper Tech 1.0/THEORY/GAMES/LWJGL_III/resources/hudvertex.vs"));
		hudShaderProgram.createFragmentShader(
				Utils.loadResource("/home/wes/Wisper Tech 1.0/THEORY/GAMES/LWJGL_III/resources/hudfragment.fs"));

		hudShaderProgram.link();

		hudShaderProgram.createUniform("projModelMatrix");
		hudShaderProgram.createUniform("colour");
		hudShaderProgram.createUniform("hasTexture");

	}

	public void clear() {
//		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void render(Window window, Camera camera, Scene scene, IHud hud) {

		clear();

		if (window.isResized()) {
			//glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}

		renderScene(window, camera, scene);

		renderSkyBox(window, camera, scene);

		renderHud(window, hud);

	}

	private void renderSkyBox(Window window, Camera camera, Scene scene) {
		skyBoxShaderProgram.bind();
		skyBoxShaderProgram.setUniform("texture_sampler", 0);
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(),
				Z_NEAR, Z_FAR);
		skyBoxShaderProgram.setUniform("projectionMatrix", projectionMatrix);
		SkyBox skyBox = scene.getSkyBox();
		Matrix4f viewMatrix = transformation.getViewMatrix(camera);
		viewMatrix.m30(0);
		viewMatrix.m31(0);
		viewMatrix.m32(0);
		Matrix4f modelViewMatrix = transformation.getModelViewMatrix(skyBox, viewMatrix);
		skyBoxShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
		skyBoxShaderProgram.setUniform("ambientLight", scene.getSceneLight().getAmbientLight());

		scene.getSkyBox().getMesh().render();

		skyBoxShaderProgram.unbind();
	}

	private void renderHud(Window window, IHud hud) {
		hudShaderProgram.bind();
		Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
		for (GameItem gameItem : hud.getGameItems()) {
			Mesh mesh = gameItem.getMesh();
			Matrix4f projModelMatrix = transformation.getOrtoProjModelMatrix(gameItem, ortho);
			hudShaderProgram.setUniform("projModelMatrix", projModelMatrix);
			hudShaderProgram.setUniform("colour", gameItem.getMesh().getMaterial().getAmbientColour());
			hudShaderProgram.setUniform("hasTexture", gameItem.getMesh().getMaterial().isTextured() ? 1 : 0);

			mesh.render();
		}

		hudShaderProgram.unbind();

	}

	private void renderScene(Window window, Camera camera, Scene scene) {
		sceneShaderProgram.bind();

		sceneShaderProgram.setUniform("fog", scene.getFog());

		// Update projection Matrix
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(),
				Z_NEAR, Z_FAR);
		sceneShaderProgram.setUniform("projectionMatrix", projectionMatrix);

		// Update view Matrix
		Matrix4f viewMatrix = transformation.getViewMatrix(camera);

		// Update Light Uniforms
		renderLights(viewMatrix, scene.getSceneLight());

		sceneShaderProgram.setUniform("texture_sampler", 0);

		Map<Mesh, List<GameItem>> mapMeshes = scene.getGameMeshes();
		for (Mesh mesh : mapMeshes.keySet()) {
			sceneShaderProgram.setUniform("material", mesh.getMaterial());
			mesh.renderList(mapMeshes.get(mesh),(GameItem gameItem)->{
				Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(gameItem,viewMatrix);
				sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
			});
		}

		sceneShaderProgram.unbind();
	}

	private void renderLights(Matrix4f viewMatrix, SceneLight sceneLight) {

		sceneShaderProgram.setUniform("ambientLight", sceneLight.getAmbientLight());
		sceneShaderProgram.setUniform("specularPower", specularPower);

		// Process Point Lights
		int numLights = sceneLight.getPointLightList() != null ? sceneLight.getPointLightList().length : 0;
		for (int i = 0; i < numLights; i++) {
			// Get a copy of the point light object and transform its position
			// to view coordinates
			ThreeDRenderer.PointLight[] pointLightList = sceneLight.getPointLightList();
			PointLight currPointLight = new PointLight(pointLightList[i]);
			Vector3f lightPos = currPointLight.getPosition();
			Vector4f aux = new Vector4f(lightPos, 1);
			aux.mul(viewMatrix);
			lightPos.x = aux.x;
			lightPos.y = aux.y;
			lightPos.z = aux.z;
			sceneShaderProgram.setUniform("pointLights", currPointLight, i);
		}

		// Process Spot Ligths

		numLights = sceneLight.getSpotLightList() != null ? sceneLight.getSpotLightList().length : 0;
		for (int i = 0; i < numLights; i++) {
			// Get a copy of the spot light object and transform its position
			// and cone direction to view coordinates
			SpotLight currSpotLight = new SpotLight(sceneLight.getSpotLightList()[i]);
			Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
			dir.mul(viewMatrix);
			currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));
			Vector3f lightPos = currSpotLight.getPointLight().getPosition();

			Vector4f aux = new Vector4f(lightPos, 1);
			aux.mul(viewMatrix);
			lightPos.x = aux.x;
			lightPos.y = aux.y;
			lightPos.z = aux.z;

			sceneShaderProgram.setUniform("spotLights", currSpotLight, i);
		}

		// Get a copy of the directional light object and transform its position
		// to view coordinates
		DirectionalLight currDirLight = new DirectionalLight(sceneLight.getDirectionalLight());
		Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
		dir.mul(viewMatrix);
		currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
		sceneShaderProgram.setUniform("directionalLight", currDirLight);

	}

	public void cleanup() {
		if (sceneShaderProgram != null) {
			sceneShaderProgram.cleanup();
		}
	}
}
