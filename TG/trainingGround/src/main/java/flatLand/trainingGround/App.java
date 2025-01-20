package flatLand.trainingGround;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import Actions.ActionsInterface;
import Actions.DrawABlob;
import Actions.DrawAProtoCloud;
import Actions.DrawArc;
import Actions.DrawArcFasterVersion1;
import Actions.GoInAStrightLineFor;
import Actions.Wonder;
import FlatLand.Physics.Physics;
import FlatLand.Physics.UpdateTimeSingleton;
import FlatLandStructure.ViewableFlatLand;
import FlatLander.FlatLandFacebook;
import FlatLander.FlatLander;
import Logging.LOG;
import Notes.Notes;
import View.FlatLandWindow;

import XMLLoader.XmlLevelLoader;
import flatLand.trainingGround.Sprites.SkeletonTwo;
import flatLand.trainingGround.theStudio.Camera;
import src.ANT;
import src.Direction;
import src.Simulation;
import src.Tiles;
import src.tile;
import theStart.thePeople.FlatLanderFaceBook;
import theStart.theView.TheControls.GameScreen;
import theStart.theView.TheControls.TheStartCamera;

public class App extends LOG {
	private static FlatLandWindow flatLandWindow;
	private static Camera theEyeInTheSky;
	private static int cameraWidth = 512;
	private static int cameraHeight = 400;
	private static int flatLandWidth = 1024;
	private static int flatLandHeight = 800;
	private static int canvasWidth = 1024;
	private static int canvasHeight = 800;
	private static final String FILENAME = "res/level.xml";
	private static Simulation simulation;
	private static ArrayList<ANT> ant_s_;
	private static Tiles tiles;
	private static TheStartCamera theStartCamera;
	private static ArrayList<Graphics> gQueue = new ArrayList<>();
	private static BigDecimal fPS = BigDecimal.ZERO;
	private static FlatLandSelector flatLandSelector;
	protected static int xInitiial;
	protected static int yInitial;
	public static void main(String[] args) throws IOException {
		GameStatus statusInstance = GameStatus.getInstance();
		 statusInstance.addStatus(GAMSTATUS.BRAIN);

		HashMap<String, String> logs = new HashMap<>();
		logs.put("log", "res/folder");
		LOG.set_current_working_directory("");
		LOG.register_output_forLogging(LOG, logs);

		int posY = (canvasHeight / 2) - cameraHeight / 2;
		Physics physics = new Physics(9.8, posY, cameraHeight);
		ViewableFlatLand flatLand = new ViewableFlatLand(flatLandWidth, flatLandHeight, true);

		XMLLoader.PlayerWrper mel = new XMLLoader.PlayerWrper(Color.BLUE, 50, 0, "wes", 0, true);
		mel.buildTerminal();

		mel.setSprite(new SkeletonTwo("res/zombie_n_skeleton2.png", 100));
		Object theRequestie = new Object();
		while (!FlatLandFacebook.getInstance().requestToken(theRequestie)) {
		}
		FlatLandFacebook.getInstance().add(mel, theRequestie);
		FlatLandFacebook.getInstance().releaseToken(theRequestie);

		

		int posX = (canvasWidth / 2) - cameraWidth / 2;
		theEyeInTheSky = new Camera(flatLand, cameraWidth, cameraHeight ,canvasWidth, canvasHeight, posX, posY,
				mel);

		theStartCamera = new TheStartCamera(cameraWidth, cameraHeight, posX, posY,
				flatLand);

		
		GameScreen panel = new GameScreen(canvasWidth, canvasHeight, statusInstance);
		panel.setTheEyeInTheSky(theEyeInTheSky);
		panel.setTheStartCamera(theStartCamera);
		flatLandSelector = new FlatLandSelector(theEyeInTheSky,flatLand);
		panel.addMouseListener(new MouseListener() {
			


			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.err.println("Clicked");
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				System.err.println("entered");
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				System.err.println("exited");
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				xInitiial = arg0.getX();
				yInitial = arg0.getY();
				
				flatLandSelector.select(xInitiial, yInitial);
			}
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
								
				

			}});
		
