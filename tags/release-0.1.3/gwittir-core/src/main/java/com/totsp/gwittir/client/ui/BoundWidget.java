/*
 * GwailsWidget.java
 *
 * Created on April 12, 2007, 12:42 PM
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

import com.google.gwt.user.client.ui.SourcesChangeEvents;

import com.totsp.gwittir.client.action.Action;
import com.totsp.gwittir.client.beans.Bindable;

import java.util.Comparator;


/**
 *
 * @author cooper
 */
public interface BoundWidget extends Bindable, SourcesChangeEvents {
    public Action getAction();

    public Comparator getComparator();

    public Object getModel();

    public Renderer getRenderer();

    public Object getValue();

    public void setAction(Action action);

    public void setComparator(Comparator comparator);

    public void setModel(Object model);

    public void setRenderer(Renderer renderer);

    public void setValue(Object value);
}