package io.github.alistairholmes.digitalnomadjobs;

// A Job object contains information related to a single job.
public class Job {

    // Role of the job
    private String position;

    // Name of the company that posted job
    private String company;

    // Date that the job was posted
    private String date;

    // Website where the job was posted
    private String url;


    /**
     * Constructs a new Job object.
     *
     * @param jobTitle is the title or role of the job
     * @param companyName is the name of the company
     * @param postDate is the date that the job was posted
     *  //applyUrl is the website URL to find more details about the job
     */

    public Job(String jobTitle, String companyName, String postDate) {
        position = jobTitle;
        company = companyName;
        date = date;
        //url = applyUrl;

    }

    // Returns the title of the job
    public String getJobTitle() {
        return position;
    }

    // Returns the name of the company
    public String getCompanyName() {
        return company;
    }

    // Returns the date that the job was posted
    public String getDatePosted() {
        return date;
    }

    // Returns the website URL to find more information about the job
    public String getUrl() {
        return url;
    }

}
