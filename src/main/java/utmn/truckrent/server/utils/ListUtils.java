package utmn.truckrent.server.utils;

import java.util.*;
import java.util.stream.Collectors;

public final class ListUtils {

    private ListUtils() {}

    public static <T> List<T> and(List<T> first, List<T> second) {
        if (first == null || second == null) return Collections.emptyList();
        Set<T> secondSet = new HashSet<>(second);
        return first.stream()
                .filter(secondSet::contains)
                .collect(Collectors.toList());
    }

    public static <T> List<T> or(List<T> first, List<T> second) {
        Set<T> result = new LinkedHashSet<>();
        if (first != null) result.addAll(first);
        if (second != null) result.addAll(second);
        return new ArrayList<>(result);
    }

    public static <T> List<T> xor(List<T> first, List<T> second) {
        Set<T> firstSet = new HashSet<>(first != null ? first : Collections.emptyList());
        Set<T> secondSet = new HashSet<>(second != null ? second : Collections.emptyList());

        Set<T> result = new LinkedHashSet<>(firstSet);
        for (T item : secondSet) {
            if (!result.add(item)) {
                result.remove(item);
            }
        }
        return new ArrayList<>(result);
    }

    public static <T> List<T> not(List<T> first, List<T> second) {
        if (first == null) return Collections.emptyList();
        if (second == null) return new ArrayList<>(first);

        Set<T> secondSet = new HashSet<>(second);
        return first.stream()
                .filter(item -> !secondSet.contains(item))
                .collect(Collectors.toList());
    }
}
