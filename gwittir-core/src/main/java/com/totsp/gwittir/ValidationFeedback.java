/*
 * ValidationFeedback.java
 *
 * Created on July 16, 2007, 12:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.totsp.gwittir;

/**
 *
 * @author cooper
 */
public interface ValidationFeedback {
    
    public void handleException( Object source, ValidationException exception );
    
}