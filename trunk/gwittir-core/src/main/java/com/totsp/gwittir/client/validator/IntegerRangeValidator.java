/*
 * IntegerRangeValidator.java
 *
 * Created on July 16, 2007, 5:04 PM
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
package com.totsp.gwittir.client.validator;

import com.totsp.gwittir.client.validator.ValidationException;
import com.totsp.gwittir.client.validator.Validator;


/**
 *
 * @author cooper
 */
public class IntegerRangeValidator implements Validator {
    private IntegerValidator iv = new IntegerValidator();
    private int max;
    private int min;

    /** Creates a new instance of IntegerRangeValidator */
    public IntegerRangeValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public Object validate(Object value) throws ValidationException {
        Integer i = (Integer) iv.validate(value);

        if(i == null) {
            return null;
        }

        if((i.intValue() <= max) && (i.intValue() >= min)) {
            return value;
        }

        throw new ValidationException("Must be a value between " + min +
            " and " + max, IntegerRangeValidator.class);
    }
}
