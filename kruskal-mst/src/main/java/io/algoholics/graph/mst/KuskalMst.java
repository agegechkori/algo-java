package io.algoholics.graph.mst;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class KuskalMst {

    private static final class EdgeComparator<T> implements Comparator<Edge<T>> {

        @Override
        public int compare(Edge<T> t1, Edge<T> t2) {
            return Double.compare(t1.getWeight(), t2.getWeight());
        }
    }

    public <T> Set<Edge<T>> mst(final Graph<T> graph) {
        final Set<Edge<T>> mst = new HashSet<>();
        final DisjointSet<T> connected = new DisjointSet<>(graph.getVertices());
        final int vertexCount = connected.getCount();
        final PriorityQueue<Edge<T>> pq = new PriorityQueue<>(new EdgeComparator<>());

        pq.addAll(graph.getEdges());

        while (!pq.isEmpty() && mst.size() < vertexCount - 1) {
            final Edge<T> edge = pq.poll();
            if (connected.union(edge.getSource(), edge.getTarget())) {
                mst.add(edge);
            }
        }

        return mst;
    }
}
