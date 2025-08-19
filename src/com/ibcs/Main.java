package com.ibcs;

import com.ibcs.db.*;
import com.ibcs.model.*;
import com.ibcs.ui.*;

import javax.swing.*;
import java.awt.*;
import com.ibcs.ui.ModernUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ModernUI.setup();

                BookDatabase bookDb = new BookDatabase();
                UserDatabase userDb = new UserDatabase();
                BookmarkDatabase bookmarkDb = new BookmarkDatabase();
                RatingDatabase ratingDb = new RatingDatabase();
                ReadingHistoryDatabase historyDb = new ReadingHistoryDatabase();
                AdminActionDatabase actionDb = new AdminActionDatabase();
                SearchLogDatabase searchLogDb = new SearchLogDatabase();

                MainFrame frame = new MainFrame(bookDb, userDb, bookmarkDb, ratingDb, historyDb, actionDb, searchLogDb);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
