package tech.ec2dlt.backend;

public class ApiResponse<T> {
    private boolean error;
    private T data;

    // Constructors, getters, and setters

    public ApiResponse(boolean error, T data) {
        this.error = error;
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
