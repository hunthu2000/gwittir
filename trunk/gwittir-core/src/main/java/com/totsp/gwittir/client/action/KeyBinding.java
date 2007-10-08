/*
 * KeyBinding.java
 *
 * Created on April 12, 2007, 3:21 PM
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
package com.totsp.gwittir.client.action;


/**
 *
 * @author <a href="mailto:cooper@screaming-penguin.com">Robert "kebernet" Cooper</a>
 */
public class KeyBinding {
    private char key;
    private int modifiers;

    /** Creates a new instance of KeyBinding */
    public KeyBinding() {
    }

    public char getKey() {
        return key;
    }

    public int getModifiers() {
        return modifiers;
    }

    public void setKey(char key) {
        this.key = key;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }
}