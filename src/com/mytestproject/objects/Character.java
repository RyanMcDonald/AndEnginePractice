package com.mytestproject.objects;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;

public class Character {

	public enum Gender {
		MALE,
		FEMALE
	}
	
	public enum CharacterClass {
		WARRIOR,
		ROGUE,
		MAGE
	}

	private String characterName;	
	private Gender gender;
	private CharacterClass characterClass;
	
	private BitmapTextureAtlas characterTextureAtlas;
	private TiledTextureRegion characterTextureRegion;
	private AnimatedSprite characterSprite;
	
	private Integer health;
	private Integer mana;
	
	private Integer strength;
	private Integer agility;
	private Integer intelligence;
	
	private Integer movementSpeed;
	
	public Character(String characterName, Gender gender) {
		
		this.characterName = characterName;
		this.gender = gender;
		
		this.health = 100;
		this.mana = 100;
		
		this.strength = 0;
		this.agility = 0;
		this.intelligence = 0;
		
		this.setMovementSpeed(100);
	}

	public void loadResources(Context context, TextureManager textureManager, String assetPath, Integer assetWidth, Integer assetHeight) {
		characterTextureAtlas = new BitmapTextureAtlas(textureManager, assetWidth, assetHeight, TextureOptions.DEFAULT);
		// 192, 256		
		characterTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(characterTextureAtlas, context, assetPath, 0, 0, 6, 8);
		characterTextureAtlas.load();
	}
	
	public void createSprite(BaseGameActivity activity) {
		characterSprite = new AnimatedSprite(0, 0, characterTextureRegion, activity.getVertexBufferObjectManager());	
	}
	
	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public CharacterClass getCharacterClass() {
		return characterClass;
	}

	public void setCharacterClass(CharacterClass characterClass) {
		this.characterClass = characterClass;
	}

	public Integer getHealth() {
		return health;
	}

	public void setHealth(Integer health) {
		this.health = health;
	}

	public Integer getMana() {
		return mana;
	}

	public void setMana(Integer mana) {
		this.mana = mana;
	}

	public Integer getStrength() {
		return strength;
	}

	public void setStrength(Integer strength) {
		this.strength = strength;
	}

	public Integer getAgility() {
		return agility;
	}

	public void setAgility(Integer agility) {
		this.agility = agility;
	}

	public Integer getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(Integer intelligence) {
		this.intelligence = intelligence;
	}

	public Integer getMovementSpeed() {
		return movementSpeed;
	}

	public void setMovementSpeed(Integer movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

	public BitmapTextureAtlas getCharacterTextureAtlas() {
		return characterTextureAtlas;
	}

	public void setCharacterTextureAtlas(BitmapTextureAtlas characterTextureAtlas) {
		this.characterTextureAtlas = characterTextureAtlas;
	}

	public TiledTextureRegion getCharacterTextureRegion() {
		return characterTextureRegion;
	}

	public void setCharacterTextureRegion(TiledTextureRegion characterTextureRegion) {
		this.characterTextureRegion = characterTextureRegion;
	}

	public AnimatedSprite getCharacterSprite() {
		return characterSprite;
	}

	public void setCharacterSprite(AnimatedSprite characterSprite) {
		this.characterSprite = characterSprite;
	}
}
