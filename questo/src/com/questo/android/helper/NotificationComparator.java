package com.questo.android.helper;

import java.util.Comparator;
import java.util.Date;

import com.questo.android.model.Notification;

public class NotificationComparator implements Comparator<Notification> {

    @Override
    public int compare(Notification n1, Notification n2) {
        Date d1 = n1.getCreatedAt();
        Date d2 = n2.getCreatedAt();
        
        //hack to reverse the sorting order... newest is first
        return d2.compareTo(d1);
    }

}
