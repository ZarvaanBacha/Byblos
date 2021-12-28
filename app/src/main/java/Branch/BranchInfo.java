package Branch;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class BranchInfo {
    protected List<String> serviceList;
    protected List<String> daysOpen;
    protected List<Integer> timings;
    protected String location;
    protected int userRating;
    protected String branchUID;
    protected String managerUID;

    public BranchInfo()
    {
        this.serviceList = new ArrayList<>();
        this.daysOpen = new ArrayList<>();
        this.timings = new ArrayList<>();
        this.location = null;
        this.userRating = 5;
        this.managerUID = null;
        this.branchUID = null;
    }

    // Getters
    public List<String> getServiceList()
    {
        return this.serviceList;
    }

    public List<String> getDaysOpen()
    {
        return this.daysOpen;
    }

    public List<Integer> getTimings()
    {
        return this.timings;
    }

    public String getLocation()
    {
        return this.location;
    }

    public int getUserRating()
    {
        return this.userRating;
    }

    public String getBranchUID()
    {
        return branchUID;
    }

    public String getManagerUID()
    {
        return managerUID;
    }

    // Setters
    public void setTimings(List<Integer> timings)
    {
        this.timings = timings;
    }

    public void setDaysOpen(List<String> daysOpen) {
        this.daysOpen = daysOpen;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUserRating(int input)
    {
        this.userRating = (this.userRating+input)/2;
    }

    public void setBranchUID(String input)
    {
        this.branchUID = input;
    }

    public void setManagerUID(String input)
    {
        this.managerUID = input;
    }


    // Add to service list
    public void addService(String serviceUID)
    {
        this.serviceList.add(serviceUID);
    }

    // Remove from service list
    public List<String> removeService(String serviceUID)
    {
        // Find entry in list
        for(int i = 0; i < this.serviceList.size(); i++)
        {
            if (serviceList.get(i).equals(serviceUID))
            {
                serviceList.remove(i);
            }
        }

        // return modified list
        return this.serviceList;
    }

    @NonNull
    @Override

    public String toString()
    {
        return "CarForm {" +
                "Service UIDs'" + getServiceList() + '\'' +
                ", Days Open'" + getDaysOpen() + '\'' +
                ", Get Timings'" + getTimings() + '\'' +
                ", Location'" + getLocation() + '\'' +
                ", User Rating='" + getUserRating() + '\'' +
                ", Branch UID='" + getBranchUID() + '\'' +
                ", Manager UID='" + getManagerUID() + '\'' +
                '}';
    }

}
