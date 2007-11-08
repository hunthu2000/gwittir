/*
 * AbstractBoundWidget.java
 *
 * Created on June 14, 2007, 9:55 AM
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.totsp.gwittir.client.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Comparator;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.totsp.gwittir.client.action.Action;
import com.totsp.gwittir.client.action.BindingAction;
import com.totsp.gwittir.client.keyboard.KeyBinding;
import com.totsp.gwittir.client.keyboard.KeyBindingException;
import com.totsp.gwittir.client.keyboard.KeyBoundWidget;
import com.totsp.gwittir.client.keyboard.KeyboardController;
import com.totsp.gwittir.client.log.Level;
import com.totsp.gwittir.client.log.Logger;


/**
 *
 * @author <a href="mailto:cooper@screaming-penguin.com">Robert "kebernet" Cooper</a>
 */
public abstract class AbstractBoundWidget extends Composite
        implements BoundWidget, KeyBoundWidget {
    protected static final Logger LOG = Logger.getLogger( "com.totsp.gwittir.client.ui.AbstractBoundWidget");
    private Action action;
    private ChangeListenerCollection changeListeners = new ChangeListenerCollection();
    private Comparator comparator;
    private Object model;
    protected PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private Renderer renderer;
    private KeyBinding binding;
    private boolean bindingRegistered = false;
    private SimplePanel container = new SimplePanel();
    private Widget widget = null;
    private boolean isVisible = true;
    
    /** Creates a new instance of AbstractBoundWidget */
    public AbstractBoundWidget() {
        super.initWidget( container );
    }
    
    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }
    
    public void addPropertyChangeListener(String propertyName,
            PropertyChangeListener l) {
        changes.addPropertyChangeListener(propertyName, l);
    }
    
    protected void fireChange() {
        changeListeners.fireChange(this);
    }
    
    public Action getAction() {
        return action;
    }
    
    public Comparator getComparator() {
        return comparator;
    }
    
    public Object getModel() {
        return model;
    }
    
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return changes.getPropertyChangeListeners();
    }
    
    public Renderer getRenderer() {
        return renderer;
    }
    
    protected void onAttach() {
        if(this.getAction() instanceof BindingAction) {
            ((BindingAction) getAction()).set(this);
            
        }
        if( this.widget == null ){
            throw new RuntimeException("initWidget() not called");
        }
        if( this.isVisible ){
            this.container.setWidget( this.widget );
        }
        super.onAttach();
        this.changes.firePropertyChange("attached", false, true);
    }
    
    protected void onLoad(){
        super.onLoad();
        if(this.getAction() instanceof BindingAction) {
            ((BindingAction) getAction()).bind(this);
        }
        if( this.binding != null ){
            try{
                KeyboardController.INSTANCE.register( this.binding, this);
                this.bindingRegistered = true;
            } catch(KeyBindingException kbe ){
                this.bindingRegistered = false;
                AbstractBoundWidget.LOG.log(Level.SPAM, "Exception adding default binding", kbe);
            }
        }
        
       
    }
    
    protected void onDetach() {
        super.onDetach();
        
        if(this.getAction() instanceof BindingAction &&
                (this.getModel() != null)) {
            ((BindingAction) getAction()).unbind(this);
        }
        if( this.binding != null && this.bindingRegistered ){
            KeyboardController.INSTANCE.unregister(this.binding);
        }
        this.container.remove( widget );
        this.changes.firePropertyChange("attached", true, false );
    }
    
    public void removeChangeListener(ChangeListener listener) {
        changeListeners.remove(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }
    
    public void removePropertyChangeListener(String propertyName,
            PropertyChangeListener l) {
        changes.removePropertyChangeListener(propertyName, l);
    }
    
    public void setAction(Action action) {
        this.action = action;
    }
    
    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }
    
    public void setModel(Object model) {
        Object old = this.getModel();
        if(this.getAction() instanceof BindingAction &&
                (this.getModel() != null)) {
            ((BindingAction) getAction()).unbind(this);
        }
        
        this.model = model;
        
        if(this.getAction() instanceof BindingAction) {
            ((BindingAction) getAction()).set(this);
            
            if(this.isAttached() && (this.getModel() != null)) {
                ((BindingAction) getAction()).bind(this);
            }
        }
        this.changes.firePropertyChange( "model", old, model );
    }
    
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }
    
    public KeyBinding getKeyBinding() {
        return this.binding;
    }
    
    public void setKeyBinding(final KeyBinding binding) {
        this.binding = binding;
    }

   
    public boolean isVisible(){
        return this.isVisible;
        
    }

    public void setVisible(boolean visible) {
        boolean old = this.isVisible;
        if( visible ){
            this.container.setWidget( this.widget );
        } else {
            this.container.remove( this.widget );
        }
        this.isVisible = visible;
        this.changes.firePropertyChange( "visible", old, visible );
    }

    protected void initWidget(Widget widget) {
        this.widget = widget;
    }

    public void addStyleDependentName(String styleSuffix) {
        this.widget.addStyleDependentName( styleSuffix );
    }

    public void addStyleName(String style) {
        this.widget.addStyleName(style);
    }

    public void setStylePrimaryName(String style) {
        this.widget.setStylePrimaryName( style );
    }

    public void setStyleName(String style) {
        this.widget.setStyleName(style);
    }

    public String getStylePrimaryName() {
        return this.widget.getStylePrimaryName();
    }

    public String getStyleName() {
        return this.widget.getStyleName();
    }

    public void removeStyleDependentName(String styleSuffix) {
        this.widget.removeStyleDependentName(styleSuffix);
    }

    public void removeStyleName(String style) {
        this.widget.removeStyleName(style);
    }
    
    
    
    
    
   
}