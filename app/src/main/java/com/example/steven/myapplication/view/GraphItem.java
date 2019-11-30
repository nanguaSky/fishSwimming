package com.example.steven.myapplication.view;

public class GraphItem {
        private String xValue;
        private float yValue;

        public String getxValue() {
            return xValue;
        }

        public void setxValue(String xValue) {
            this.xValue = xValue;
        }

        public float getyValue() {
            return yValue;
        }

        public void setyValue(float yValue) {
            this.yValue = yValue;
        }

        public GraphItem(String xValue, float yValue) {

            this.xValue = xValue;
            this.yValue = yValue;
        }
    }