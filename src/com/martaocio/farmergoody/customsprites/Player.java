package com.martaocio.farmergoody.customsprites;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.martaocio.farmergoody.BodyGroups;
import com.martaocio.farmergoody.ResourceManager;
import com.martaocio.farmergoody.domain.GameSession;
import com.martaocio.farmergoody.domain.UserState;
import com.martaocio.farmergoody.domain.Vehicle;
import com.martaocio.farmergoody.providers.VelocityProvider;

public abstract class Player extends AnimatedSprite {

	private Body body;
	public boolean isAlive = true;
	public boolean isTurbo = false;
	public boolean canJump = true;
	public boolean canTurbo = true;
	public boolean canEat = false;
	public Vehicle vehicleUsed;

	public Player(float pX, float pY, float width, float height, VertexBufferObjectManager vbom, BoundCamera camera,
			PhysicsWorld physicsWorld) {
		super(pX, pY, width, height, getSelectedPlayerTexture(UserState.getInstance().getSelectedSession().getVehicleUsed()), vbom);
		GameSession selectedSession = UserState.getInstance().getSelectedSession();
		vehicleUsed = selectedSession.getVehicleUsed();
		boolean isTraningLevel = selectedSession.getCurrentLevel() == 0;
		createPhysics(camera, physicsWorld);

		camera.setChaseEntity(this);
		if (isTraningLevel) {
			camera.setBounds(0, 190, 6400, 290);
		} else {
			camera.setBounds(0, 190, 10560, 290);
		}
		camera.setBoundsEnabled(true);
		camera.offsetCenter(0, 0);

	}

	private void createPhysics(final BoundCamera camera, PhysicsWorld physicsWorld) {
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, BodyGroups.PLAYER_FIXTURE_DEF);

		body.setUserData("player");
		body.setFixedRotation(true);// because the body has to be still, it
									// doesnt need to rotate

