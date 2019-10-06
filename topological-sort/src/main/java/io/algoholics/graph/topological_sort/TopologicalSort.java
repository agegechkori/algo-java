package io.algoholics.graph.topological_sort;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class TopologicalSort {

    public <V> List<V> sort(final DirectedGraph<V> dag) {
        final DirectedGraph<V> graph = new DirectedGraph<>(dag);
        final Queue<V> sources = new LinkedList<>(graph.getSources());

        final List<V> sorted = new ArrayList<>(graph.size());

        while(!sources.isEmpty()) {
            final V vertex = sources.remove();
            sorted.add(vertex);
            final List<V> adjacentVertices = graph.removeSource(vertex);
            for (final V adjacent : adjacentVertices) {
                if (graph.getIndegree(adjacent) == 0) {
                    sources.add(adjacent);
                }
            }
        }

        if (!graph.isEmpty()) {
            throw new IllegalArgumentException("Graph contains at least one cycle and cannot have a topological sort.");
        }

        return sorted;
    }

    public <V> boolean isTopologicalSort(DirectedGraph<V> dag, List<V> sorted) {
        assertCompatible(dag, sorted);

        final Positions<V> pos = new Positions<>(sorted);

        return dag.getAdjacencyList().entrySet().stream().allMatch(e -> pos.position(e.getKey()).comesBefore(pos.positions(e.getValue())));
    }

    private <V> void assertCompatible(DirectedGraph<V> dag, List<V> sorted) {
        final Set<V> vertices = new HashSet<>(dag.getVertices());
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

        List<Position> positions(List<V> vertices) {
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

        boolean comesBefore(List<Position> positions) {
            return positions.stream().allMatch(this::comesBefore);
        }
    }
}
