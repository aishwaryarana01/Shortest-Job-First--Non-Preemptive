import java.util.Random;

public class Job implements Comparable<Job>
{
    private double IO_EVENT_GENERATE_PROBABILITY = 0.95;
    private static int numOfJobsGenerated = 0;

    private int processJobID;
    private long arrivalTimestamp;
    private int jobLength;
    private int IOLength;
    private long latency;

    public Job(int currentTimeStamp, double IO_EVENT_GENERATE_PROBABILITY)
    {
        this.jobLength = getRandomNumber(10, 50);
        Job.numOfJobsGenerated++;
        this.processJobID = numOfJobsGenerated;
        this.arrivalTimestamp = currentTimeStamp;
        this.IO_EVENT_GENERATE_PROBABILITY = IO_EVENT_GENERATE_PROBABILITY;
    }

    public boolean hasIOEvent()
    {
        if(this.IOLength > 0)
        {
            return true;
        }
        else if(canGenerateIOEvent())
        {
            this.IOLength = getRandomNumber(10, 30);
            return true;
        }
        return false;
    }

    private boolean canGenerateIOEvent()
    {
        int rand_int = getRandomNumber(0, 100);

        if((rand_int / (double) 100) > IO_EVENT_GENERATE_PROBABILITY)
            return true;
        else
            return false;
    }

    public void decrementJobLength()
    {
        this.jobLength--;
    }

    public void decrementIOeventLength()
    {
        this.IOLength--;
    }

    @Override
    public String toString()
    {
        return "Job ID = " + this.processJobID + " Current Job Length = " + this.jobLength + " Current IO length = " + this.IOLength;
    }

    @Override
    public int compareTo(Job job)
    {
        if(job.jobLength == this.jobLength)
        {
            if(job.arrivalTimestamp > this.arrivalTimestamp)
                return -1;
            else
                return 1;
        }
        else if(job.jobLength > this.jobLength)
            return -1;
        else
            return 1;
    }

    // Arrival Time Getter Setter
    public void setArrivaltimestamp(long arrivalTimestamp)
    {
        this.arrivalTimestamp = arrivalTimestamp;
    }

    public long getArrivalTimestamp()
    {
        return arrivalTimestamp;
    }

    // Latency Getter Setter
    public void setLatency(long latency)
    {
        this.latency = latency;
    }

    public long getLatency()
    {
        return latency;
    }

    // Job Length Getter Setter
    public void setJobLength(int jobLength)
    {
        this.jobLength = jobLength;
    }

    public int remainingExecutionTime()
    {
        return this.jobLength;
    }

    // IO Length Getter Setter
    public void setIOLength(int IOLength)
    {
        this.IOLength = IOLength;
    }

    public int remainingIOTime()
    {
        return this.IOLength;
    }

    // Helper Functions
    private int getRandomNumber(int lowerLimit, int upperLimit)
    {
        Random rand = new Random(System.currentTimeMillis());
        return rand.nextInt(upperLimit - lowerLimit + 1) + lowerLimit;
    }
}