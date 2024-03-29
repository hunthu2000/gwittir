/*
 * ReflectedImageGroup.java
 *
 * Created on November 7, 2007, 8:43 PM
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

package com.totsp.gwittir.client.fx.ui;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;
import com.totsp.gwittir.client.log.Level;
import com.totsp.gwittir.client.ui.AbstractBoundCollectionWidget;
import com.totsp.gwittir.client.ui.Renderer;
import com.totsp.gwittir.client.ui.ToStringRenderer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author cooper
 */
public class ReflectedImageGroup<T> extends AbstractBoundCollectionWidget<T,String> {
    
    private Collection value;
    final private double maxScalar = 1.25D;
    private transient ReflectedImage[] images;
    final private int baseWidth;
    final private int baseHeight;
    final private double reflectHeight;
    final private double opacity;
    final private Grid grid = new Grid(1,3);
    final private FlexTable imagesPanel = new FlexTable();
    private Object selected;
    private int lastSelectedIndex = -1;
    
    /** Creates a new instance of ReflectedImageGroup */
    public ReflectedImageGroup(final int baseWidth,
            final int baseHeight, final double reflectHeight, final double opacity) {
        super();
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
        this.reflectHeight = reflectHeight;
        this.opacity = opacity;
        this.imagesPanel.setHeight("100%");
        final int  padding = ((int)(baseHeight * maxScalar) - baseHeight )/4;
        this.grid.setCellSpacing( padding );
        this.grid.setHeight("100%");
        this.grid.setStyleName("gwittir-ReflectedFisheyeImageGroup");
        this.grid.getRowFormatter().setVerticalAlign(0, HasVerticalAlignment.ALIGN_MIDDLE);
        this.imagesPanel.getRowFormatter().setVerticalAlign(0, HasVerticalAlignment.ALIGN_MIDDLE);
        this.grid.setWidget(0,1, imagesPanel );
        this.imagesPanel.setCellPadding(0);
        this.imagesPanel.setCellSpacing(2);
        Renderer<T, String> renderer = (Renderer<T, String>) ToStringRenderer.INSTANCE;
        this.setRenderer( renderer );
        super.initWidget( grid );
    }
    
    
    public Collection<T> getValue() {
        return this.value;
    }
    
    public void setValue(Collection<T> value) {
        this.value = value;
        this.changes.firePropertyChange( "value", null, value );
        this.render( this.value == null ? new ArrayList() : this.value );
    }
    
    protected void render(Collection value){
        this.images = new ReflectedImage[ value.size() ];
        this.lastSelectedIndex = -1;
        this.imagesPanel.clear();
        if( this.imagesPanel.getRowCount() > 0 ){
            this.imagesPanel.removeRow(0);
        }
        if( value == null ){
            return;
        }
        Iterator<T> it = value.iterator();
        final int maxSize = (int)(baseWidth*maxScalar) +4;
        this.imagesPanel.setWidth( maxSize * value.size() +"px");
        for( int i=0; it.hasNext(); i++ ){
            final int index = i;
            final T selectObject = it.next();
            this.images[i] = new ReflectedImage(
                    this.getRenderer().render( selectObject ).toString(),
                    this.baseWidth, this.baseHeight,
                    this.reflectHeight, this.opacity );
            
            this.imagesPanel.getCellFormatter().setWidth(0, index, maxSize+"px");
            this.imagesPanel.getCellFormatter().setHorizontalAlignment(0, index, HasHorizontalAlignment.ALIGN_CENTER);
            this.imagesPanel.setWidget( 0, index, this.images[i] );
            
            
            this.images[i].addMouseListener( new MouseListener(){
                final Timer t = new Timer(){
                    public void run() {
                        if( selectObject != selected ){
                            images[index].setHeight( (int) (baseHeight * maxScalar) );
                            images[index].setWidth( (int) (baseWidth * maxScalar) );
                        }
                    }
                    
                };
                public void onMouseUp(Widget sender, int x, int y) {
                }
                
                public void onMouseMove(Widget sender, int x, int y) {
                }
                
                public void onMouseDown(Widget sender, int x, int y) {
                    setSelected( selectObject );
                }
                
                public void onMouseLeave(Widget sender) {
                    t.cancel();
                    if( selectObject != selected ){
                        images[index].setHeight( baseHeight );
                        images[index].setWidth( baseWidth );
                    }
                }
                
                public void onMouseEnter(Widget sender) {
                    t.schedule(50);
                }
                
            });
        }
    }
    
    public Object getSelected() {
        return selected;
    }
    
    public void setSelected(Object selected) {
        LOG.log( Level.SPAM, selected + " :: " + selected.getClass(), null);
        if( selected == this.selected ){
            return;
        }
        Object old = this.selected;
        
        Object[] arr = value.toArray();
        if( lastSelectedIndex != -1 ){
            this.imagesPanel.getCellFormatter()
            .removeStyleName(0, lastSelectedIndex, "selected");
            this.images[lastSelectedIndex].setHeight( baseHeight );
            this.images[lastSelectedIndex].setWidth( baseWidth );   
        }
        this.selected = selected;
        
        boolean found = false;
        int index;
        for( index = 0 ; selected != null &&  index < arr.length; index++ ){
            if( arr[index] == selected ){
                found = true;
                break;
            }
        }
        if( !found && selected !=null ){
            throw new RuntimeException("Not an option");
        }
        images[index].setHeight( (int) (baseHeight * maxScalar) );
        images[index].setWidth( (int) (baseWidth * maxScalar) );
        this.imagesPanel.getCellFormatter().setStyleName(0, index, "selected");
        this.changes.firePropertyChange( "selected", old, selected );
        this.lastSelectedIndex = index;
    }
    
    
}