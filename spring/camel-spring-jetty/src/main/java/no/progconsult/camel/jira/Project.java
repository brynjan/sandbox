package no.progconsult.camel.jira;

import java.io.Serializable;

public class Project implements Serializable {

    private String id;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }
}