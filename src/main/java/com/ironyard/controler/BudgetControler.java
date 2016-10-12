package com.ironyard.controler;

import com.ironyard.data.Budget;
import com.ironyard.services.BudgetService;

import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Raul on 10/11/16.
 */
@RestController
public class BudgetControler {

    private BudgetService budgetService = new BudgetService();

    @RequestMapping(value = "/budget", method = RequestMethod.POST)
    public Budget createBudget(@RequestBody Budget mbudget){
        Budget saved = null;
        try {
            budgetService.createBudget(mbudget);
            saved = budgetService.getBudgetById(mbudget.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saved;
    }

    @RequestMapping(value = "/budget/update", method = RequestMethod.PUT)
    public Budget update(@RequestBody Budget mbudget){
        Budget updated = null;
        try {
            budgetService.update(mbudget);
            updated = budgetService.getBudgetById(mbudget.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    @RequestMapping(value = "/budget/{id}", method = RequestMethod.GET)
    public Budget show(@PathVariable Integer id){
        Budget found = null;
        try {
            found = budgetService.getBudgetById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return found;
    }

    @RequestMapping(value = "/budgets", method = RequestMethod.GET)
    public List<Budget> list(){
        List all = null;
        try {
            all = budgetService.getAllBudgets();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }

    @RequestMapping(value = "/budget/delete/{id}", method = RequestMethod.DELETE)
    public Budget delete(@PathVariable Integer id){
        Budget deleted = null;
        try {
            deleted = budgetService.getBudgetById(id);
            budgetService.delete(id);
        } catch (SQLException e) {
            deleted  = null;
            e.printStackTrace();
        }
        return deleted;
    }

}
