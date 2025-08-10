package FlatLand.Physics;

import java.util.ArrayList;

import FlatLander.FlatLandFacebook;
import FlatLander.FlatLander;
import Logging.LOG;
import Player.Player;
import XMLLEVELLOADER.FlatLanderWrper;
import XMLLEVELLOADER.PlayerWrper;

public class Physics extends LOG implements Actions.Physics  {

	private Double gravity;
    private double airResistance; // New field for air resistance
    private int cameraPosYinFlatland;
    private int cameraHeight;

    public Physics(double gravity, double airResistance, int cameraPosYinFlatland, int cameraHeight) {
        this.gravity = gravity;
        this.airResistance = airResistance;
        this.cameraPosYinFlatland = cameraPosYinFlatland;
        this.cameraHeight = cameraHeight;
    }


	public Integer fallDistance(FlatLander flatLander) {
        double mass = flatLander.getMass(); // Assuming FlatLander has a getMass() method
        double time = UpdateTimeSingleton.getInstance().getCurrentTime();

        // Calculate acceleration with air resistance
        double acceleration = gravity - (airResistance / mass);

        // Ensure acceleration is not negative
        if (acceleration < 0) {
            acceleration = 0;
        }

        // Calculate velocity and distance
        double velocity = acceleration * time;
        double distance = 0.5 * acceleration * Math.pow(time, 2);

        if (distance > 0 && distance < 1) {
            return 1;
        }

        return (int) distance;
    }

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
				} else if (flatLander instanceof PlayerWrper
						&& flatLanderToCheckForCollisions instanceof FlatLanderWrper) {

					if (!flatLander.equals(flatLanderToCheckForCollisions) && (((PlayerWrper) flatLander)
							.collidesWith(((FlatLanderWrper) flatLanderToCheckForCollisions)))) {
						collision = true;
						collidesFrom = (((PlayerWrper) flatLander)
								.collidesFrom(((FlatLanderWrper) flatLanderToCheckForCollisions)));

						flatLanderToCheckForCollisionsCollided = flatLanderToCheckForCollisions;
						break;

					}
					if (!flatLander.equals(flatLanderToCheckForCollisions) && ((((PlayerWrper) flatLander)
							.passesThrough(((FlatLanderWrper) flatLanderToCheckForCollisions))))) {
						collision = true;
						collidesFrom = (((PlayerWrper) flatLander)
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
						&& flatLanderToCheckForCollisions instanceof PlayerWrper) {

					if (!flatLander.equals(flatLanderToCheckForCollisions) && (((FlatLanderWrper) flatLander)
							.collidesWith(((PlayerWrper) flatLanderToCheckForCollisions)))) {
						collision = true;
						collidesFrom = (((FlatLanderWrper) flatLander)
								.collidesFrom(((PlayerWrper) flatLanderToCheckForCollisions)));

						flatLanderToCheckForCollisionsCollided = flatLanderToCheckForCollisions;
						break;

					}
					if (!flatLander.equals(flatLanderToCheckForCollisions) && ((((FlatLanderWrper) flatLander)
							.passesThrough(((PlayerWrper) flatLanderToCheckForCollisions))))) {
						collision = true;
						collidesFrom = (((FlatLanderWrper) flatLander)
								.collidesFrom(((PlayerWrper) flatLanderToCheckForCollisions)));

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
			if (flatLander instanceof PlayerWrper) {
				PlayerWrper flatLander2 = (PlayerWrper) flatLander;
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
			if (flatLander instanceof PlayerWrper) {
				PlayerWrper flatLander2 = (PlayerWrper) flatLander;
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

				if (flatLander instanceof PlayerWrper) {
					PlayerWrper flatLander2 = (PlayerWrper) flatLander;
					if (flatLanderToCheckForCollisionsCollided instanceof FlatLanderWrper) {
						while (flatLander2.collidesWith(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))
								|| flatLander2
										.passesThrough(((FlatLanderWrper) flatLanderToCheckForCollisionsCollided))) {
							flatLander2.changeMoveYBy(1);
							flatLander2.update();
						}
					}
					LOG.println("hey");
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
				if (flatLander instanceof PlayerWrper) {
					PlayerWrper flatLander2 = (PlayerWrper) flatLander;
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

	
    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public void setAirResistance(double airResistance) {
        this.airResistance = airResistance;
    }
	
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
