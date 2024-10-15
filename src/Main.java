import java.util.*;

public class Main {
    // создаем потокобезопасную мапу для хранения "ключ" - "значение";
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();


    public static void main(String[] args) throws InterruptedException {
        // количество потоков (равно количеству маршрутов)
        int numThreads = 1000;
        // создаем список для хранения потоков;
        List<Thread> threads = new ArrayList<>();

        char charToCount = 'R';

        // создаем потоки;
        for (int i = 0; i < numThreads; i++) {
            Thread thread = new Thread(() -> {
                String generateLine = generateRoute("RLRFR", 100);
                // считаем количество 'R' в строке;
                int rCount = (int) generateLine.chars().filter(ch -> ch == charToCount).count();

                // синхронизируем доступ к общей мапе
                synchronized (sizeToFreq) {
                    // проверяем Мапу по ключу rCount и если находит то увеличивает колличество повторов на +1;
                    sizeToFreq.put(rCount, sizeToFreq.getOrDefault(rCount, 0) + 1);
                }
            });
            threads.add(thread);
            thread.start();
        }
        // дожидаемся завершения всех потоков
        for (Thread thread : threads) {
            thread.join();
        }

        // получаем ключ мах частоты-значения;
        int maxSize = Collections.max(sizeToFreq.entrySet(),
                Map.Entry.comparingByValue()).getKey();
        // получаем мах значение по ключу;
        int maxFrequency = sizeToFreq.get(maxSize);

        System.out.printf("Самое частое количество повторений %d (встретилось %d раз) " +
                "\n другие размеры:\n", maxSize, maxFrequency);

        // в entry  кладем пару ключ значение;
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            int size = entry.getKey(); // получаем ключ;
            int frequency = entry.getValue(); // получаем значение;
            if (size != maxSize) {
                // вывод частот для остальных ключей;
                System.out.printf("- %d (%d раз) \n", size, frequency);
            }
        }
    }

    // метод генерации строки в 100 символов;
    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
