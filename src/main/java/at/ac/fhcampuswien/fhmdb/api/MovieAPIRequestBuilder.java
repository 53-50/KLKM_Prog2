package at.ac.fhcampuswien.fhmdb.api;

public class MovieAPIRequestBuilder {
    private final StringBuilder url;
    private boolean hasParams = false;

    public MovieAPIRequestBuilder(String baseUrl) {
        this.url = new StringBuilder(baseUrl);
    }

    public MovieAPIRequestBuilder query(String query) {
        return addParam("query", query);
    }

    public MovieAPIRequestBuilder genre(String genre) {
        return addParam("genre", genre);
    }

    public MovieAPIRequestBuilder releaseYear(String year) {
        return addParam("releaseYear", year);
    }

    public MovieAPIRequestBuilder ratingFrom(String rating) {
        return addParam("ratingFrom", rating);
    }

    private MovieAPIRequestBuilder addParam(String key, String value) {
        if (value == null || value.isBlank()) return this;

        if (!hasParams) {
            url.append("?");
            hasParams = true;
        } else {
            url.append("&");
        }
        url.append(key).append("=").append(value);
        return this;
    }

    public String build() {
        return url.toString();
    }
}
