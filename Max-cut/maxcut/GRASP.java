package maxcut;

import java.util.*;

class GRASP {
    private Graph graph;
    private Random random;
    private int maxIterations;
    public double semiGreedyScore;
    private double alpha;

    public GRASP(Graph graph, int maxIterations, double alpha) {
        this.graph = graph;
        this.random = new Random();
        this.maxIterations = maxIterations;
        semiGreedyScore = Double.NEGATIVE_INFINITY;
        this.alpha = alpha;
    }

    public Set<Integer> findMaxCut() {
        Set<Integer> bestPartitionA = null;
        double bestCutWeight = Double.MIN_VALUE;

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            Set<Integer> partitionA = constructGreedyRandomizedSolution();
            Set<Integer> partitionB = getRemainingVertices(partitionA);

            // Run local search (2-opt or any other technique) to improve the current solution
            localSearch(partitionA, partitionB);

            double cutWeight = graph.calculateCutWeight(partitionA, partitionB);

            if (cutWeight > bestCutWeight) {
                bestCutWeight = cutWeight;
                bestPartitionA = partitionA;
            }
        }

        return bestPartitionA;
    }

    // Method to perform the greedy randomized construction phase
    public Set<Integer> constructGreedyRandomizedSolution() {
        Set<Integer> partitionA = new HashSet<>();
        Set<Integer> partitionB = new HashSet<>();
        Set<Integer> unassignedVertices = new HashSet<>();

        for (Vertex vertex : graph.getVertices()) {
            unassignedVertices.add(vertex.getId());
        }

        while (!unassignedVertices.isEmpty()) {
            List<Integer> candidateList = new ArrayList<>();
            double maxGreedyScore = Double.NEGATIVE_INFINITY;

            // Calculate the greedy score for each unassigned vertex
            for (Integer vertexId : unassignedVertices) {
                double sumWeightPartitionA = sumWeightInPartition(vertexId, partitionA);
                double sumWeightPartitionB = sumWeightInPartition(vertexId, partitionB);

                double greedyScore = Math.max(sumWeightPartitionA, sumWeightPartitionB);
                graph.getVertexById(vertexId).setGreedyScore(greedyScore);
                //System.out.println(greedyScore);
                if (greedyScore >= maxGreedyScore) {
                    maxGreedyScore = greedyScore;
                }
                candidateList.add(vertexId);
            }

            // Create a restricted candidate list based on the greedy scores
            List<Integer> restrictedCandidateList = new ArrayList<>();
            for (Integer vertexId : candidateList) {
                double greedyScore = graph.getVertexById(vertexId).getGreedyScore();
                if(maxGreedyScore<0){
                    if(alpha > 0){
                        if (greedyScore >= 1/alpha * maxGreedyScore) { // Adjust the threshold as needed (e.g., 0.5)
                            restrictedCandidateList.add(vertexId);
                        }
                    } else restrictedCandidateList.add(vertexId);
                }
                else{
                    if(alpha > 0){
                        if(greedyScore >= alpha * maxGreedyScore) { // Adjust the threshold as needed (e.g., 0.5)
                            restrictedCandidateList.add(vertexId);
                        }
                    }  else restrictedCandidateList.add(vertexId);

                }
            }

            // Randomly select a vertex from the restricted candidate list
            int selectedVertex = restrictedCandidateList.get(random.nextInt(restrictedCandidateList.size()));

            // Place the selected vertex in the appropriate partition
            if (sumWeightInPartition(selectedVertex, partitionA) > sumWeightInPartition(selectedVertex, partitionB)) {
                partitionB.add(selectedVertex);
            } else {
                partitionA.add(selectedVertex);
            }

            // Remove the selected vertex from the unassigned vertices
            unassignedVertices.remove(selectedVertex);
        }
        return partitionA;
    }

    private void localSearch(Set<Integer> partitionA, Set<Integer> partitionB) {
        boolean improved = true;
         if(graph.calculateCutWeight(partitionA,partitionB)>semiGreedyScore)
             semiGreedyScore = graph.calculateCutWeight(partitionA,partitionB);
        while (improved) {
            improved = false;

            for(Vertex vertex: graph.getVertices()){
                int n= vertex.getId();
                double currentWeight = graph.calculateCutWeight(partitionA,partitionB);
                double tempcurweight;
                if(partitionA.contains(n)) {
                    partitionA.remove(n);
                    partitionB.add(n);
                    tempcurweight = graph.calculateCutWeight(partitionA,partitionB);
                    if(tempcurweight>currentWeight) {
                        System.out.println(tempcurweight);
                        improved = true;
                        break;
                    } else {
                        partitionA.add(n);
                        partitionB.remove(n);
                    }
                } else {
                    partitionB.remove(n);
                    partitionA.add(n);
                    tempcurweight = graph.calculateCutWeight(partitionA,partitionB);
                    if(tempcurweight>currentWeight) {
                        System.out.println(tempcurweight);
                        improved = true;
                        break;
                    } else {
                        partitionB.add(n);
                        partitionA.remove(n);
                    }
                }
            }
        }
    }

    private double calculateCutWeightAfterSwap(Set<Integer> partitionA, Set<Integer> partitionB, int vertexA, int vertexB) {
        // Create copies of partitionA and partitionB to avoid modifying the original sets
        Set<Integer> newPartitionA = new HashSet<>(partitionA);
        Set<Integer> newPartitionB = new HashSet<>(partitionB);

        // Perform the vertex swap
        newPartitionA.remove(vertexA);
        newPartitionA.add(vertexB);
        newPartitionB.remove(vertexB);
        newPartitionB.add(vertexA);

        // Calculate the cut weight after the swap
        double newCutWeight = graph.calculateCutWeight(newPartitionA, newPartitionB);
        return newCutWeight;
    }


    // Method to calculate the greedy score for a vertex
    private double calculateGreedyScore(int vertexId, Set<Integer> partitionA, Set<Integer> partitionB) {
        double greedyScore = 0.0;
        for (Edge edge : graph.getEdges()) {
            if (edge.getStartVertexId() == vertexId) {
                int otherVertexId = edge.getEndVertexId();
                if (partitionB.contains(otherVertexId)) {
                    greedyScore += edge.getWeight();
                    //System.out.println(greedyScore+"for vertex id "+ vertexId+" from 1");
                }
            } else if (edge.getEndVertexId() == vertexId) {
                int otherVertexId = edge.getStartVertexId();
                if (partitionB.contains(otherVertexId)) {
                    greedyScore += edge.getWeight();
                    //System.out.println(greedyScore+"for vertex id "+ vertexId+" from 2");
                }
            }
        }
        return greedyScore;
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

    private double sumWeightInPartition(int vertexId, Set<Integer> partition) {
        double sumWeight = 0.0;
        for (Edge edge : graph.getEdges()) {
            if (edge.getStartVertexId() == vertexId && partition.contains(edge.getEndVertexId())) {
                sumWeight += edge.getWeight();
            }
        }
        return sumWeight;
    }
}