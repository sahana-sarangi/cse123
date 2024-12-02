import java.util.*;

//Sahana Sarangi
//14 November 2024

public class Client {
    private static final Random RAND = new Random();

    public static void main(String[] args) throws Exception {
        // List<Region> scenario = createRandomScenario(10, 10, 100, 1000, 100000);
        List<Region> scenario = createSimpleScenario();
        System.out.println(scenario);
        
        double budget = 2000;
        Allocation allocation = allocateRelief(budget, scenario);
        printResult(allocation, budget);
    }

    //This method calculates the best way to allocate money within the budget to different
    //regions with disaster. It picks the allocation of money that stays within the budget
    //and helps the most number of people. If two different allocations have the same amount of
    //people that can be help, it picks the one that is cheapest.
    //Return: the best allocation 
    //Parameters:
    //  - budget: the non-null maximum amount of money that can be spent on disaster relief in a
    //            scenario (double)
    //  - sites: a non-null list of Region objects that contains all the different regions that 
    //           need relief
    public static Allocation allocateRelief(double budget, List<Region> sites) {
        return allocateRelief(budget, new ArrayList<>(sites), new Allocation(), null, 0);
    }

    //This method calculates the best allocation of relief so far that stays within budget.
    //Return: the best allocation of relief so far
    //Parameters:
    //  - budget: the maximum amount of money that can be spent on disaster relief in a scenario
    //            (double)
    //  - sites: a list of Region objects that contains all the different regions that need relief
    //  - soFar: an Allocation representing the current allocation scenario that will be compared
    //           to the best allocation scenario, if there is one
    //  - bestAllocation: an Allocation representing the best possible allocation scenario that 
    //                    has been found so far, if there is one
    //  - allocIndex: the index/ordering of a Region if it were to be added to the soFar 
    //                allocation
    private static Allocation allocateRelief(double budget, List<Region> sites, Allocation soFar,
            Allocation bestAllocation, int allocIndex) {
        if (!hasAffordableLocation(budget, sites, allocIndex)) {
            if (bestAllocation == null || soFar.totalPeople() > bestAllocation.totalPeople() || 
                    (soFar.totalPeople() == bestAllocation.totalPeople() && 
                    soFar.totalCost() < bestAllocation.totalCost())) {
                    
                    bestAllocation = soFar;
            }
            return bestAllocation;
            
        } else {
            for (int i = 0; i < sites.size(); i++) {
                Region currRegion = sites.get(i);
                if (currRegion.getCost(allocIndex) <= budget) {
                    budget -= currRegion.getCost(allocIndex);
                    soFar = soFar.withRegion(currRegion);
                    sites.remove(i);

                    bestAllocation = allocateRelief(budget, sites, soFar, bestAllocation, 
                            allocIndex + 1);

                    budget += currRegion.getCost(allocIndex);
                    soFar = soFar.withoutRegion(currRegion);
                    sites.add(i, currRegion);
                }
            }
            return bestAllocation;
        }
    }

    //This method checks whether or not a list of Regions contains any disaster areas that 
    //require relief within the given budget. 
    //Return: if there is a disaster area that meets this criteria, it returns true. Otherwise,
    //it returns false.
    //Parameters:
    //  - budget: the maximum amount of money that can be spent on disaster relief in a scenario
    //            (double)
    //  - sites: a list of Region objects that contains all the different regions that need relief
    //  - allocIndex: the index/ordering of a Region if it were to be added to the soFar 
    //                allocation
    private static boolean hasAffordableLocation(double budget, List<Region> sites, 
            int allocIndex) {
        for (int i = 0; i < sites.size(); i++) {
            if (sites.get(i).getCost(allocIndex) <= budget) {
                return true;
            }
        }
        return false;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PROVIDED HELPER METHODS - **DO NOT MODIFY ANYTHING BELOW THIS LINE!** //
    ///////////////////////////////////////////////////////////////////////////
    
    /**
    * Prints each allocation in the provided set. Useful for getting a quick overview
    * of all allocations currently in the system.
    * @param allocations Set of allocations to print
    */
    public static void printAllocations(Set<Allocation> allocations) {
        System.out.println("All Allocations:");
        for (Allocation a : allocations) {
            System.out.println("  " + a);
        }
    }

    /**
    * Prints details about a specific allocation result, including the total people
    * helped, total cost, and any leftover budget. Handy for checking if we're
    * within budget limits!
    * @param alloc The allocation to print
    * @param budget The budget to compare against
    */
    public static void printResult(Allocation alloc, double budget) {
        System.out.println("Result: ");
        System.out.println("  " + alloc);
        System.out.println("  People helped: " + alloc.totalPeople());
        System.out.printf("  Cost: $%.2f\n", alloc.totalCost());
        System.out.printf("  Unused budget: $%.2f\n", (budget - alloc.totalCost()));
    }

    /**
    * Creates a scenario with numRegions regions by randomly choosing the population 
    * and cost of each region.
    * @param numLocs Number of regions to create
    * @param minPop Minimum population per region
    * @param maxPop Maximum population per region
    * @param minCostPer Minimum cost per person
    * @param maxCostPer Maximum cost per person
    * @return A list of randomly generated regions
    */
    public static List<Region> createRandomScenario(int numLocs, int minPop, int maxPop,
                                                    double minCostPer, double maxCostPer) {
        List<Region> result = new ArrayList<>();

        for (int i = 0; i < numLocs; i++) {
            int pop = RAND.nextInt(minPop, maxPop + 1);
            double cost = RAND.nextDouble(minCostPer, maxCostPer) * pop;
            result.add(new Region("Region #" + i, pop, round2(cost)));
        }

        return result;
    }

    /**
    * Manually creates a simple list of regions to represent a known scenario.
    * @return A simple list of regions
    */
    public static List<Region> createSimpleScenario() {
        List<Region> result = new ArrayList<>();

        result.add(new Region("Region #1", 50, 500));
        result.add(new Region("Region #2", 100, 700));
        result.add(new Region("Region #3", 60, 1000));
        result.add(new Region("Region #4", 20, 1000));
        result.add(new Region("Region #5", 200, 900));

        return result;
    }    

    /**
    * Rounds a number to two decimal places.
    * @param num The number to round
    * @return The number rounded to two decimal places
    */
    private static double round2(double num) {
        return Math.round(num * 100) / 100.0;
    }
}
