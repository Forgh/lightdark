package com.me.lightdark.vues;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.me.lightdark.modeles.Animal;
import com.me.lightdark.modeles.Anime;
import com.me.lightdark.modeles.Anime.AnimeEspece;
import com.me.lightdark.modeles.Case;
import com.me.lightdark.modeles.Dark;
import com.me.lightdark.modeles.Epee;
import com.me.lightdark.modeles.Form;
import com.me.lightdark.modeles.Light;
import com.me.lightdark.modeles.Monde;
import com.me.lightdark.modeles.Niveau;
import com.me.lightdark.modeles.Objet;
import com.me.lightdark.modeles.Perso;
import com.me.lightdark.modeles.Perso.direction;
import com.me.lightdark.modeles.Projectile;

public class AfficherMonde {

	private float CAM_LARG = 15f;
	private float CAM_HAUT = 13f;
	private static final float DUREE_IMAGES = 0.06f;
	
	private AfficherSideMenu menu;
	private Monde monde;
	private OrthographicCamera cam;
	
	ShapeRenderer debugRenderer = new ShapeRenderer();
	
	//Les textures fixes TODO : animer celles qui en ont besoin---------------------------------
	private TextureRegion imgSol;
	private TextureRegion imgObstacle;
	private TextureRegion imgOmbre;
	//private TextureRegion imgLightForm;
	//private TextureRegion imgDarkForm;
	private TextureRegion currentFrameLight_left;
	private TextureRegion currentFrameLight_right;
	private TextureRegion currentFrameLight_up;
	private TextureRegion currentFrameLight_down;
	
	private TextureRegion currentFrameDark_left;
	private TextureRegion currentFrameDark_right;
	private TextureRegion currentFrameDark_up;
	private TextureRegion currentFrameDark_down;
	
	private TextureRegion lightIdle_left;
	private TextureRegion lightIdle_right;
	private TextureRegion lightIdle_up;
	private TextureRegion lightIdle_down;
	
	private TextureRegion darkIdle_left;
	private TextureRegion darkIdle_right;
	private TextureRegion darkIdle_up;
	private TextureRegion darkIdle_down;
	
	
	private TextureRegion imgDarkFormTaming;
	private TextureRegion imgProjectile;
	private TextureRegion imgSword;
	private TextureRegion imgAnimal;
	private TextureRegion imgMonstreCube;
	private TextureRegion imgTorche;
	//Les Atlas pour les textures animées--------------------------------------
	private Texture atlasPersoLight;//L'atlas brut (.png) de l'ensemble des animations du perso
	private Texture atlasPersoDark;
	
	//Les Animations TbT (Texture-by-Texture)
	private TextureRegion[] lightWalking_leftTbt;
	private TextureRegion[] lightWalking_rightTbt;
	private TextureRegion[] lightWalking_upTbt;
	private TextureRegion[] lightWalking_downTbt;
	
	private TextureRegion[] darkWalking_leftTbt;
	private TextureRegion[] darkWalking_rightTbt;
	private TextureRegion[] darkWalking_upTbt;
	private TextureRegion[] darkWalking_downTbt;
	
	
	
	//Les Textures animées----------------------------------
	private Animation lightWalking_left;
	private Animation lightWalking_right;
	private Animation lightWalking_up;
	private Animation lightWalking_down;
	
	private Animation darkWalking_left;
	private Animation darkWalking_right;
	private Animation darkWalking_up;
	private Animation darkWalking_down;
	
	
	
	private SpriteBatch spriteBatch;
	
	private boolean deboguage = false;
	
	float persoTime;//Compteur de l'état d'animation du personnage
	
	private int width;
	private int height;
	// ppu: Pixel Par Unite
	private float ppuX;	
	private float ppuY;	
	
	public void setSize (int w, int h) {
		this.width = w;
		this.height = h;
		
		ppuX = (float)width / CAM_LARG;
		ppuY = (float)height / CAM_HAUT;
		
		//System.out.println("ppuX, ppuY :"+ppuX+" "+ppuY);
	}
	
	public int getWidth(){
		return this.width;
	}
	public AfficherMonde( Monde monde, boolean debug) {
		this.monde = monde;
		
		this.cam =  new OrthographicCamera(CAM_LARG / 2f,CAM_HAUT / 2f);
		this.cam.position.set(CAM_LARG / 2f, CAM_HAUT / 2f, 0); // ici on place la camera au centre
		this.deboguage = debug;
		
		spriteBatch = new SpriteBatch();
		mapperAnimations();
		
		chargerTextures();
	}

