package com.dkkk.soothsayer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.Random;

/**
 * ViewModel для Matrix Activity.
 * Содержит логику расчетов матрицы по дате.
 */
public class MatrixViewModel extends ViewModel {

    private final String[] descriptions = {
            "Состояние человека «в плюсе»:\n\nДвижение. Подобно Колеснице...",
            "Состояние человека «в плюсе»:\n\nПроницательность...",
            "Состояние человека «в плюсе»:\n\nАмбиции..."
    };

    private final MutableLiveData<MatrixResult> matrixResult = new MutableLiveData<>();
    private final Random random = new Random();

    public LiveData<MatrixResult> getMatrixResult() {
        return matrixResult;
    }

    public void calculateMatrix(String input) {
        if (!validateInput(input)) {
            matrixResult.setValue(null);
            return;
        }

        String[] parts = input.split("\\.");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        int central = calculateCentralArcan(day, month, year);
        String randomText = descriptions[random.nextInt(descriptions.length)];

        matrixResult.setValue(new MatrixResult(
                String.valueOf(day),
                String.valueOf(month),
                String.valueOf(year / 100),
                String.valueOf(year % 100),
                String.valueOf(central),
                randomText
        ));
    }

    private boolean validateInput(String input) {
        if (!input.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) return false;
        try {
            String[] parts = input.split("\\.");
            int d = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            return d >= 1 && d <= 31 && m >= 1 && m <= 12 && y >= 1900 && y <= 2100;
        } catch (Exception e) {
            return false;
        }
    }

    private int calculateCentralArcan(int day, int month, int year) {
        int sum = sumDigits(day) + sumDigits(month) + sumDigits(year);
        while (sum > 22) sum = sumDigits(sum);
        return sum;
    }

    private int sumDigits(int num) {
        int s = 0;
        while (num > 0) {
            s += num % 10;
            num /= 10;
        }
        return s;
    }

    public static class MatrixResult {
        public final String m1, m2, g1, g2, central, description;

        public MatrixResult(String m1, String m2, String g1, String g2, String central, String description) {
            this.m1 = m1; this.m2 = m2; this.g1 = g1; this.g2 = g2;
            this.central = central; this.description = description;
        }
    }
}
