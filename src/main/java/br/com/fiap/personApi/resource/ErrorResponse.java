package br.com.fiap.personApi.resource;

public class ErrorResponse {

    private String messageError;

    public ErrorResponse(String statusText) {
        this.messageError = statusText;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }
}
