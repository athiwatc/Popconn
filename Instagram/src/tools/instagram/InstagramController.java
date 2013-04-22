package tools.instagram;

import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;

import android.util.Log;

/**
 * Instagram authentication controller
 * 
 * @author Gauche Guyz
 * @version 1.0
 */
public class InstagramController {
	private Token accessToken = null;
	private InstagramService service;
	private final Token EMPTY_TOKEN = null;
	private String authorizationUrl;
	private String redirectUrl;
	private static InstagramController INSTANCE;
	private Instagram instagram;
	
	private InstagramController(){
		this.redirectUrl = "http://google.com";
		initInstagramAuth();
	}
	
	public static InstagramController getInstance(){
		if(INSTANCE == null)
			INSTANCE = new InstagramController();
		return INSTANCE;
	}
	
	private void initInstagramAuth(){
		this.service = new InstagramAuthService()
        .apiKey("f80b9378f0834d1e84b6efd613bdec5d")
        .apiSecret("f9ddb8fa40c6446089bccdc439b9f75e")
        .callback(redirectUrl) //redirect url    
        .build();
		this.authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
	}
	
	public String getAuthorizationUrl(){
		return authorizationUrl;
	}
	
	public String getRedirectUrl(){
		return redirectUrl;
	}
	

	private Token verifyToken(String token){
		Verifier verifier = new Verifier(token);
    	Token result = service.getAccessToken(EMPTY_TOKEN, verifier);
    	return result;
	}
	
	/**
	 * 
	 * @param token - token code, string form that can retrieve from instagram redirect URL
	 */
	public void setAccessToken(String token){
		Token v = verifyToken(token);
		this.accessToken = v;
	}

	
	/**
	 * Generate token code from instagram redirect URL
	 * @param code - redirect url with instagram's token 
	 */
	public String generateToken(String code){
		int start = code.indexOf("code=") + 5;
		int end = code.length();
		return code.substring(start, end);
	}
	
	public Instagram getInstagram(){
		if(instagram == null)
			instagram = new Instagram(accessToken);
		return instagram;
	}
}
