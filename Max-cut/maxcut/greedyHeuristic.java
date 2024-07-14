package maxcut;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

class greedyHeuristic {
    Graph graph;
    public greedyHeuristic(Graph graph){
        this.graph = graph;
    }
    public Set<Integer> greedyMaxCut() {
        Set<Integer> partitionX = new HashSet<>();
        Set<Integer> partitionY = new HashSet<>();

        // Find the largest-weight edge and place its endpoints in different partitions
        Edge maxWeightEdge = Collections.max(graph.getEdges(), Comparator.comparingDouble(Edge::getWeight));
        partitionX.add(maxWeightEdge.getStartVertexId());
        partitionY.add(maxWeightEdge.getEndVertexId());

        // Place remaining vertices in partitions to maximize the cut weight
        for (Vertex vertex : graph.getVertices()) {
            int vertexId = vertex.getId();
            if (!partitionX.contains(vertexId) && !partitionY.contains(vertexId)) {
                double cutWeightX = calculateCutWeightAfterPlacement(partitionX, partitionY, vertexId);
                double cutWeightY = calculateCutWeightAfterPlacement(partitionY, partitionX, vertexId);

                if (cutWeightX > cutWeightY) {
                    partitionX.add(vertexId);
                } else {
                    partitionY.add(vertexId);
                }
            }
        }

        return partitionX; // Return partition X, you can modify as needed
    }

    private double calculateCutWeightAfterPlacement(Set<Integer> partitionA, Set<Integer> partitionB, int vertexId) {
        double cutWeight = 0.0;
        for (Edge edge : graph.getEdges()) {
            if (edge.getStartVertexId() == vertexId && partitionB.contains(edge.getEndVertexId())) {
                cutWeight += edge.getWeight();
            } else if (edge.getEndVertexId() == vertexId && partitionB.contains(edge.getStartVertexId())) {
                cutWeight += edge.getWeight();
            }
        }
        return cutWeight;
    }

    public Set<Integer> getRemainingVertices(Set<Integer> partitionA) {
        Set<Integer> allVertices = new HashSet<>();
        Set<Integer> partitionB = new HashSet<>();

        for (Vertex vertex : graph.getVertices()) {
            allVertices.add(vertex.getId());
        }

        allVertices.removeAll(partitionA);
        partitionB.addAll(allVertices);

        return partitionB;
    }

}
