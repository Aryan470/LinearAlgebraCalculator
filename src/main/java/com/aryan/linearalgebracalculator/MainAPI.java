package com.aryan.linearalgebracalculator;

import java.util.HashMap;
import io.javalin.Javalin;

public class MainAPI {
	private static HashMap<String, Parser> sessions = new HashMap<String, Parser>();
	private static Parser global = new Parser();

    private static final SecureRandom randomGen = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder();
	
	public static void main(String[] args) {
        sessions.put("test", global);
		Javalin app = Javalin.create().start(7000);
		app.get("/", ctx -> ctx.result("Hello World"));
        app.get("/makeSession", ctx -> {
            String newToken = makeToken();
            sessions.put(newToken, new Parser());
            ctx.cookie("token", newToken);
            ctx.status(200);
        });
		app.post("/query", ctx -> {
			String expression = ctx.formParam("expression", "0");
			LinearAlgebraObject result = global.parse(expression);
			ctx.result(result.toString());
		});
	}

    private static String makeToken() {
        byte[] randBytes = new byte[24];
        randomGen.nextBytes(randBytes);
        return encoder.encodeToString(randBytes);
    }
}