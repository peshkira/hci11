package com.questo.android.common;

public enum Level {

    PEASANT, SERF, SQUIRE, SPEARMAN, PIKEMAN, ARCHER, CROSSBOWMAN, SWORDSMAN, CHAMPION, KNIGHT, CAVALIER, PALADIN, DUKE, BARON, KING, IMPERATOR;
    
    public static String getHumanReadableCode(int points) {
        if (points == 0) {
            return "Peasant";
        } 
        
        if (points < 30) {
            return "Serf";
        }
        
        if (points < 60) {
            return "Squire";
        }
        
        if (points < 100) {
            return "Spearman";
        }
        
        if (points < 150) {
            return "Pikeman";
        }
        
        if (points < 250) {
            return "Archer";
        }
        
        if (points < 350) {
            return "Crossbowman";
        }
        
        if (points < 500) {
            return "Swordsman";
        }
        
        if (points < 750) {
            return "Champion";
        }
        
        if (points < 1000) {
            return "Knight";
        }
        
        if (points < 1300) {
            return "Cavalier";
        }
        
        if (points < 1500) {
            return "Paladin";
        }
        
        if (points < 1750) {
            return "Duke";
        }
        
        if (points < 2000) {
            return "Baron";
        }
        
        if (points < 3000) {
            return "King";
        }

        return "Imperator";
    }
    
}
