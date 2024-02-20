package com.bible.app.creator;

import java.io.IOException;

import com.bible.app.creator.bible.AmericanStandardVersion;
import com.bible.app.creator.bible.Elberfelder1905;
import com.bible.app.creator.bible.Luther1912;
import com.bible.app.creator.bible.Luther1912Strong;
import com.bible.app.creator.bible.Menge1939;
import com.bible.app.creator.bible.Schlachter1951;
import com.bible.app.creator.bible.WorldEnglishBible;

public class BibleCreator {
	public static Bible getBible(String name) throws IOException {
		if ("Luther 1912".equalsIgnoreCase(name))
			return new Luther1912(name);
		else if ("Luther 1912 Strong".equalsIgnoreCase(name))
			return new Luther1912Strong(name);
		else if ("Elberfelder 1905".equalsIgnoreCase(name))
			return new Elberfelder1905(name);
		else if ("Menge 1939".equalsIgnoreCase(name))
			return new Menge1939(name);
		else if ("Schlachter 1951".equalsIgnoreCase(name))
			return new Schlachter1951(name);
		else if ("World English Bible".equalsIgnoreCase(name))
			return new WorldEnglishBible("World English");
		else if ("American Standard Version".equalsIgnoreCase(name))
			return new AmericanStandardVersion("American Std");

		return null;
	}
}
