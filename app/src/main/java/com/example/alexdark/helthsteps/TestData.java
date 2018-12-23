package com.example.alexdark.helthsteps;

import java.util.ArrayList;

public class TestData {
    String google_id;
    ArrayList<MoveItem> data;

    public TestData(String id, ArrayList<MoveItem> data) {
        this.google_id = id;
        this.data = data;
    }
}
