package com.revplay.service;

import java.util.List;
import com.revplay.dao.HistoryDAO;

public class HistoryService {

    private HistoryDAO historyDAO = new HistoryDAO();

    public void viewListeningHistory(int userId) {

        List<String> history = historyDAO.getUserHistory(userId);

        if (history.isEmpty()) {
            System.out.println("❌ No listening history found.");
            return;
        }

        System.out.println("\n🎧 Your Listening History:");
        history.forEach(System.out::println);
    }
}
