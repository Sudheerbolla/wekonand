package com.weekend.server.ssl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class DataLoader {

	/**Used for GET request
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws URISyntaxException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 */
	public HttpResponse secureLoadData(String url)
			throws ClientProtocolException, IOException,
            NoSuchAlgorithmException, KeyManagementException,
            URISyntaxException, KeyStoreException, UnrecoverableKeyException {

		HttpGet get = new HttpGet(new URI(url));
		HttpResponse response = getClient().execute(get);

		return response;
	}

	/**Returns the httpClient for secure ssl
	 * 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws URISyntaxException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 */
	public HttpClient getClient() throws ClientProtocolException, IOException,
            NoSuchAlgorithmException, KeyManagementException,
            URISyntaxException, KeyStoreException, UnrecoverableKeyException {
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(null, new TrustManager[] { new CustomX509TrustManager() }, new SecureRandom());
		HttpClient client = new DefaultHttpClient();

		SSLSocketFactory ssf = new CustomSSLSocketFactory(ctx);
		ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		ClientConnectionManager ccm = client.getConnectionManager();
		SchemeRegistry sr = ccm.getSchemeRegistry();
		sr.register(new Scheme("https", ssf, 443));
		DefaultHttpClient sslClient = new DefaultHttpClient(ccm, client.getParams());
		return sslClient;
	}

	public static HttpClient createHttpClient(){
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);

		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

		return new DefaultHttpClient(conMgr, params);
	}

	// For testing reasons one can add a X509TrustManager to a HttpClient to avoid the SSLPeerUnverifiedException
	// References:
	// http://javaskeleton.blogspot.com/2010/07/avoiding-peer-not-authenticated-with.html
	// http://en.wikibooks.org/wiki/Programming:WebObjects/Web_Services/How_to_Trust_Any_SSL_Certificate
	// Made some customizations to these guides. Works with Java 7 (no NullPointerException when calling init() of SSLContext) and does not use any deprecated functions of HttpClient, SSLContext...

	/*public static DefaultHttpClient getSecuredHttpClient(HttpClient httpClient) throws Exception {
		final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return _AcceptedIssuers;
				}
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}
			};
			ctx.init(null, new TrustManager[] { tm }, new SecureRandom());
			SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = httpClient.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));
			return new DefaultHttpClient(ccm, httpClient.getParams());
		} catch (Exception e) {
			throw e;
		}
	}*/

	/********************************************************************/

	/**
	 * Not using this method anywhere just taken for reference for exchange login issue for NIF
	 * Exception was: javax.net.ssl.SSLPeerUnverifiedException: No peer certificate
	 * 
	 * Used code from: http://stackoverflow.com/questions/16719959/android-ssl-httpget-no-peer-certificate-error-or-connection-closed-by-peer-e
	 */
	/*private void connect(){
		try {
			DataLoader dl = new DataLoader();
			String url = "https://IpAddress";
			HttpResponse response = dl.secureLoadData(url); 

			StringBuilder sb = new StringBuilder();
			sb.append("HEADERS:\n\n");

			Header[] headers = response.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				Header h = headers[i];
				sb.append(h.getName()).append(":\t").append(h.getValue()).append("\n");
			}

			InputStream is = response.getEntity().getContent();
			StringBuilder out = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			for (String line = br.readLine(); line != null; line = br.readLine())
				out.append(line);
			br.close();

			sb.append("\n\nCONTENT:\n\n").append(out.toString()); 

			Log.i("response", sb.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

}