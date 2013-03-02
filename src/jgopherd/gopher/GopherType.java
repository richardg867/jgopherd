package jgopherd.gopher;

/**
 * Gopher item types.
 * 
 * References:
 * - RFC 1436
 * - OverbiteFF source
 * 
 * @author Richard
 */
public enum GopherType {
	Text('0', new String[] {"txt"}),
	Menu('1', null),
	CSO('2', null),
	Error('3', null),
	BinHex('4', new String[] {"hqx"}),
	BinDOS('5', new String[] {"exe"}),
	BinUUE('6', new String[] {"uue"}),
	Server('7', null),
	Telnet('8', null),
	BinGen('9', new String[] {"bin","xpi","zip","jar","gz","bz2","7z","ar","rar","part","sit"}),
	ImageGIF('g', new String[] {"gif"}),
	ImageGen('I', new String[] {"jpg"}),
	TN3270('T', null),
	HTML('h', new String[] {"htm","html"}),
	Info('i', null),
	Sound('s', new String[] {"wav","mp3","au","aiff","wma","snd"}),
	Document('d', new String[] {"pdf","doc","xps","odf","ods","xls","ppt","pps","odp","docx","docm","xlsx","xlsm","pptx","pptm"}),
	ImagePNG('p', new String[] {"png"}),
	Video(';', new String[] {"wmv","avi","mov","wm","mpg","flv","mp4"}),
	End('.', null);
	
	/**
	 * Using a cached values array is faster
	 */
	public static final GopherType[] VALUES = values();
	
	/**
	 * Type character
	 */
	public final char type;
	/**
	 * File extensions this type should represent, or null for a non-item type
	 */
	public final String[] extensions;
	
	private GopherType(char type, String[] extensions) {
		this.type = type;
		this.extensions = extensions;
	}
	
	/**
	 * Gets the GopherType for the specified type character.
	 * 
	 * @param type Type character
	 * @return Type, or null if not found
	 */
	public static GopherType fromType(char type) {
		for (GopherType gtype : VALUES) {
			if (gtype.type == type) return gtype;
		}
		
		return null;
	}
	
	/**
	 * Gets the GopherType for the specified file name through the extension.
	 * 
	 * @param filename File name
	 * @return Type, or null if none can represent the extension
	 */
	public static GopherType fromExtension(String filename) {
		String lower = filename.toLowerCase();
		for (GopherType gtype : VALUES) {
			if (gtype.extensions == null) continue;
			for (String extension : gtype.extensions) {
				if (lower.endsWith("." + extension)) return gtype;
			}
		}
		
		return null;
	}
}
