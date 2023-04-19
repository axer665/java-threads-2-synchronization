import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static void main(String[] args) {

        new Thread(() -> {
            synchronized (sizeToFreq) {
                for (int i = 0; i < 1000; i++) {
                    String route = generateRoute("RLRFR", 100);
                    int count = 0;
                    for (char element : route.toCharArray()) {
                        if (element == 'R') {
                            count++;
                        }
                    }
                    if (sizeToFreq.containsKey(count)) {
                        sizeToFreq.replace(count, sizeToFreq.get(count) + 1);
                    } else {
                        sizeToFreq.put(count, 1);
                    }

                    sizeToFreq.notify();
                }
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                return;
            }
        }).start();

        new Thread(() -> {
            synchronized (sizeToFreq) {
                if (sizeToFreq.size() == 0) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                int max = 0;
                int maxItem = 0;

                for (Map.Entry<Integer, Integer> item : sizeToFreq.entrySet()) {
                    if (item.getValue() > 0){
                        max = item.getValue();
                        maxItem = item.getKey();
                    }
                }

                System.out.println("Самое частое количество повторений " + maxItem + "(встретилось " + max + " раз)");
                sizeToFreq.remove(maxItem);
                System.out.println("Другие размеры:");
                for (Map.Entry<Integer, Integer> item : sizeToFreq.entrySet()) {
                    System.out.println("- " + item.getKey() + "("+ item.getValue() +" раз)");
                }
            }
        }).start();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}