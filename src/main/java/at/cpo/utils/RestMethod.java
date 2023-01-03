package at.cpo.utils;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

// TODO: Auto-generated Javadoc
/**
 * The Enum RestMethod.
 */
public enum RestMethod {

    /** The get. */
    GET(new HttpGet()),
    
    /** The put. */
    PUT(new HttpPut()),
    
    /** The post. */
    POST(new HttpPost()),
    
    /** The delete. */
    DELETE(new HttpPost());

    /** The method. */
    private HttpRequestBase method;

    /**
     * Instantiates a new rest method.
     *
     * @param method the method
     */
    RestMethod(HttpRequestBase method) {
        this.method = method;
    }

    /**
     * Gets the method.
     *
     * @return the method
     */
    public HttpRequestBase getMethod() {
        return method;
    }

    /**
     * Sets the method.
     *
     * @param method the new method
     */
    public void setMethod(HttpRequestBase method) {
        this.method = method;
    }

}
