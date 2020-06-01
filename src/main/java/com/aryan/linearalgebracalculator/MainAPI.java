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
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
        }).start(7000);
        
        // Guarantee a valid token for session
        app.before(ctx -> {
            // Could be null or invalid/expired, we need to fix this
            String token = ctx.cookieStore("token");
            if (token == null || !sessions.containsKey(token)) {
                // This is an invalid token, we must replace with a fresh one
                token = makeToken();
                ctx.cookieStore("token", token);
                sessions.put(token, new Parser());
            }
        });

        app.get("/", ctx -> {
            
        });
        
		app.post("/query", ctx -> {
            String token = ctx.cookieStore("token");
            Parser thisSession = sessions.get(token);
            String expression = ctx.formParam("expression", "0");
            System.out.println(token + ": " + expression);
            try {
                LinearAlgebraObject result = thisSession.parse(expression);
                ctx.json(result);
                ctx.status(200);
            } catch (Exception e) {
                System.out.println("Error!");
                System.out.println("Token: " + token);
                System.out.println("Session: " + thisSession);
                System.out.println("Expression: " + expression);
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