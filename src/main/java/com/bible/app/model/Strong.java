package com.bible.app.model;

import java.util.ArrayList;

import com.bible.app.text.Verse;

public class Strong {
    private ArrayList<Verse> verses;
    private Object concordance;

    public ArrayList<Verse> getVerses() {
        return verses;
    }

    public void setVerses(ArrayList<Verse> verses) {
        this.verses = verses;
    }

    public Object getConcordance() {
        return concordance;
    }

    public void setConcordance(Object concordance) {
        this.concordance = concordance;
    }
}
