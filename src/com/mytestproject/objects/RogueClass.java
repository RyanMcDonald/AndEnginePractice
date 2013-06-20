package com.mytestproject.objects;

import org.andengine.opengl.texture.TextureManager;


import android.content.Context;

public class RogueClass extends Character {

	private String spriteAssetPath;
	
	public RogueClass (String characterName, Gender gender) {
		super(characterName, gender);
		
		setHealth(150);
		setMana(150);
		
		setStrength(5);
		setAgility(10);
		setIntelligence(5);
		
		setMovementSpeed(120);
	}
	
	public void loadResources(Context context, TextureManager textureManager) {
		
		switch (getGender()) {
		case MALE:
			spriteAssetPath = "male_rogue.png";
			break;
		case FEMALE:
			spriteAssetPath = "female_rogue.png";
			break;
		}

		super.loadResources(context, textureManager, spriteAssetPath, 192, 256);
	}
}
