package com.ibcs;

import com.ibcs.db.*;
import com.ibcs.model.*;
import com.ibcs.ui.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import com.ibcs.ui.ModernUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ModernUI.setup();

                BookDatabase bookDb = new BookDatabase("data/books.csv");
                UserDatabase userDb = new UserDatabase("data/users.csv");
                BookmarkDatabase bookmarkDb = new BookmarkDatabase("data/bookmarks.csv");
                RatingDatabase ratingDb = new RatingDatabase("data/ratings.csv");
                ReadingHistoryDatabase historyDb = new ReadingHistoryDatabase("data/history.csv");
                AdminActionDatabase actionDb = new AdminActionDatabase("data/admin_actions.csv");
                SearchLogDatabase searchLogDb = new SearchLogDatabase("data/search_log.csv");

                MainFrame frame = new MainFrame(bookDb, userDb, bookmarkDb, ratingDb, historyDb, actionDb, searchLogDb);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
