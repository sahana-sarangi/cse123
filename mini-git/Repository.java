import java.util.*;
import java.text.SimpleDateFormat;

//This class represents a repository--a chronoloical assembly of commits (edits made to a 
//document). Commits are arranged so that the most recent ones appear first.
public class Repository {
    private String name;
    private Commit node;
      
    //This constructor initializes a new Repository object with a provided string to name the
    //repository.
    //Exception: if the name the user provides is empty or null, an IllegalArgumentException is
    //thrown
    //Parameters:
    //  - name: a non-null string that is the name of the repository
    public Repository(String name) {
        if (name == "" || name == null) {
            throw new IllegalArgumentException("Name is empty or null.");
        }
        this.name = name;
        this.node = null;
    }

    //This method returns the ID of the most recent commit in the repository. If 
    //there are no commits, it returns null.
    public String getRepoHead() {
        if (node == null) {
            return null;
        }
        return node.id;
    }

    //This method returns the number of commits in the repository.   
    public int getRepoSize() {
        int counter = 0;
        Commit currNode = node;
        while (currNode != null) {
            counter++;
            currNode = currNode.past;
        }
        return counter;
    }

    //Behavior: This methode returns the string representation of an instance of Repository. 
    //Return: If there are no commits in the repository, it returns the name of the repository,
    //as well as the fact that there are no commits. Otherwise, it returns the name of the
    //repository and the string representation of the last commit in the repository.
    public String toString() {
        if (node == null) {
            return name + " - No commits";
        }
        return (name + " - Current head: " + node.toString());
    }

    //Behavior: checks if a commit in the repository has the same ID as the ID the user 
    //provides.
    //Return: if there is a commit with the same ID as the provided one, the method
    //returns true. Otherwise, it returns false.
    //Parameters:
    //  - targetId: the non-null ID (string) the user wants to check if the repository contains.
    public boolean contains(String targetId) {
        Commit currNode = node;
        while (currNode != null) {
            if (currNode.id.equals(targetId)) {
                return true;
            }
            currNode = currNode.past;
        }
        return false;
    }

    //This method returns the the first n commits in the repository in backwards 
    //chronological order as a string. If there are no commits in the repository, it returns
    //an empty string.
    //Exception: if n (the number of commits the user wants to see) is less than 1,
    //an IllegalArgumentException is thrown.
    //Parameters:
    //  - n: the non-null, non-negative, and nonzero number of commits the user wants to see from
    //       the history (integer)
    public String getHistory(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Invalid number of commits (must be at least 1.)");
        }
            
        String hist = "";
        Commit currNode = node;
        while (currNode != null && n > 0) {
            hist += currNode.toString() + "\n";
            currNode = currNode.past;
            n--;
        }
        return hist;
    }

    //Behavior: this method adds a new commit to the repository with a provided message.
    //Return: the ID of the newly created commit
    //Parameter:
    //  - message: the non-null message (string) the created commit contains
    public String commit(String message) {
        Commit temp = node;
        node = new Commit(message, temp);
        return node.id;
    }

    //Behavior: this method drops (removes) the commit in the repository whose ID matches the
    //user's given target ID, if there is one.
    //Return: if a commit's ID matches the target ID and is removed from the repository, it 
    //returns true. If there is no commit whose ID matches the target ID, it returns false.
    //Parameters:
    //  - targetId: the non-null ID (string) of the commit the user wants removed from the 
    //              repository
    public boolean drop(String targetId) {
        if (this.node != null && this.node.id.equalsIgnoreCase(targetId)) {
            this.node = this.node.past;
            return true;
        } 
        Commit currNode = this.node;
        while (currNode != null && currNode.past != null) {
            if (currNode.past.id.equalsIgnoreCase(targetId)) {
                currNode.past = currNode.past.past;
                return true;
            }
            currNode = currNode.past;
        }
        return false;
    }
    
    //Behavior: this method synchronizes the current repository with another repository by 
    //moving all the commits in the other repository to the current one and organizing them
    //so that all commits appear in chronological order (most recent commits first). After 
    //synchronization, the other repository is empty. 
    //Parameters:
    //  - other: the non-null repository to synchronize the current repository with
    public void synchronize(Repository other) {
        if (this.node == null) {
            this.node = other.node;
            other.node = null;
        } 
        
        if (this.node != null && other.node != null) {
            if (this.node.timeStamp <= other.node.timeStamp) {
                Commit tempNode = other.node;
                other.node = other.node.past;
                tempNode.past = this.node;
                this.node = tempNode;
            }
    
            Commit currNode = this.node;
            while (currNode.past != null && other.node != null) {
                if (currNode.past.timeStamp <= other.node.timeStamp) {
                    Commit tempNode = other.node;
                    other.node = other.node.past;
                    tempNode.past = currNode.past;
                    currNode.past = tempNode;
                }
                currNode = currNode.past;
            }
            if (other.node != null) {
                currNode.past = other.node;
                other.node = null;
            }
        }
    }
              
        
        

    /**
     * DO NOT MODIFY
     * A class that represents a single commit in the repository.
     * Commits are characterized by an identifier, a commit message,
     * and the time that the commit was made. A commit also stores
     * a reference to the immediately previous commit if it exists.
     *
     * Staff Note: You may notice that the comments in this 
     * class openly mention the fields of the class. This is fine 
     * because the fields of the Commit class are public. In general, 
     * be careful about revealing implementation details!
     */
     public static class Commit {

        private static int currentCommitID;

        /**
         * The time, in milliseconds, at which this commit was created.
         */
        public final long timeStamp;

        /**
         * A unique identifier for this commit.
         */
        public final String id;

        /**
         * A message describing the changes made in this commit.
         */
        public final String message;

        /**
         * A reference to the previous commit, if it exists. Otherwise, null.
         */
        public Commit past;

        /**
         * Constructs a commit object. The unique identifier and timestamp
         * are automatically generated.
         * @param message A message describing the changes made in this commit. Should be non-null.
         * @param past A reference to the commit made immediately before this
         *             commit.
         */
        public Commit(String message, Commit past) {
            this.id = "" + currentCommitID++;
            this.message = message;
            this.timeStamp = System.currentTimeMillis();
            this.past = past;
        }

        /**
         * Constructs a commit object with no previous commit. The unique
         * identifier and timestamp are automatically generated.
         * @param message A message describing the changes made in this commit. Should be non-null.
         */
        public Commit(String message) {
            this(message, null);
        }

        /**
         * Returns a string representation of this commit. The string
         * representation consists of this commit's unique identifier,
         * timestamp, and message, in the following form:
         *      "[identifier] at [timestamp]: [message]"
         * @return The string representation of this collection.
         */
        @Override
        public String toString() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(timeStamp);

            return id + " at " + formatter.format(date) + ": " + message;
        }

        /**
        * Resets the IDs of the commit nodes such that they reset to 0.
        * Primarily for testing purposes.
        */
        public static void resetIds() {
            Commit.currentCommitID = 0;
        }
    }
}
