/*
 * Property.java
 *
 * Created on July 15, 2007, 1:44 PM
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
package com.totsp.gwittir.client.beans;


/**
 *
 * @author <a href="mailto:cooper@screaming-penguin.com">Robert "kebernet" Cooper</a>
 */
public class Property {
    private Class type;
    private Method accessorMethod;
    private Method mutatorMethod;
    private String name;

    /** Creates a new instance of Property */
    public Property(String name, Class type, Method accessorMethod,
        Method mutatorMethod) {
        this.name = name;
        this.accessorMethod = accessorMethod;
        this.mutatorMethod = mutatorMethod;
        this.type = type;
    }

    public Method getAccessorMethod() {
        return accessorMethod;
    }

    public Method getMutatorMethod() {
        return mutatorMethod;
    }

    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

    public String toString() {
        return "Property[ name=" + name + " ]";
    }
}
