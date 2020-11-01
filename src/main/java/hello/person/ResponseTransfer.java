package hello.person;

public class ResponseTransfer {
    private String response;

    ResponseTransfer(String hash) {
        this.response = hash;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
