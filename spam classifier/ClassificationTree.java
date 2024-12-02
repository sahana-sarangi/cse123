import java.util.*;
import java.io.*;

//Sahana Sarangi
//22 November 2024

//This class creates a new ClassificationTree object by extending the Classifier class. 
//ClassificationTree is a simple machine learning model that classifies data and returns its 
//predicted label for the data.
public class ClassificationTree extends Classifier {

    //This class creates a new instance of the ClassificationNode object. A ClassificationNode
    //can represent splits in a classification tree, or classifiable data and a label for the 
    //data. ClassificationNodes include left and right pointers to other ClassificationNodes.
    private static class ClassificationNode {
        public Classifiable data;
        public Split split;
        public String label;
        public ClassificationNode left;
        public ClassificationNode right;

        //This constructor creates a new ClassificationNode object with a given Split object.
        //Parameters:
        //  - split: a non-null Split object that contains the feature that data is being split 
        //           by and the threshold that data is compared against
        public ClassificationNode(Split split) {
            this.split = split;
        }

        //This constructor creates a new ClassificationNode object with a given label and the
        //data to associate with it.
        //Parameters:
        //  - label: the non-null classification label that the node represents (String)
        //  - data: non-null data to be classified by the classification tree (Classifiable 
        //          object)
        public ClassificationNode(String label, Classifiable data) {
            this.label = label;
            this.data = data;
        }

    }

    private ClassificationNode overallRoot;


    //This constructor creates a new classification tree by loading data from a file connected to
    //the given scanner. 
    //Parameters:
    //  - sc: a non-null Scanner object that scans over a file containing data to be loaded into
    //        the classification tree
    public ClassificationTree(Scanner sc) {
        overallRoot = createTree(sc);
    }


    //Creates a new classification tree recursively using the data from a file connected
    //to the given scanner object to create a nodes and their children where applicable.
    //Return: if the scanner has no data to scan over (or the file is empty), the method
    //returns null. If the file has data, the method returns the overallRoot of the created 
    //classification tree (a ClassificationNode).
    //Parameters:
    //  - sc: a non-null Scanner object that scans over a file containing data to be loaded into
    //        the classification tree
    private ClassificationNode createTree(Scanner sc) {
        if (!sc.hasNextLine()) {
            return null;
        } 
        String currLine = sc.nextLine();
        if (currLine.contains("Feature:")) {
            String feature = currLine.substring(9);
            double threshold = Double.parseDouble(sc.nextLine().substring(11));
            ClassificationNode node = new ClassificationNode(new Split(feature, threshold));
            node.left = createTree(sc);
            node.right = createTree(sc);
            return node;
        } else {
            return new ClassificationNode(currLine, null);
        }
    }
    

    //This constructor creates a new classification tree from scratch using two
    //lists--data and results--and processing them parallely.
    //Exceptions: if the data list is empty, the results list is empty, or if the size of the
    //data list and results list don't match, an IllegalArgumentException is thrown.
    //Parameters:
    //  - data: a non-null List of Classifiable objects that contains the data that the 
    //          classification tree will be trained on
    //  - results: a non-null List of Strings that contains the labels for each Classifiable
    //             object in the data list
    public ClassificationTree(List<Classifiable> data, List<String> results) {
        if (data.isEmpty() || results.isEmpty() || results.size() != data.size()) {
            throw new IllegalArgumentException("Data and labels provided are of different length"
                    + "or are empty");
        }
        for (int i = 0; i < data.size(); i++) {
            overallRoot = build(data.get(i), results.get(i), overallRoot);
        }
    }


