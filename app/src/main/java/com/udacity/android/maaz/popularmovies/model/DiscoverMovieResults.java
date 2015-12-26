
package com.udacity.android.maaz.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DiscoverMovieResults {

    @SerializedName("page")
    @Expose
    private Long page;
    @SerializedName("results")
    @Expose
    private List<MovieData> results = new ArrayList<MovieData>();
    @SerializedName("total_results")
    @Expose
    private Long totalResults;
    @SerializedName("total_pages")
    @Expose
    private Long totalPages;

    /**
     * 
     * @return
     *     The page
     */
    public Long getPage() {
        return page;
    }

    /**
     * 
     * @param page
     *     The page
     */
    public void setPage(Long page) {
        this.page = page;
    }

    /**
     * 
     * @return
     *     The results
     */
    public List<MovieData> getResults() {
        return results;
    }

    /**
     * 
     * @param results
     *     The results
     */
    public void setResults(List<MovieData> results) {
        this.results = results;
    }

    /**
     * 
     * @return
     *     The totalResults
     */
    public Long getTotalResults() {
        return totalResults;
    }

    /**
     * 
     * @param totalResults
     *     The total_results
     */
    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

    /**
     * 
     * @return
     *     The totalPages
     */
    public Long getTotalPages() {
        return totalPages;
    }

    /**
     * 
     * @param totalPages
     *     The total_pages
     */
    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

}
