package cn.edu.bnu.cms.common;

/**
 * Created by dave on 16/2/13.
 * @param <T>
 */
public class JsonMessage<T> {
    private boolean result;
    private String message = "";
    private String error = "";
    private T data;

    public JsonMessage(boolean result) {
        this.result = result;
    }

    public JsonMessage(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public JsonMessage(boolean result, String error, String message) {
        this(result, message);
        this.error = error;
    }

    public JsonMessage(boolean result, String error, String message, T data) {
        this.result = result;
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
