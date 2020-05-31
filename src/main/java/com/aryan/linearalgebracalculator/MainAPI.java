package com.aryan.linearalgebracalculator;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import io.javalin.Javalin;

public class MainAPI {
	private static HashMap<String, Parser> sessions = new HashMap<String, Parser>();

    private static final SecureRandom randomGen = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder();
	
	public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        
        app.before(ctx -> {
            if (ctx.cookieStore("token") == null) {
                String token = makeToken();
                ctx.cookieStore("token", token);
                sessions.put(token, new Parser());
            }
        });

        app.get("/", ctx -> ctx.result("Hello World"));
        
		app.post("/query", ctx -> {
            String token = ctx.cookieStore("token");
            Parser thisSession = sessions.get(token);
            String expression = ctx.formParam("expression", "0");
            
            try {
                LinearAlgebraObject result = thisSession.parse(expression);
                ctx.result(result.toString());
                ctx.status(200);
            } catch (Exception e) {
                e.printStackTrace();
                ctx.result("Bad input: " + e.getMessage());
                ctx.status(422);
            }
		});
	}

    private static String makeToken() {
        byte[] randBytes = new byte[24];
        randomGen.nextBytes(randBytes);
        return encoder.encodeToString(randBytes);
    }
}