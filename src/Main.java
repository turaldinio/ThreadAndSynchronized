import java.util.*;

public class Main {
    private static final Map<Integer, Integer> sizeToFreq = new HashMap<>();


    public static void main(String[] args) {
        List<Thread> list = new ArrayList<>();
        for (int a = 0; a < 100; a++) {
            list.add(new Thread(() -> {
                putInMap(numberOfRepetitions(generateRoute("RLRFR", 100)));
            }));
        }
        list.forEach(Thread::start);
        list.forEach(x -> {
            try {
                x.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(sizeToFreq);
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }

        return route.toString();
    }

    public static synchronized void putInMap(int count) {
        sizeToFreq.merge(count, 1, Integer::sum);

    }

    public static int numberOfRepetitions(String line) {
        return (int) line.chars().filter(x -> x == 'R').count();
    }
}
