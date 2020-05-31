package com.aryan.linearalgebracalculator;

import java.util.HashMap;
import io.javalin.Javalin;

public class MainAPI {
	private static HashMap<String, Parser> sessions = new HashMap<String, Parser>();
	private static Parser global = new Parser();
	
	public static void main(String[] args) {
		Javalin app = Javalin.create().start(7000);
		app.get("/", ctx -> ctx.result("Hello World"));
		app.post("/query", ctx -> {
			String expression = ctx.formParam("expression", "0");
			LinearAlgebraObject result = global.parse(expression);
			ctx.result(result.toString());
		});
	}
}