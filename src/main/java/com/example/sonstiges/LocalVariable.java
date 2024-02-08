package com.example.sonstiges;

import java.util.*;

public class LocalVariable {

    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<>();

        // infers ArrayList<String>
        var list = new ArrayList<String>();

        // infers Stream<String>
        var stream = list.stream();
    }

}