	/*charge les animations depuis les atlas bruts*/
	private void mapperAnimations(){
		mapperPerso();
	}
	
	
	private void mapperPerso(){
	
		atlasPersoLight = new Texture(Gdx.files.internal("images/atlas/perso_light.png"));//32 colonnes, 34 lignes d'images (parfois vides)
		atlasPersoDark = new Texture(Gdx.files.internal("images/atlas/perso_dark.png"));//idem
		
		TextureRegion[][] tempLight = TextureRegion.split(atlasPersoLight, atlasPersoLight.getWidth()/32, atlasPersoLight.getHeight()/64);
		TextureRegion[][] tempDark = TextureRegion.split(atlasPersoDark, atlasPersoDark.getWidth()/32, atlasPersoDark.getHeight()/64);
		
		//Inactif
		lightIdle_left = tempLight[0][1];
		lightIdle_right = tempLight[0][3];
		lightIdle_up = tempLight[0][0];
		lightIdle_down = tempLight[0][2];
		
		darkIdle_left = tempDark[0][1];
		darkIdle_right = tempDark[0][3];
		darkIdle_up = tempDark[0][0];
		darkIdle_down = tempDark[0][2];
		
		//Marchant
		lightWalking_leftTbt = new TextureRegion[9];//9 images pour le déplacement
		lightWalking_rightTbt = new TextureRegion[9];
		lightWalking_upTbt = new TextureRegion[9];
		lightWalking_downTbt = new TextureRegion[9];
		
		darkWalking_leftTbt = new TextureRegion[9];
		darkWalking_rightTbt = new TextureRegion[9];
		darkWalking_upTbt = new TextureRegion[9];
		darkWalking_downTbt = new TextureRegion[9];
		
		//Déplacements lightForm
		for(int i=0; i<9; i++)
			lightWalking_leftTbt[i]=tempLight[9][i];//8e ligne
		for(int i=0; i<9; i++)
			lightWalking_rightTbt[i]=tempLight[11][i];
		for(int i=0; i<9; i++)
			lightWalking_upTbt[i]=tempLight[8][i];
		for(int i=0; i<9; i++)
			lightWalking_downTbt[i]=tempLight[10][i];
		
		//Déplacements shdowForm
		for(int i=0; i<9; i++)
			darkWalking_leftTbt[i]=tempDark[9][i];
		for(int i=0; i<9; i++)
			darkWalking_rightTbt[i]=tempDark[11][i];
		for(int i=0; i<9; i++)
			darkWalking_upTbt[i]=tempDark[8][i];
		for(int i=0; i<9; i++)
			darkWalking_downTbt[i]=tempDark[10][i];
		
		
		lightWalking_left = new Animation(1/9f, lightWalking_leftTbt);
		lightWalking_right = new Animation(1/9f, lightWalking_rightTbt);
		lightWalking_up = new Animation(1/9f, lightWalking_upTbt);
		lightWalking_down = new Animation(1/9f, lightWalking_downTbt);
		
		darkWalking_left = new Animation(1/9f, darkWalking_leftTbt);
		darkWalking_right = new Animation(1/9f, darkWalking_rightTbt);
		darkWalking_up = new Animation(1/9f, darkWalking_upTbt);
		darkWalking_down = new Animation(1/9f, darkWalking_downTbt);
		
		persoTime = 0f;//reset du stateTime, augmente à chaque appel de render()
	}
	
	/*Met à jour les currentFrames du perso*/
	private void majPerso(){
		persoTime+=Gdx.graphics.getDeltaTime();
		
		currentFrameLight_left = lightWalking_left.getKeyFrame(persoTime, true);
		currentFrameLight_right = lightWalking_right.getKeyFrame(persoTime, true);
		currentFrameLight_up = lightWalking_up.getKeyFrame(persoTime, true);
		currentFrameLight_down = lightWalking_down.getKeyFrame(persoTime, true);
		
		currentFrameDark_left = darkWalking_left.getKeyFrame(persoTime, true);
		currentFrameDark_right = darkWalking_right.getKeyFrame(persoTime, true);
		currentFrameDark_up = darkWalking_up.getKeyFrame(persoTime, true);
		currentFrameDark_down = darkWalking_down.getKeyFrame(persoTime, true);
	}
	
