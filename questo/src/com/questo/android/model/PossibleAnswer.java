package com.questo.android.model;

/**
 * Interface for all answer types.
 * Implementing objects are stored as JSON, ONLY PRIMITIVE ATTRIBUTES OR ARRAYS PLZ!!
 *
 */
public interface PossibleAnswer extends Comparable<PossibleAnswer>{
    
    public String getAnswer();
}
