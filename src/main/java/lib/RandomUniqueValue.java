package lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Класс с методом возвращающим кникальное значение типа int.
 */
public class RandomUniqueValue {

    private Random random = new Random();
    private List<Integer> list = new ArrayList<>();

    /**
     * Этот метод описывает генерацию уникального значения типа int.
     * Возвращает случайное значение типа int и заносит в массив.
     * При следующем вызове метода значение будет генироваться и будет выполняться првоерка на наличие его в массиве
     * и в случае его отсутствия он будет добавляться в массив, в противном случае значение будет сгенерировано заново.
     *
     * @return
     *          unique int.
     */
    public int nextUniqueInt(){
        int value = random.nextInt(10000);
        if (list.isEmpty()) {
            list.add(value);
            return value;
        } else {
            if (list.contains(value)) nextUniqueInt();
            else list.add(value);
            return value;
        }
    }
}
