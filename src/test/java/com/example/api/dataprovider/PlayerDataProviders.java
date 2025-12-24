package com.example.api.dataprovider;

import com.example.api.domain.Editor;
import org.testng.annotations.DataProvider;

import java.util.Map;
import java.util.function.Consumer;

public final class PlayerDataProviders {
    private PlayerDataProviders() {
    }

    @DataProvider(name = "validationEditors")
    public static Object[][] validationEditors() {
        return new Object[][]{
                {Editor.ADMIN},
                {Editor.SUPERVISOR},
        };
    }

    @DataProvider(name = "invalidPasswords")
    public static Object[][] invalidPasswords() {
        return withEditors(new Object[][]{
                {"too short", "a1b2c3"},
                {"too long", "abc1234567890123"},
                {"letters only", "abcdefg"},
                {"digits only", "1234567"},
        });
    }

    @DataProvider(name = "invalidUpdateAges")
    public static Object[][] invalidUpdateAges() {
        return withEditors(new Object[][]{
                {"age negative", -1},
                {"age too young", 16},
                {"age too old", 60},
        });
    }

    @DataProvider(name = "invalidCreateQueryParams")
    public static Object[][] invalidCreateQueryParams() {
        return withEditors(new Object[][]{
                {"missing age", (Consumer<Map<String, Object>>) m -> m.remove("age")},
                {"missing gender", (Consumer<Map<String, Object>>) m -> m.remove("gender")},
                {"missing login", (Consumer<Map<String, Object>>) m -> m.remove("login")},
                {"missing password", (Consumer<Map<String, Object>>) m -> m.remove("password")},
                {"missing screenName", (Consumer<Map<String, Object>>) m -> m.remove("screenName")},
                {"missing role", (Consumer<Map<String, Object>>) m -> m.remove("role")},
                {"age non-numeric", (Consumer<Map<String, Object>>) m -> m.put("age", "abc")},
                {"age negative", (Consumer<Map<String, Object>>) m -> m.put("age", "-1")},
                {"age too young", (Consumer<Map<String, Object>>) m -> m.put("age", 16)},
                {"age too old", (Consumer<Map<String, Object>>) m -> m.put("age", 60)},
                {"invalid gender", (Consumer<Map<String, Object>>) m -> m.put("gender", "unknown")},
                {"role supervisor", (Consumer<Map<String, Object>>) m -> m.put("role", "supervisor")},
                {"invalid role", (Consumer<Map<String, Object>>) m -> m.put("role", "manager")},
        });
    }

    private static Object[][] withEditors(Object[][] cases) {
        Object[][] out = new Object[cases.length * 2][];
        int index = 0;
        for (Object[] testCase : cases) {
            out[index++] = prepend(Editor.ADMIN, testCase);
            out[index++] = prepend(Editor.SUPERVISOR, testCase);
        }
        return out;
    }

    private static Object[] prepend(Editor editor, Object[] tail) {
        Object[] row = new Object[tail.length + 1];
        row[0] = editor;
        System.arraycopy(tail, 0, row, 1, tail.length);
        return row;
    }
}
