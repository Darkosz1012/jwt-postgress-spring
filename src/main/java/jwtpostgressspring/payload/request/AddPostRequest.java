package jwtpostgressspring.payload.request;

import javax.validation.constraints.NotBlank;

public class AddPostRequest {

    @NotBlank
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String username) {
        this.content = content;
    }
}
