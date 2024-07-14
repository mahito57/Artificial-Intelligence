package DecisionTree;

import java.util.HashMap;
import java.util.Map;

public class TreeNode {
    private String value;
    private Map<String, TreeNode> children;

    public TreeNode(String value) {
        this.value = value;
        this.children = new HashMap<>();
    }

    public String getValue() {
        return value;
    }

    public void addChild(String attributeValue, TreeNode childNode) {
        children.put(attributeValue, childNode);
    }

    public TreeNode getChild(String attributeValue) {
        return children.get(attributeValue);
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public Map<String, TreeNode> getChildren(){
        return children;
    }
}
