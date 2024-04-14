package ru.juli.practicum.order;

import java.util.ArrayList;
import java.util.List;

public class Color {
        private static List<String> emptyList = new ArrayList<>();
        private static List<String> blackOnly = new ArrayList<>();
        private static List<String> greyOnly = new ArrayList<>();
        private static List<String> allColor = new ArrayList<>();

        public static List<String> getEmptyList() {
            return emptyList;
        }

        public static List<String> getBlackOnly() {
            blackOnly.add("BLACK");
            return blackOnly;
        }
        public static List<String> getGreyOnly() {
            greyOnly.add("GREY");
            return greyOnly;
        }

        public static List<String> getBothColor() {
            allColor.add("BLACK");
            allColor.add("GREY");
            return allColor;
        }

}
