package com.chanuka.cash_flow.service;

import com.chanuka.cash_flow.dto.ExpenseDTO;
import com.chanuka.cash_flow.entity.ProfileEntity;
import com.chanuka.cash_flow.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${cashflow.frontend.url}")
    private String frontendUrl;

    @Scheduled(cron = "0 0 22 * * *", zone = "Asia/Colombo")
    public void sendDailyIncomeExpenseReminder() {
        log.info("Job Started: sendDailyIncomeExpenseReminder()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for (ProfileEntity profile : profiles) {
            try {
                String body = "Hello " + profile.getFullName() + ",<br><br>"
                        + "This is a friendly reminder to add your incomes and expenses for today in CashFlow."
                        + "<a href=\""+frontendUrl+"\"> Go to CashFlow</a>"
                        + "<br><br>Best regards, <br>CashFlow Team";

                emailService.sendEmail(
                        profile.getEmail(),
                        "Daily reminder: Add your incomes and expenses for today in CashFlow",
                        body
                );
            } catch (Exception e) {
                log.error("Failed to send reminder to {}", profile.getEmail(), e);
            }
        }
        log.info("Job completed: sendDailyIncomeExpenseReminder()");
    }

//    @Scheduled(cron = "0 * * * * *", zone = "Asia/Colombo")
    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Colombo")
    public void sendDailyExpenseSummary() {
        log.info("Job Started: sendDailyExpenseSummary()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for (ProfileEntity profile : profiles) {
            List<ExpenseDTO> todayExpenses = expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now(ZoneId.of("Asia/Colombo")));
            if (!todayExpenses.isEmpty()) {
                StringBuilder table = new StringBuilder();
                table.append("<table style='border-collapse: collapse;width: 100%;'>");
                table.append("<tr style='background-color: #f2f2f2;'><th>No</th><th>Name</th><th>Amount</th><th>Category</th></tr>");

                int i = 1;
                for (ExpenseDTO expenseDTO : todayExpenses) {
                    table.append("<tr>");
                    table.append("<td>").append(i++).append("</td>");
                    table.append("<td>").append(expenseDTO.getName()).append("</td>");
                    table.append("<td>").append(expenseDTO.getAmount()).append("</td>");
                    table.append("<td>").append(expenseDTO.getCategoryId() != null ? expenseDTO.getCategoryName() : "N/A").append("</td>");
                    table.append("</tr>");
                }
                table.append("</table>");
                String body = "Hello " + profile.getFullName() + ",<br><br>Here is a summary of your expenses for today in CashFlow.<br/><br/>" + table + "<br/><br/>Best regards,<br/>CashFlow Team";
                try {
                    emailService.sendEmail(profile.getEmail(), "Daily Expense Summary", body);
                } catch (Exception e) {
                    log.error("Failed to send summary to {}", profile.getEmail(), e);
                }
            }
        }
        log.info("Job completed: sendDailyExpenseSummary()");
    }
}
