package maxcut;

import java.util.*;

class Vertex {
    private int id;
    private double greedyScore;

    public Vertex(int id) {
        this.id = id;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    //Set greedy score
    public void setGreedyScore(double x){
        greedyScore = x;
    }

    public double getGreedyScore(){
        return greedyScore;
    }


    // Override toString() method to represent the vertex as a string
    @Override
    public String toString() {
        return "Vertex(id=" + id + ")";
    }
}


class Edge {
    private int startVertexId;
    private int endVertexId;
    private double weight;

    public Edge(int startVertexId, int endVertexId, double weight) {
        this.startVertexId = startVertexId;
        this.endVertexId = endVertexId;
        this.weight = weight;
    }

    // Getters for startVertexId, endVertexId, and weight

    public int getStartVertexId() {
        return startVertexId;
    }

    public int getEndVertexId() {
        return endVertexId;
    }

    public double getWeight() {
        return weight;
    }

    // Override toString() method to represent the edge as a string
    @Override
    public String toString() {
        return "Edge(startVertexId=" + startVertexId + ", endVertexId=" + endVertexId + ", weight=" + weight + ")";
    }
}

class Graph {
    private int numVertices;
    private int numEdges;
    private List<Edge> edges;
    private List<Vertex> vertices;

    public Graph(int numVertices, int numEdges) {
        this.numVertices = numVertices;
        this.numEdges = numEdges;
        this.edges = new ArrayList<>();
        this.vertices = new ArrayList<>();
    }

    // Method to add an edge to the graph
    public void addEdge(int startVertexId, int endVertexId, double weight) {
        Edge edge = new Edge(startVertexId, endVertexId, weight);
        edges.add(edge);
    }

    // Method to add a vertex to the graph
    public void addVertex(int id) {
        Vertex vertex = new Vertex(id);
        vertices.add(vertex);
    }

    // Getters for edges

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public Vertex getVertexById(int id){
        for(Vertex vertex: vertices){
            if(vertex.getId() == id){
                return vertex;
            }
        }
        return null;
    }

    // Method to calculate the total weight of the cut between two partitions
    public double calculateCutWeight(Set<Integer> partitionA, Set<Integer> partitionB) {
        double totalCutWeight = 0.0;
        for (Edge edge : edges) {
            if ((partitionA.contains(edge.getStartVertexId()) && partitionB.contains(edge.getEndVertexId()))
                    || (partitionA.contains(edge.getEndVertexId()) && partitionB.contains(edge.getStartVertexId()))) {
                totalCutWeight += edge.getWeight();
            }
        }
        return totalCutWeight;
    }

    // Override toString() method to represent the graph as a string
    @Override
    public String toString() {
        return "Graph(numVertices=" + numVertices + ", numEdges=" + numEdges + ", edges=" + edges + ")";
    }
}
