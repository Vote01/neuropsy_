package com.kursach.neuropsy;

public class Glossary {

    private int term_id;
    private String term_name;
    private String description;
    private String tags;

    public Glossary(int id, String title, String content,  String tags) {
        this.term_id = id;
        this.term_name = title;
        this.description = content;
        this.tags = tags;
    }

    public int getId() {
        return term_id;
    }

    public void setId(int id) {
        this.term_id = id;
    }

    public String getTerm_name() {
        return term_name;
    }

    public void setTerm_name(String term_name) {
        this.term_name = term_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }


}