	private void chargerTextures() {
		// TODO mettre a jour avec des atlas
		//this.imgLightForm = new TextureRegion(new Texture(Gdx.files.internal("images/light.png")));
		//this.imgDarkForm = new TextureRegion(new Texture(Gdx.files.internal("images/dark.png")));
		this.imgDarkFormTaming = new TextureRegion(new Texture(Gdx.files.internal("images/taming.png")));
		this.imgSol = new TextureRegion(new Texture(Gdx.files.internal("images/herbe_seche.png")));
		this.imgObstacle = new TextureRegion(new Texture(Gdx.files.internal("images/roche.png")));
		this.imgOmbre= new TextureRegion(new Texture(Gdx.files.internal("images/ombre.png")));
		this.imgProjectile = new TextureRegion(new Texture(Gdx.files.internal("images/projectile.png")));
		this.imgSword = new TextureRegion(new Texture(Gdx.files.internal("images/sword.png")));
		this.imgAnimal = new TextureRegion(new Texture(Gdx.files.internal("images/cat_laptop.png")));
		this.imgMonstreCube = new TextureRegion(new Texture(Gdx.files.internal("images/monstre_cube.png")));
		
		this.imgTorche  = new TextureRegion(new Texture(Gdx.files.internal("images/torche.png")));
		
		// TODO rajouter pour toutes les cases
		 
	}
	
	public void render() {
		
		
		 
		
		majPerso();
		
		spriteBatch.begin();

		// dessiner strate 1
		drawMap();
		// dessiner strate 2
		drawProjectile();
		drawSword();
		drawFleche();
		// dessiner strate 3
		drawAnimals();
		drawPerso();
		
		
		drawObjet();
		
		spriteBatch.end();
		
	debugRenderer.setProjectionMatrix(cam.combined);
	  debugRenderer.begin(ShapeType.Line);
	  Rectangle rect = monde.getPerso().getCadre();
	debugRenderer.rect(monde.getPerso().getPosition().x/ppuX + rect.x, monde.getPerso().getPosition().y + rect.y/ppuY, rect.width, rect.height);
		debugRenderer.setColor(new Color(1, 0, 0, 1));
		debugRenderer.end();
	
	}
	
	private void drawMap(){
		Niveau niveau = monde.getNiveau();
		Array<Case> cases=  monde.getAffichable(this.width, this.height);
		for(int i = 0; i<cases.size;i++){
			 Case c = cases.get(i);
			 if (c.getTypeCase() != null){
				 if(c.getTypeCase().equals("TERRE")){
					 spriteBatch.draw(this.imgSol, c.getPosition().x * ppuX, c.getPosition().y * ppuY, c.TAILLE * ppuX, c.TAILLE * ppuY);
				 }
				 else if(c.getTypeCase().equals("MONTAGNE")){
					 spriteBatch.draw(this.imgObstacle, c.getPosition().x * ppuX, c.getPosition().y * ppuY, c.TAILLE * ppuX, c.TAILLE * ppuY);
				 }
				 else if(c.getTypeCase().equals("OMBRE")){
					 spriteBatch.draw(this.imgOmbre, c.getPosition().x * ppuX, c.getPosition().y * ppuY, c.TAILLE * ppuX, c.TAILLE * ppuY);
				 }
			 }
		}
		/*for(int i = 0; i<monde.niveau.size;i++){
			 Case c = monde.getCase().get(i);
			 spriteBatch.draw(this.imgCase, c.getPosition().x * ppuX, c.getPosition().y * ppuY, c.TAILLE * ppuX, c.TAILLE * ppuY);
		}*/
	}
	
