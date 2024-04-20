package team.bham.service.APIWrapper;

import org.springframework.http.HttpStatus;

public class SpotifyAPIResponse<T> {

    private boolean success;
    private HttpStatus status;
    private T data;

    public SpotifyAPIResponse() {}

    public SpotifyAPIResponse(Boolean success, HttpStatus status, T data) {
        this.success = success;
        this.status = status;
        this.data = data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public boolean getSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
