package opent4c;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import opent4c.utils.ID;
import opent4c.utils.SpriteName;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;

import tools.ByteArrayToNumber;
import tools.DataInputManager;
import tools.UnsignedInt;

public class SpriteManager {
	
	static Logger logger = LogManager.getLogger(SpriteManager.class.getSimpleName());
	static Map<String,Palette> palettes = null;
	static int nb_palettes_from_dpd = -1;
	static int nb_sprites_from_did = -1;
	private static boolean dpd_done = false;
	private static boolean did_done = false;
	private static boolean dda_done = false;
	private static int nb_writen;
	private static int nb_extracted_from_dda;


	///////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////     DPD        /////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Decrypts dpd file
	 */
	public static void decryptDPD(){
		ByteBuffer buf = null;
		ByteBuffer header;
		ByteBuffer bufUnZip;
		
		byte[] header_hashMd5 = new byte[16];
		byte[] header_hashMd52 = new byte[17];
		byte[] header_hash = new byte[32];
		
		final byte clef = (byte) 0x66;
		
		int header_taille_unZip = 0;
		int header_taille_zip;
		
		/*byte checksum;
		byte azt;*/
		
		File f = SourceDataManager.getDPD();
		logger.info("Décryptage du fichier DPD.");
		UpdateDataCheckStatus.setStatus("Décryptage du fichier DPD.");
		header = ByteBuffer.allocate(41);
		
		try {
			DataInputManager in = new DataInputManager (f);
			while (header.position()<header.capacity()){
				header.put(in.readByte());
			}
			header.rewind();
			
			header_hashMd5 = SpriteUtils.extractBytes(header, header_hashMd5.length);
			header_taille_unZip = ByteArrayToNumber.bytesToInt(SpriteUtils.extractInt(header,false));
			header_taille_zip = ByteArrayToNumber.bytesToInt(SpriteUtils.extractInt(header,false));
			header_hashMd52 = SpriteUtils.extractBytes(header, header_hashMd52.length);

			ByteBuffer buf_hash = ByteBuffer.allocate(33);
			buf_hash.put(header_hashMd5);
			buf_hash.put(header_hashMd52);
			buf_hash.rewind();
			buf_hash.get(header_hash);
			//azt = header_hashMd52[16];			
			
			buf = ByteBuffer.allocate(header_taille_zip);
			while (buf.position() < buf.capacity()){
				buf.put(in.readByte());
			}
			in.close();
		}catch(IOException exc){
			logger.fatal("Erreur d'ouverture");
			exc.printStackTrace();
			Gdx.app.exit();
		}
		buf.rewind();
		
	    bufUnZip = ByteBuffer.allocate(header_taille_unZip);
		bufUnZip = SpriteUtils.unzip(buf,header_taille_unZip);
		
		for (int i=0; i<bufUnZip.capacity(); i++){
			bufUnZip.array()[i] ^= clef;
		}
		
		nb_palettes_from_dpd = header_taille_unZip/(64 + 768);
		palettes = SpriteUtils.extractPalettes(bufUnZip, nb_palettes_from_dpd);
		//TODO moyen de contrôle
		//logger.info("Fichier "+f.getName()+" lu : "+palettes.size()+" palettes.");
		setDpd_done(true);
	}

	public static boolean isDpd_done() {
		return dpd_done;
	}

