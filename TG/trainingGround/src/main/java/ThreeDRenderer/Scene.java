package ThreeDRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector3f;

public class Scene {


	private SkyBox skyBox;

	private SceneLight sceneLight;

	private Map<Mesh, List<GameItem>> meshMap;
	
	private Fog fog = Fog.NOFOG;
	
	public Scene(){
		meshMap= new HashMap<Mesh,List<GameItem>>();
	}


	public void setGameItems(GameItem[] gameItems) {
		int numGameItems = gameItems != null ? gameItems.length : 0;
		for(int i =0;i<numGameItems;i++){
			GameItem gameItem = gameItems[i];
			Mesh mesh = gameItem.getMesh();
			List<GameItem> list = meshMap.get(mesh);
			if(list == null){
				list = new ArrayList<>();
				meshMap.put(mesh, list);
			}
			list.add(gameItem);
		}
	}

	public SkyBox getSkyBox() {
		return skyBox;
	}

	public void setSkyBox(SkyBox skyBox) {
		this.skyBox = skyBox;
	}

	public SceneLight getSceneLight() {
		return sceneLight;
	}

	public void setSceneLight(SceneLight sceneLight) {
		this.sceneLight = sceneLight;
	}

	public Map<Mesh, List<GameItem>> getGameMeshes() {
		return meshMap;
	}


	public Fog getFog() {
		return fog;
	}


	public void setFog(Fog fog) {
		this.fog = fog;
	}

}
