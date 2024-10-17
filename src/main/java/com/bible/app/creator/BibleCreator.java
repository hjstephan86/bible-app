package com.bible.app.creator;

import java.io.IOException;

import com.bible.app.Constants;
import com.bible.app.creator.bible.AmericanStandardVersion;
import com.bible.app.creator.bible.Elberfelder1905;
import com.bible.app.creator.bible.Luther1912;
import com.bible.app.creator.bible.Luther1912Strong;
import com.bible.app.creator.bible.Menge1939;
import com.bible.app.creator.bible.Schlachter1951;
import com.bible.app.creator.bible.WorldEnglishBible;

public class BibleCreator {
	public static Bible getBible(String name) throws IOException {
		if (Constants.BIBLE_LUTHER_1912.equalsIgnoreCase(name))
			return new Luther1912(name);
		else if (Constants.BIBLE_LUTHER_1912_STRONG.equalsIgnoreCase(name))
			return new Luther1912Strong(name);
		else if (Constants.BIBLE_ELBERFELDER.equalsIgnoreCase(name))
			return new Elberfelder1905(name);
		else if (Constants.BIBLE_MENGE.equalsIgnoreCase(name))
			return new Menge1939(name);
		else if (Constants.BIBLE_SCHLACHTER.equalsIgnoreCase(name))
			return new Schlachter1951(name);
		else if (Constants.BIBLE_WORLD_ENG.equalsIgnoreCase(name))
			return new WorldEnglishBible("World English");
		else if (Constants.BIBLE_AMERICAN_STD.equalsIgnoreCase(name))
			return new AmericanStandardVersion("American Std");

		return null;
	}
}
