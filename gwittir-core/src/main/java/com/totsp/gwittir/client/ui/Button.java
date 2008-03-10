/*
 * Button.java
 *
 * Created on July 11, 2007, 7:14 PM
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

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author <a href="mailto:cooper@screaming-penguin.com">Robert "kebernet" Cooper</a>
 */
public class Button<B> extends AbstractBoundWidget<B, String> implements SourcesClickEvents, HasFocus {
    private final com.google.gwt.user.client.ui.Button base = new com.google.gwt.user.client.ui.Button();
    private String value;
    
    /** Creates a new instance of Button */
    public Button() {
        this.init();
    }

    public Button(B label) {
        this.init();
        this.setValue(label);
    }
    public Button(B label, ClickListener listener) {
        this.init();
        this.setValue(label);
        this.addClickListener( listener );
    }

    public void addClickListener(ClickListener listener) {
        this.base.addClickListener(listener);
    }

    public void addFocusListener(FocusListener listener) {
        this.base.addFocusListener(listener);
    }

    public void addKeyboardListener(KeyboardListener listener) {
        this.base.addKeyboardListener(listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    public void addPropertyChangeListener(String propertyName,
        PropertyChangeListener l) {
        changes.addPropertyChangeListener(propertyName, l);
    }

    public String getHTML() {
        String retValue;

        retValue = this.base.getHTML();

        return retValue;
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return changes.getPropertyChangeListeners();
    }

    public String getStyleName() {
        String retValue;

        retValue = this.base.getStyleName();

        return retValue;
    }

    public int getTabIndex() {
        int retValue;

        retValue = this.base.getTabIndex();

        return retValue;
    }

    public String getText() {
        String retValue;

        retValue = this.base.getText();

        return retValue;
    }

    public String getTitle() {
        String retValue;

        retValue = this.base.getTitle();

        return retValue;
    }

    public String getValue() {
        return this.value;
    }

    protected void init() {
        final Button instance = this;
        ClickListener listener = new ClickListener() {
                public void onClick(Widget sender) {
                    if(getAction() != null) {
                        getAction().execute(instance);
                    }
                }
            };

        this.base.addClickListener(listener);
        this.setRenderer(new ToStringRenderer());
        this.initWidget(this.base);
    }

    public void removeClickListener(ClickListener listener) {
        this.base.removeClickListener(listener);
    }

    public void removeFocusListener(FocusListener listener) {
        this.base.removeFocusListener(listener);
    }

    public void removeKeyboardListener(KeyboardListener listener) {
        this.base.removeKeyboardListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }

    public void removePropertyChangeListener(String propertyName,
        PropertyChangeListener l) {
        changes.removePropertyChangeListener(propertyName, l);
    }

    public void setEnabled(boolean enabled) {
        this.base.setEnabled(enabled);
    }

    public void setFocus(boolean focused) {
        this.base.setFocus(focused);
    }

    public void setHTML(String html) {
        this.base.setHTML(html);
    }

    public void setRenderer(Renderer<String, B> renderer) {
        super.setRenderer(renderer);
    }

    public void setTabIndex(int index) {
        this.base.setTabIndex(index);
    }

    public void setText(String text) {
        this.base.setText(text);
    }

    public void setTitle(String title) {
        this.base.setTitle(title);
    }

    public void setValue(B value) {
        //GWT.log("Setting value "+ value, null );
        
        String old = this.value;
         
        this.setText( this.getRenderer() != null ? (String) this.getRenderer().render(value) : ""+value);
        this.changes.firePropertyChange("value", old, value);
    }

    public boolean isEnabled() {
        return this.base.isEnabled();
    }

    public void setAccessKey(char key) {
        this.base.setAccessKey( key );
    }
    
}
