package opent4c;

import java.awt.Point;
import java.io.Serializable;

import opent4c.utils.PointsManager;
import opent4c.utils.SpriteName;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tools.UnsignedInt;
import tools.UnsignedShort;

public class MapPixel implements Serializable{
	private static final long serialVersionUID = 9062566898235672185L;
	private static Logger logger = LogManager.getLogger(MapPixel.class);
	
	/**
	 * C'est un pixel de la carte. il contient toutes les infos sur cette zone.
	 */
	private String atlas;
	private SpriteName name;
	private Point offset;
	private Point offset2;
	private int id;
	private Point taille;
	private Point modulo;
	private int indexation;
	private long numDDA;
	private Palette pal;
	private int ombre;
	private int type;
	private int transColor;
	private int inconnu9;
	private int taille_unzip;
	private int taille_zip;
	private int bufPosition;
	private String paletteName;
	private boolean tuile = false;
	private boolean valid_palette = true;

	/**
	 * 
	 */
	public MapPixel() {
		atlas = "Unknown";
		name = new SpriteName("Unknown Tile");
		offset = PointsManager.getPoint(0,0);
		offset2 = PointsManager.getPoint(0,0);
		taille = PointsManager.getPoint(1,1);
		modulo = PointsManager.getPoint(1,1);
		id = -1;
		indexation = -1;
		numDDA = -1;
		pal = null;
		ombre = -1;
		type = -1;
		transColor = -1;
		inconnu9 = -1;
		taille_unzip = -1;
		taille_zip = -1;
		bufPosition = -1;
		paletteName = "not set";
	}

	/**
	 * @param key
	 * @param name2
	 */
	public MapPixel(int key, SpriteName spriteName) {
		this.name = spriteName;
		id = key;
		atlas = "Unknown";
		offset = PointsManager.getPoint(0,0);
		offset2 = PointsManager.getPoint(0,0);
		taille = PointsManager.getPoint(1,1);
		modulo = PointsManager.getPoint(1,1);
		indexation = -1;
		numDDA = -1;
		pal = null;
		ombre = -1;
		type = -1;
		transColor = -1;
		inconnu9 = -1;
		taille_unzip = -1;
		taille_zip = -1;
		bufPosition = -1;
		paletteName = "init";
	}

	public String getTex() {
		if (atlas == null){
			logger.warn("Attention, on tente de récupérer une texture null.");
			logger.warn("ID : "+this.id);
			logger.warn("nom : "+this.name.getName());
		}
		return name.getName();
	}

	public void setTex(String tex) {
		if(tex == null) {
			logger.warn("On essaye de mettre un tex null dans un MapPixel!");
		}
		else {
			this.name.setName(tex);
		}
	}

	public String getAtlas() {
		if (atlas == null){
			logger.warn("Attention, on tente de récupérer un atlas null.");
			logger.warn("ID : "+this.id);
			logger.warn("nom : "+this.name);
			logger.warn("texture : "+this.getTex());
		}
		return atlas;
	}

	//TODO rajouter une methode pour retirer les comparaisons a "foo" et "bar"
	public void setAtlas(String atlas) {
		this.atlas = atlas;
	}

	public Point getOffset() {
		return offset;
	}

	public void setOffset(Point offset) {
		this.offset = offset;
	}

	public int getOffsetX() {
		return offset.x;
	}
	
	public int getOffsetY() {
		return offset.y;
	}
	
	/**
	 * @return
	 */
	public int getLargeur() {
		return getTaille().x;
	}

	/**
	 * @return
	 */
	public int getHauteur() {
		return getTaille().y;
	}

	/**
	 * @param unsignedShort
	 */
	public void setHauteur(UnsignedShort hauteur) {
		this.setTaille(PointsManager.getPoint(this.getTaille().x, hauteur.getValue()));
	}