		// this conector links the body to the sprite so the sprite can follow
		// the body and viceversa
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false) {

			// add an update handler
			@Override
			public void onUpdate(float pSecondsElapsed) {

				// every second the body is gonna update two body so the body
				// keep moving

				super.onUpdate(pSecondsElapsed);

				camera.onUpdate(.1f);// to keep the camera centered when the
										// sprite is moving

				if (!isTurbo) {
					// detect the position of the player, this detection must be
					// in
					// the onnUpdate so the position of the
					// player can be check constantly
					if (getY() >= 480) {
						// onDie();
						camera.setChaseEntity(null);
					}
					float velocityX = VelocityProvider.getVelocityByLevel(vehicleUsed);
					body.setLinearVelocity(new Vector2(velocityX, body.getLinearVelocity().y));
				} else {
					float velocityX = VelocityProvider.getTurboVelocity(vehicleUsed);
					body.setLinearVelocity(new Vector2(velocityX, body.getLinearVelocity().y));

				}
			}
		});

	}

	private static ITiledTextureRegion getSelectedPlayerTexture(Vehicle vehiculeSelected) {
		if (vehiculeSelected.equals(vehiculeSelected.UNICYCLE)) {
			return ResourceManager.getInstance().playerRidingUnicycleTexture;
		} else if (vehiculeSelected.equals(vehiculeSelected.BICYCLE)) {
			return ResourceManager.getInstance().playerRidingBicycleTexture;
		} else if (vehiculeSelected.equals(vehiculeSelected.SCOOTER)) {
			return ResourceManager.getInstance().playerRidingBicycleTexture;
		} else if (vehiculeSelected.equals(vehiculeSelected.HARLEY)) {
			return ResourceManager.getInstance().playerRidingBicycleTexture;
		} else {
			return ResourceManager.getInstance().playerTexture;
		}

	}

	public abstract void onDie();

	// to animate the player
	public void setRunning() {
		canTurbo = true;
		if (isAlive) {
			Vehicle selectedVehicle = UserState.getInstance().getSelectedSession().getVehicleUsed();
			if (!this.isAnimationRunning()) {// if animation is not running
				canEat = false;
				final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100, 100 };// array
																				// to
																				// hold
																				// the
				boolean isPlayerWalkingOrMonocycle = selectedVehicle.equals(Vehicle.NONE)
						|| UserState.getInstance().getSelectedSession().getVehicleUsed().equals(Vehicle.UNICYCLE);
				if (isPlayerWalkingOrMonocycle) // animations
					animate(PLAYER_ANIMATE, 0, 3, true);
				else {
					if (selectedVehicle.equals(Vehicle.BICYCLE)) {
						animate(PLAYER_ANIMATE, 0, 3, true);
					} else if (selectedVehicle.equals(Vehicle.SCOOTER)) {
						animate(PLAYER_ANIMATE, 6, 9, true);
					} else if (selectedVehicle.equals(Vehicle.HARLEY)) {
						animate(PLAYER_ANIMATE, 12, 15, true);
					}

				}
			} else if (this.isAnimationRunning()) {
				this.stopAnimation();
				canEat = false;
				final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100, 100 };// array
																				// to
																				// hold
																				// the
																				// animations
				boolean isPlayerWalkingOrMonocycle = selectedVehicle.equals(Vehicle.NONE)
						|| UserState.getInstance().getSelectedSession().getVehicleUsed().equals(Vehicle.UNICYCLE);
				if (isPlayerWalkingOrMonocycle) // animations
					animate(PLAYER_ANIMATE, 0, 3, true);
				else {
					if (selectedVehicle.equals(Vehicle.BICYCLE)) {
						animate(PLAYER_ANIMATE, 0, 3, true);
					} else if (selectedVehicle.equals(Vehicle.SCOOTER)) {
						animate(PLAYER_ANIMATE, 6, 9, true);
					} else if (selectedVehicle.equals(Vehicle.HARLEY)) {
						animate(PLAYER_ANIMATE, 12, 15, true);
					}

				}
			}

		}
	}

	// make the player jumps
	public void jump() {
		// first stop the player from running
		//if (canJump) {

			ResourceManager.getInstance().jumpSound.play();
			// comment this if the walking animation is gonna keep running while
			// jumping
			this.stopAnimation();
			Vehicle selectedVehicle = UserState.getInstance().getSelectedSession().getVehicleUsed();
			boolean isPlayerWalkingOrMonocycle = selectedVehicle.equals(Vehicle.NONE)
					|| UserState.getInstance().getSelectedSession().getVehicleUsed().equals(Vehicle.UNICYCLE);
			if (isPlayerWalkingOrMonocycle) // animations
				this.setCurrentTileIndex(4);
			else {

				if (selectedVehicle.equals(Vehicle.BICYCLE)) {
					this.setCurrentTileIndex(4);
				} else if (selectedVehicle.equals(Vehicle.SCOOTER)) {
					this.setCurrentTileIndex(10);
				} else if (selectedVehicle.equals(Vehicle.SCOOTER)) {
					this.setCurrentTileIndex(16);
				}

			}

			//canJump = false;

			Vector2 velocity = new Vector2(0, -8); // set the height of the
													// jump(10 metres off the
													// ground)

			body.setLinearVelocity(velocity);
	//	}
	}

	public void eat() {

		if (!canEat) {
			this.stopAnimation();

			this.setCurrentTileIndex(4);

			canEat = true;

			Vector2 velocity = new Vector2(0, 0);

			body.setLinearVelocity(velocity);

		}
	}

	public void runTurbo() {

		this.stopAnimation();
		Vehicle selectedVehicle = UserState.getInstance().getSelectedSession().getVehicleUsed();
		boolean isPlayerWalkingOrMonocycle = selectedVehicle.equals(Vehicle.NONE)
				|| UserState.getInstance().getSelectedSession().getVehicleUsed().equals(Vehicle.UNICYCLE);

		if (!isPlayerWalkingOrMonocycle) {

			if (selectedVehicle.equals(Vehicle.BICYCLE)) {
				this.setCurrentTileIndex(18);

			} else if (selectedVehicle.equals(Vehicle.SCOOTER)) {
				this.setCurrentTileIndex(19);
				// body.applyForce(velocity,body.getWorldCenter());
			} else if (selectedVehicle.equals(Vehicle.HARLEY)) {
				this.setCurrentTileIndex(20);

			}

		}
		
	}

	public void runFaster(BoundCamera camera) {

		body.setTransform(body.getPosition().x + 1f, body.getPosition().y, 0);
		camera.offsetCenter(camera.getCenterX() + 1, camera.getCenterY());

	}

	public void runSlower() {
		// float currentVelocity=body.getLinearVelocity().x;
		body.setTransform(body.getPosition().x - 1f, body.getPosition().y, 0);

	}

	public void setDead() {
		isAlive = false;
		if (this.isAnimationRunning())
			this.stopAnimation();
		this.setCurrentTileIndex(5);
	}

}
