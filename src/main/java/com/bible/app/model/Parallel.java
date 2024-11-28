package com.bible.app.model;

import java.util.ArrayList;

import com.bible.app.text.Verse;

public class Parallel {

    private ArrayList<Verse> verses;
    private ArrayList<String> bibleNames;

    public Parallel() {
        this.verses = new ArrayList<Verse>();
        this.bibleNames = new ArrayList<String>();
    }

    public ArrayList<Verse> getVerses() {
        return verses;
    }

    public void setVerses(ArrayList<Verse> verses) {
        this.verses = verses;
    }

    public ArrayList<String> getBibleNames() {
        return bibleNames;
    }

    public void setBibleNames(ArrayList<String> bibleNames) {
        this.bibleNames = bibleNames;
    }

    public void addVerse(Verse verse) {
        this.verses.add(verse);

    }

    public void addBibleName(String name) {
        this.bibleNames.add(name);
    }
}