	private void drawPerso(){
		Perso p = monde.getPerso();
		
		direction d = p.getDirection();
		System.out.println("Forme : "+p.getForm()+ " Etat : "+p.getEtat()+" Direction : "+p.getDirection());
		
		if(p.getForm()==Form.LIGHTFORM){
			
			if(p.getEtat()!=null && p.getEtat()==Light.MARCHANT){//si marche
		
				
				if(d==direction.BAS || d==direction.BAS_GAUCHE || d==direction.BAS_DROITE)
					spriteBatch.draw(currentFrameLight_down, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
				if(d==direction.HAUT || d==direction.HAUT_GAUCHE || d==direction.HAUT_DROITE)
					spriteBatch.draw(currentFrameLight_up, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
				if(d==direction.GAUCHE)
					spriteBatch.draw(currentFrameLight_left, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
				if(d==direction.DROITE)
					spriteBatch.draw(currentFrameLight_right, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
					
			}
			else if(p.getEtat()==null || p.getEtat()==Light.INACTIF || p.getEtat()==Dark.GRABBING){//si inactif
				
				if(d==direction.BAS)
					spriteBatch.draw(lightIdle_down, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
				if(d==direction.HAUT)
					spriteBatch.draw(lightIdle_up, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
				if(d==direction.GAUCHE)
					spriteBatch.draw(lightIdle_left, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
				if(d==direction.DROITE)
					spriteBatch.draw(lightIdle_right, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
			}
		}
		
		
		else if(p.getForm()==Form.SHADOWFORM){
			if(p.getEtat()==Dark.SHADOWWALKING){//ShadowForm classique
				if(d==direction.BAS || d==direction.BAS_GAUCHE || d==direction.BAS_DROITE)
					spriteBatch.draw(currentFrameDark_down, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
				if(d==direction.HAUT || d==direction.HAUT_GAUCHE || d==direction.HAUT_DROITE)
					spriteBatch.draw(currentFrameDark_up, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
				if(d==direction.GAUCHE)
					spriteBatch.draw(currentFrameDark_left, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
				if(d==direction.DROITE)
					spriteBatch.draw(currentFrameDark_right, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
			}
			if(p.getEtat()==Dark.TAMING) //ShadowForm Taming
				spriteBatch.draw(this.imgDarkFormTaming, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
			
			else if(p.getEtat()==null || p.getEtat()==Dark.IDLE){//si inactif
				
				if(d==direction.BAS)
					spriteBatch.draw(darkIdle_down, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
				if(d==direction.HAUT)
					spriteBatch.draw(darkIdle_up, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
				if(d==direction.GAUCHE)
					spriteBatch.draw(darkIdle_left, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
				if(d==direction.DROITE)
					spriteBatch.draw(darkIdle_right, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
			}
			
		}
	}
	
	private void drawAnimals(){
		Array<Anime> project = monde.getAnime();
		 for(int i=0;i<project.size;i++){
			 if (project.get(i).getAnimeEspece() == AnimeEspece.MONSTRE_CUBE){
				 spriteBatch.draw(this.imgMonstreCube, project.get(i).getPosition().x * ppuX, project.get(i).getPosition().y * ppuY, project.get(i).TAILLE * ppuX, project.get(i).TAILLE * ppuY);
			 }else{
				 spriteBatch.draw(this.imgAnimal, project.get(i).getPosition().x * ppuX, project.get(i).getPosition().y * ppuY, project.get(i).TAILLE * ppuX, project.get(i).TAILLE * ppuY);
			 }
			
		 }
	}
	
	
	private void drawProjectile(){
		 Array<Projectile> project = monde.getProjectile();
		 for(int i=0;i<project.size;i++){
			 spriteBatch.draw(this.imgProjectile, project.get(i).getPosition().x * ppuX, project.get(i).getPosition().y * ppuY, project.get(i).TAILLE * ppuX, project.get(i).TAILLE * ppuY);
		 }
	}
	
	private void drawFleche(){
		 Array<Projectile> project = monde.getFleche();
		 for(int i=0;i<project.size;i++){
			 spriteBatch.draw(this.imgProjectile, project.get(i).getPosition().x * ppuX, project.get(i).getPosition().y * ppuY, project.get(i).TAILLE * ppuX, project.get(i).TAILLE * ppuY);
		 }
	}
	
	private void drawObjet(){
		 Array<Objet> project = monde.getObjet();
		 for(int i=0;i<project.size;i++){
			 spriteBatch.draw(this.imgTorche, project.get(i).getPosition().x * ppuX, project.get(i).getPosition().y * ppuY, project.get(i).TAILLE * ppuX, project.get(i).TAILLE * ppuY);
		 }
	}
	
	private void drawSword(){
		 Array<Epee> sword = monde.getEpee();
		 for(int i=0;i<sword.size;i++){
			 spriteBatch.draw(this.imgSword, sword.get(i).getPosition().x * ppuX, sword.get(i).getPosition().y * ppuY, sword.get(i).TAILLE * ppuX, sword.get(i).TAILLE * ppuY);
		 }
	}
	
	

}
