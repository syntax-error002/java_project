package com.chatbot.utils;

import java.io.IOException;

public class CommandExecutor {

    public static String execute(String command) {
        String os = System.getProperty("os.name").toLowerCase();
        String[] cmdArray;
        String appName = "";

        if (command.contains("camera")) {
            appName = "Camera";
            if (os.contains("win")) {
                cmdArray = new String[]{"cmd", "/c", "start", "microsoft.windows.camera:"};
            } else if (os.contains("mac")) {
                cmdArray = new String[]{"open", "-a", "Photo Booth"};
            } else {
                cmdArray = new String[]{"xdg-open", "camera"};
            }
        } else if (command.contains("notepad")) {
            appName = "Notepad";
            if (os.contains("win")) {
                cmdArray = new String[]{"cmd", "/c", "start", "notepad"};
            } else if (os.contains("mac")) {
                cmdArray = new String[]{"open", "-a", "TextEdit"};
            } else {
                cmdArray = new String[]{"xdg-open", "gedit"};
            }
        } else if (command.contains("chrome")) {
            appName = "Google Chrome";
            if (os.contains("win")) {
                cmdArray = new String[]{"cmd", "/c", "start", "chrome"};
            } else if (os.contains("mac")) {
                cmdArray = new String[]{"open", "-a", "Google Chrome"};
            } else {
                cmdArray = new String[]{"google-chrome"};
            }
        } else {
            return null; // Command not recognized
        }

        try {
            new ProcessBuilder(cmdArray).start();
            return "Opening " + appName + "...";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Could not open " + appName + ".";
        }
    }
}
