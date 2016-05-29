package no.westerdals.shiale14.pikachucatcher.Net;

/**
 * Created by Alexander on 29.05.2016.
 *
 */
public class Response {

    private int statusCode;
    private String body;

    public Response(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }
}
