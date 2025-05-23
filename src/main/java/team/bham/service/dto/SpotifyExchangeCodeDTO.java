package team.bham.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpotifyExchangeCodeDTO {

    private String code;
    private String url;

    public SpotifyExchangeCodeDTO() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
