package io.algoholics.graph.topological_sort;

import java.util.List;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        DirectedGraph<Integer> dag = new DirectedGraph<>();
        dag.addEdge(5, 11);
        dag.addEdge(11, 2);
        dag.addEdge(7, 11);
        dag.addEdge(7, 8);
        dag.addEdge(8, 9);
        dag.addEdge(3, 8);
        dag.addEdge(3, 10);
        dag.addEdge(11, 9);
        dag.addEdge(11, 10);

        System.out.println(dag.getAdjacencyList());

        TopologicalSort sort = new TopologicalSort();
        List<Integer> sorted = sort.sort(dag);
        System.out.println(String.join(", ", sorted.stream().map(e -> e.toString()).collect(Collectors.toList())));
        System.out.println("Is topological sort: " + sort.isTopologicalSort(dag, sorted));
    }
}
