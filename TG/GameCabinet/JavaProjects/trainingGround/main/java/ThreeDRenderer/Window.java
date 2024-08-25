package ThreeDRenderer;



public class Window {

	static long windowID;
	private boolean VSYNC;
	private String title;
	private int width;
	private int height;
	private boolean resized;
	
	
	
	
	
	public Window(int h, int w, String t, boolean vSync) {
		height = h;
		width = w;
		title = t;
		VSYNC = vSync;
		resized = false;
	}





	public void init() {
//
//		GLFWErrorCallback.createPrint(System.err).set();
//
//		if (!glfwInit()) {
//			throw new IllegalStateException("GLFW init failed");
//		}
//
//		glfwDefaultWindowHints();
//		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
//		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
//        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
//        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
//		
//		
//		
//
//		windowID = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
//
//		if (windowID == MemoryUtil.NULL) {
//			throw new IllegalStateException("window failed");
//		}
//
//
//
//        glfwSetFramebufferSizeCallback(windowID, (window, width, height) -> {
//            width = width;
//            height = height;
//            resized = true;
//        });
//		
//		
//		glfwSetKeyCallback(windowID, (windowID, key, scancode, action, mods) -> {
//			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
//				glfwSetWindowShouldClose(windowID, true); // We will detect this
//															// in the rendering
//															// loop
//		});
//
//		// Get the thread stack and push a new frame
//		try (MemoryStack stack = stackPush()) {
//			IntBuffer pWidth = stack.mallocInt(1); // int*
//			IntBuffer pHeight = stack.mallocInt(1); // int*
//
//			// Get the window size passed to glfwCreateWindow
//			glfwGetWindowSize(windowID, pWidth, pHeight);
//
//			// Get the resolution of the primary monitor
//			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
//
//			// Center the window
//			glfwSetWindowPos(windowID, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
//		} // the stack frame is popped automatically
//
//		
//		glfwMakeContextCurrent(windowID);
//
//        if (VSYNC) {
//            glfwSwapInterval(1);
//        }
//
//		glfwShowWindow(windowID);
//		
//		GL.createCapabilities();
//	
//		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//		glEnable(GL_DEPTH_TEST);
//		
//		glEnable(GL_BLEND);
//		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//		
////		glEnable(GL_CULL_FACE);
////		glCullFace(GL_BACK);
		
	}





	public void dispose() {
//		glfwFreeCallbacks(windowID);
//		glfwDestroyWindow(windowID);
//
//		glfwTerminate();
//		glfwSetErrorCallback(null).free();
				
	}
	
	
    public void update() {
//        glfwSwapBuffers(windowID);
//        glfwPollEvents();
    }





	public boolean shouldClose() {
//		return glfwWindowShouldClose(windowID);
		return false;
	}





	public boolean isvSync() {
		return VSYNC;
	}





	public boolean isResized() {
		return resized;
	}





	public int getWidth() {
		return width;
	}





	
	public int getHeight() {
		return height;
	}





	public void setResized(boolean b) {
		resized = b;
	}





	 public boolean isKeyPressed(int keyCode) {
//	        return glfwGetKey(windowID
//	        		, keyCode) == GLFW_PRESS;
		 return false;
	    }





	 public void setClearColor(float r, float g, float b, float alpha) {
	        //glClearColor(r, g, b, alpha);
	    }





	public long getWindowHandle() {
		return windowID;
	}
	
	
}