    //This method finds a location in the classification tree to insert a certain data point 
    //and its associated label (as a ClassificationNode), and then inserts the datapoint and
    //label. It does this by traversing through the classification tree.
    //Returns: the overallRoot (ClassificationNode object) of an updated subtree of the 
    //classification tree.
    //Parameters:
    //  - inputData: the non-null data to add to the classification tree (Classifiable object)
    //  - label: the non-null label associated with the data point to add to the tree (String)
    //  - curr: the current node in the tree that the method is evaluating (ClassificationNode)
    private ClassificationNode build(Classifiable inputData, String newLabel, 
            ClassificationNode curr) {
        if (curr == null) {
            return new ClassificationNode(newLabel, inputData);
        } else if (curr.split == null && curr.label.equals(newLabel)) {
            return curr;
        } else if (curr.split == null) {
            Split split = curr.data.partition(inputData);
            ClassificationNode newNode = new ClassificationNode(split);
            if (split.evaluate(inputData)) {
                newNode.left = new ClassificationNode(newLabel, inputData);
                newNode.right = curr;
            } else {
                newNode.left = curr;
                newNode.right = new ClassificationNode(newLabel, inputData);
            }
            return newNode;
        } else if (curr.split.evaluate(inputData)) {
            curr.left = build(inputData, newLabel, curr.left);
            return curr;
        } else {
            curr.right = build(inputData, newLabel, curr.right);
            return curr;
        }
    }


    //This method returns whether or not the type of data contained in a given Classifable object
    //(inputData) can be classified using the classification tree.
    //Returns: if the type of data can be classified, the method returns true. Else, it returns 
    //false. If the classification tree is null, it returns false.
    //Parameters:
    //  - inputData: a non-null Classifiable object containing datapoints
    public boolean canClassify(Classifiable inputData) {
        return canClassify(inputData, overallRoot);
    }


    //This method determines whether or not the type of data in the given inputData can be
    //classified using the classification tree recursively by traversing through it.
    //Returns: if the type of data can be classified based on the current node the method is
    //evaluating, the method returns true. Else, it returns false. If the classification tree
    //is null, it returns false.
    //Parameters:
    //  - inputData: a non-null Classifiable object containing datapoints
    private boolean canClassify(Classifiable inputData, ClassificationNode curr) {
        if (curr == null) {
            return false;
        } else if (curr.split == null) {
            return true;
        } else if (!inputData.getFeatures().contains(curr.split.getFeature())) {
            return false;
        } else if (curr.split.evaluate(inputData)) {
            return canClassify(inputData, curr.left);
        } else {
            return canClassify(inputData, curr.right);
        }
    }


    //This method classifies the given Classifiable object (input)
    //Exceptions: if the input is not classifiable, an IllegalArgumentException is thrown
    //Returns: the learned label associated with the data (String)
    //Parameters:
    //  - inputData: a non-null Classifiable object containing datapoints
    public String classify(Classifiable inputData) {
        if (!canClassify(inputData)) {
            throw new IllegalArgumentException("Data provided is not classifiable.");
        }
        return classify(inputData, overallRoot);
    }

    //This method classifies the given Classifiable object (input) recursively by traversing
    //the classification tree.
    //Returns: the tree's predicted label for the input (String)
    //Parameters:
    //  - input: a non-null Classifiable object containing datapoints
    //  - curr: the current node in the tree that the method is evaluating (ClassifricationNode)
    private String classify(Classifiable inputData, ClassificationNode curr) {
        if (curr.split == null) {
            return curr.label;
        } else if (curr.split.evaluate(inputData)) {
            return classify(inputData, curr.left);
        } else {
            return classify(inputData, curr.right);
        }
    }

    //This method saves the ClassificationTree to a given PrintStream
    //Parameters:
    //  - ps: the non-null PrintStream to save the ClassificationTree to
    public void save(PrintStream ps) {
        save(ps, overallRoot);
    }

    //This method saves the classification tree to the given PrintStream recursively by 
    //traversing through the classification tree.
    //Parameters:
    //  - ps: the non-null PrintStream to save the classification tree to
    //  - curr: the current node in the tree that the method is evaluating (a ClassifricationNode)
    private void save(PrintStream ps, ClassificationNode curr) {
        if (curr != null) {
            if (curr.split == null) {
                ps.println(curr.label);
            } else {
                ps.println(curr.split.toString());
                save(ps, curr.left);
                save(ps, curr.right);
            }
        }
    }
}
