package io.github.alistairholmes.digitalnomadjobs.data.local.entity;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "favorite_jobs")
public class FavoriteJob implements Parcelable {

    @PrimaryKey
    public int id;
    private String position;
    private String company;
    private String company_logo;
    private Date date;
    private String logo;
    private String description;
    private boolean favorite;

    @Ignore
    public FavoriteJob() {
    }

    /**
     * Constructs a new Job object.
     *
     * @param position is the title or role of the job
     * @param company  is the name of the company
     * @param date     is the date that the job was posted
     * @param logo     is the company logo
     */
    @Ignore
    public FavoriteJob(String position, String company, String company_logo, Date date,
                       String logo, String description) {
        this.position = position;
        this.company = company;
        this.company_logo = company_logo;
        this.date = date;
        this.logo = logo;
        this.description = description;
    }

    public FavoriteJob(int id, String position, String company, String company_logo, Date date,
                       String logo, String description, boolean favorite) {
        this.id = id;
        this.position = position;
        this.company = company;
        this.company_logo = company_logo;
        this.date = date;
        this.logo = logo;
        this.description = description;
        this.favorite = favorite;
    }

    public static FavoriteJob fromContentValues(ContentValues values) {
        final FavoriteJob favoriteJob = new FavoriteJob();
        if (values.containsKey("id")) {
            favoriteJob.id = values.getAsInteger("id");
        }
        if (values.containsKey("position")) {
            favoriteJob.position = values.getAsString("position");
        }
        if (values.containsKey("company")) {
            favoriteJob.company = values.getAsString("company");
        }
        if (values.containsKey("company_logo")) {
            favoriteJob.company_logo = values.getAsString("company_logo");
        }
        if (values.containsKey("date")) {
            favoriteJob.date = new Date(values.getAsLong("date"));
        }
        if (values.containsKey("logo")) {
            favoriteJob.logo = values.getAsString("logo");
        }
        if (values.containsKey("description")) {
            favoriteJob.description = values.getAsString("description");
        }
        if (values.containsKey("favorite")) {
            favoriteJob.favorite = values.getAsBoolean("favorite");
        }
        return favoriteJob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(position);
        parcel.writeString(company);
        parcel.writeString(company_logo);
        parcel.writeLong(date != null ? date.getTime() : -1);
        parcel.writeString(logo);
        parcel.writeString(description);
        parcel.writeByte(favorite ? (byte) 1 : (byte) 0);
    }

    protected FavoriteJob(Parcel in) {
        this.id = in.readInt();
        this.position = in.readString();
        this.company = in.readString();
        this.company_logo = in.readString();
        this.date = new Date(in.readLong());
        this.logo = in.readString();
        this.description = in.readString();
        this.favorite = in.readByte() != 0;
    }

    public static final Creator<FavoriteJob> CREATOR = new Creator<FavoriteJob>() {
        @Override
        public FavoriteJob createFromParcel(Parcel in) {
            return new FavoriteJob(in);
        }

        @Override
        public FavoriteJob[] newArray(int size) {
            return new FavoriteJob[size];
        }
    };

}