	public static void setDpd_done(boolean done) {
		dpd_done = done;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////     DID        /////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Extracts sprite infos from .did file
	 */
	public static void decryptDID(){
		File f = SourceDataManager.getDID();
		logger.info("Décryptage du fichier DID.");
		UpdateDataCheckStatus.setStatus("Décryptage du fichier DID.");
		ByteBuffer buf = null;
		ByteBuffer header;
		ByteBuffer bufUnZip;
		
		byte clef = (byte) 0x99;
		
		byte[] header_hashMd5 = new byte[16];
		byte[] header_hashMd52 = new byte[17];
		
		int header_taille_unZip = 0;
		int header_taille_zip;
				
		header = ByteBuffer.allocate(41);
		try {
			DataInputManager in = new DataInputManager(f);
			while (header.position()<header.capacity()){
				header.put(in.readByte());
			}
			header.rewind();
			
			header_hashMd5 = SpriteUtils.extractBytes(header, header_hashMd5.length);
			header_taille_unZip = ByteArrayToNumber.bytesToInt(SpriteUtils.extractInt(header,false));
			header_taille_zip = ByteArrayToNumber.bytesToInt(SpriteUtils.extractInt(header,false));
			header_hashMd52 = SpriteUtils.extractBytes(header, header_hashMd52.length);

	
			buf = ByteBuffer.allocate(header_taille_zip);
			while(buf.position() < buf.capacity()){
				buf.put(in.readByte());
			}
			in.close();
		}catch(IOException exc){
			logger.fatal("Erreur d'ouverture");
			exc.printStackTrace();
			Gdx.app.exit();
		}
		
		buf.rewind();
		bufUnZip = SpriteUtils.unzip(buf, header_taille_unZip);
		for (int i=0; i<bufUnZip.capacity(); i++){
			bufUnZip.array()[i] ^= clef;
		}
		nb_sprites_from_did = (header_taille_unZip/(64 + 256 + 4 + 8));
		DataChecker.setNbSprites(nb_sprites_from_did);
		for(int i=1 ; i<=nb_sprites_from_did ; i++){
			addSprite(bufUnZip);
			UpdateDataCheckStatus.setStatus("Sprites lus depuis le fichier DID : "+i+"/"+nb_sprites_from_did);
		}
		setDid_done(true);
	}
	
	/**
	 * Adds a new sprite
	 * @param buf
	 */
	public static void addSprite(ByteBuffer buf) {
		MapPixel pixel = new MapPixel();
		SpriteName sn = SpriteUtils.extractName(buf);
		String atlas = SpriteUtils.extractChemin(buf, sn);
		UnsignedInt indexation = new UnsignedInt(SpriteUtils.extractInt(buf,false));
		long numDDA = SpriteUtils.extractLong(buf,false);
		pixel.setName(sn);
		pixel.setAtlas(atlas);
		pixel.setIndexation(indexation);
		pixel.setNumDDA(numDDA);
		ID.matchIdWithPixel(pixel);
		PixelIndex.putOriginPixel(pixel);
	}
	
	public static boolean isDid_done() {
		return did_done;
	}

	public static void setDid_done(boolean done) {
		did_done = done;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////     DDA        /////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Extracts sprites from .dda files
	 */
	public static void decryptDDA(boolean doWrite){
		logger.info("Décryptage des fichiers DDA : ecriture = "+doWrite);
		UpdateDataCheckStatus.setStatus("Décryptage des fichiers DDA : ecriture = "+doWrite);
		File f = null;
		List<File> ddas = SourceDataManager.getDDA();
		Iterator<File>iter_dda = ddas.iterator();
		while (iter_dda.hasNext()){
			f = iter_dda.next();
			decrypt_dda_file(f,doWrite);
		}
		setDda_done(true);
	}

	/**
	 * Decrypts a dda file
	 * @param f
	 * @param doWrite
	 */
	public static void decrypt_dda_file(File f, boolean doWrite) {
		int numDDA = Integer.parseInt(f.getName().substring(f.getName().length()-6, f.getName().length()-4),10);
		ByteBuffer buf = SpriteUtils.readDDA(f);
		byte[] signature = new byte[4];
		signature = SpriteUtils.extractBytes(buf,signature.length);
		Iterator<MapPixel> iter = PixelIndex.getOrigin().iterator();
		while(iter.hasNext()){
			MapPixel pixel = iter.next();
			int indexation = (pixel.getIndexation()+4);
			if (pixel.getNumDDA() == numDDA){
				try{
					buf.position(indexation);
				}catch(IllegalArgumentException e){
					e.printStackTrace();
					Gdx.app.exit();
				}
				SpriteUtils.extractDDASprite(buf, pixel);//lit l'entête du sprite et ajoute les infos de l'entête dans le Sprite
				if(!doWrite){
					addOneExtractedFromDDA();
				}else{
					boolean writen = false;
					writen = SpriteUtils.doTheWriting(pixel, buf);
					if (!writen){
						logger.fatal("Sprite non écrit => "+pixel.getTex());
						Gdx.app.exit();
					}
					addOneWriten();
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public static void addOneExtractedFromDDA() {
		nb_extracted_from_dda++;
		UpdateDataCheckStatus.setStatus("Sprites extraits des fichiers DDA: "+nb_extracted_from_dda+"/"+DataChecker.nb_expected_sprites);
	}
	
	/**
	 * 
	 */
	public static void addOneWriten() {
		nb_writen++;
		UpdateDataCheckStatus.setStatus("Sprites écrits: "+nb_writen+"/"+DataChecker.nb_expected_sprites);
	}
	
	public static boolean isDda_done() {
		return dda_done;
	}

	public static void setDda_done(boolean done) {
		dda_done = done;
		PixelIndex.getOrigin().clear();
	}
}

