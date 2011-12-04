package com.viamep.richard.jgopherd;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class JMole {
	public abstract ArrayList<GopherEntry> run(HashMap<String,String> params, OutputStream outputstream);
}