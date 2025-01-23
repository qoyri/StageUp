package com.example.stageup.data.model;

import java.util.List;

public class StageResponse<T> {
    private String $id;
    private List<T> $values;

    public String get$id() {
        return $id;
    }

    public List<T> get$values() {
        return $values;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public void set$values(List<T> $values) {
        this.$values = $values;
    }
}
