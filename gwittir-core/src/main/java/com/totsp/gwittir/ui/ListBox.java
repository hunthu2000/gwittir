/*
 * ListBox.java
 *
 * Created on July 5, 2007, 6:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.totsp.gwittir.ui;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author cooper
 */
public class ListBox extends AbstractBoundWidget {
    
    private Object value;
    private com.google.gwt.user.client.ui.ListBox base = new com.google.gwt.user.client.ui.ListBox();
    private Collection options = new ArrayList();
    private ArrayList selected = new ArrayList();
    
    /** Creates a new instance of ListBox */
    public ListBox() {
        super.initWidget( base );
        this.setRenderer( new ToStringRenderer() );
        this.setComparator( new SimpleComparator() );
    }
    
    public Object getValue() {
        return selected;
    }
    
    public void setValue(Object value) {
        this.selected = (ArrayList) value;
    }
    
    public Collection getOptions() {
        return options;
    }
    
    public void setOptions(Collection options) {
        this.options = new ArrayList();
        base.clear();
        ArrayList newSelected = new ArrayList();
        for( Iterator it = options.iterator(); it.hasNext(); ){
            Object item = it.next();
            this.base.addItem( this.getRenderer().render( item ) );
            if( contains(  this.selected, item ) ){
                this.base.setItemSelected( this.base.getItemCount() -1, true );
                newSelected.add( item );
            }
            this.options.add( item );
        }
        this.selected = newSelected;
    }
    
    protected boolean contains( Collection c, Object o ){
        for( Iterator it = c.iterator(); it.hasNext(); ){
            if( this.getComparator().compare( o, it.next() ) == 0 ){
                return true;
            }
        }
        return false;
    }

    public void setModel(Object model) {
        int i=0;
        this.selected = new ArrayList();
        if( model instanceof Collection ){
            Collection c = (Collection) model;
            for(Iterator it = this.options.iterator(); it.hasNext(); i++ ){
                Object item = it.next();
                if( contains( c, item ) ){
                    base.setItemSelected( i, true );
                } else {
                    base.setItemSelected( i, false ); 
                }
                this.selected.add( item );
            }
        } else {
            for(Iterator it = this.options.iterator(); it.hasNext(); i++ ){
                Object item = it.next();
                if( this.getComparator().compare( model, item) == 0 ){
                    base.setItemSelected( i, true );
                } else {
                    base.setItemSelected( i, false );
                }
            }
            this.selected.add( model );
        }
    }

    public Object getModel() {
        if( this.base.isMultipleSelect() ){
            return this.selected; 
        } else if( this.selected.size() == 0 ){
            return null;
        } else {
            return this.selected.get(0);
        }
    }
    
    
    public void addItem( Object o ){
        options.add( o );
        this.base.addItem( this.getRenderer().render( o ) );
    }
    
    public void removeItem( Object o ){
        int i = 0;
        for( Iterator it = this.options.iterator(); it.hasNext(); i++ ){
            Object option = it.next();
            if( this.getComparator().compare( option, o ) == 0 ){
                this.options.remove( option );
                this.base.removeItem(i);
            }
        }
    }

    public void removeKeyboardListener(KeyboardListener listener) {
        this.base.removeKeyboardListener(listener);
    }

    public void addKeyboardListener(KeyboardListener listener) {
        this.base.addKeyboardListener(listener);
    }

    public boolean equals(Object object) {
        boolean retValue;
        
        retValue = this.base.equals(object);
        return retValue;
    }

    public void setEnabled(boolean enabled) {
        this.base.setEnabled(enabled);
    }

    public void setFocus(boolean focused) {
        this.base.setFocus(focused);
    }

    public void setMultipleSelect(boolean multiple) {
        this.base.setMultipleSelect(multiple);
        if( this.selected.size() > 1 ){
            Object o = this.selected.get(0);
            this.selected = new ArrayList();
            this.selected.add(o);
        }
    }

    public void setVisible(boolean visible) {
        this.base.setVisible(visible);
    }

    public void setAccessKey(char key) {
        this.base.setAccessKey(key);
    }

    public void addChangeListener(ChangeListener listener) {
        this.base.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        this.base.removeChangeListener(listener);
    }

    public void removeClickListener(ClickListener listener) {
        this.base.removeClickListener(listener);
    }

    public void addClickListener(ClickListener listener) {
        this.base.addClickListener(listener);
    }

    public void onBrowserEvent(Event event) {
        this.base.onBrowserEvent(event);
    }

