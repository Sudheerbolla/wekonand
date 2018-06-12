package com.weekend.gcm;

import java.io.Serializable;

/**
 * Created by hb on 23/9/16.
 */

public class PushNotificationModel implements Serializable {
    public String orderId = "";
    public String propertyId = "";
    public String token = "";
    public String userId = "";
    public String propertyTypeId = "";
}
