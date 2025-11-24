package service;

public interface ChatCallback {
    void onSuccess(String response);
    void onError(String error);
}
