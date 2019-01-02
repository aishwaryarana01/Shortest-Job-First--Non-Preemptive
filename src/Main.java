public class Main
{
    public static void main(String[] args) throws InterruptedException
    {
        new NonPreemptiveSJF().RunSimulation(); // For Running with default configuration for assignment

        //Customizable Simulation:
        // 1st Param = Total Simulation Ticks,
        // 2nd Param = OS Decision Period,
        // 3rd Param = Probability for process generation,
        // 4th Param = Probability for IO Event generation
        // 5th Param = flag for LOG output
        // 6th Param = flag for running Test Processes set
        // Examples
        // new NonPreemptiveSJF(1500, 20, 0.95, 0.95, false, false).RunSimulation();
        // For Running Test Process Set
        // new NonPreemptiveSJF(1500, 1, 0.95, 0.95, false, true).RunSimulation();
    }
}