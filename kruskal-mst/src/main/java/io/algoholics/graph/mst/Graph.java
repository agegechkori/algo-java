package io.algoholics.graph.mst;

import java.util.*;
import java.util.stream.Collectors;

public class Graph<T> {
    private final Map<T, List<Edge<T>>> adjacencyList;
    private int edgesCount;

    public Graph(final Map<T, List<Edge<T>>> adjacencyList) {
        this.adjacencyList = adjacencyList;
        this.edgesCount = getEdges().size();
    }

    public Graph() {
        this(new HashMap<>());
    }

    public void addEdge(final Edge<T> edge) {
        adjacencyList.merge(edge.getSource(), new ArrayList<>(Collections.singletonList(edge)), (oldVal, newVal) -> {
            oldVal.addAll(newVal);
            return oldVal;
        });
        adjacencyList.merge(edge.getTarget(), new ArrayList<>(Collections.singletonList(edge)), (oldVal, newVal) -> {
            oldVal.addAll(newVal);
            return oldVal;
        });
        edgesCount++;
    }

    public List<Edge<T>> getEdges() {
        return adjacencyList.entrySet().stream().flatMap(p -> p.getValue().stream()).distinct().collect(Collectors.toList());
    }

    public List<Edge<T>> getEdges(T vertex) {
        return new ArrayList<>(adjacencyList.get(vertex));
    }

    public List<T> getVertices() {
        return new ArrayList<>(adjacencyList.keySet());
    }
}
