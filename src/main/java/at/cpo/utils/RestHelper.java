package at.cpo.utils;

import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class RestHelper.
 */
public class RestHelper {

    /**
     * The Constant Logger of the class.
     */
    public static final Logger log = Logger.getLogger(RestHelper.class.getSimpleName());

    /**
     * The Constant CONTENT.
     */
    public static final String CONTENT = "content";

    /** The httpclient. */
    private static CloseableHttpClient httpclient;
    
    /** The http response. */
    private static CloseableHttpResponse httpResponse;

    static {
        if (httpclient == null) {
            try {
                // ein client ohne proxy und ohne definierten CookieStore wird der BasicCookieStore verwendet
                httpclient = buildHttpClient(null, null);
            } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
                httpclient = null;
            }
        }
    }

    /**
     * Instantiates a new rest helper.
     */
    private RestHelper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Builds the http client.
     *
     * @param proxy  the proxy
     * @param store  the store
     * @param useSSL the use ssl
     * @return the closeable http client
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws KeyStoreException        the key store exception
     * @throws KeyManagementException   the key management exception
     */
    public static CloseableHttpClient buildHttpClient(final HttpHost proxy, final CookieStore store, boolean useSSL)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        CookieStore cookieStore = store;
        if (cookieStore == null) {
            cookieStore = new BasicCookieStore();
        }
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        sslContextBuilder.loadTrustMaterial(null, (chain, authType) -> true);
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        CloseableHttpClient httpclient;
        HttpClientBuilder httpCB = HttpClientBuilder.create()
                .disableRedirectHandling()
                .setDefaultCookieStore(cookieStore)
                .setDefaultRequestConfig(requestConfig);
        if (useSSL) {
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build(),
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());

            httpCB.setSSLSocketFactory(sslSocketFactory);
        }
        if (proxy != null) {
            httpclient = httpCB.setProxy(proxy).build();
        } else {
            httpclient = httpCB.build();
        }
        return httpclient;
    }

    /**
     * Builds the http client.
     *
     * @param proxy the proxy
     * @param store the store
     * @return the closeable http client
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws KeyStoreException        the key store exception
     * @throws KeyManagementException   the key management exception
     */
    public static CloseableHttpClient buildHttpClient(final HttpHost proxy, final CookieStore store)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return buildHttpClient(proxy, store, true);
    }

    /**
     * Overload von doHttpPost, verwendet einen kompletten String für den Request.
     *
     * @param uri     Die URI gegen welche der Request abgesetzt werden soll.
     * @param content Der zu sendende Content.
     * @return Den Statuscode des Responses.
     * @throws IOException the io exception
     */
    public static CloseableHttpResponse doHttpPost(String uri, String content) throws IOException {
        return doHttpPost(uri, content, null);
    }

    /**
     * Overload von doHttpPost, verwendet einen kompletten String für den Request
     * ermöglicht es, zusätzlich Header-Informationen mitzugeben.
     *
     * @param uri     Die URI gegen welche der Request abgesetzt werden soll.
     * @param content Der zu sendende Content.
     * @param headers the headers
     * @return Den Statuscode des Responses.
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static CloseableHttpResponse doHttpPost(String uri, String content, List<NameValuePair> headers) throws IOException {
        return doHttpPost(uri, ContentType.APPLICATION_JSON, content, headers);
    }

    /**
     * Overload von doHttpPost, verwendet einen kompletten String für den Request
     * ermöglicht es, zusätzlich Header-Informationen mitzugeben.
     *
     * @param uri         Die URI gegen welche der Request abgesetzt werden soll.
     * @param contentType the content type
     * @param content     Der zu sendende Content.
     * @param headers     the headers
     * @return Den Statuscode des Responses.
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static CloseableHttpResponse doHttpPost(String uri, ContentType contentType, String content, List<NameValuePair> headers) throws IOException {
        return doHttpPost(httpclient, uri, contentType, content, headers);
    }

    /**
     * Overload von doHttpPost, verwendet einen kompletten String für den Request
     * ermöglicht es, zusätzlich Header-Informationen mitzugeben.
     *
     * @param client      the client
     * @param uri         Die URI gegen welche der Request abgesetzt werden soll.
     * @param contentType the content type
     * @param content     Der zu sendende Content.
     * @param headers     the headers
     * @return Den Statuscode des Responses.
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static CloseableHttpResponse doHttpPost(CloseableHttpClient client, String uri, ContentType contentType, String content, List<NameValuePair> headers) throws IOException {
        HttpPost httpMethod = new HttpPost(uri);
        if (contentType == null) {
            httpMethod.setEntity(new ByteArrayEntity(content.getBytes(StandardCharsets.UTF_8), ContentType.APPLICATION_JSON));
        } else {
            httpMethod.setEntity(new ByteArrayEntity(content.getBytes(StandardCharsets.UTF_8), contentType));
        }
        if (headers != null) {
            for (NameValuePair header : headers) {
                httpMethod.addHeader(header.getName(), header.getValue());
            }
        }
        return client.execute(httpMethod);
    }

    /**
     * Führt ein HTTP-GET durch.
     *
     * @param uri Die URL auf welche der GET-Request abgesetzt werden soll.
     * @return Den Statuscode des Responses.
     * @throws IOException the io exception
     */
    public static int doHttpGet(String uri) throws IOException {
        HttpRequestBase httpMethod = new HttpGet(uri);
        try {
            httpResponse = httpclient.execute(httpMethod);
            return httpResponse.getStatusLine().getStatusCode();
        } finally {
            httpResponse.close();
        }
    }

    /**
     * Overload von doHttpGet, ermöglicht es, zusätzlich Header-Informationen mitzugeben.
     *
     * @param client  the client
     * @param uri     Die URL auf welche der GET-Request abgesetzt werden soll.
     * @param headers Die Header-Informationen als NameValue-Pairs.
     * @return Den Statuscode des Responses.
     * @throws IOException the io exception
     */
    public static CloseableHttpResponse doHttpGet(CloseableHttpClient client, String uri, List<NameValuePair> headers) throws IOException {
        HttpRequestBase httpMethod = new HttpGet(uri);
        for (NameValuePair header : headers) {
            httpMethod.addHeader(header.getName(), header.getValue());
        }
        return client.execute(httpMethod);
    }

    /**
     * Do http get.
     *
     * @param uri     the uri
     * @param headers the headers
     * @return the closeable http response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static CloseableHttpResponse doHttpGet(String uri, List<NameValuePair> headers) throws IOException {
        return doHttpGet(httpclient, uri, headers);
    }

    /**
     * Overload von doHttpGet, nimmt einen URIBuilder entgegen.
     *
     * @param uribuilder Der URI-Builder für den Request.
     * @return Den Statuscode des Responses.
     * @throws IOException the io exception
     */
    public static int doHttpGet(URIBuilder uribuilder) throws IOException {
        try {
            HttpRequestBase httpMethod = new HttpGet(uribuilder.build());
            httpResponse = httpclient.execute(httpMethod);
            return httpResponse.getStatusLine().getStatusCode();
        } catch (IOException | URISyntaxException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Setzt einen Http-Post Request gegen eine bestimmte URI mit bestimmten Parametern ab.
     *
     * @param uri        Die URI gegen welche der Request abgesetzt werden soll.
     * @param parameters Die Parameter für den Request.
     * @return Den Statuscode des Responses.
     * @throws IOException the io exception
     */
    public static int doHttpPost(String uri, List<NameValuePair> parameters) throws IOException {
        HttpPost httpMethod = new HttpPost(uri);
        httpMethod.setEntity(new UrlEncodedFormEntity(parameters));
        httpResponse = httpclient.execute(httpMethod);
        return httpResponse.getStatusLine().getStatusCode();
    }

    /**
     * Führt ein HTTP-Put durch.
     *
     * @param uri Die URL auf welche der PUT-Request abgesetzt werden soll.
     * @return Den Statuscode des Responses.
     * @throws IOException the io exception
     */
    public static int doHttpPut(String uri) throws IOException {
        HttpRequestBase httpMethod = new HttpPut(uri);
        httpResponse = httpclient.execute(httpMethod);
        return httpResponse.getStatusLine().getStatusCode();
    }

    /**
     * Overload von doHttpPut, ermöglicht es, zusätzlich Header-Informationen mitzugeben.
     *
     * @param client  the client
     * @param uri     Die URL auf welche der PUT-Request abgesetzt werden soll.
     * @param headers Die Header-Informationen als NameValue-Pairs.
     * @return Den Statuscode des Responses.
     * @throws IOException the io exception
     */
    public static CloseableHttpResponse doHttpPut(CloseableHttpClient client, String uri, List<NameValuePair> headers) throws IOException {
        HttpRequestBase httpMethod = new HttpPut(uri);
        for (NameValuePair header : headers) {
            httpMethod.addHeader(header.getName(), header.getValue());
        }
        return client.execute(httpMethod);
    }

    /**
     * Overload von HttpDelete, ermöglicht es, zusätzlich Header-Informationen mitzugeben.
     *
     * @param client  the client
     * @param uri     Die URL auf welche der DELETE-Request abgesetzt werden soll.
     * @param headers Die Header-Informationen als NameValue-Pairs.
     * @return Den Statuscode des Responses.
     * @throws IOException the io exception
     */
    public static CloseableHttpResponse doHttpDelete(CloseableHttpClient client, String uri, List<NameValuePair> headers) throws IOException {
        HttpRequestBase httpMethod = new HttpDelete(uri);
        for (NameValuePair header : headers) {
            httpMethod.addHeader(header.getName(), header.getValue());
        }
        return client.execute(httpMethod);
    }

    /**
     * Do http put closeable http response.
     *
     * @param uri     the uri
     * @param headers the headers
     * @return the closeable http response
     * @throws IOException the io exception
     */
    public static CloseableHttpResponse doHttpPut(String uri, List<NameValuePair> headers) throws IOException {
        return doHttpPut(httpclient, uri, headers);
    }


    /**
     * Call rest method.
     *
     * @param client     the client
     * @param methodType the method type
     * @param uri        the uri
     * @return the closeable http response
     * @throws IOException            Signals that an I/O exception has occurred.
     * @throws URISyntaxException     the URI syntax exception
     */
    public static int callRestMethod(final CloseableHttpClient client, RestMethod methodType, final String uri)
            throws IOException, URISyntaxException {
        URI url = new URI(uri);
        HttpRequestBase method = methodType.getMethod();
        method.setURI(url);
        return getResponseCode(client, method);
    }

    /**
     * Call rest method.
     *
     * @param client      the client
     * @param methodType  the method type
     * @param uri         the uri
     * @param contentType the content type
     * @param content     the content
     * @param headers     the headers
     * @return the int
     * @throws IOException            Signals that an I/O exception has occurred.
     */
    public static CloseableHttpResponse callRestMethod(CloseableHttpClient client, RestMethod methodType, String uri,
                                                       ContentType contentType, String content, List<NameValuePair> headers)
            throws IOException {
        switch (methodType.name()) {
            case "GET":
                return doHttpGet(client, uri, headers);
            case "PUT":
                return doHttpPut(client, uri, headers);
            case "POST":
                return doHttpPost(client, uri, contentType, content, headers);
            case "DELETE":
                return doHttpDelete(client, uri, headers);
            default:
                return null;
        }
    }

    /**
     * Call rest method.
     *
     * @param methodType the method type
     * @param uri        the uri
     * @return the int
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static int callRestMethod(RestMethod methodType, final String uri) throws IOException {
        int responseCode;
        try {
            CloseableHttpClient client = RestHelper.buildHttpClient(null, new BasicCookieStore());
            responseCode = callRestMethod(client, methodType, uri);
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | URISyntaxException | IOException e) {
            throw new IOException(e);
        }
        return responseCode;
    }

    /**
     * Call rest method.
     *
     * @param httpclient the httpclient
     * @param method     the method
     * @return the closeable http response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static CloseableHttpResponse callRestMethod(final CloseableHttpClient httpclient, final HttpRequestBase method) throws IOException {
        log.info("### REQUEST: " + method.getMethod());
        // log request
        log.info("request content: " + getRequestContent(method));
        Header[] allHeaders = method.getAllHeaders();
        for (Header header : allHeaders) {
            log.info("request Header: " + header);
        }

        CloseableHttpResponse response = httpclient.execute(method);
        String responseContent = getResponseContent(response);
        System.setProperty(CONTENT, responseContent);

        allHeaders = response.getAllHeaders();
        // log response
        log.info("### RESPONSE: ");
        for (Header header : allHeaders) {
            log.info("response Header: " + header);
        }
        log.info("response content: " + responseContent);
        return response;
    }

    /**
     * Gets the request content.
     *
     * @param request the request
     * @return the request content
     */
    public static String getRequestContent(final HttpRequestBase request) {

        if(request!=null){
            // only POST requests have body data
            if(request.getMethod().equals("POST")){
                HttpEntity entity = ((HttpPost) request).getEntity();
                return getEntityContent(entity);
            } else {
                return request.toString();
            }
        } else {
            return "";
        }

    }

    /**
     * Gets the response content.
     *
     * @param response the response
     * @return the response content
     */
    public static String getResponseContent(final HttpResponse response) {
        return getEntityContent(response.getEntity());
    }

    /**
     * Gets the entity content.
     *
     * @param entity the entity
     * @return the entity content
     */
    private static String getEntityContent(HttpEntity entity) {
        StringBuilder content = new StringBuilder();
        if (entity == null) {
            return "";
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()))) {
            String line;
            while (((line = reader.readLine()) != null)) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException ex) {
            log.info("getResponseContent failed with message: " + ex.getMessage());
        }
        return content.toString();
    }

    /**
     * Gets the response code of rest call.
     *
     * @param httpclient the httpclient
     * @param method     the method
     * @return the response code of rest call
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static int getResponseCode(final CloseableHttpClient httpclient,final HttpRequestBase method) throws IOException {
        CloseableHttpResponse response = null;
        int statusCode = -1;
        try {
            response = callRestMethod(httpclient, method);
            statusCode = response.getStatusLine().getStatusCode();
        } catch (IOException ex) {
            log.info("getResponseCode failed with message: " + ex.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return statusCode;
    }
}
