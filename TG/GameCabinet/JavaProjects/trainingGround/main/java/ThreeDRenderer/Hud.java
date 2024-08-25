package ThreeDRenderer;

import java.awt.Font;

import org.joml.Vector4f;



public class Hud implements IHud {

	private static final int FONT_COLS = 16;
	private static final int FONT_ROWS = 16; 
	private static final String FONT_TEXTURE = "C:/Users/BitBot01/workspace/LWJGL_III/resources/textures/font_texture.png";
	private final GameItem[] gameItems;
	private TextItem statusTextItem;
	private GameItem compassItem;
	private static final Font FONT = new Font("Helvetica", Font.PLAIN, 42);

    private static final String CHARSET = "ISO-8859-1";
	
	
	public Hud(String statusText)throws Exception{
		
		
		FontTexture fontTexture = new FontTexture(FONT, CHARSET);
		statusTextItem = new TextItem(statusText,fontTexture);
		
		statusTextItem.getMesh().getMaterial().setAmbientColour(new Vector4f(1,1,1,1));
		statusTextItem.setPosition(100, 100, 0);
		
		
		Mesh mesh = OBJLoader.loadMesh("/home/wes/Wisper Tech 1.0/THEORY/GAMES/LWJGL_III/resources/models/compass.obj");
		Material material = new Material();
		material.setAmbientColour(new Vector4f(1,0,0,1));
		mesh.setMaterial(material);
		compassItem = new GameItem(mesh);
		compassItem.setScale(40.0f);
		compassItem.setRotation(0f, 0f, 180f);
		compassItem.setPosition(100, 100, 1);
	
		
		gameItems = new GameItem[]{statusTextItem,compassItem};
	
	}
	
	public void rotateCompass(float angle){
		compassItem.setRotation(0, 0, 180+angle);
	}
	
	public void setStatusText(String statusText){
		statusTextItem.setText(statusText);
	}
	
	
	@Override
	public GameItem[] getGameItems() {
		return gameItems;
	}
	
	public void updateSize(Window window){
		statusTextItem.setPosition(10f, window.getHeight()-50f, 0);
	}
	
	

}
