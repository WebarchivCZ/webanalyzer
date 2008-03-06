/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.webarchiv.webanalyzer.multithread.analyzer;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author praso
 */
public class PointsCounter {

    /* Points, that stores reached points for processed Url */
    private AtomicLong points;    
    
    
    /**
     * Public constructor, inicializes points to zero.
     */
    public PointsCounter() {
        this.points = new AtomicLong(0);
    }
    
    /**
     * Increments points with the given value.
     * @param value, that will be added to points
     * @return points reached for processed Url.
     */
    public long increment(int value) {
        return points.addAndGet((long) value);
    }
    
    /**
     * Decrements points with the given value.
     * @param value, that will be removed
     * @return points reached for processed Url.
     */
    public long decrement(long value) {
        return points.addAndGet(-value);
    }
    
    /**
     * Reached points.
     * @return points, that the processed Url reached.
     */
    public long getPoints() {
        return points.get();
    }
}
