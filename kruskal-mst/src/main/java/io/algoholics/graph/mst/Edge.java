package io.algoholics.graph.mst;

import java.util.Objects;
import java.util.StringJoiner;

public class Edge<T> {
    private final T source;
    private final T target;
    private final double weight;

    public Edge(T source, T target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public T getSource() {
        return source;
    }

    public T getTarget() {
        return target;
    }

    public double getWeight() {
        return weight;
    }

    public T getOther(T vertex) {
        if (source.equals(vertex)) {
            return target;
        }
        if (target.equals(vertex)) {
            return source;
        }
        throw new IllegalArgumentException(String.format("No vertex %s in the edge %s", vertex, this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge<?> edge = (Edge<?>) o;
        return Double.compare(edge.weight, weight) == 0 &&
                Objects.equals(source, edge.source) &&
                Objects.equals(target, edge.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, weight);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Edge.class.getSimpleName() + "[", "]")
                .add("source=" + source)
                .add("target=" + target)
                .add("weight=" + weight)
                .toString();
    }
}
