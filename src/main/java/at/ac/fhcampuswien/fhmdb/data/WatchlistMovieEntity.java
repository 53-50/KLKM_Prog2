package at.ac.fhcampuswien.fhmdb.data;

import com.j256.ormlite.field.DatabaseField;

public class WatchlistMovieEntity {
    // (is a MovieEntity) - 1:1 Relation

   // @DatabaseField(generatedId = true)
    //private long id;

    @DatabaseField(id = true, columnName = "apiId")
    private String apiId;

    //@DatabaseField(foreign = true)
    //MovieEntity movie;  // 1:1 Relation?

    public WatchlistMovieEntity() {
        // ORMLite needs the empty constructor
    }

    public WatchlistMovieEntity(String apiId) {
        this.apiId = apiId;
    }

   /* public long getId() {
        return id;
    }
    */

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

}
