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
     * @param position is the title or role of the job
     * @param company is the name of the company
     * @param date is the date that the job was posted
     * @param logo is the company logo
     */

    @Ignore
    public Job(String position, String company, Date date, String logo, String description) {
        this.position = position;
        this.company = company;
        this.date = date;
        this.logo = logo;
        this.description = description;
    }

    public Job(int id, String position, String company, Date date, String logo, String description) {
        this.id = id;
        this.position = position;
        this.company = company;
        this.date = date;
        this.logo = logo;
        this.description = description;
    }

    // Returns the title of the job
    public String getPosition() {
        return position;
    }

    // Returns the name of the company
    public String getCompany() {
        return company;
    }

    // Returns the date that the job was posted
    public Date getDate() {
        return date;
    }

    // Returns the logo of the company
    public String getLogo() {
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
