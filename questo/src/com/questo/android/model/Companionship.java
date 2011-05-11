package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Companionship {

    public static final String UUID = "UUID";

    public static final String INITIATOR_UUID = "INITIATOR_UUID";

    public static final String CONFIRMER_UUID = "CONFIRMER_UUID";

    public static final String CONFIRMED = "CONFIRMED";

    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField(columnName = UUID)
    private String uuid;
    @DatabaseField(columnName = INITIATOR_UUID)
    private String initiator;
    @DatabaseField(columnName = CONFIRMER_UUID)
    private String confirmer;
    @DatabaseField(columnName = CONFIRMED)
    private boolean confirmed;
    @DatabaseField
    private Date requestedAt;
    @DatabaseField
    private Date confirmedAt;

    /**
     * No-args Constructor only for ORM-access, always use parameterized
     * constructors.
     */
    public Companionship() {
    }

    public Companionship(String uuid, String initiator, String confirmer, Date requestedAt) {
        super();
        this.uuid = uuid;
        this.initiator = initiator;
        this.confirmer = confirmer;
        this.requestedAt = requestedAt;
    }

    public String getUuid() {
        return uuid;
    }

    public Integer getId() {
        return id;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Date getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(Date confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public String getInitiator() {
        return initiator;
    }

    public String getConfirmer() {
        return confirmer;
    }

    public Date getRequestedAt() {
        return requestedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!this.getClass().equals(o.getClass()))
            return false;
        Companionship other = (Companionship) o;
        return other.id.equals(this.id);
    }

}