	/**
	 * @param unsignedShort
	 */
	public void setLargeur(UnsignedShort largeur) {
		this.setTaille(PointsManager.getPoint(largeur.getValue(), this.getTaille().y));
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPaletteName() {
		return paletteName;
	}

	/**
	 * @param sn
	 */
	public void setName(SpriteName sn) {
		name = sn;
	}

	/**
	 * @param indexation
	 */
	public void setIndexation(UnsignedInt indexation) {
		this.indexation = (int) indexation.getValue();
	}

	/**
	 * @param numDDA
	 */
	public void setNumDDA(long numDDA) {
		this.numDDA = numDDA;		
	}

	public int getNumDDA() {
		return (int)numDDA;
	}

	public int getIndexation() {
		return indexation;
	}

	/**
	 * @param p
	 */
	public void setPalette(Palette p) {
		pal = p;
		paletteName = pal.getNom();
	}

	/**
	 * @param unsignedShort
	 */
	public void setOmbre(UnsignedShort ombre) {
		this.ombre = ombre.getValue();		
	}

	/**
	 * @param unsignedShort
	 */
	public void setType(UnsignedShort type) {
		this.type = type.getValue();
	}



	/**
	 * @param bytesToShort
	 */
	public void setOffsetY(short offsetY) {
		this.offset = PointsManager.getPoint(this.offset.x, offsetY);

	}

	/**
	 * @param bytesToShort
	 */
	public void setOffsetX(short offsetX) {
		this.offset = PointsManager.getPoint(offsetX, this.offset.y);
	}

	/**
	 * @param bytesToShort
	 */
	public void setOffsetY2(short offsetY2) {
		this.offset2 = PointsManager.getPoint(this.offset2.x, offsetY2);
	}

	/**
	 * @param bytesToShort
	 */
	public void setOffsetX2(short offsetX2) {
		this.offset2 = PointsManager.getPoint(offsetX2, this.offset2.y);
	}

	/**
	 * @param unsignedShort
	 */
	public void setCouleurTrans(UnsignedShort transColor) {
		this.transColor = transColor.getValue();
	}

	/**
	 * @param unsignedShort
	 */
	public void setInconnu9(UnsignedShort inconnu9) {
		this.inconnu9 = inconnu9.getValue();
	}

	/**
	 * @param unsignedInt
	 */
	public void setTaille_unzip(UnsignedInt taille_unzip) {
		this.taille_unzip = (int) taille_unzip.getValue();
	}

	/**
	 * @param unsignedInt
	 */
	public void setTaille_zip(UnsignedInt taille_zip) {
		this.taille_zip = (int) taille_zip.getValue();
	}

	/**
	 * @param position
	 */
	public void setBufPos(int position) {
		this.bufPosition = position;
	}



	public int getType() {
		return type;
	}

	/**
	 * @param type
	 */
	protected void setType(int type) {
		this.type = type;		
	}

	/**
	 * @param type
	 */
	protected void setOmbre(int ombre) {
		this.ombre = ombre;		
	}
	
	/**
	 * @return
	 */
	public int getBufPos() {
		return bufPosition;
	}

	/**
	 * @return
	 */
	public int getOmbre() {
		return ombre;
	}

	/**
	 * @return
	 */
	public Point getOffset2() {
		return offset2;
	}

	/**
	 * @return
	 */
	public int getInconnu9() {
		return inconnu9;
	}

	/**
	 * @return
	 */
	public int getCouleurTrans() {
		return transColor;
	}

	/**
	 * @return
	 */
	public int getTaille_zip() {
		return taille_zip;
	}

	/**
	 * @return
	 */
	public int getTaille_unzip() {
		return taille_unzip;
	}

	/**
	 * @return
	 */
	public Palette getPal() {
		return pal;
	}

	public Point getTaille() {
		return taille;
	}

	public void setTaille(Point taille) {
		this.taille = taille;
	}
	
	/**
	 * @param taille_unzip
	 */
	public void setTaille_unzip(int taille_unzip) {
		this.taille_unzip = taille_unzip;		
	}
	
	/**
	 * @param taille_zip
	 */
	public void setTaille_zip(int taille_zip) {
		this.taille_zip = taille_zip;		
	}
	
	/**
	 * 
	 * @param pal
	 */
	public void setPal(Palette pal){
		this.pal = pal;
	}

	/**
	 * @param b
	 */
	public void setTuile(boolean b) {
		tuile = b;
	}

	public Point getModulo() {
		return modulo;
	}

	public void setModulo(Point modulo) {
		this.modulo = modulo;
	}

	public boolean isTuile() {
		return tuile;
	}

	public int getOffsetX2() {
		return offset2.x;
	}
	
	public int getOffsetY2() {
		return offset2.y;
	}

	/**
	 * @param b
	 */
	public void setValidPalette(boolean b) {
		valid_palette  = b;
	}

	/**
	 * @return
	 */
	public boolean isPaletteValid() {
		return valid_palette;
	}
}