    public void addFocusListener(FocusListener listener) {
        this.base.addFocusListener(listener);
    }

    public void removeFocusListener(FocusListener listener) {
        this.base.removeFocusListener(listener);
    }

    public void setWidth(String width) {
        this.base.setWidth(width);
    }

    public void removeStyleName(String style) {
        this.base.removeStyleName(style);
    }

    public void addStyleName(String style) {
        this.base.addStyleName(style);
    }

    public void setHeight(String height) {
        this.base.setHeight(height);
    }

    public void setName(String name) {
        this.base.setName(name);
    }

    public void setStyleName(String style) {
        this.base.setStyleName(style);
    }

    public void setTitle(String title) {
        this.base.setTitle(title);
    }

    public void unsinkEvents(int eventBitsToRemove) {
        this.base.unsinkEvents(eventBitsToRemove);
    }

    public void sinkEvents(int eventBitsToAdd) {
        this.base.sinkEvents(eventBitsToAdd);
    }

    public void setVisibleItemCount(int visibleItems) {
        this.base.setVisibleItemCount(visibleItems);
    }

    public boolean isItemSelected(int index) {
        boolean retValue;
        
        retValue = this.base.isItemSelected(index);
        return retValue;
    }

    public String getValue(int index) {
        String retValue;
        
        retValue = this.base.getValue(index);
        return retValue;
    }

    public void removeItem(int index) {
        this.base.removeItem(index);
    }

    public String getItemText(int index) {
        String retValue;
        
        retValue = this.base.getItemText(index);
        return retValue;
    }

    public void setTabIndex(int index) {
        this.base.setTabIndex(index);
    }

    public void setItemText(int index, String text) {
        this.base.setItemText(index, text);
    }

    public void setValue(int index, String value) {
        this.base.setValue(index, value);
    }

    public String toString() {
        String retValue;
        
        retValue = this.base.toString();
        return retValue;
    }

    public int getVisibleItemCount() {
        int retValue;
        
        retValue = this.base.getVisibleItemCount();
        return retValue;
    }

    public int getItemCount() {
        int retValue;
        
        retValue = this.base.getItemCount();
        return retValue;
    }

    public boolean isVisible() {
        boolean retValue;
        
        retValue = this.base.isVisible();
        return retValue;
    }

    public boolean isAttached() {
        boolean retValue;
        
        retValue = this.base.isAttached();
        return retValue;
    }

    public String getStyleName() {
        String retValue;
        
        retValue = this.base.getStyleName();
        return retValue;
    }

    public int getAbsoluteLeft() {
        int retValue;
        
        retValue = this.base.getAbsoluteLeft();
        return retValue;
    }

    public int getOffsetHeight() {
        int retValue;
        
        retValue = this.base.getOffsetHeight();
        return retValue;
    }

    public String getName() {
        String retValue;
        
        retValue = this.base.getName();
        return retValue;
    }

    public boolean isMultipleSelect() {
        boolean retValue;
        
        retValue = this.base.isMultipleSelect();
        return retValue;
    }

    public Element getElement() {
        Element retValue;
        
        retValue = this.base.getElement();
        return retValue;
    }

    public int getTabIndex() {
        int retValue;
        
        retValue = this.base.getTabIndex();
        return retValue;
    }

    public int getSelectedIndex() {
        int retValue;
        
        retValue = this.base.getSelectedIndex();
        return retValue;
    }

    public void removeFromParent() {
        this.base.removeFromParent();
    }

    public int getOffsetWidth() {
        int retValue;
        
        retValue = this.base.getOffsetWidth();
        return retValue;
    }

    public String getTitle() {
        String retValue;
        
        retValue = this.base.getTitle();
        return retValue;
    }

    public int getAbsoluteTop() {
        int retValue;
        
        retValue = this.base.getAbsoluteTop();
        return retValue;
    }

    public int hashCode() {
        int retValue;
        
        retValue = this.base.hashCode();
        return retValue;
    }

    public boolean isEnabled() {
        boolean retValue;
        
        retValue = this.base.isEnabled();
        return retValue;
    }

    public Widget getParent() {
        Widget retValue;
        
        retValue = this.base.getParent();
        return retValue;
    }

    public void setPixelSize(int width, int height) {
        this.base.setPixelSize(width, height);
    }

    public void setSize(String width, String height) {
        this.base.setSize(width, height);
    }
    
    
    
    
    
    
}
