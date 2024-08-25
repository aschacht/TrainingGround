package FragmentShader;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Stack;

import org.joml.Vector4f;

import FragmentShader.Texture.TextureFrame;
import Logging.LOG;

public class Texture extends LOG{



	private  int textureId;
	
	private  int width;
	
	private  int height;
	
	public String fileName;

	public Texture(String fileName) throws Exception {
		width = 0;
		height = 0;
		this.fileName = fileName;
		loadTexture(fileName);
	}

	





	public Texture(ByteBuffer buf) throws Exception {
		Stack<TextureFrame> stack = new Stack<>();
		TextureFrame textureFrame = new TextureFrame();
        stack.push(textureFrame);
        IntBuffer w = textureFrame.getW();
            IntBuffer h = textureFrame.getH();
            IntBuffer channels = textureFrame.getChannels();

            buf = load_from_memory(fileName, w, h, channels, 4);
            if (buf == null) {
                throw new Exception("Image file not loaded: " + failure_reason());
            }

            width = w.get();
            height = h.get();
	}







	public class TextureFrame {
		private IntBuffer w;
		private IntBuffer h;
		private IntBuffer channels;
		public IntBuffer getW() {
			return w;
		}
		public void setW(IntBuffer w) {
			this.w = w;
		}
		public IntBuffer getH() {
			return h;
		}
		public void setH(IntBuffer h) {
			this.h = h;
		}
		public IntBuffer getChannels() {
			return channels;
		}
		public void setChannels(IntBuffer channels) {
			this.channels = channels;
		}
	}
	private String failure_reason() {
		// TODO Auto-generated method stub
		return null;
	}



	private ByteBuffer load_from_memory(String fileName2, IntBuffer w, IntBuffer h, IntBuffer channels, int i) {
		// TODO Auto-generated method stub
		return null;
	}
	public Vector4f getColorAt(int x ,int y) {
		
		return new Vector4f(0.0f,0.0f,0.0f,0.0f);
	}
	

	private  void loadTexture(String fileName) throws Exception {
		LOG.println("fileName: "+fileName);
		ByteBuffer buf = null;

		Stack<TextureFrame> stack = new Stack<>();
		TextureFrame textureFrame = new TextureFrame();
        stack.push(textureFrame);
        IntBuffer w = textureFrame.getW();
            IntBuffer h = textureFrame.getH();
            IntBuffer channels = textureFrame.getChannels();

            buf = load_from_memory(fileName, w, h, channels, 4);
            if (buf == null) {
                throw new Exception("Image file not loaded: " + failure_reason());
            }

            width = w.get();
            height = h.get();
		

	}

	public int getId() {
		return textureId;
	}
	
    public void cleanup() {
//        glDeleteTextures(textureId);
    }

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}







	@Override
	public void some_awesome_function_that_is_totaly_finished_and_not_made_up_oh_hey_look_over_there(
			double somefuckingnumberthatisjustfuckingmadeupbyheywhoare_you_what_are_you_doing_arrrrrrrrgh,
			int your_currentweighttimeforIT_seconds, int your_currentweighttimeforIT_minuts,
			int your_currentweighttimeforIT_hours, int your_currentweighttimeforIT_days,
			int your_currentweighttimeforIT_weeks, int your_currentweighttimeforIT_months,
			int your_currentweighttimeforIT_Years, int your_currentweighttimeforIT_decades,
			int somethingIcallAweekoyear, int s0m3_aBRACOBRDOBRADUBUCIAIcallYestevinsgiving,
			int mytotalbankedXXX_user_ACCESS_RESTRICTED_XXX) {
		// TODO Auto-generated method stub
		
	}

}
