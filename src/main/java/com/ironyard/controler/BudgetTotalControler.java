package com.ironyard.controler;

import com.ironyard.data.BudgetTotal;
import com.ironyard.services.BudgetService;

import org.springframework.web.bind.annotation.*;


import java.sql.SQLException;
import java.util.List;

/**
 * Created by Raul on 10/11/16.
 */
@RestController
public class BudgetTotalControler {

    private BudgetService budgetService = new BudgetService();

    @RequestMapping(value = "/budgettotal", method = RequestMethod.GET)
    public List<BudgetTotal> list(){
        List<BudgetTotal> stats = null;
        try {
            stats = budgetService.getBudgetTotal();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }
}
