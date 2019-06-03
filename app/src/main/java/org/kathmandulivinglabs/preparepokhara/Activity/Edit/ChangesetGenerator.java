package org.kathmandulivinglabs.preparepokhara.Activity.Edit;

/**
 * Created by Bhawak on 6/8/18.
 */
public class ChangesetGenerator {
    private ChangesetGenerator(){

    }
    private static String getChangeset(String comment){
        return "<osm>\n<changeset>\n"
                + "<tag k=\"created_by\" v=\"ExplorePokhara\"/>\n"
                + "<tag k=\"comment\" v=\"" + comment + "\"/>\n"
                + "</changeset>\n</osm>";
    }


}
