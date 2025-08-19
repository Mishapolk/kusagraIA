package com.ibcs.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Utility for asynchronously loading and caching images from URLs.
 */
public class ImageLoader {
    private static final Map<String, BufferedImage> CACHE = new ConcurrentHashMap<>();

    /**
     * Loads the image at the given URL and scales it to fit within the provided
     * width and height while preserving aspect ratio. The callback is invoked on
     * the EDT once the image is available.
     */
    public static void load(String url, int maxWidth, int maxHeight, Consumer<ImageIcon> callback) {
        BufferedImage cached = CACHE.get(url);
        if (cached != null) {
            callback.accept(new ImageIcon(scale(cached, maxWidth, maxHeight)));
            return;
        }

        new SwingWorker<BufferedImage, Void>() {
            @Override
            protected BufferedImage doInBackground() throws Exception {
                return ImageIO.read(new URL(url));
            }

            @Override
            protected void done() {
                try {
                    BufferedImage img = get();
                    CACHE.put(url, img);
                    callback.accept(new ImageIcon(scale(img, maxWidth, maxHeight)));
                } catch (Exception ignored) { }
            }
        }.execute();
    }

    private static Image scale(BufferedImage img, int maxWidth, int maxHeight) {
        double scale = Math.max((double) maxWidth / img.getWidth(), (double) maxHeight / img.getHeight());
        int w = (int) Math.round(img.getWidth() * scale);
        int h = (int) Math.round(img.getHeight() * scale);
        Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage out = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = out.createGraphics();
        g2.drawImage(scaled, (maxWidth - w) / 2, (maxHeight - h) / 2, null);
        g2.dispose();
        return out;
    }
}
