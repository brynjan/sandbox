package no.progconsult.camel.jira;

import java.io.Serializable;

public class Issue implements Serializable {

    public Fields fields;

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }
}