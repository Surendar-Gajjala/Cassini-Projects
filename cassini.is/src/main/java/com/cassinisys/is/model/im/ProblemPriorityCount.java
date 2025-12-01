package com.cassinisys.is.model.im;

import java.io.Serializable;

/**
 * Created by swapna on 08/05/19.
 */
public class ProblemPriorityCount implements Serializable {

    private Integer high;

    private Integer medium;

    private Integer low;

    public Integer getHigh() {
        return high;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    public Integer getMedium() {
        return medium;
    }

    public void setMedium(Integer medium) {
        this.medium = medium;
    }

    public Integer getLow() {
        return low;
    }

    public void setLow(Integer low) {
        this.low = low;
    }
}
