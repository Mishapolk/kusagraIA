package com.ibcs.ui;

import com.ibcs.db.*;
import com.ibcs.model.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    private BookDatabase bookDb;
    private UserDatabase userDb;
    private BookmarkDatabase bookmarkDb;
    private RatingDatabase ratingDb;
    private ReadingHistoryDatabase historyDb;
    private AdminActionDatabase actionDb;
    private SearchLogDatabase searchLogDb;

    private User currentUser;

    public MainFrame(BookDatabase bookDb, UserDatabase userDb, BookmarkDatabase bookmarkDb, RatingDatabase ratingDb,
                     ReadingHistoryDatabase historyDb, AdminActionDatabase actionDb, SearchLogDatabase searchLogDb) {
        super("Book Recommendation System");
        this.bookDb = bookDb;
        this.userDb = userDb;
        this.bookmarkDb = bookmarkDb;
        this.ratingDb = ratingDb;
        this.historyDb = historyDb;
        this.actionDb = actionDb;
        this.searchLogDb = searchLogDb;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        add(cardPanel);

        showLogin();
    }

    private void showLogin() {
        LoginPanel login = new LoginPanel(this, userDb);
        cardPanel.add(login, "login");
        cardLayout.show(cardPanel, "login");
    }

    public void loginSuccess(User user) {
        this.currentUser = user;
        showHome();
    }

    public void logout() {
        currentUser = null;
        cardPanel.removeAll();
        showLogin();
    }

    public void showHome() {
        HomePanel home = new HomePanel(this, bookDb, bookmarkDb, currentUser);
        cardPanel.add(home, "home");
        cardLayout.show(cardPanel, "home");
    }

    public void showSearch() {
        SearchPanel panel = new SearchPanel(this, bookDb, bookmarkDb, searchLogDb, currentUser);
        cardPanel.add(panel, "search");
        cardLayout.show(cardPanel, "search");
    }

    public void showBookmarks() {
        BookmarksPanel panel = new BookmarksPanel(this, bookDb, bookmarkDb, currentUser);
        cardPanel.add(panel, "bookmarks");
        cardLayout.show(cardPanel, "bookmarks");
    }

    public void showRecommendations() {
        RecommendationPanel panel = new RecommendationPanel(this, bookDb, bookmarkDb, currentUser);
        cardPanel.add(panel, "recommend");
        cardLayout.show(cardPanel, "recommend");
    }

    public void showAdmin() {
        AdminPanel panel = new AdminPanel(this, bookDb, actionDb);
        cardPanel.add(panel, "admin");
        cardLayout.show(cardPanel, "admin");
    }

    public void showBookDetail(Book book) {
        try {
            historyDb.add(currentUser.getId(), book.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BookDetailPanel panel = new BookDetailPanel(this, book, ratingDb, currentUser, bookmarkDb);
        cardPanel.add(panel, "detail");
        cardLayout.show(cardPanel, "detail");
    }

    public void showProfile() {
        UserPanel panel = new UserPanel(this, currentUser, userDb, historyDb, bookDb);
        cardPanel.add(panel, "profile");
        cardLayout.show(cardPanel, "profile");
    }

    public User getCurrentUser() { return currentUser; }
}
