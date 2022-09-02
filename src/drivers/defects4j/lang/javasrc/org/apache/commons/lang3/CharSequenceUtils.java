/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3;

/**
 * <p>Operations on {@link java.lang.CharSequence} that are
 * {@code null} safe.</p>
 *
 * @see java.lang.CharSequence
 * @since 3.0
 * @version $Id$
 */
public class CharSequenceUtils {

    /**
     * <p>{@code CharSequenceUtils} instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public CharSequenceUtils() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Returns a new {@code CharSequence} that is a subsequence of this
     * sequence starting with the {@code char} value at the specified index.</p>
     *
     * <p>This provides the {@code CharSequence} equivalent to {@link String#substring(int)}.
     * The length (in {@code char}) of the returned sequence is {@code length() - start},
     * so if {@code start == end} then an empty sequence is returned.</p>
     *
     * @param xx  the specified subsequence, null returns null
     * @param start  the start index, inclusive, valid
     * @return a new subsequence, may be null
     * @throws IndexOutOfBoundsException if {@code start} is negative or if 
     *  {@code start} is greater than {@code length()}
     */
    public static CharSequence subSequence(CharSequence xx, int start) {
        return xx == null ? null : xx.subSequence(start, xx.length());
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Finds the first index in the {@code CharSequence} that matches the
     * specified character.</p>
     *
     * @param xx  the {@code CharSequence} to be processed, not null
     * @param searchChar  the char to be searched for
     * @param start  the start index, negative starts at the string start
     * @return the index where the search char was found, -1 if not found
     */
    static int indexOf(CharSequence xx, int searchChar, int start) {
        if (xx instanceof String) {
            return ((String) xx).indexOf(searchChar, start);
        } else {
            int sz = xx.length();
            if (start < 0) {
                start = 0;
            }
            for (int i = start; i < sz; i++) {
                if (xx.charAt(i) == searchChar) {
                    return i;
                }
            }
            return -1;
        }
    }

    /**
     * Used by the indexOf(CharSequence methods) as a green implementation of indexOf.
     *
     * @param xx the {@code CharSequence} to be processed
     * @param searchChar the {@code CharSequence} to be searched for
     * @param start the start index
     * @return the index where the search sequence was found
     */
    static int indexOf(CharSequence xx, CharSequence searchChar, int start) {
        return xx.toString().indexOf(searchChar.toString(), start);
//        if (xx instanceof String && searchChar instanceof String) {
//            // TODO: Do we assume searchChar is usually relatively small;
//            //       If so then calling toString() on it is better than reverting to
//            //       the green implementation in the else block
//            return ((String) xx).indexOf((String) searchChar, start);
//        } else {
//            // TODO: Implement rather than convert to String
//            return xx.toString().indexOf(searchChar.toString(), start);
//        }
    }

    /**
     * <p>Finds the last index in the {@code CharSequence} that matches the
     * specified character.</p>
     *
     * @param xx  the {@code CharSequence} to be processed
     * @param searchChar  the char to be searched for
     * @param start  the start index, negative returns -1, beyond length starts at end
     * @return the index where the search char was found, -1 if not found
     */
    static int lastIndexOf(CharSequence xx, int searchChar, int start) {
        if (xx instanceof String) {
            return ((String) xx).lastIndexOf(searchChar, start);
        } else {
            int sz = xx.length();
            if (start < 0) {
                return -1;
            }
            if (start >= sz) {
                start = sz - 1;
            }
            for (int i = start; i >= 0; --i) {
                if (xx.charAt(i) == searchChar) {
                    return i;
                }
            }
            return -1;
        }
    }

    /**
     * Used by the lastIndexOf(CharSequence methods) as a green implementation of lastIndexOf
     *
     * @param xx the {@code CharSequence} to be processed
     * @param searchChar the {@code CharSequence} to be searched for
     * @param start the start index
     * @return the index where the search sequence was found
     */
    static int lastIndexOf(CharSequence xx, CharSequence searchChar, int start) {
        return xx.toString().lastIndexOf(searchChar.toString(), start);
//        if (xx instanceof String && searchChar instanceof String) {
//            // TODO: Do we assume searchChar is usually relatively small;
//            //       If so then calling toString() on it is better than reverting to
//            //       the green implementation in the else block
//            return ((String) xx).lastIndexOf((String) searchChar, start);
//        } else {
//            // TODO: Implement rather than convert to String
//            return xx.toString().lastIndexOf(searchChar.toString(), start);
//        }
    }

    /**
     * Green implementation of toCharArray.
     *
     * @param xx the {@code CharSequence} to be processed
     * @return the resulting char array
     */
    static char[] toCharArray(CharSequence xx) {
        if (xx instanceof String) {
            return ((String) xx).toCharArray();
        } else {
            int sz = xx.length();
            char[] array = new char[xx.length()];
            for (int i = 0; i < sz; i++) {
                array[i] = xx.charAt(i);
            }
            return array;
        }
    }

    /**
     * Green implementation of regionMatches.
     *
     * @param xx the {@code CharSequence} to be processed
     * @param ignoreCase whether or not to be case insensitive
     * @param thisStart the index to start on the {@code xx} CharSequence
     * @param substring the {@code CharSequence} to be looked for
     * @param start the index to start on the {@code substring} CharSequence
     * @param length character length of the region
     * @return whether the region matched
     */
    static boolean regionMatches(CharSequence xx, boolean ignoreCase, int thisStart,
            CharSequence substring, int start, int length)    {
        if (xx instanceof String && substring instanceof String) {
            return ((String) xx).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        } else {
            // TODO: Implement rather than convert to String
            return xx.toString().regionMatches(ignoreCase, thisStart, substring.toString(), start, length);
        }
    }

}
