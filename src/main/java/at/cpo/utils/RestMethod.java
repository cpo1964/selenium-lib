package at.cpo.utils;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

public enum RestMethod {

    GET(new HttpGet()),
    PUT(new HttpPut()),
    POST(new HttpPost()),
    DELETE(new HttpPost());

    private HttpRequestBase method;

    RestMethod(HttpRequestBase method) {
        this.method = method;
    }

    public HttpRequestBase getMethod() {
        return method;
    }

    public void setMethod(HttpRequestBase method) {
        this.method = method;
    }

}
