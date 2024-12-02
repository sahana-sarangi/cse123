import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class Testing {

    @Test
    @DisplayName("STUDENT TEST - Case #1")
    public void firstCaseTest() {
        List<Region> sites = new ArrayList<>();
        Region regionOne = new Region("Region 1", 100, 400);
        Region regionTwo = new Region("Region 2", 150, 600);
        sites.add(regionOne);
        sites.add(regionTwo);
        List<Region> expected = new ArrayList<>();
        expected.add(regionOne);
        assertTrue(Client.allocateRelief(500, sites).getRegions().equals(expected));
    }

    @Test
    @DisplayName("STUDENT TEST - Case #2")
    public void secondCaseTest() {
        List<Region> sites = new ArrayList<>();
        Region regionOne = new Region("Region 1", 150, 400);
        Region regionTwo = new Region("Region 2", 100, 450);
        sites.add(regionOne);
        sites.add(regionTwo);
        List<Region> expected = new ArrayList<>();
        expected.add(regionOne);
        assertTrue(Client.allocateRelief(500, sites).getRegions().equals(expected));
    }

    @Test
    @DisplayName("STUDENT TEST - Case #3")
    public void thirdCaseTest() {
        List<Region> sites = new ArrayList<>();
        Region regionOne = new Region("Region 1", 150, 450);
        Region regionTwo = new Region("Region 2", 150, 400);
        sites.add(regionOne);
        sites.add(regionTwo);
        List<Region> expected = new ArrayList<>();
        expected.add(regionTwo);
        assertTrue(Client.allocateRelief(500, sites).getRegions().equals(expected));
    }

    @Test
    @DisplayName("STUDENT TEST - DIY")
    public void diyTest() {
        List<Region> sites = new ArrayList<>();
        Region regionOne = new Region("Region 1", 200, 600);
        Region regionTwo = new Region("Region 2", 250, 600);
        sites.add(regionOne);
        sites.add(regionTwo);
        List<Region> expected = new ArrayList<>();
        assertTrue(Client.allocateRelief(500, sites).getRegions().equals(expected));
    }
}
