package com.ironyard.data;

/**
 * Created by Raul on 10/11/16.
 */
public class Budget {

        private String description;
        private String category;
        private double budgetamount;
        private double actualamount;
        private long id;


        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public double getBudgetamount() {
            return budgetamount;
        }

        public void setBudgetamount(double budgetamount) {
            this.budgetamount = budgetamount;
        }

        public double getActualamount() {
            return actualamount;
        }

        public void setActualamount(double actualamount) {
            this.actualamount = actualamount;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }





