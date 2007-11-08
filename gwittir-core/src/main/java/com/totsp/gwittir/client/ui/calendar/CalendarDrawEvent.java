/*
 * CalendarDrawEvent.java
 *
 * Created on October 31, 2007, 8:51 PM
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

package com.totsp.gwittir.client.ui.calendar;

import java.util.Date;

/**
 *
 * @author cooper
 */
public class CalendarDrawEvent {
    
    private Calendar source;
    private Date date;
    
    
    /** Creates a new instance of CalendarDrawEvent */
    public CalendarDrawEvent(Calendar source, Date date) {
        this.source = source;
        this.date = date;
    }

    public Calendar getSource() {
        return source;
    }

    public Date getDate() {
        return date;
    }
    
}
