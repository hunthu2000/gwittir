/*
 * Property.java
 *
 * Created on July 21, 2007, 4:49 PM
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

package com.totsp.gwittir.rebind.beans;

import com.google.gwt.core.ext.typeinfo.JType;

/**
 *
 * @author cooper
 */
class Property {
     private MethodWrapper readMethod;
     private MethodWrapper writeMethod;
     private String name;
     private JType type;
    public Property() {
    }

    public MethodWrapper getReadMethod() {
        return readMethod;
    }

    public void setReadMethod(MethodWrapper readMethod) {
        this.readMethod = readMethod;
    }

    public MethodWrapper getWriteMethod() {
        return writeMethod;
    }

    public void setWriteMethod(MethodWrapper writeMethod) {
        this.writeMethod = writeMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JType getType() {
        return type;
    }

    public void setType(JType type) {
        this.type = type;
    }
    
}