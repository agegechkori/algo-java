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
        final Map<V, Integer> indegrees = graph.getIndegrees();
        final Queue<V> sources = getSourceVertices(indegrees);

        final List<V> sorted = new ArrayList<>(indegrees.size());

        while(!sources.isEmpty()) {
            final V vertex = sources.remove();
            sorted.add(vertex);
            final List<V> adjacentVertices = graph.remove(vertex);
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

    private <V> boolean hasZeroIndegree(V adjacent, final Map<V, Integer> indegrees) {
        return indegrees.compute(adjacent, (k, v) -> (--v == 0) ? null : v) == null;
    }

    private <V> Queue<V> getSourceVertices(final Map<V, Integer> indegrees) {
        final List<V> list = indegrees.entrySet().stream().filter(e -> e.getValue() == 0).map(e -> e.getKey()).collect(Collectors.toList());
        return new LinkedList<>(list);
    }

    public <V> boolean isTopologicalSort(DirectedGraph<V> dag, List<V> sorted) {
        assertCompatible(dag, sorted);

        final Map<V, Integer> positions = mapVerticesPositions(sorted);

        return dag.getAdjacencyList().entrySet().stream().allMatch(e -> allSourcesBeforeTargets(e.getKey(), e.getValue(), positions));
    }

    private <V> void assertCompatible(DirectedGraph<V> dag, List<V> sorted) {
        final Set<V> vertices = new HashSet<>(dag.getVertices());
        final Set<V> sortedSet = new HashSet<>(sorted);

        if (!vertices.equals(sortedSet) || sorted.size() != sortedSet.size()) {
            throw new IllegalArgumentException("Provided topological sort is not compatible with the graph.");
        }
    }

    private static final class Positions<V> {
        private final Map<V, Integer> positions;

        public Positions(List<V> sorted) {
            this.positions = new HashMap<>();
            for (int i = 0; i < sorted.size(); i++) {
                positions.put(sorted.get(i), i);
            }
        }

        public Position getPosition(V vertex) {
            return new Position(positions.get(vertex));
        }
    }

    private static final class Position {
        private final int index;

        Position(final int index) {
            this.index = index;
        }

        public boolean comesBefore(Position that) {
            return that.index - this.index > 0; 
        }

        public boolean comesBefore(List<Position> positions) {
            return positions.stream().allMatch(this::comesBefore);
        }
    }

    private <V> Map<V, Integer> mapVerticesPositions(List<V> sorted) {
        final Map<V, Integer> positions = new HashMap<>();
        for (int i = 0; i < sorted.size(); i++) {
            positions.put(sorted.get(i), i);
        }
        return positions;
    }

    private <V> boolean allSourcesBeforeTargets(V source, List<V> adjacentVertices, Map<V, Integer> positions) {
        return adjacentVertices.stream().allMatch(target -> isSourceBeforeTarget(source, target, positions));
    }

    private <V> boolean isSourceBeforeTarget(V source, V target, Map<V, Integer> positions) {
        return positions.get(target) - positions.get(source) > 0;
    }
}
