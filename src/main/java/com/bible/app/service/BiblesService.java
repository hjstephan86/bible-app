package com.bible.app.service;

import java.util.ArrayList;
import java.util.Map;

import com.bible.app.creator.Bible;

public interface BiblesService {

    Map<String, Bible> getBibleMap();

    ArrayList<String> getBiblesAsList();

}