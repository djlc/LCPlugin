package com.github.djlc;

import java.util.HashMap;

public class UserCommandManager {
	// String  : UserName
	// Boolean : mode
	private static HashMap<String, Boolean> userList;

	public static Boolean isRunning(String userName) {
		return (userList.get(userName));
	}

	public static void toggle(String userName) {
		if (userList.containsKey(userName)) {
			userList.remove(userName);
		} else {
			userList.put(userName, true);
		}
	}
}
