package io.algoholics.graph.mst;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DisjointSet<T> {
    private final Map<T, T> parents;
    private final Map<T, Integer> heights;
    private int count;

    public DisjointSet() {
        this(null);
    }

    public DisjointSet(Collection<T> elements) {
        if (null == elements || elements.isEmpty()) {
            parents = new HashMap<>();
            heights = new HashMap<>();
            count = 0;
        } else {
            parents = elements.stream().collect(Collectors.toMap(p -> p, p -> p));
            heights = elements.stream().collect(Collectors.toMap(p -> p, p -> 0));
            count = elements.size();
        }
    }

    public int getCount() {
        return count;
    }

    public T find(T element) {
        if (!parents.containsKey(element)) {
            parents.put(element, element);
            heights.put(element, 0);
            count++;
            return element;
        }

        while (element != parents.get(element)) {
            parents.put(element, parents.get(parents.get(element)));
            element = parents.get(element);
        }
        return element;
    }

    public boolean connected(T element1, T element2) {
        return find(element1) == find(element2);
    }

    public boolean union(T element1, T element2) {
        final T rootElement1 = find(element1);
        final T rootElement2 = find(element2);

        if (rootElement1 == rootElement2)
            return false;

        if (heights.get(rootElement1) < heights.get(rootElement2)) {
            parents.put(rootElement1, rootElement2);
        } else if (heights.get(rootElement1) > heights.get(rootElement2)) {
            parents.put(rootElement2, rootElement1);
        } else {
            parents.put(rootElement2, rootElement1);
            heights.merge(rootElement1, 1, Integer::sum);
        }

        count--;
        return true;
    }
}
