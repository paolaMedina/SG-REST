/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.ArrayList;
import java.util.Observer;

/**
 *
 * @author Daniel
 */
public interface Observable {
    
    ArrayList<Observer> observadores = new ArrayList();
    
    public void addObserver(Observer o);
    
    public void notifyAllObservers();
}
