package com.viamep.richard.jgopherd;

import java.io.InputStream;
import java.util.ArrayList;

public abstract class Gophermap {
	public abstract ArrayList<GopherEntry> parse(String currentdir, InputStream inputstream);
}