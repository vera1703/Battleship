package org.example.apigateway.dto;

public class PlayerDTO {

    private String id;
    private String name;

    public PlayerDTO() {
    }

    public PlayerDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter & Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
