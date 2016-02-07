package com.udacity.android.maaz.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReviewResults {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("page")
    @Expose
    private Long page;
    @SerializedName("results")
    @Expose
    private List<Review> results = new ArrayList<Review>();
    @SerializedName("total_pages")
    @Expose
    private Long totalPages;
    @SerializedName("total_results")
    @Expose
    private Long totalResults;

    /**
     *
     * @return
     * The id
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The page
     */
    public Long getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(Long page) {
        this.page = page;
    }

    /**
     *
     * @return
     * The results
     */
    public List<Review> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<Review> results) {
        this.results = results;
    }

    /**
     *
     * @return
     * The totalPages
     */
    public Long getTotalPages() {
        return totalPages;
    }

    /**
     *
     * @param totalPages
     * The total_pages
     */
    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    /**
     *
     * @return
     * The totalResults
     */
    public Long getTotalResults() {
        return totalResults;
    }

    /**
     *
     * @param totalResults
     * The total_results
     */
    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

}