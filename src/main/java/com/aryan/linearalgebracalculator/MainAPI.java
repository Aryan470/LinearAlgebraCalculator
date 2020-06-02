package com.aryan.linearalgebracalculator;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import io.javalin.Javalin;

public class MainAPI {
	private static HashMap<String, Parser> sessions = new HashMap<String, Parser>();

    private static final SecureRandom randomGen = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder();
    public static final int SESSION_TIMEOUT = 3600; // Seconds of inactivity before sessions die

	public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
        }).start(getHerokuAssignedPort());

        scheduleCleanser();
        
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
                // e.printStackTrace();
                ctx.result("Bad input: " + e.getMessage());
                ctx.status(422);
            }
		});
    }

    private static int getHerokuAssignedPort() {
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            return Integer.parseInt(herokuPort);
        } else {
            return 7000;
        }
    }

    private static void scheduleCleanser() {
        Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                clearExpired();
            }
        }, 0, 300 * 1000);
    }
    
    private static void clearExpired() {
        int cleared = 0;
        Instant ageLimit = Instant.now().minusSeconds(SESSION_TIMEOUT);
        for (String session : sessions.keySet()) {
            Instant age = sessions.get(session).getLastUsed();
            if (age.isBefore(ageLimit)) {
                sessions.remove(session);
                cleared += 1;
            }
        }
        System.out.println("Cleared " + cleared + " inactive sessions");
    }

    private static String makeToken() {
        byte[] randBytes = new byte[24];
        randomGen.nextBytes(randBytes);
        return encoder.encodeToString(randBytes);
    }
}