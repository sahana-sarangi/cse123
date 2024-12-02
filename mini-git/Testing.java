import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class Testing {
    private Repository repo1;
    private Repository repo2;

    // Occurs before each of the individual test cases
    // (creates new repos and resets commit ids)
    @BeforeEach
    public void setUp() {
        repo1 = new Repository("repo1");
        repo2 = new Repository("repo2");
        Repository.Commit.resetIds();
    }

    // TODO: Write your tests here!
    @Test
    public void testSynchronizeFrontCase() throws InterruptedException {
        String[] commitMessages = new String[]{"First commit", "Second commit", "Third commit",
                "Fourth commit"};

        for (int i = 0; i < commitMessages.length; i++) {
            repo1.commit(commitMessages[i]);
            Thread.sleep(1);
        }

        repo1.synchronize(repo2);

        assertTrue(repo1.toString().contains("Fourth commit"));
        assertEquals(repo2.getRepoSize(), 0);
        assertEquals(repo1.getRepoSize(), 4);
    }

    @Test
    public void testSynchronizeEndCase() throws InterruptedException {
        String[] thisCommitMessages = new String[]{"this First commit", "this Second commit", 
                "this Third commit", "this Fourth commit"};
        String[] otherCommitMessages = new String[]{"other First commit", "other Second commit",
                "other Third commit", "other Fourth commit", "other Fifth commit"};
        
        for (int i = 0; i < thisCommitMessages.length; i++) {
            repo1.commit(thisCommitMessages[i]);
            Thread.sleep(1);
        }
        for (int i = 0; i < otherCommitMessages.length; i++) {
            repo2.commit(otherCommitMessages[i]);
            Thread.sleep(1);
        }

        repo1.synchronize(repo2);

        assertTrue(repo1.toString().contains("other Fifth commit"));
        assertEquals(repo2.getRepoSize(), 0);
        assertEquals(repo1.getRepoSize(), 9);
    
    }

    @Test
    public void testSynchronizeMiddleCase() throws InterruptedException {
        String[] thisCommitMessages = new String[]{"this First commit", "this Second commit", 
                "this Third commit", "this Fourth commit"};
        String[] otherCommitMessages = new String[]{"other First commit", "other Second commit",
                "other Third commit", "other Fourth commit"};
        
        for (int i = 0; i < otherCommitMessages.length; i++) {
            repo2.commit(otherCommitMessages[i]);
            Thread.sleep(1);
        }

        for (int i = 0; i < thisCommitMessages.length; i++) {
            repo1.commit(thisCommitMessages[i]);
            Thread.sleep(1);
        }

        repo1.synchronize(repo2);
        assertTrue(repo1.toString().contains("this Fourth commit"));
        assertEquals(repo2.getRepoSize(), 0);
        assertEquals(repo1.getRepoSize(), 8);
    }


    /////////////////////////////////////////////////////////////////////////////////
    // PROVIDED HELPER METHODS (You don't have to use these if you don't want to!) //
    /////////////////////////////////////////////////////////////////////////////////

    // Commits all of the provided messages into the provided repo, making sure timestamps
    // are correctly sequential (no ties). If used, make sure to include
    //      'throws InterruptedException'
    // much like we do with 'throws FileNotFoundException'. Example useage:
    //
    // repo1:
    //      head -> null
    // To commit the messages "one", "two", "three", "four"
    //      commitAll(repo1, new String[]{"one", "two", "three", "four"})
    // This results in the following after picture
    // repo1:
    //      head -> "four" -> "three" -> "two" -> "one" -> null
    //
    // YOU DO NOT NEED TO UNDERSTAND HOW THIS METHOD WORKS TO USE IT! (this is why documentation
    // is important!)
    public void commitAll(Repository repo, String[] messages) throws InterruptedException {
        // Commit all of the provided messages
        for (String message : messages) {
            int size = repo.getRepoSize();
            repo.commit(message);
            
            // Make sure exactly one commit was added to the repo
            assertEquals(size + 1, repo.getRepoSize(),
                         String.format("Size not correctly updated after commiting message [%s]",
                                       message));

            // Sleep to guarantee that all commits have different time stamps
            Thread.sleep(2);
        }
    }

    // Makes sure the given repositories history is correct up to 'n' commits, checking against
    // all commits made in order. Example useage:
    //
    // repo1:
    //      head -> "four" -> "three" -> "two" -> "one" -> null
    //      (Commits made in the order ["one", "two", "three", "four"])
    // To test the getHistory() method up to n=3 commits this can be done with:
    //      testHistory(repo1, 3, new String[]{"one", "two", "three", "four"})
    // Similarly, to test getHistory() up to n=4 commits you'd use:
    //      testHistory(repo1, 4, new String[]{"one", "two", "three", "four"})
    //
    // YOU DO NOT NEED TO UNDERSTAND HOW THIS METHOD WORKS TO USE IT! (this is why documentation
    // is important!)
    public void testHistory(Repository repo, int n, String[] allCommits) {
        int totalCommits = repo.getRepoSize();
        assertTrue(n <= totalCommits,
                   String.format("Provided n [%d] too big. Only [%d] commits",
                                 n, totalCommits));
        
        String[] nCommits = repo.getHistory(n).split("\n");
        
        assertTrue(nCommits.length <= n,
                   String.format("getHistory(n) returned more than n [%d] commits", n));
        assertTrue(nCommits.length <= allCommits.length,
                   String.format("Not enough expected commits to check against. " +
                                 "Expected at least [%d]. Actual [%d]",
                                 n, allCommits.length));
        
        for (int i = 0; i < n; i++) {
            String commit = nCommits[i];

            // Old commit messages/ids are on the left and the more recent commit messages/ids are
            // on the right so need to traverse from right to left
            int backwardsIndex = totalCommits - 1 - i;
            String commitMessage = allCommits[backwardsIndex];

            assertTrue(commit.contains(commitMessage),
                       String.format("Commit [%s] doesn't contain expected message [%s]",
                                     commit, commitMessage));
            assertTrue(commit.contains("" + backwardsIndex),
                       String.format("Commit [%s] doesn't contain expected id [%d]",
                                     commit, backwardsIndex));
        }
    }
}
