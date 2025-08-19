package com.ibcs.ui;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class configuring a dark, minimalistic FlatLaf theme with
 * rounded components and a cool blue accent. Calling {@link #setup()}
 * once at startup will apply the look globally.
 */
public class ModernUI {
    public static void setup() {
        FlatDarkLaf.setup();
        Font uiFont = new Font("SansSerif", Font.PLAIN, 16);
        UIManager.put("defaultFont", uiFont);

        // Rounded corners and subtle focus indicator
        UIManager.put("Component.arc", 20);
        UIManager.put("Button.arc", 20);
        UIManager.put("TextComponent.arc", 20);
        UIManager.put("Component.focusWidth", 1);
        UIManager.put("Component.focusColor", new Color(0x5E81AC));

        // Dark surfaces with blue accents
        Color bg = new Color(0x121212);
        Color fg = Color.white;
        Color accent = new Color(0x2979FF);
        UIManager.put("Panel.background", bg);
        UIManager.put("OptionPane.background", bg);
        UIManager.put("OptionPane.messageForeground", fg);
        UIManager.put("Label.foreground", fg);
        UIManager.put("Button.background", accent);
        UIManager.put("Button.foreground", fg);
        UIManager.put("Button.focusedBackground", accent.darker());
        UIManager.put("TextComponent.background", new Color(0x1E1E1E));
        UIManager.put("TextComponent.foreground", fg);
    }
}
