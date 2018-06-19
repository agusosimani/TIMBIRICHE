package Model;

import java.io.FileWriter;
import java.io.IOException;

public class DotBuilder {
    private String tree;
    private int id;
    private int startingPlayer;

    public DotBuilder() {
        tree = "digraph {\n";
        id = 0;
    }

    void restart(int player) {
        tree = "digraph {\n";
        id = 0;
        startingPlayer = player;
    }

    void addEdge(Turn father, Turn child) {
        addNode(father);
        addNode(child);
        tree += father.getId() + " -> " + child.getId() + "\n";
    }

    private void addNode(Turn node) {
        if (node.getId() == 0)
            node.setId(++id);
    }

    void changeColor(Turn node) {
        addNode(node);
        tree += node.getId() + " [color= red , style = filled ]\n";
    }

    void setLabel(Turn node) {
        addNode(node);
        if (node.isFirst())
            tree += node.getId() + " [label = \"START " + node.getPlayer() +"\" shape = " + getShape(node) + ", color = red, style = filled ]\n";
        else
            tree += getOutput(node);
    }

    private String getOutput(Turn node) {
        String out = node.getId() + " [label = \"" + getLabel(node) + "\" , shape = " + getShape(node);
        if (node.isPruned()) {
            out += ", color = grey, style = filled";
        }
        out += "]\n";
        return out;
    }

    private String getLabel(Turn node) {
        String label = node.toString();
        if (!node.isPruned())
            label += " " + node.getValue();
        return label;
    }

    private String getShape(Turn node) {
        return node.getPlayer() != startingPlayer ? "rectangle" : "ellipse";
    }

    public void generateDotFile() {
        FileWriter output;
        tree = tree.concat("}");
        try {
            output = new FileWriter("tree.dot");
            output.write(tree);
            output.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
