package opent4c;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public class Palette implements Serializable {
	private static final long serialVersionUID = 1796133002829676923L;
	private String nom = "";
	ArrayList<PalettePixel> pixels = new ArrayList<PalettePixel>();
	//TODO je ne sais pas à quoi servent le B et le P dans les noms de palettes donc je les dégage du nom et je les mets là pour le moment.
	private boolean flagP = false;
	private boolean flagB = false;

	/**
	 * Creates a palette from a ByteBuffer (.dpd file)
	 * @param buf
	 */
	public Palette(ByteBuffer buf) {
		byte[] bytes = new byte[64];
		buf.get(bytes);
		String nom = new String(bytes);
		nom = nom.substring(0, nom.indexOf(0x00));
		nom = nom.replace("_","");
		if(nom.endsWith("P")){
			nom = nom.substring(0, nom.length()-1);
			setFlagP(true);
		}
		if(nom.endsWith("B")){
			nom = nom.substring(0, nom.length()-1);
			setFlagB(true);
		}
		setNom(nom);
		//logger.info("Nouvelle Palette : "+getNom());
		for (int i=0 ; i<256 ; i++){
			short tmp1,tmp2,tmp3;
			byte b;
			b = buf.get();
			tmp1 = tools.ByteArrayToNumber.bytesToShort(new byte[]{0,b});
			b = buf.get();
			tmp2 = tools.ByteArrayToNumber.bytesToShort(new byte[]{0,b});
			b = buf.get();
			tmp3 = tools.ByteArrayToNumber.bytesToShort(new byte[]{0,b});
			pixels.add(new PalettePixel (tmp1, tmp2, tmp3));
		}
	}

	/**
	 * 
	 * @return a ByteBuffer with alla the palettes pixels
	 */
	public ByteBuffer getPixels() {
		ByteBuffer palette = ByteBuffer.allocate(pixels.size()*3);
		Iterator<PalettePixel> iter = pixels.iterator();
		while (iter.hasNext()){
			PalettePixel p = iter.next();
			palette.put((byte)((p.red)&0xFF));
			palette.put((byte)((p.green)&0xFF));
			palette.put((byte)((p.blue)&0xFF));
		}
		palette.rewind();
		return palette;
	}

	/**
	 * @return
	 */

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public boolean isFlagB() {
		return flagB;
	}

	public void setFlagB(boolean flagB) {
		this.flagB = flagB;
	}

	public boolean isFlagP() {
		return flagP;
	}

	public void setFlagP(boolean flagP) {
		this.flagP = flagP;
	}
	
}
