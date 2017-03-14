package com.example.himanshuluthra.testinguber;

import android.location.Location;

/**
 * Created by himanshuluthra on 09/03/17.
 */

public interface LocationCallback {
    public void locationResult(boolean result, Location location);
}
