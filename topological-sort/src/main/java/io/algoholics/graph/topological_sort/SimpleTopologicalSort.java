package io.algoholics.graph.topological_sort;

import java.util.*;
import java.util.stream.Collectors;

class Pair<T1, T2> {
    T1 first;
    T2 second;

    Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }
}

public class SimpleTopologicalSort {
    public <V> List<V> sort(List<Pair<V, V>> edges) {
        final Map<V, List<V>> graph = listToMap(edges);
        final Map<V, Integer> indegrees = getIndegrees(edges);
        final Queue<V> sources = getSourceVertices(indegrees);

        final List<V> sorted = new ArrayList<>(indegrees.size());

        while (!sources.isEmpty()) {
            final V vertex = sources.remove();
            sorted.add(vertex);
            final List<V> adjacentVertices = removeVertex(vertex, graph);
            for (final V adjacent : adjacentVertices) {
                if (hasZeroIndegree(adjacent, indegrees)) {
                    sources.add(adjacent);
                }
            }
        }

        if (!graph.isEmpty()) {
            throw new IllegalArgumentException("Graph contains at least one cycle and cannot have a topological sort.");
        }

        return sorted;
    }

    <V> Map<V, Integer> getIndegrees(List<Pair<V, V>> edges) {
        final Map<V, Integer> indegree = new HashMap<>();
        edges.forEach(p -> {
            indegree.putIfAbsent(p.first, 0);
            indegree.merge(p.second, 1, Integer::sum);
        });
        return indegree;
    }

    <V> Map<V, List<V>> listToMap(List<Pair<V, V>> edges) {
        return edges.stream().collect(Collectors.toMap(p -> p.first, p -> new ArrayList<>(Collections.singletonList(p.second)),
                (oldList, newList) -> {
                    oldList.addAll(newList);
                    return oldList;
                }));
    }

    <V> Queue<V> getSourceVertices(final Map<V, Integer> indegrees) {
        return indegrees.entrySet().stream().filter(e -> e.getValue() == 0).map(Map.Entry::getKey).collect(Collectors.toCollection(LinkedList::new));
    }

    <V> List<V> removeVertex(V vertex, Map<V, List<V>> graph) {
        final List<V> adjacentVertices = graph.remove(vertex);
        return adjacentVertices != null ? adjacentVertices : Collections.emptyList();
    }

    <V> boolean hasZeroIndegree(V adjacent, final Map<V, Integer> indegrees) {
        return indegrees.compute(adjacent, (k, v) -> (--v == 0) ? null : v) == null;
    }

    public <V> boolean isTopologicalSort(List<Pair<V, V>> dag, List<V> sorted) {
        assertCompatible(dag, sorted);

        final Positions<V> pos = new Positions<>(sorted);

        return dag.stream().allMatch(e -> pos.position(e.first).comesBefore(pos.position(e.second)));
    }

    private <V> void assertCompatible(List<Pair<V, V>> dag, List<V> sorted) {
        final Set<V> vertices = dag.stream().flatMap(v -> new HashSet<>(Arrays.asList(v.first, v.second)).stream()).collect(Collectors.toSet());
        final Set<V> sortedSet = new HashSet<>(sorted);

        if (!vertices.equals(sortedSet) || sorted.size() != sortedSet.size()) {
            throw new IllegalArgumentException("Provided topological sort is not compatible with the graph.");
        }
    }

    private static final class Positions<V> {
        private final Map<V, Integer> pos;

        Positions(List<V> sorted) {
            this.pos = new HashMap<>();
            for (int i = 0; i < sorted.size(); i++) {
                pos.put(sorted.get(i), i);
            }
        }

        Position position(V vertex) {
            return new Position(pos.get(vertex));
        }

        public List<Position> positions(List<V> vertices) {
            return vertices.stream().map(this::position).collect(Collectors.toList());
        }
    }

    private static final class Position {
        private final int index;

        Position(final int index) {
            this.index = index;
        }

        boolean comesBefore(Position that) {
            return that.index - this.index > 0;
        }

        public boolean comesBefore(List<Position> positions) {
            return positions.stream().allMatch(this::comesBefore);
        }
    }

}