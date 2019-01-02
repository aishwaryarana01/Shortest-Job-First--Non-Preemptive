import java.util.PriorityQueue;
import java.util.Random;

public class NonPreemptiveSJF
{
    private int OS_DECISION_TIMESLICE = 20;
    private double PROCESS_GENERATE_PROBABILITY = 0.95;
    private double IO_EVENT_GENERATE_PROBABILITY = 0.95;
    private int SIMULATION_TIME = 1500;

    private boolean FOR_TEST = false;
    private boolean OUTPUT_LOG = false;

    private static int num_of_completed_Jobs = 0;
    private static Job currentJob = null;
    private static long total_latency_for_served_Jobs;

    public NonPreemptiveSJF()
    {
    }

    public NonPreemptiveSJF(int SIMULATION_TIME, int OS_DECISION_TIMESLICE, double PROCESS_GENERATE_PROBABILITY, double IO_EVENT_GENERATE_PROBABILITY, boolean OUTPUT_LOG, boolean FOR_TEST)
    {
        this.SIMULATION_TIME = SIMULATION_TIME;
        this.OS_DECISION_TIMESLICE = OS_DECISION_TIMESLICE;
        this.PROCESS_GENERATE_PROBABILITY = PROCESS_GENERATE_PROBABILITY;
        this.IO_EVENT_GENERATE_PROBABILITY = IO_EVENT_GENERATE_PROBABILITY;
        this.OUTPUT_LOG = OUTPUT_LOG;
        this.FOR_TEST = FOR_TEST;
    }

    private void Test(int i, PriorityQueue<Job> pQ)
    {
        if (i == 0)
        {
            Job j1 = new Job(i, IO_EVENT_GENERATE_PROBABILITY);
            j1.setArrivaltimestamp(0);
            j1.setJobLength(7);
            pQ.add(j1);

            if (OUTPUT_LOG)
                System.out.println("CREATED: " + j1.toString());
        }
        else if(i == 2)
        {
            Job j2 = new Job(i, IO_EVENT_GENERATE_PROBABILITY);
            j2.setArrivaltimestamp(2);
            j2.setJobLength(4);
            pQ.add(j2);

            if (OUTPUT_LOG)
                System.out.println("CREATED: " + j2.toString());
        }
        else if(i == 4)
        {
            Job j3 = new Job(i, IO_EVENT_GENERATE_PROBABILITY);
            j3.setArrivaltimestamp(4);
            j3.setJobLength(1);
            pQ.add(j3);

            if (OUTPUT_LOG)
                System.out.println("CREATED: " + j3.toString());
        }
        else if (i == 5)
        {
            Job j4 = new Job(i, IO_EVENT_GENERATE_PROBABILITY);
            j4.setArrivaltimestamp(5);
            j4.setJobLength(4);
            pQ.add(j4);

            if (OUTPUT_LOG)
                System.out.println("CREATED: " + j4.toString());
        }
    }

    public void RunSimulation() throws InterruptedException
    {
        PriorityQueue<Job> pQ = new PriorityQueue<Job>();

        for(int CurrentTimeStamp = 0; CurrentTimeStamp < SIMULATION_TIME; CurrentTimeStamp++)
        {
            Thread.sleep(1);

            if(FOR_TEST)
            {
                Test(CurrentTimeStamp, pQ);
            }
            else
            {
                if(canGenerateProcess())
                {
                    Job obj = new Job(CurrentTimeStamp, IO_EVENT_GENERATE_PROBABILITY);
                    pQ.add(obj);

                    if (OUTPUT_LOG)
                        System.out.println("CREATED: " + obj.toString());
                }
            }

            if(CurrentTimeStamp % OS_DECISION_TIMESLICE == 0 && (currentJob == null || currentJob.remainingExecutionTime() <= 0))
            {
                if(pQ.size() > 0)
                {
                    currentJob = pQ.remove();
                    currentJob.setLatency(CurrentTimeStamp - currentJob.getArrivalTimestamp());
                    total_latency_for_served_Jobs += currentJob.getLatency();
                }
            }

            if(currentJob != null && currentJob.remainingExecutionTime() > 0)
            {
                if(currentJob.hasIOEvent())
                {
                    if(currentJob.remainingIOTime() > 0)
                    {
                        currentJob.decrementIOeventLength();

                        if (OUTPUT_LOG)
                            System.out.println("PERFORMING I/O: " + currentJob.toString());
                    }
                }
                else
                {
                    currentJob.decrementJobLength();

                    if (OUTPUT_LOG)
                        System.out.println("EXECUTING: " + currentJob.toString());

                    if(currentJob.remainingExecutionTime() == 0)
                    {
                        if (OUTPUT_LOG)
                            System.out.println(" > COMPLETED: " + currentJob.toString());

                        num_of_completed_Jobs++;
                    }
                }
            }
        }

        System.out.println("Total Jobs: " + getTotalJobs(pQ, currentJob));
        System.out.println("Number of Completed Jobs = " + num_of_completed_Jobs);
        System.out.println("Throughput = " + getThroughput());
        System.out.println("Latency = " + (getAverageLatency()==-1?"No Completed Jobs":getAverageLatency()));
    }

    private int getTotalJobs(PriorityQueue<Job> pQ, Job currentJob)
    {
        return num_of_completed_Jobs + pQ.size() + (currentJob.remainingExecutionTime() > 0 ? 1 : 0);
    }

    private double getThroughput()
    {
        return ((double) num_of_completed_Jobs / SIMULATION_TIME);
    }

    private static double getAverageLatency()
    {
        return num_of_completed_Jobs > 0 ?((double) total_latency_for_served_Jobs / num_of_completed_Jobs) : -1;
    }

    private boolean canGenerateProcess()
    {
        Random rand = new Random(System.currentTimeMillis());
        int rand_int1 = rand.nextInt(101);

        if(rand_int1 / (double) 100 > PROCESS_GENERATE_PROBABILITY)
            return true;
        else
            return false;
    }
}