panel.addMouseMotionListener(new MouseMotionListener() {
	
	@Override
	public void mouseMoved(MouseEvent arg0) {

		
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {

		int x = arg0.getX();
		int y = arg0.getY();
		flatLandSelector.reposition(x, y,xInitiial,yInitial);
		
	}
});
		flatLandWindow = new FlatLandWindow("Hello World", flatLand, panel, canvasWidth, canvasHeight);
		mel.buildKeyBoardHandler(panel);
		panel.requestFocus();
		ant_s_ = new ArrayList<>();
		tiles = new Tiles(cameraWidth, cameraHeight, Color.WHITE, ant_s_);
		generateBoard();
		generateAnts();
		simulation = new Simulation(true, MouseInfo.getPointerInfo().getLocation(), 0, flatLandWindow, null,
				cameraWidth, cameraWidth, cameraHeight, tiles);

		panel.setFlatLandWindow(flatLandWindow);
		panel.setSimulation(simulation);

		new XmlLevelLoader(FILENAME, flatLand, physics);

		FlatLandFacebook instance = FlatLandFacebook.getInstance();
		instance.getFlatlanderFaceBook();

		Notes NOTES = new Notes();
		refreshNotes(NOTES);
		try {
			boolean firstTime = false;

			NOTES.pack();
			NOTES.setVisible(true);

			refreshNotes(NOTES);
			int fpscount = 0;
			long[] FPSframes = new long[60];
			long[] timeSpentOnUpdate = new long[60];
			long[] timeSpentOnTakingPictureAndDeveloping = new long[60];
			long[] timeSpentOnOther = new long[60];

			LOG.println("run the simulation");
			while (!flatLandWindow.isClose()) {
				long start = System.currentTimeMillis();
				long startUpdate = System.currentTimeMillis();
				panel.getGraphics();

				if (firstTime) {
					physics.applyPhysics();
					flatLand.update();
					theStartCamera.update(FlatLanderFaceBook.getInstance().getFlatlanderFaceBookPool());

				}
				long endUpdate = System.currentTimeMillis();
				long startPicAndDevelop = System.currentTimeMillis();

				panel.repaint();

				long endPicAndDevelop = System.currentTimeMillis();
				long startOther = System.currentTimeMillis();

				long endOther = System.currentTimeMillis();

				long end = System.currentTimeMillis();

				long length = end - start;
				if (16 - (length / 1000) > 0)	
					Thread.sleep(16 - (length / 1000));
				long lengthUpdate = endUpdate - startUpdate;
				long lengthPicAndDevelop = endPicAndDevelop - startPicAndDevelop;
				long lengthOther = endOther - startOther;

				long end2 = System.currentTimeMillis();

				

//					System.err.println("MSPF "+tot);

				fpscount++;

				firstTime = true;
				if (fpscount < 60) {
					FPSframes[fpscount] = length;
					timeSpentOnUpdate[fpscount] = lengthUpdate;
					timeSpentOnTakingPictureAndDeveloping[fpscount] = lengthPicAndDevelop;
					timeSpentOnOther[fpscount] = lengthOther;
				} else {

					long total = length;
					for (long l : FPSframes) {
						total += l;
					}
					long totalUpdate = lengthUpdate;
					for (long l : timeSpentOnUpdate) {
						totalUpdate += l;
					}
					long totalPicAndDevelop = lengthPicAndDevelop;
					for (long l : timeSpentOnTakingPictureAndDeveloping) {
						totalPicAndDevelop += l;
					}
					long totalOther = lengthOther;
					for (long l : timeSpentOnOther) {
						totalOther += l;
					}
					BigDecimal totalMAX = BigDecimal.valueOf(total).divide(BigDecimal.valueOf(1000));
					BigDecimal totalMAXU = BigDecimal.valueOf(totalUpdate).divide(BigDecimal.valueOf(1000));
					BigDecimal totalMAXP = BigDecimal.valueOf(totalPicAndDevelop).divide(BigDecimal.valueOf(1000));
					BigDecimal totalMAXO = BigDecimal.valueOf(totalOther).divide(BigDecimal.valueOf(1000));

					if (totalMAX == BigDecimal.ZERO)
						totalMAX = BigDecimal.ONE;

					fPS = BigDecimal.valueOf(61).divide(totalMAX, 1, RoundingMode.HALF_UP);

					if (totalMAX != BigDecimal.valueOf(0)) {

						flatLandWindow.notify("" + fPS);
					}

					if (totalMAXU != BigDecimal.valueOf(0)) {
						flatLandWindow.notifyUpdate("" + totalMAXU);
					}
					if (totalMAXP != BigDecimal.valueOf(0)) {
						flatLandWindow.notifyPic("" + totalMAXP);
					}
					if (totalMAXO != BigDecimal.valueOf(0)) {
						flatLandWindow.notifyOther("" + totalMAXO);
					}
					UpdateTimeSingleton.getInstance().setCurrentTime(
							totalMAXU.doubleValue() + totalMAXP.doubleValue() + totalMAXO.doubleValue());
					fpscount = 0;

					LOG.println(mel.getName() + ": " + mel.x + " , " + mel.y + " , " + mel.direction);
				}
//				
//				
			}

			LOG.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
			LOG.println(e.getMessage());
		}

		LOG.close();
	}

	public static void generateAnts() {
		for (int i = 0; i < 1// 50000
		; i++) {
			ant_s_.add(new ANT((int) (Math.random() * (cameraWidth - 1)), (int) (Math.random() * (cameraHeight - 1)),
					Direction.random(), cameraWidth, cameraHeight, new Color(255, 255, 0)));
		}

	}

	public static void generateBoard() {

		tile[][] tilez = tiles.getTiles();

		for (int i = 0; i < cameraWidth; i++) {
			for (int j = 0; j < cameraHeight; j++) {

				tilez[i][j] = new tile(i, j, Color.WHITE);

			}
		}
	}

	private static void refreshNotes(Notes NOTES) {
		NOTES.repaint();
	}

	private static void buildMelsActs(ArrayList<ActionsInterface> acts, FlatLander flatlander,
			ViewableFlatLand flatLand) {

		DrawArc drawAArcMel = new DrawArc(flatlander, flatLand);
		DrawAProtoCloud drawAProtoCloudMel = new DrawAProtoCloud(flatlander, flatLand);
		Wonder wonderMel = new Wonder(flatlander);
		new DrawABlob(flatlander, flatLand);
		for (int i = 0; i < 200; i++) {
			for (int j = 0; j < 10; j++) {
				// acts.add(drawAArcMel);
				acts.add(drawAArcMel);
//				acts.add(new DrawArcFasterVersion1(mel, flatLand));
				acts.add(new GoInAStrightLineFor(flatlander, (int) (Math.random() * 50)));
				acts.add(wonderMel);
				// acts.add(drawAArcMel);
				// acts.add(drawAProtoCloudMel);

			}

			for (int j = 0; j < 10; j++) {
				// acts.add(drawAArcMel);
				acts.add(wonderMel);
				acts.add(new GoInAStrightLineFor(flatlander, (int) (Math.random() * 50)));
				// acts.add(drawAArcMel);
				acts.add(new DrawArcFasterVersion1(flatlander, flatLand));
			}
			acts.add(drawAProtoCloudMel);
			// acts.add(drawAArcMel);

		}

//		acts.add(new DrawArcFasterVersion1(mel, flatLand));
		acts.add(new GoInAStrightLineFor(flatlander, (int) (Math.random() * 500)));

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
