package team.bham.service.APIWrapper;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;

public class SpotifyAPIResponse {

    private boolean success;
    private HttpStatus status;
    private JSONObject data;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public boolean getSuccess() {
        return success;
    }

    public JSONObject getData() {
        return data;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
