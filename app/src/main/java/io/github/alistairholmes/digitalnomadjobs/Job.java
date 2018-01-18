package io.github.alistairholmes.digitalnomadjobs;

import java.util.Date;

// A Job object contains information related to a single job.
public class Job {

    // Role of the job
    private String mJobTitle;

    // Name of the company that posted job
    private String mCompanyName;

    // Date that the job was posted
    private String mDatePosted;

    // Website where the job was posted
    private String mUrl;

    /**
     * Constructs a new Job object.
     *
     * @param jobTitle is the title or role of the job
     * @param companyName is the name of the company
     * @param date is the date that the job was posted
     * @param url is the website URL to find more details about the job
     */

    public Job(String jobTitle, String companyName, String date) {
        mJobTitle = jobTitle;
        mCompanyName = companyName;
        mDatePosted = date;
        //mUrl = url;
    }

    // Returns the title of the job
    public String getJobTitle() {
        return mJobTitle;
    }

    // Returns the name of the company
    public String getCompanyName() {
        return mCompanyName;
    }

    // Returns the date that the job was posted
    public String getDatePosted() {
        return mDatePosted;
    }

    // Returns the website URL to find more information about the job

    public String getUrl() {
        return mUrl;
    }
}
