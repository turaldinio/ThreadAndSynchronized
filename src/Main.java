import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private final static Map<Integer, Integer> sizeToFreq = new HashMap<>();


    public static void main(String[] args) {

        List<Thread> list = new ArrayList<>();
        for (int a = 0; a < 100; a++) {
            list.add(new Thread(() -> {
                putInMap(numberOfRepetitions(generateRoute("RLRFR", 100)));
            }));
        }

        list.forEach(Thread::start);

        Thread sortThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                sortMap();

            }
        });
        sortThread.start();


        list.forEach(x -> {
            try {
                x.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        sortThread.interrupt();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }

        return route.toString();
    }

    public static void putInMap(int count) {
        synchronized (sizeToFreq) {
            sizeToFreq.merge(count, 1, Integer::sum);
            sizeToFreq.notify();
        }

    }

    public static void sortMap() {
        synchronized (sizeToFreq) {

            if (sizeToFreq.isEmpty()) {
                try {
                    sizeToFreq.wait();
                } catch (InterruptedException e) {
                    return;
                }
            }
            sizeToFreq.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Integer>comparingByValue()
                            .reversed())
                    .limit(1)
                    .forEach(System.out::println);

        }


    }

    public static int numberOfRepetitions(String line) {
        return (int) line.chars().filter(x -> x == 'R').count();
    }
}
