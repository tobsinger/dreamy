package de.dreamy.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * Convenience methods for String operations
 */

public class Strings {

    public static String join(Collection<String> strings, String separator) {
        if (strings == null || strings.isEmpty()) {
            return "";
        }
        final Iterator<String> iterator = strings.iterator();
        final StringBuilder sb = new StringBuilder(iterator.next());
        while (iterator.hasNext()) {
            sb.append(separator).append(iterator.next());
        }
        return sb.toString();
    }
}
