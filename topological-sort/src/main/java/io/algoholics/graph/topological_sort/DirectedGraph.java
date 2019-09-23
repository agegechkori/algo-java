package io.algoholics.graph.topological_sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectedGraph<V> {
    private final Map<V, List<V>> adjacencyList;

    public DirectedGraph(Map<V, List<V>> adjacencyList) {
        this.adjacencyList = adjacencyList;
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
    }

    public List<V> getAdjacentVertices(V vertex) {
        return new ArrayList<>(adjacencyList.get(vertex));
    }

    public Map<V, Integer> getIndegrees() {
        final Map<V, Integer> indegree = new HashMap<>();

        adjacencyList.forEach((k, v) -> {
            indegree.putIfAbsent(k, 0);
            v.forEach(e -> indegree.merge(e, 1, (prev, one) -> prev + one));
        });
        return indegree;
    }

    public List<V> getVertices() {
        return new ArrayList<>(getIndegrees().keySet());
    }

    public List<V> remove(V vertex) {
        final List<V> adjacentVertices = adjacencyList.remove(vertex);
        return adjacentVertices != null ?  adjacentVertices : Collections.emptyList();
    }

    public boolean isEmpty() {
        return adjacencyList.isEmpty();
    }

    Map<V, List<V>> getAdjacencyList() {
        return new HashMap<>(adjacencyList);
    }
}
