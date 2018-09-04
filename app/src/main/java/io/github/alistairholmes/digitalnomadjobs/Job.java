package io.github.alistairholmes.digitalnomadjobs;

import java.util.Date;

// A Job object contains information related to a single job.
public class Job {

    // Role of the job
    private String position;

    // Name of the company that posted job
    private String company;

    // Date that the job was posted
    private Date date;

    // Website where the job was posted
    private String url;

    // Logo of company
    private String logo;


    /**
     * Constructs a new Job object.
     *
     * @param jobTitle is the title or role of the job
     * @param companyName is the name of the company
     * @param postDate is the date that the job was posted
     * @param logo is the company logo
     */

    public Job(String jobTitle, String companyName, Date postDate, String companyLogo) {
        position = jobTitle;
        company = companyName;
        date = postDate;
        logo = companyLogo;
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
    public Date getDatePosted() {
        return date;
    }

    // Returns the website URL to find more information about the job
    public String getUrl() {
        return url;
    }

    // Returns the logo of the company
    public String getCompanyLogo() {
        return logo;
    }

}
