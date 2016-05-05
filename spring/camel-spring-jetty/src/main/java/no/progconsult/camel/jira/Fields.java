package no.progconsult.camel.jira;

import java.io.Serializable;

public class Fields implements Serializable {

    public Project project;

    public String summary;

    public String description;

    public Issuetype issuetype;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Issuetype getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(Issuetype issuetype) {
        this.issuetype = issuetype;
    }
}