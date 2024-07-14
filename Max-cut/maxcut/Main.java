package maxcut;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        String filename = "g43.rud";
        Scanner scanner = new Scanner(new File("set1/"+filename));
        double upperbound = 7027;
        FileWriter writer;
        if(!(new File("output.csv").exists())){
            writer = new FileWriter("output.csv", true);
            writer.write("Name,Vertices,Edges,RandomizedScore,GreedyScore,SemiGreedyScore,GraspScore,UpperBound,Iterations,alpha\n");
        } else {
            writer = new FileWriter("output.csv", true);
        }

        System.out.print("Enter the number of vertices and edges: ");
        int numVertices = scanner.nextInt();
        int numEdges = scanner.nextInt();

        Graph graph = new Graph(numVertices, numEdges);
        Set<Integer> vertexIds = new HashSet<>(); // To keep track of unique vertex IDs

        System.out.println("Enter start vertex ID, end vertex ID, and weight for edge : ");
        for (int i = 0; i < numEdges; i++) {
            int startVertexId = scanner.nextInt();
            int endVertexId = scanner.nextInt();
            double weight = scanner.nextDouble();

            // Add the vertex IDs to the set to ensure no duplicates
            vertexIds.add(startVertexId);
            vertexIds.add(endVertexId);

            graph.addEdge(startVertexId, endVertexId, weight);
        }
        System.out.println("done");

        // Add the unique vertex IDs to the graph
        for (int vertexId : vertexIds) {
            graph.addVertex(vertexId);
        }

        int maxiterations;
        double alpha;

        //Greedy heuristic calculation
        maxiterations = 1;
        alpha = 1;
        GRASP greedy = new GRASP(graph, maxiterations, alpha);
        Set<Integer> bestgreedyA = greedy.constructGreedyRandomizedSolution();
        Set<Integer> bestgreedyB = greedy.getRemainingVertices(bestgreedyA);
        double bestGreedyWeight = graph.calculateCutWeight(bestgreedyA, bestgreedyB);
        System.out.println("Best greedy Weight: " + bestGreedyWeight);

        //Randomized Partition
        alpha = 0;
        GRASP random = new GRASP(graph, maxiterations, alpha);
        Set<Integer> randomPartitionA = random.constructGreedyRandomizedSolution();
        Set<Integer> randomPartitionB = random.getRemainingVertices(randomPartitionA);
        double bestRandomWeight = graph.calculateCutWeight(randomPartitionA, randomPartitionB);
        System.out.println("Best Randomized Weight: " + bestRandomWeight);


        alpha = 0.75;
        maxiterations = 3;
        GRASP grasp = new GRASP(graph,maxiterations, alpha);
        // Find the Max-Cut solution using GRASP
        Set<Integer> bestPartitionA = grasp.findMaxCut();
        double SemiGreedyScore = grasp.semiGreedyScore;
        Set<Integer> bestPartitionB = grasp.getRemainingVertices(bestPartitionA);

        // Calculate and print the cut weight of the best solution
        double bestCutWeight = graph.calculateCutWeight(bestPartitionA, bestPartitionB);
        System.out.println("Best Cut Weight: " + bestCutWeight);

        // Print the partitions (vertex IDs) of the best solution
        System.out.println("Best Partition A: " + bestPartitionA);
        System.out.println("Best Partition B: " + bestPartitionB);

        scanner.close();

        performanceCalc(filename, numVertices, numEdges, bestRandomWeight, bestGreedyWeight, SemiGreedyScore, bestCutWeight, upperbound, maxiterations, alpha, writer);
        writer.close();
    }


    public static void performanceCalc(String g1, int numVertices, int numEdges, double bestRandomWeight, double greedyScore, double semiGreedyScore,
                                       double graspScore, double upperbound, int maxiteration, double alpha, FileWriter writer) throws IOException {
        String csvRow = String.format("%s,%d,%d,%.2f,%.2f,%.2f,%.2f,%.2f,%d,%.2f\n",
                g1, numVertices, numEdges, bestRandomWeight, greedyScore, semiGreedyScore, graspScore, upperbound, maxiteration,alpha);

        writer.write(csvRow);
    }


}

