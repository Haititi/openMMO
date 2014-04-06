package t4cPlugin;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;


/**
 *   PStructureNomsSprites = ^StructureNomsSprites;
  StructureNomsSprites = Record
    Nom : Array [0..63] Of Char;
    Chemin : Array [0..255] Of Char;
    Indexation : LongInt;
    NumDda : Int64;
  End;
 * @author synoga
 *
 */

public class Sprite {
	static int correspondances = 0;
	String nom = "";
	String chemin = "";
	boolean tuile = false;
	ArrayList<Integer> id = new ArrayList<Integer>();
	ArrayList<Integer> pos = new ArrayList<Integer>();
	int indexation;
	int index_next;
	int zoneX = 1;
	int zoneY = 1;
	int type;//short
	int ombre;//short
	int largeur;//short
	int hauteur;//short
	int inconnu9;//short
	byte couleurTrans;//short
	int offsetX;//short
	int offsetY;//short
	int offsetX2;//short
	int offsetY2;//short
	static int maxOffsetX = 0;
	static int maxOffsetY = 0;
	static int maxX = 0;
	static int maxY = 0;
	long numDda;
	public long taille_unzip = -1;
	public long taille_zip = -1;
	public DPDPalette palette;
	public int bufPos = -1;
	public int moduloX = 1;
	public int moduloY = 1;
	static int last = -1;
	
	public Sprite(ByteBuffer buf) {
		byte[] bytes = new byte[64];
		buf.get(bytes);
		nom = new String(bytes);
		nom = nom.substring(0, nom.indexOf(0x00));
		nom = nom.replace("_","");
		if (nom.equals("Cemetery Gates /^"))nom = "Cemetery Gates1";
		if (nom.equals("Cemetery Gates /"))nom = "Cemetery Gates2";
		if (nom.equals("Cemetery Gates \\v"))nom = "Cemetery Gates3";
		if (nom.equals("Cemetery Gates v"))nom = "Cemetery Gates4";
		if (nom.equals("Cemetery Gates -"))nom = "Cemetery Gates5";
		if (nom.equals("Cemetery Gates >"))nom = "Cemetery Gates6";
		if (nom.equals("Cemetery Gates ^"))nom = "Cemetery Gates7";
		if (nom.equals("Cemetery Gates X"))nom = "Cemetery Gates8";
		if (nom.equals("Cemetery Gates .|"))nom = "Cemetery Gates9";

		
		bytes = new byte[256];
		buf.get(bytes);
		chemin = new String(bytes);
		chemin = chemin.substring(0, chemin.indexOf(0x00));
		chemin = chemin.replace("_","");
		chemin = chemin.replace(".","");
		chemin = chemin.replace("\\", "/");
		String[] split;
		split = chemin.split("\\/");
		chemin = split[split.length-1];
		//if (chemin.startsWith("Cemetery Gates")) chemin = "Cemetery Gates";
		//System.out.println("		- Chemin : "+chemin+" Nom : "+nom);
		
		byte b1,b2,b3,b4;
		b1 = buf.get();
		b2 = buf.get();
		b3 = buf.get();
		b4 = buf.get();
		indexation = tools.ByteArrayToNumber.bytesToInt(new byte[]{b4,b3,b2,b1});
		//System.out.println("		- Indexation : "+indexation);
		
		byte b5,b6,b7,b8;
		b1 = buf.get();
		b2 = buf.get();
		b3 = buf.get();
		b4 = buf.get();
		b5 = buf.get();
		b6 = buf.get();
		b7 = buf.get();
		b8 = buf.get();
		numDda = tools.ByteArrayToNumber.bytesToLong(new byte[]{b8,b7,b6,b5,b4,b3,b2,b1});
		//System.out.println("		- N° DDA : "+numDda);
		
		Iterator <Integer> iter = DID.ids.keySet().iterator();
		while (iter.hasNext()){
			int val = iter.next();
			if (nom.contains(DID.ids.get(val))){
				id.add(val);
				correspondances++;
				DID.sprites_with_ids.put(val, this);
				if(DID.sprites_with_ids.size() != last) System.out.println("Nombre de Sprites avec ID : "+DID.sprites_with_ids.size()+". Ajout : "+val+" => "+nom);
			}
			/*if (nom.contains("(")&nom.contains(", ")&nom.contains(")")){
				if (nom.contains(DID.ids.get(val))){
					id.add(val);
					correspondances++;
					DID.sprites_with_ids.put(val, this);
					if(DID.sprites_with_ids.size() != last) System.out.println(DID.sprites_with_ids.size()+" ID ajoutée(s).");

				}
			}*/
			last = DID.sprites_with_ids.size();
			if (nom.equals("Black Tile")){
				DID.black = this;
			}
		}
		
		//if (nom.contains("Wooden")) System.out.println(file);
		//System.out.println("Nouveau Sprite : "+chemin+nom);
		Params.STATUS = "Nouveau Sprite : "+chemin+nom;
		DID.sprites_without_ids.put(DID.sprites_without_ids.size(), this);


	}
	
	public Sprite(boolean tuile, String atlas, String tex, int offsetX,	int offsetY, int moduloX, int moduloY, int id) {
		this.tuile = tuile;
		this.chemin = atlas;
		this.nom = tex;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.moduloX = moduloX;
		this.moduloY = moduloY;
		this.id = new ArrayList<Integer>();
		this.id.add(id);
	}

	public int compareTo(Sprite o2) {
		return indexation-o2.indexation;
	}
}