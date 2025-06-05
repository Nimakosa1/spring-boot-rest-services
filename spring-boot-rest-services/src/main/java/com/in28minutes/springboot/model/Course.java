package com.in28minutes.springboot.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "description", "steps" })
public record Course(String id,
                     String name,
                     String description,
                     List<String> steps) {

}