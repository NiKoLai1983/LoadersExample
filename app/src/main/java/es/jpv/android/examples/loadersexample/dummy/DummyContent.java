/*
 * Copyright (C) 2015 Jesús Platas Varet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.jpv.android.examples.loadersexample.dummy;

import android.database.Cursor;
import android.database.MatrixCursor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static Map<String, String> ITEMS = new LinkedHashMap<String, String>();
    public static MatrixCursor ITEMS_CURSOR;

    static {
        //Create root items
        for (int i=0 ; i<30 ; i++) {
            ITEMS.put(String.valueOf(i), "Item " + (i));
        }
        // Add 30 sample items to the MatrixCursor
        buildCursor();
    }

    public static Cursor buildCursor() {
        ITEMS_CURSOR = new MatrixCursor(new String[] {"_id", "value"});
        for (Map.Entry<String, String> entry : ITEMS.entrySet()) {
            ITEMS_CURSOR.addRow(new String[] {entry.getKey(), entry.getValue()});
        }
        return ITEMS_CURSOR;
    }

    public static Cursor addItem(Integer key) {
        ITEMS.put(String.valueOf(key), "Item " + key);
        return buildCursor();
    }

    public static Cursor deleteItem(int position) {
        ITEMS_CURSOR.moveToPosition(position);
        String keyToDelete = ITEMS_CURSOR.getString(0); //ID
        if (keyToDelete != null) {
            ITEMS.remove(keyToDelete);
        }
        return buildCursor();
    }

}
