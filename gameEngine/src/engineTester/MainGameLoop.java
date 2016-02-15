package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		// Create the Display
		DisplayManager.createDisplay();
		
		// Creates a new loader, renderer, and a new static shader
		Loader loader = new Loader();
		
		// Create the model
		RawModel model = OBJLoader.loadObjModel("tree", loader);
		RawModel grassModel = OBJLoader.loadObjModel("grassModel", loader);
		RawModel fernModel = OBJLoader.loadObjModel("fern", loader);
		
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture
				(loader.loadTexture("tree")));
		TexturedModel grass = new TexturedModel(grassModel, 
				new ModelTexture(loader.loadTexture("grassTexture")));
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		TexturedModel fern = new TexturedModel(fernModel, 
				new ModelTexture(loader.loadTexture("fern")));
		fern.getTexture().setHasTransparency(true);
		
		
		List<Entity> entities = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			entities.add(new Entity(staticModel, new Vector3f(random.nextFloat() * 800 - 400, 0,
					random.nextFloat() * -600), 0, 0, 0, 3));
			entities.add(new Entity(grass, new Vector3f(random.nextFloat() * 800 - 400, 0,
					random.nextFloat() * -600), 0, 0, 0, 1));
			entities.add(new Entity(fern, new Vector3f(random.nextFloat() * 800 - 400, 0,
					random.nextFloat() * -600), 0, 0, 0, 0.6f));
		}		
		
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
		
		Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass")));
		
		Camera camera = new Camera();		
		MasterRenderer renderer = new MasterRenderer();
		
		// Main game loop
		while(!Display.isCloseRequested()) {
			camera.move();
			
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			for(Entity entity:entities) {
				renderer.processEntity(entity);
			}
			
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
			
		}
		
		// Delete all of the vaos, vbos, shaders, and destroy the display

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}

}
