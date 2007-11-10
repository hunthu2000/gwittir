/*
 * PopupDatePicker.java
 *
 * Created on November 9, 2007, 3:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.totsp.gwittir.client.ui.calendar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.totsp.gwittir.client.beans.Binding;
import com.totsp.gwittir.client.ui.AbstractBoundWidget;
import com.totsp.gwittir.client.ui.Label;
import com.totsp.gwittir.client.ui.Renderer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

/**
 * Renders a Date value to a Label with a Image src of 
 * "[module root]/calendar-icon.gif", which when clicked, will pop up a DatePicker
 * for selection.
 * @author rcooper
 */
public class PopupDatePicker extends AbstractBoundWidget
        implements SourcesCalendarDrawEvents, SourcesCalendarEvents, Renderers {
    DatePicker base = new DatePicker();
    Label label = new Label();
    Image icon = new Image(GWT.getModuleBaseURL()+"/calendar-icon.gif");
    HorizontalPanel hp = new HorizontalPanel();
    PopupPanel pp = new PopupPanel(false);
    /** Creates a new instance of PopupDatePicker */
    public PopupDatePicker() {
        this.setRenderer( PopupDatePicker.SHORT_DATE_RENDERER );
        Binding b = new Binding( label, "value", base, "value");
        b.setLeft();
        b.bind();
        pp.setWidget(base);
        this.hp.add( this.label );
        this.hp.add( this.icon );
        this.initWidget( hp );
        this.setStyleName("gwittir-PopupDatePicker");
        icon.addClickListener( new ClickListener(){
            public void onClick(Widget sender) {
                if( pp.isAttached() ){
                    pp.hide();
                } else {
                    pp.setPopupPosition( getAbsoluteLeft(),
                            getAbsoluteTop() + getOffsetHeight() );
                    base.addCalendarListener(new CalendarListener(){
                        public boolean onDateClicked(Calendar calendar, Date date) {
                            if(date.getMonth() != base.getRenderDate().getMonth() ||
                                    date.getYear() != base.getRenderDate().getYear() ){
                                return true;
                            }
                            pp.hide();
                            calendar.removeCalendarListener(this);
                            return true;
                        }
                        
                    });
                    pp.show();
                }
            }
        });
        
    }
    
    
    /**
     * Current Date value.
     * @return Current Date value.
     */
    public Object getValue() {
        return this.base.getValue();
    }
    
    /**
     * Current Date value.
     * @param value Current Date value.
     */
    public void setValue(Object value) {
        this.base.setValue(value);
    }
    
    /**
     * 
     * @param cdl 
     */
    public void addCalendarDrawListener(CalendarDrawListener cdl) {
        this.base.addCalendarDrawListener( cdl );
    }
    
    /**
     * 
     * @param cdl 
     */
    public void removeCalendarDrawListener(CalendarDrawListener cdl) {
        this.base.removeCalendarDrawListener( cdl );
    }
    
    /**
     * 
     * @return 
     */
    public CalendarDrawListener[] getCalendarDrawListeners() {
        return this.base.getCalendarDrawListeners();
    }
    
    /**
     * 
     * @param l 
     */
    public void addCalendarListener(CalendarListener l) {
        this.base.addCalendarListener( l );
    }
    
    /**
     * 
     * @param l 
     */
    public void removeCalendarListener(CalendarListener l) {
        this.base.removeCalendarListener( l );
    }
    
    /**
     * 
     * @return 
     */
    public CalendarListener[] getCalendarListeners() {
        return this.base.getCalendarListeners();
    }
    
    
    
    /**
     * Gets the current Renderer. 
     * Defaults to Renderers.SHORT_DATE_RENDERER.
     * 
     * @return Current Renderer
     */
    public Renderer getRenderer() {
        return this.label.getRenderer();
    }
    
    /**
     * Sets the current Renderer. 
     * Defaults to Renderers.SHORT_DATE_RENDERER.
     * 
     * @param renderer Renderer to use.
     */
    public void setRenderer(Renderer renderer) {
        this.label.setRenderer( renderer );
    }
    
}