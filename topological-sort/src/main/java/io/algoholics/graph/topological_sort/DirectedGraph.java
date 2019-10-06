package io.algoholics.graph.topological_sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DirectedGraph<V> {
    private final Map<V, List<V>> adjacencyList;
    private final Map<V, Integer> indegrees;

    public DirectedGraph(Map<V, List<V>> adjacencyList) {
        this.adjacencyList = adjacencyList;
        this.indegrees = new HashMap<>();

        adjacencyList.forEach((k, v) -> {
            indegrees.putIfAbsent(k, 0);
            v.forEach(e -> indegrees.merge(e, 1, Integer::sum));
        });
    }

    public DirectedGraph(DirectedGraph<V> graph) {
        this(new HashMap<>(graph.adjacencyList));
    }

    public DirectedGraph() {
        this(new HashMap<>());
    }

    public void addEdge(V source, V target) {
        final List<V> adjacentVertices = adjacencyList.getOrDefault(source, new ArrayList<>());
        adjacentVertices.add(target);
        adjacencyList.put(source, adjacentVertices);
        indegrees.putIfAbsent(source, 0);
        indegrees.merge(target, 1, Integer::sum);
    }

    public List<V> getAdjacentVertices(V vertex) {
        return new ArrayList<>(adjacencyList.get(vertex));
    }

    public List<V> getVertices() {
        return new ArrayList<>(indegrees.keySet());
    }

    public List<V> removeSource(V vertex) {
        if (indegrees.get(vertex) != 0) {
            throw new IllegalArgumentException("Cannot remove a vertex with a non-zero indegree");
        }
        indegrees.remove(vertex);
        final List<V> adjacentVertices = adjacencyList.remove(vertex);
        if (adjacentVertices == null) {
            return Collections.emptyList();
        }
        adjacentVertices.forEach(v -> indegrees.put(v, indegrees.get(v) - 1));
        return adjacentVertices;
    }

    public int getIndegree(V vertex) {
        return indegrees.get(vertex);
    }

    public int size() {
        return indegrees.size();
    }

    public List<V> getSources() {
        return indegrees.entrySet().stream().filter(e -> e.getValue() == 0).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return adjacencyList.isEmpty();
    }

    Map<V, List<V>> getAdjacencyList() {
        return new HashMap<>(adjacencyList);
    }
}
