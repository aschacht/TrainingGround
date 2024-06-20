package FlatLand.Physics;

import java.util.ArrayList;

import FlatLander.FlatLandFacebook;
import FlatLander.FlatLander;
import Player.Player;
import XMLLoader.FlatLanderWrper;

public class Physics implements Actions.Physics {

	private Double gravity;
	private int cameraPosYinFlatland;
	private int cameraHeight;

	public Physics(double d, int cameraPosYinFlatland, int cameraHeight) {
		this.gravity = d;
		this.cameraPosYinFlatland = cameraPosYinFlatland;
		this.cameraHeight = cameraHeight;

	}

	@Override
	public Integer fallDistance(FlatLander flatLander) {
		Double time = UpdateTimeSingleton.getInstance().getCurrentTime();
		double tim = Math.pow(time, 2) / 2;

		double d = gravity * tim;

		if (d > 0 && d < 1) {

			return 1;
		}

		return (int) d;

	}

	@Override
	public void applyPhysics() {
		ArrayList<FlatLander> bookOfFlatLanders = FlatLandFacebook.getInstance().getFlatlanderFaceBook();

		for (FlatLander flatLander : bookOfFlatLanders) {
			if (flatLander.shouldPhysicsApply()) {
				Integer fallDistance = fallDistance(flatLander);
				int moveY = flatLander.getMoveY() + fallDistance;

				flatLander.changeMoveYBy(moveY);
				flatLander.updatecurrentBB();
				checkForCollisions(bookOfFlatLanders);
			}
		}

	}

	private void checkForCollisions(ArrayList<FlatLander> flatLanderFaceBook) {

		for (FlatLander flatLander : flatLanderFaceBook) {
			Boolean collision = false;
			int collidesFrom = 0;
			boolean above = false;
			FlatLander flatLanderToCheckForCollisionsCollided = null;
			for (FlatLander flatLanderToCheckForCollisions : flatLanderFaceBook) {

				if (flatLander instanceof FlatLanderWrper
						&& flatLanderToCheckForCollisions instanceof FlatLanderWrper) {

					if (!flatLander.equals(flatLanderToCheckForCollisions) && (((FlatLanderWrper) flatLander)
							.collidesWith(((FlatLanderWrper) flatLanderToCheckForCollisions)))) {
						collision = true;
						collidesFrom = (((FlatLanderWrper) flatLander)
								.collidesFrom(((FlatLanderWrper) flatLanderToCheckForCollisions)));

						flatLanderToCheckForCollisionsCollided = flatLanderToCheckForCollisions;
						break;

					}
					if (!flatLander.equals(flatLanderToCheckForCollisions) && ((((FlatLanderWrper) flatLander)
							.passesThrough(((FlatLanderWrper) flatLanderToCheckForCollisions))))) {
						collision = true;
						collidesFrom = (((FlatLanderWrper) flatLander)
								.collidesFrom(((FlatLanderWrper) flatLanderToCheckForCollisions)));

						flatLanderToCheckForCollisionsCollided = flatLanderToCheckForCollisions;
						break;

					}
					if (!flatLander.equals(flatLanderToCheckForCollisions)
							&& (flatLander.above(flatLanderToCheckForCollisions))) {
						above = true;

						break;

					}
				} else if (flatLander instanceof XMLLoader.PlayerWrper
						&& flatLanderToCheckForCollisions instanceof FlatLanderWrper) {

					if (!flatLander.equals(flatLanderToCheckForCollisions) && (((XMLLoader.PlayerWrper) flatLander)
							.collidesWith(((FlatLanderWrper) flatLanderToCheckForCollisions)))) {
						collision = true;
						collidesFrom = (((XMLLoader.PlayerWrper) flatLander)
								.collidesFrom(((FlatLanderWrper) flatLanderToCheckForCollisions)));

						flatLanderToCheckForCollisionsCollided = flatLanderToCheckForCollisions;
						break;

					}
					if (!flatLander.equals(flatLanderToCheckForCollisions) && ((((XMLLoader.PlayerWrper) flatLander)
							.passesThrough(((FlatLanderWrper) flatLanderToCheckForCollisions))))) {
						collision = true;
						collidesFrom = (((XMLLoader.PlayerWrper) flatLander)
								.collidesFrom(((FlatLanderWrper) flatLanderToCheckForCollisions)));

						flatLanderToCheckForCollisionsCollided = flatLanderToCheckForCollisions;
						break;

					}
					if (!flatLander.equals(flatLanderToCheckForCollisions)
							&& (flatLander.above(flatLanderToCheckForCollisions))) {
						above = true;

						break;

					}
				} else if (flatLander instanceof FlatLanderWrper
						&& flatLanderToCheckForCollisions instanceof XMLLoader.PlayerWrper) {

					if (!flatLander.equals(flatLanderToCheckForCollisions) && (((FlatLanderWrper) flatLander)
							.collidesWith(((XMLLoader.PlayerWrper) flatLanderToCheckForCollisions)))) {
						collision = true;
						collidesFrom = (((FlatLanderWrper) flatLander)
								.collidesFrom(((XMLLoader.PlayerWrper) flatLanderToCheckForCollisions)));

						flatLanderToCheckForCollisionsCollided = flatLanderToCheckForCollisions;
						break;

					}
					if (!flatLander.equals(flatLanderToCheckForCollisions) && ((((FlatLanderWrper) flatLander)
							.passesThrough(((XMLLoader.PlayerWrper) flatLanderToCheckForCollisions))))) {
						collision = true;
						collidesFrom = (((FlatLanderWrper) flatLander)
								.collidesFrom(((XMLLoader.PlayerWrper) flatLanderToCheckForCollisions)));

						flatLanderToCheckForCollisionsCollided = flatLanderToCheckForCollisions;
						break;

					}
					if (!flatLander.equals(flatLanderToCheckForCollisions)
							&& (flatLander.above(flatLanderToCheckForCollisions))) {
						above = true;

						break;

					}
				}

			}
			if (collision) {
				checkEntity(flatLander, collidesFrom, flatLanderToCheckForCollisionsCollided);

			} else if (above) {

				flatLander.setMoveY(0);
			}

		}

	}

