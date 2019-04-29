package com.example.bopit;

import android.os.Handler;
import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GameActivityTests {
    private int successCount;
    private double nextMoveDelay;
    private int points;

    private void processSuccess() {
        if (successCount == 10) {
            nextMoveDelay -= 0.2;
            successCount = 0;
        } else {
            successCount++;
        }
        points++;
    }

    @Test
    public void processSuccessAt10Test() {
        successCount = 10;
        nextMoveDelay = 1.0;
        points = 0;
        processSuccess();
        assertEquals(0, successCount);
        assertEquals(0.8, nextMoveDelay, 0.0);
    }

    @Test
    public void processSuccessSub10Test() {
        successCount = 3;
        nextMoveDelay = 1.0;
        points = 0;
        processSuccess();
        assertEquals(4, successCount);
        assertEquals(1.0, nextMoveDelay, 0.0);
        assertEquals(1, points);
    }

}