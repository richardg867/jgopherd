import java.util.ArrayList;
import java.util.HashMap;

import com.viamep.richard.jgopherd.GopherEntry;
import com.viamep.richard.jgopherd.JMole;


public class ExampleJMole extends JMole {

	@Override
	public ArrayList<GopherEntry> run(HashMap<String, String> params) {
		ArrayList<GopherEntry> al = new ArrayList<GopherEntry>();
		al.add(new GopherEntry('i',"Hi! J-Mole here running on Java "+System.getProperty("java.version")));
		al.add(new GopherEntry('i',"You seem to be "+params.get("REMOTE_HOST")+" or "+params.get("REMOTE_ADDR")+"!"));
		return al;
	}

}