	private void checkEntity(FlatLander flatLander, int collidesFrom,
			FlatLander flatLanderToCheckForCollisionsCollided) {
		if (collidesFrom == 1) {
			// top
			if (flatLander instanceof XMLLoader.PlayerWrper) {
				XMLLoader.PlayerWrper flatLander2 = (XMLLoader.PlayerWrper) flatLander;
				if (flatLanderToCheckForCollisionsCollided instanceof FlatLanderWrper) {
					while (flatLander2.collidesWith(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))
							|| flatLander2.passesThrough(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))) {
						flatLander2.changeMoveYBy(-1);
						flatLander2.update();

					}
				}
			} else {
				if (flatLanderToCheckForCollisionsCollided instanceof FlatLanderWrper
						&& flatLander instanceof FlatLanderWrper) {
					while (((FlatLanderWrper) flatLander)
							.collidesWith(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))
							|| ((FlatLanderWrper) flatLander)
									.passesThrough(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))) {
						flatLander.changeMoveYBy(-1);
						flatLander.update();
					}
				}
			}

		} else if (collidesFrom == 2) {
			// right
			if (flatLander instanceof XMLLoader.PlayerWrper) {
				XMLLoader.PlayerWrper flatLander2 = (XMLLoader.PlayerWrper) flatLander;
				if (flatLanderToCheckForCollisionsCollided instanceof FlatLanderWrper) {
					while (flatLander2.collidesWith(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))
							|| flatLander2.passesThrough(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))) {
						flatLander2.changeMoveXBy(1);
						flatLander2.update();
					}
				}
			} else if (flatLander instanceof FlatLanderWrper) {
				{

					if (flatLanderToCheckForCollisionsCollided instanceof FlatLanderWrper) {
						while (((FlatLanderWrper) flatLander)
								.collidesWith(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))
								|| ((FlatLanderWrper) flatLander)
										.passesThrough(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))) {
							flatLander.changeMoveXBy(1);
							flatLander.update();
						}
					}
				}
			} else if (collidesFrom == 3) {
				// bottem

				if (flatLander instanceof XMLLoader.PlayerWrper) {
					XMLLoader.PlayerWrper flatLander2 = (XMLLoader.PlayerWrper) flatLander;
					if (flatLanderToCheckForCollisionsCollided instanceof FlatLanderWrper) {
						while (flatLander2.collidesWith(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))
								|| flatLander2
										.passesThrough(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))) {
							flatLander2.changeMoveYBy(1);
							flatLander2.update();
						}
					}
					System.out.println("hey");
				} else {

					if (flatLanderToCheckForCollisionsCollided instanceof FlatLanderWrper
							&& flatLander instanceof FlatLanderWrper) {
						while (((FlatLanderWrper) flatLander)
								.collidesWith(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))
								|| ((FlatLanderWrper) flatLander)
										.passesThrough(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))) {
							flatLander.changeMoveYBy(1);
							flatLander.update();
						}
					}
				}

			} else if (collidesFrom == 4) {
				// left
				if (flatLander instanceof XMLLoader.PlayerWrper) {
					XMLLoader.PlayerWrper flatLander2 = (XMLLoader.PlayerWrper) flatLander;
					if (flatLanderToCheckForCollisionsCollided instanceof FlatLanderWrper) {
						while (flatLander2.collidesWith(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))
								|| flatLander2
										.passesThrough(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))) {
							flatLander2.changeMoveXBy(-1);
							flatLander2.update();

						}
					}
				} else {

					if (flatLanderToCheckForCollisionsCollided instanceof FlatLanderWrper
							&& flatLander instanceof FlatLanderWrper) {
						while (((FlatLanderWrper) flatLander)
								.collidesWith(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))
								|| ((FlatLanderWrper) flatLander)
										.passesThrough(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))) {
							flatLander.changeMoveXBy(-1);
							flatLander.update();

						}
					}
				}
			}
		}
	}
}
