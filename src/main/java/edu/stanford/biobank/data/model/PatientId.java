package edu.stanford.biobank.data.model;

import org.springframework.lang.NonNull;

public class PatientId {

    @NonNull
    private String id;
    @NonNull
    private String id_source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_source() {
        return id_source;
    }

    public void setId_source(String id_source) {
        this.id_source = id_source;
    }
}
