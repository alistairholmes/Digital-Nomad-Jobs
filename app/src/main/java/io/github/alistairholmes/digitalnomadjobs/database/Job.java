package io.github.alistairholmes.digitalnomadjobs.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

// A Job object contains information related to a single job.
@Entity (tableName = "Remote Jobs")
public class Job {

    //Variables names for the columns in the database table
    @PrimaryKey(autoGenerate = true)
    // id of job that links to site where the job was posted
    private int id;
    // Role of the job
    private String position;
    // Name of the company that posted job
    private String company;
    // Date that the job was posted
    private Date date;
    // Logo of company
    private String logo;
    // Description of Job
    private String description;



    /**
     * Constructs a new Job object.
     *
     * @param jobTitle is the title or role of the job
     * @param companyName is the name of the company
     * @param postDate is the date that the job was posted
     * @param companyLogo is the company logo
     */

    @Ignore
    public Job(String jobTitle, String companyName, Date postDate, String companyLogo) {
        position = jobTitle;
        company = companyName;
        date = postDate;
        logo = companyLogo;
    }

    public Job(int id, String jobTitle, String companyName, Date postDate, String companyLogo) {
        this.id = id;
        this.position = jobTitle;
        this.company = companyName;
        this.date = postDate;
        this.logo = companyLogo;
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

    // Returns the logo of the company
    public String getCompanyLogo() {
        return logo;
    }

    // Returns the description of the job
    public String getDescription() {
        return description;
    }

    // Returns the id of the job link
    public int getId() {
        return id;
    }
}
